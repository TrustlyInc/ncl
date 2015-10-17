package com.paywithmybank.ncl.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.paywithmybank.ncl.parser.Parser;
import com.paywithmybank.ncl.parser.nodes.ASTNode;

public class Node {
    String id;
    String nodeName;
    Set<String> contexts = new HashSet<>();
	Map<String,Object> attributes = new HashMap<>();
	Node parent = null;
	List<Node> children;
	
	public Node() {
		super();
	}
	
	public Node(String str) {
		Parser parser = new Parser(str);
		ASTNode selector = new ASTNode(ASTNode.Type.EXPRESSIONS);
		if(!parser._simple_selector(selector)){
			parser.error("Expecting simple selector");
		}
		
		ASTNode node = selector.get(ASTNode.Type.SIMPLE_SELECTOR);
		

		nodeName = (String) node.getChildNodeData(ASTNode.Type.TAG);
		id = (String) node.getChildNodeData(ASTNode.Type.HASH);
		
		List<ASTNode> ctx = node.list(ASTNode.Type.CONTEXT);
		
		if(contexts != null){
			for(ASTNode n : ctx){
				contexts.add((String)n.data);
			}
		}

	}

	public Node add(Node child) {
		if(children == null){
			children = new ArrayList<>();
		}
		if(child.parent != null){
			child.parent.children.remove(child);
		}
		child.parent = this;
		children.add(child);
		
		return this;
	}
	
	public String getId() {
		return id;
	}

	public Node setId(String id) {
		this.id = id;
		return this;
	}

	public String getType() {
		return (String) get("type");
	}

	public Node setType(String type) {
		set("type",type);
		return this;
	}

	public Node addContext(String context){
		if(contexts == null){
			contexts = new HashSet<>();
		}
		contexts.add(context);
		return this;
	}

	public Node setNodeName(String nodeName) {
		this.nodeName = nodeName;
		return this;
	}



	public Node set(String key, Object value) {
		if(attributes == null){
			attributes = new HashMap<>();
		}
		attributes.put(key, value);
		
		return this;
	}
	
	public Object get(String key){
		return attributes != null ? attributes.get(key) : null;
	}
	
	public boolean isAttributesEmpty(){
		return attributes == null || attributes.isEmpty();
	}
	
	public Node setIfNotContains(String key, Object value){
		if(attributes == null){
			attributes = new HashMap<>();
			attributes.put(key, value);
		} else {
			if(!attributes.containsKey(key)){
				attributes.put(key, value);
			}
		}
		return this;
	}
	
	Node root() {
		Node root = this;
		while(root.parent != null){
			root = root.parent;
		}
		return root;
	}

	public String getNodeName(){
		return nodeName;
	}

	public String getNodeNamespace()  {
		return null;
	}
	
	public List<Node> getChildren(){
		return children;
	}

	public Node getParentNode()  {
		return parent;
	}

	public Set<String> getContexts() {
		return contexts;
	}
	
	public String toString() {
		return toString(0);
	}
	protected String toString(int nest) {
    	char[] spc = new char[nest];
    	for(int i = 0; i < nest; i++) spc[i] = ' ';
    	
    	StringBuilder sb = new StringBuilder();
    	sb.append(spc); sb.append(nodeName);
    	if(id != null) {
			sb.append("#");
			sb.append(id);
		}
    	if(contexts != null){
    		for(String context : contexts){
    			sb.append("@");
    			sb.append(context);
    		}
    	}
    	
    	if(attributes != null && !attributes.isEmpty()) {
    		sb.append("{");
    		if(attributes != null) {
	    		for(Map.Entry<String,Object> e :attributes.entrySet()){
	    			sb.append(e.getKey());
	    			sb.append(" = ");
	    			sb.append(e.getValue().toString());
	    			sb.append("; ");
	    		}
    		}
    		sb.append("}");
    	}
    	
    	if(children != null && children.size() > 0){
    		boolean first = true;
    		sb.append("[\n");
    		for(Node n : children){
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
