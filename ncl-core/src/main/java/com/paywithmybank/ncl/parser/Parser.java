package com.paywithmybank.ncl.parser;

import static com.paywithmybank.ncl.parser.Lexer.Token.Type.*;


import com.paywithmybank.ncl.model.Source;
import com.paywithmybank.ncl.parser.Lexer.Token;
import com.paywithmybank.ncl.parser.nodes.ASTNode;

public class Parser extends BaseParser{
	private static final Lexer.Token MODULE = new Lexer.Token(IDENT,"module");
	private static final Lexer.Token IMPORT = new Lexer.Token(IDENT,"import");
	private static final Lexer.Token AT = new Lexer.Token(SYMBOL,"@");
	private static final Lexer.Token L_BRACE = new Lexer.Token(SYMBOL,"{");
	private static final Lexer.Token R_BRACE = new Lexer.Token(SYMBOL,"}");
	private static final Lexer.Token L_ROUND = new Lexer.Token(SYMBOL,"(");
	private static final Lexer.Token R_ROUND = new Lexer.Token(SYMBOL,")");
	private static final Lexer.Token L_BRACKET = new Lexer.Token(SYMBOL,"[");
	private static final Lexer.Token R_BRACKET = new Lexer.Token(SYMBOL,"]");
	private static final Lexer.Token SEMICOLON = new Lexer.Token(SYMBOL,";");
	private static final Lexer.Token STAR = new Lexer.Token(SYMBOL,"*");
	private static final Lexer.Token EQUAL = new Lexer.Token(SYMBOL,"=");
	private static final Lexer.Token PLUS = new Lexer.Token(SYMBOL,"+");
	private static final Lexer.Token VBAR = new Lexer.Token(SYMBOL,"|");
	private static final Lexer.Token HASH = new Lexer.Token(SYMBOL,"#");
	private static final Lexer.Token DOLLAR = new Lexer.Token(SYMBOL,"$");
	//private static final Lexer.Token DOT = new Lexer.Token(SYMBOL,".");
	private static final Lexer.Token GT = new Lexer.Token(SYMBOL,">");
	private static final Lexer.Token TILDE = new Lexer.Token(SYMBOL,"~");
	private static final Lexer.Token SPC = new Lexer.Token(SPACE,null);
	private static final Lexer.Token COMMA = new Lexer.Token(SYMBOL,",");
	private static final Lexer.Token EXCLAMATION = new Lexer.Token(SYMBOL,"!");

	
	
    ASTNode expressions = new ASTNode(ASTNode.Type.NCL);
	
	public Parser(Source src){
		lex = new Lexer(src);
		consumeToken(lookAhead.length+1);
	}
	
	public Parser(String s){
		lex = new Lexer(s);
		consumeToken(lookAhead.length+1);
	}
	
	//expressions = expression | expressions expression
	public ASTNode parse() {
		expressions = new ASTNode(ASTNode.Type.EXPRESSIONS);
		
		while(token != null){
			optional(SPC); 
			if(!_expression(expressions)){
				error("Unknow expression");
			}
		};
		
		return expressions;
	}
	
	//expression = SPACE* directive | SPACE* rule  
	private boolean _expression(ASTNode expressions) {
		optional(SPACE);

		if(!_directive(expressions)){
			if(!_rule(expressions)) {
				return false;
			}
			return true;
		}
		return true;
	}

	//directive = '$' [import | module]
	private boolean _directive(ASTNode expression) {
		if(optional(DOLLAR) == null) {
			return false;
		}

		ASTNode directive = expression.add(new ASTNode(ASTNode.Type.DIRECTIVE));
		
		if(!_module(directive)){
			if(!_import(directive)) {
				error("Invalid directive");
				return false;
			}
		}
		return true;
	}
	
