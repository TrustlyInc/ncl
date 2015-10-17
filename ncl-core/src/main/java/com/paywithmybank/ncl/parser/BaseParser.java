package com.paywithmybank.ncl.parser;

import com.paywithmybank.ncl.parser.Lexer.Token;
import com.paywithmybank.ncl.parser.nodes.ASTNode;


public abstract class BaseParser {
	
	Lexer lex;
	Token token = null;
	Token[] lookAhead = new Token[2];
	
	public abstract ASTNode parse();
	
	protected Lexer.Token optional(Lexer.Token.Type type){
		if(token == null || !type.equals(token.type)) {
			return null;
		}
		Lexer.Token t = this.token;
		
		consumeToken();
		return t;
	}
	
	protected Lexer.Token optional(Lexer.Token.Type... types){
		if(token != null){
			for(Lexer.Token.Type tp : types ){
				if(tp.equals(token.type)){
					Lexer.Token t = token;
					consumeToken();
					return t;
				}
			}
		}
		return null;
	}
	
	protected Lexer.Token optional(Lexer.Token... tokens){
		if(token != null){
			for(Lexer.Token tk : tokens ){
				if(tk.equals(token)){
					Lexer.Token t = token;
					consumeToken();
					return t;
				}
			}
		}
		return null;
	}
	
	
	protected Lexer.Token mandatory(Lexer.Token.Type tp,String errMessage){
		if(token == null || !tp.equals(this.token.type)) {
			error(errMessage);
			return null;
		}
		Lexer.Token t = this.token;
		
		consumeToken();
		return t;
	}
	
	protected Lexer.Token mandatory(Lexer.Token tk,String errMessage){
		if(token == null || !tk.equals(token)) {
			error(errMessage);
			return null;
		}
		Lexer.Token t = token;
		
		consumeToken();
		return t;
	}
	
	
	public void error(String msg) {
		if(token == null){
			throw new RuntimeException(msg + ". Unexpected EOF. Source: " + lex.source.getName());
		}
		throw new RuntimeException(msg + ". Got " + token +" at source: "+lex.source.getName()+" line: " + token.line + " column: " + token.column);
	}
	
	protected Token consumeToken() {
		return consumeToken(1);
	}
	protected Token consumeToken(int n) {
		for(int i = 0; i < n ; i++){
			token = lookAhead[0];
			for(int p = 0; p < (lookAhead.length - 1); p++){
				lookAhead[p] = lookAhead[p+1];
			}
			lookAhead[lookAhead.length - 1] = lex.next();
		}
		
		return token;
	}
}
