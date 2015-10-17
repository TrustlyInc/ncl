package com.paywithmybank.ncl.parser.nodes;

import java.util.ArrayList;
import java.util.List;


public class ASTNode {
	public static enum Type {
        NCL, DIRECTIVE, EXPRESSIONS, EXPRESSION, IMPORT, 
        RULE, SELECTORS_GROUP, BLOCK_OR_VECTOR, SELECTOR, 
        COMBINATOR, SIMPLE_SELECTOR, TAG, HASH, CONTEXT, BLOCK, 
        VECTOR, NEGATION, MODULE, DECLARATION, VALUE, ATTRIBUTE, FILTER, PARAMETER, FUNCTION, NAME, SIMPLE_RULE
    }
	
    public ASTNode.Type type;
    public ASTNode parent;
    public List<ASTNode> children;
    public Object data;

    public ASTNode(ASTNode.Type t) {
        this.type = t;
        parent = null;
        children = new ArrayList<>();
    }
    
    public ASTNode add(ASTNode n) {
    	if(n != null) {
    		if(n.parent != null){
    			n.parent.remove(n);
    		}
        	n.parent = this;
        	children.add(n);
    	}
    	
    	return n;
    }
    
    public ASTNode child(int idx){
    	if(children != null) {
    		return children.get(idx);
    	}
    	
    	return null;
    }
    
    public Object getChildNodeData(ASTNode.Type t) {
    	ASTNode n = get(t);
    	if(n != null){
    		return  n.data;
    	}
    	return null;
    }
    
    public ASTNode get(ASTNode.Type t) {
    	for(ASTNode n : children){
    		if(n.type == t) {
    			return n;
    		}
    	}
    	return null;
    }
    
    public List<ASTNode> list(ASTNode.Type t) {
    	List<ASTNode> l = new ArrayList<>();
    	for(ASTNode n : children){
    		if(n.type == t) {
    			l.add(n);
    		}
    	}
    	return l;
    }
    
    public void addAllChildren(ASTNode n){
    	if(n != null && n.children != null) {
    		for(ASTNode a : n.children){
    			add(a);
    		}
    	}
    }
    
    public ASTNode remove(ASTNode n){
    	if(n!= null) {
    		if(children.remove(n)){
    			n.parent = null;
    		}
    	}
    	return n;
    }
    
    public ASTNode detach() {
    	if(parent != null){
    		parent.remove(this);
    	}
    	
    	return this;
    }
    
    public String toString() {
    	
    	return toString(0);
    }
    
    protected String toString(int nest) {
    	char[] spc = new char[nest];
    	for(int i = 0; i < nest; i++) spc[i] = ' ';
    	
    	StringBuilder sb = new StringBuilder();
    	sb.append(spc); sb.append(type);
    	if(data != null) {
    		sb.append("{ data = ");
    		sb.append(data.toString());
    		sb.append("}");
    	}
    	
    	if(children.size() > 0){
    		boolean first = true;
    		sb.append("[\n");
    		for(ASTNode n : children){
    			if(first) {
    				first = false;
    			} else {
    				sb.append(",\n");
    			}
    			sb.append(n.toString(nest+2));
    		}
    		sb.append("\n");sb.append(spc);sb.append("]");
    	}
    	return sb.toString();
    }
}