	//module = 'module' SPACE+ IDENT ';'
	private boolean _module(ASTNode directive) {
		if(optional(MODULE) == null) {
			return false;
		}
		
		ASTNode module = directive.add(new ASTNode(ASTNode.Type.MODULE));
		
		mandatory(SPACE,"Invalid module");
		module.data = mandatory(IDENT,"Invalid module identifier").value;
		optional(SPACE);	
		mandatory(SEMICOLON,"Missing ';'");

		return true;
	}

	//import = 'import' SPACE+ IDENT SPACE* ';'
	private boolean _import(ASTNode directive) {
		if(optional(IMPORT) == null) {
			return false;
		}
		
		ASTNode importNode = directive.add(new ASTNode(ASTNode.Type.IMPORT));
		
		mandatory(SPACE,"Invalid import");
		importNode.data = mandatory(IDENT,"Invalid import identifier").value;
		optional(SPACE);	
		mandatory(SEMICOLON,"Missing ';'");

		return true;
	}
	
	//rule = selectors_group block_or_vector	
	private boolean _rule(ASTNode expression) {
		ASTNode rule = expression.add(new ASTNode(ASTNode.Type.RULE));
		
		if(!_selectors_group(rule)){
			return false;
		}
		optional(SPC);
		if(!_block_or_vector(rule)){
			error("Mising block or vector");
			return false;
		}
		
		return true;
	}
	
	//selectors_group = selector SPACE* |  selector SPACE* ',' SPACE* selectors_group
	private boolean _selectors_group(ASTNode rule) {
		ASTNode selectors_group = rule.add(new ASTNode(ASTNode.Type.SELECTORS_GROUP));
		
		if(!_selector(selectors_group)){
			return false;
		}
		optional(SPACE);
		
		while(optional(COMMA) != null) {
			optional(SPC);
			if(!_selector(selectors_group)){
				error("Mising selector");
				return false;
			}
			optional(SPACE);
		}
		
		return true;
	}

	//selector = simple_selector [ combinator selector | S+ [ combinator? selector ]? ]?
	//combinator = '+' SPACE* | '>' SPACE* | '~' SPACE* 
	private boolean _selector(ASTNode selectors_group) {
		ASTNode selector = selectors_group.add(new ASTNode(ASTNode.Type.SELECTOR));

		if(!_simple_selector(selector)){
			return false;
		}

		while (token != null){
			Token spc = optional(SPC);
			Token comb = optional(PLUS,GT,TILDE);
			
			if(comb != null) {
				ASTNode combinator = selector.add(new ASTNode(ASTNode.Type.COMBINATOR));
				combinator.data = comb.value;
				optional(SPC);
				if(!_simple_selector(selector)){
					error("Mising selector");
					return false;
				}
			} else if (spc != null){
				if(!_simple_selector(selector)) {
					break;
				};
			} else {
				break;
			}
		}
		
		return true;
	}
	
	//simple_selector= tag [ tag_selector ]* | [ tag_selector ]+
	public boolean _simple_selector(ASTNode selector) {
		ASTNode simple_selector = selector.add(new ASTNode(ASTNode.Type.SIMPLE_SELECTOR));

		if(_tag(simple_selector)) {
			_tag_selector(simple_selector);
		} else {
			if(!_tag_selector(simple_selector)){
				simple_selector.detach();
				return false;
			}
		}
		while(_tag_selector(simple_selector));
		
		return true;
	}
	
	//tag = '*' | IDENT
	private boolean _tag(ASTNode simple_selector) {
		Token tag = optional(STAR);
		if(tag == null) {
			tag = optional(IDENT);
		}
		if(tag == null){
			return false;
		}
		simple_selector.add(new ASTNode(ASTNode.Type.TAG)).data = tag.value;
		return true;
	}
	 
	//tag_selector = hash | context | filter | negation
	private boolean _tag_selector(ASTNode simple_selector) {
		if(!_hash(simple_selector)){
			if(!_context(simple_selector)){
				if(!_filter(simple_selector)){
					if(!_negation(simple_selector)){
						return false;
					}
				}
			}
		}
		return true;
	}

