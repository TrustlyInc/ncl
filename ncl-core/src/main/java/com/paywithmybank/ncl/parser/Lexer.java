package com.paywithmybank.ncl.parser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.paywithmybank.ncl.model.Source;

public class Lexer {
    Source source;
	private BufferedReader reader;
	StringBuilder sb = null;
	char[] buffer = new char[5000]; //token buffer
	int pos = 0;
	int line = 1;
	int column = 0;
	int markLine = 1;
	int markColumn = 1;
	
	
    public static class Token {
    	public static enum Type {
            SPACE, IDENT, BOOLEAN, SYMBOL, STRING, INTEGER, FLOAT
        }
    	
        public final Type type;
        public final String value; // contents mainly for atom tokens
        public final int line;
        public final int column;
        

        public Token(Type t, String c, int line, int column) {
            this.type = t;
            this.value = c;
            this.line = line;
            this.column = column;
        }
        
        public Token(Type t, String c) {
            this.type = t;
            this.value = c;
            this.line = -1;
            this.column = -1;
        }
        
        public String toString() {
        	return type+"<" + value + ">";
        }
        
        public boolean equals(Object obj) {
        	Token tk = (Token)obj;
            return this.type == tk.type && ((this.value == null && tk.value == null) || this.value.equals(tk.value));
        }
    }
	
	public Lexer(Source src){
		source = src;
		reader = new BufferedReader(new InputStreamReader(source.getInputStream()));
	}
	