	//negation = '!' simple_selector
	private boolean _negation(ASTNode simple_selector) {
		if(optional(EXCLAMATION) == null){
			return false;
		}
		ASTNode negation = simple_selector.add(new ASTNode(ASTNode.Type.NEGATION));
		
		if(!_simple_selector(negation)){
			error("Missing selector.");
			return false;
		}
		return true;
	}

	//hash = '#' IDENT
	private boolean _hash(ASTNode simple_selector) {
		if(optional(HASH) == null){
			return false;
		}
		simple_selector.add(new ASTNode(ASTNode.Type.HASH)).data = mandatory(IDENT,"Missing identifier").value;
		return true;
	}
	
	//context = '@' IDENT
	private boolean _context(ASTNode simple_selector) {
		if(optional(AT) == null){
			return false;
		}
		simple_selector.add(new ASTNode(ASTNode.Type.CONTEXT)).data = mandatory(IDENT,"Missing context identifier").value;
		return true;
	}
	
	//filter= '|' function '|'
	private boolean _filter(ASTNode simple_selector) {
		if(optional(VBAR) == null){
			return false;
		}
		optional(SPC);
		
		ASTNode filter = simple_selector.add(new ASTNode(ASTNode.Type.FILTER));
		
		if(!_function(filter)){
			error("Expection function ");
		}
		
		optional(SPC);
		mandatory(VBAR, "Expecting '|'");
		return true;
	}
	
	//function = simple_selector? '(' name parameters ')'
	private boolean _function(ASTNode parent) {
		
		ASTNode fn = parent.add(new ASTNode(ASTNode.Type.FUNCTION));
		
		if(_simple_selector(fn)){
			optional(SPACE);
			if(!_function_call(fn)){
				error("Expecting '('");
				return false;
			}
			
		} else {
			if(!_function_call(fn)){
				fn.detach();
				return false;
			}
		}
		
		return true;
	}
	
	public boolean _function_call(ASTNode fn){
		if(optional(L_ROUND) == null){
			return false;
		}
		
		optional(SPACE);
		
		fn.add(new ASTNode(ASTNode.Type.NAME)).data = mandatory(IDENT,"Missing function name").value;
		
		ASTNode p = fn.add(new ASTNode(ASTNode.Type.PARAMETER));
		boolean hasParameter = false;
		while(token != null) {
			if(optional(SPACE) != null){
				if(_value(p)) {
					hasParameter = true;
					continue;
				}
			}
			break;
		}
		if(!hasParameter) {
			p.detach();
		}
		mandatory(R_ROUND,"Expecting ')'");
		return true;
	}

	//block_or_vector = block vector | block | vector
	private boolean _block_or_vector(ASTNode rule) {
		if(!_block(rule)){
			if(!_vector(rule)){
				return false;
			}
		} else {
			optional(SPC);
			_vector(rule);
		}
		return true;
	}

	//block = '{' block_items '}'
	//block_items = block_item | block_items |
	private boolean _block(ASTNode rule) {
		if(optional(L_BRACE) == null){
			return false;
		}
		
		ASTNode block = rule.add(new ASTNode(ASTNode.Type.BLOCK));
		
		while(token != null && !R_BRACE.equals(token)) {
			optional(SPC);
			if(! _block_item(block)) {
				error("Invalid block item");
			}
			optional(SPC);
		}

		mandatory(R_BRACE,"Missing '}'");
		return true;
	}
	
	//block_item = rule | declaration
	private boolean _block_item(ASTNode block) {
		if(!_declaration(block)){
			if(!_rule(block)){
				return false;
			}
		}
		return true;
	}

	//declaration = property '=' value ';'
	private boolean _declaration(ASTNode block) {
		if(token == null || !IDENT.equals(token.type) || !(EQUAL.equals(lookAhead[0]) || EQUAL.equals(lookAhead[1]) )) {
			return false;
		}
		
		ASTNode declaration = block.add(new ASTNode(ASTNode.Type.DECLARATION));

		declaration.add(new ASTNode(ASTNode.Type.ATTRIBUTE)).data = mandatory(IDENT, "Missing property").value;
		
		optional(SPC);
		
		mandatory(EQUAL,"Expecting '='");
		
		optional(SPC);
		
		if(!_value(declaration)){
			error("Expecting value.");
			return false;
		}
		
		optional(SPC);
		
		mandatory(SEMICOLON,"Expecting ';'");
		return true;
	}

	//value = boolean | string | integer | float | function | vector
	private boolean _value(ASTNode declaration) {
		Lexer.Token tk = optional (BOOLEAN,STRING,INTEGER);
		if(tk == null) {
			if(!_function(declaration)){
				if(!_vector(declaration)){
					return false;
				}
			}
		} else {
			ASTNode value = declaration.add(new ASTNode(ASTNode.Type.VALUE));
			switch(tk.type){
			case BOOLEAN:
				value.data = Boolean.parseBoolean(tk.value);
				break;
			case FLOAT:
				value.data = Float.parseFloat(tk.value);
				break;
			case INTEGER:
				value.data = Integer.parseInt(tk.value);
				break;
			default:
				value.data = tk.value;
			
			}
		}
		
		return true;
	}

	//vector = '[' SPC* vector_item+ [ SPC* ',' SPC* _vector_item+ SPC*]* ']'
	private boolean _vector(ASTNode parent) {
		if(optional(L_BRACKET) == null){
			return false;
		}
		
		ASTNode vector = parent.add(new ASTNode(ASTNode.Type.VECTOR));
		
		
		boolean first = true;
		while(token != null && !R_BRACKET.equals(token)) {
			boolean required = false;
			optional(SPC);
			if(!first) {
				required = optional(COMMA) != null;
			}
			optional(SPC);
			if(!_vector_item(vector)){
				if(required){
					error("Expecting vector element");
				}
				break;
			}
			
			first = false;
		}
		
		mandatory(R_BRACKET,"Missing ']'");
		
		return true;
	}	
	
	//vector_item = simple_selector | simple_rule | function
	private boolean _vector_item(ASTNode block_item) {
		
		ASTNode p = block_item.add(new ASTNode(ASTNode.Type.SELECTOR));

		if(_simple_selector(p)){
			optional(SPC);
			if(_block_or_vector(p)){
				p.type = ASTNode.Type.SIMPLE_RULE;
			} else {
				if(_function_call(p)){
					p.type = ASTNode.Type.FUNCTION;
				} else {
					block_item.add(p.children.get(0));
					p.detach();
				}
			}
		} else {
			p.detach();
			if(!_value(block_item)){
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args){
		
		String code = 

				"$module data; $import base_module;"
				+ " ParentRule#myid@mycontext Z {"
				+ "  ChildRule { a = 'b'; }"
				+ "}"
				+ "LinearLayout, B, C > D {\n" +
						"    layout_width = 'match_parent';\n" +
						"    layout_height = 100 ;\n" +
						"    paddingLeft = ['a','b','c',[1,2,3]];\n" +
						"    paddingRight = '16dp';\n" +
						"    orientation = 'vertical'; } [\n" +
						"		EditText {" +
						"			layout_width='match_parent';" +
						"			layout_height='wrap_content';" +
						"			hint='@string/to';} ," +
						"		EditText { " +
						"			layout_width='match_parent';" +
						"			layout_height='wrap_content';" +
						"			hint='@string/subject'; } ," +
						"		EditText {" +
						"			layout_width='match_parent';" +
						"			layout_height='0dp';" +
						"			layout_weight='1';" +
						"			gravity='top';" +
						"			hint='@string/message'; } ," +
						"		Button {" +
						"			layout_width='100dp';" +
						"			layout_height='wrap_content';" +
						"			layout_gravity='right';" +
						"			text='@string/send'; }" +
						"]" ;
						
		
		System.out.println(new Parser(code).parse().toString());
	}
}