	public Lexer(String s){
		this(new Source("String", new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8))));
	}
	
	public Token next() {
		try {
			int i = -1;
			char ch;

			do{
				i = lookAhead();

				if(i > 0) {
					ch = (char)i;
					if(Character.isWhitespace(ch)){
						return getWhitespace();
					} else if (Character.isJavaIdentifierStart(ch) && ch != '$'){
						append(ch);
						return getIdentifierOrBoolean();
					} else if (Character.isDigit(ch)){
						append(ch);
						return getNumber();
					} else if (ch == '/') {
						char la = (char) lookAhead();
						if(la == '/'){
							stripSingleLineComment();
						} else if (la == '*') {
							stripMultiLineComment();
						} else {
							append(ch);
							return getSymbol();
						}
					} else if (ch == '\"' || ch == '\''){
						return getString(ch);
					} else {
						append(ch);
						return getSymbol();
					}
				}
			} while (i > 0);
		} catch (IOException e) {
			error(e.getMessage(),e);
		}
		return null;
	}
	
	private Token getString(char st) throws IOException {
		boolean backslash = false;
		int cp;
		while ((cp = lookAhead()) >= 0){
			char c = (char)cp;
			if(!backslash) {
				if(c == st){
					break;
				}

				if(c == '\\'){
					backslash = true;
					continue;
				}
				append(c);
			} else { 
				switch(cp) {
					case 'b': append('\b'); break;
					case 't': append('\t'); break;
					case 'n': append('\n'); break;
					case 'f': append('\f'); break;
					case 'r': append('\r'); break;
					case '\\':
					case '\"':
					case '\'': append(c); break;
					
					case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7':
						StringBuilder sb = new StringBuilder(3);
						sb.append(c);
						int n = 0;
						while ((cp = lookAhead()) >= 0 && n <2){
							if(cp >= '0' && cp <= '7'){
								n++;
								sb.append(c);
							} else {
								reset();
								break;
							}
						}
						append((char) Integer.parseInt(sb.toString(), 8)); 
					break;
					
					case 'u':
						StringBuilder sb2 = new StringBuilder(4);
						int n2 = 0;
						while ((cp = lookAhead()) >= 0 && n2 <4){
							if((cp >= '0' && cp <= '9') || (cp >= 'a' && cp <= 'f') || (cp >= 'A' && cp <= 'F')){
								n2++;
								sb2.append(cp);
							} else {
								reset();
								break;
							}
						}
						append((char) Integer.parseInt(sb2.toString(), 16)); 
					break;
					
					default:
						error("Invalid escape " + (char)cp);
						break;
						
				}
				backslash = false;
			}
		}
		if(backslash){
			append('\\');
		}
		return createToken(Token.Type.STRING);
	}

	private void error(String string) {
		throw new RuntimeException("Error: " + string + ". At source: "+source.getName()+" line: " + line + " column: " +column);
	}
	
	private void error(String string,Exception e) {
		throw new RuntimeException("Error: " + string + ". At source: "+source.getName()+" line: " + line + " column: " +column,e);
	}

	private Token getSymbol()  throws IOException {
		
		if(isUnarySymbol(buffer[pos-1])){
			return createToken(Token.Type.SYMBOL);
		}
		
		while (true) {
			int i = lookAhead();
			char c = (char)i;
			if(i > 0  && isSymbol(c) &&  !isUnarySymbol(c) && !(isPrefixOperator(c) && buffer[pos-1] != i)){
				append(c);
			}
			else {
				reset();
				return createToken(Token.Type.SYMBOL);
			}
		}
	}
	
	private boolean isSymbol(char c){
		return !Character.isLetterOrDigit(c) && !Character.isWhitespace(c);
	}
	
	private boolean isUnarySymbol(char c) {
		return c == '{' || c == '}' || c == '[' || c == ']' || c == '(' || c == ')' || c == '\"' || c == '\'' || c == ';';
	}
	
	private boolean isPrefixOperator(char c){
		return c == '+' || c == '-' || c == '!' || c == '~' || c =='.' || c =='#';
	}

	private void stripMultiLineComment()  throws IOException {
		char ch;
		boolean star = false;
		while ((ch = (char) lookAhead()) >= 0){
			if(ch == '*'){
				star = true;
				continue;
			}
			if(star && ch == '/'){
				break;
			} else {
				star = false;
			}
		}
		
	}

	private void stripSingleLineComment()  throws IOException {
		char ch;
		while ((ch = (char) lookAhead()) >= 0){
			if(ch == '\n'){
				break;
			}
		}
		
	}

	private Token getNumber()  throws IOException {
		
		//TODO Better number support. Float, real, long,...
		char ch;
		boolean hasDot = false;
		while ((ch = (char) lookAhead()) >= 0){
			if(Character.isDigit(ch)){
				append(ch);
			} else if(!hasDot && ch == '.'){
				append(ch);
				hasDot = true;
			} else {
				reset();
				break;
			}
		}
		if(hasDot){
			return createToken(Token.Type.FLOAT);
		}
		return createToken(Token.Type.INTEGER);
	}

	private Token getIdentifierOrBoolean()  throws IOException {
		while (true) {
			int i = lookAhead();
			
			if(i > 0  && (Character.isJavaIdentifierPart((char)i)) || '-' == i ){
				append((char)i);
			}
			else {
				reset();
				String str = getBufferString();
				if("true".equals(str) || "false".equals(str) ){
					return createToken(Token.Type.BOOLEAN);
				}
				return createToken(Token.Type.IDENT);
			}
		}
	}

	private Token getWhitespace()  throws IOException {
		while (true) {
			int i = lookAhead();
			
			if(!Character.isWhitespace((char)i )){
				reset();
				return createToken(Token.Type.SPACE);
			}
		}
	}

	private int lookAhead() throws IOException {
		markLine = line;
		markColumn = column;
		reader.mark(5);
		int c = reader.read();
		if(c >= 0){
			column++;
			if(c == '\n'){
				line++;
				column = 0;
			}
		}
		return c;
	}
	
	private void reset() throws IOException {
		reader.reset();
		line = markLine;
		column = markColumn;
	}
	
	private void append(char c){
		buffer[pos++] = c;
		if(pos >= buffer.length){
			if(sb == null){
				sb = new StringBuilder(buffer.length);
			}
			sb.append(buffer);
			pos = 0;
		}
	}
	
	private String getBufferString() {
		String str = null;
		if(pos > 0){
			if(sb == null){
				str = new String(buffer,0,pos);
			}
			else {
				sb.append(buffer,0,pos);
				pos = 0;
				str = sb.toString();
			}
			
		} else {
			if(sb != null){ 
				str = sb.toString();
			}
		}
		
		return str;
	}
	
	private Token createToken(Token.Type t){
		String str = getBufferString();
		pos = 0;
		sb = null;
		return new Token( t,str,line,column);
	}
	
	
	public static void main(String[] args) throws IOException{
		String ncl = " /*5*/  a(mm)//mycomment\n{data.l +=+10.00\n}['\\uf0\\t\\tda\\nta\\\"a']";
		
		Lexer lex = new Lexer(ncl);
		
		for(Token t = lex.next(); t != null; t = lex.next()) {
			System.out.println(t);
		}
	}
}
