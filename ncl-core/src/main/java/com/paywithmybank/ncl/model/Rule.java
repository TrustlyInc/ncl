package com.paywithmybank.ncl.model;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import com.paywithmybank.ncl.Module;
import com.paywithmybank.ncl.parser.nodes.ASTNode;

public class Rule {
	Rule parent;
	private List<Selector> selectors;
	private List<Declaration> declarations;
	private List<Rule> children;
	private List<Rule> vector;
	private int order;
	
	
	
	public Rule(int order) {
		this.setOrder(order);
	}
	
	public Rule(Rule rule, int order) {
		this.parent = rule.parent;
		this.setSelectors(rule.getSelectors());
		this.setChildren(rule.getChildren());
		this.setVector(rule.getVector());
		this.setDeclarations(rule.getDeclarations());
		this.setOrder(order);
	}

	//Only simple rules can live inside rule vector
	public boolean isSimple(){
		return getSelectors() != null && getSelectors().size() == 1 && getSelectors().get(0).isSimple();
	}

	public Rule addVectorRule(Rule r){
		if(!r.isSimple()){
			throw new RuntimeException("Must be a simple rule");
		}
		if(r.parent != null && r.parent.getVector() != null){
			r.parent.getVector().remove(r);
		}
		r.parent = this;
		if(getVector() == null){
			setVector(new ArrayList<>());
		}
		getVector().add(r);
		return this;
	}
	
	public Rule addChildrenRule(Rule r){
		if(r.parent != null && r.parent.getVector() != null){
			r.parent.getVector().remove(r);
		}
		r.parent = this;
		if(getChildren() == null){
			setChildren(new ArrayList<>());
		}
		getChildren().add(r);
		return this;
	}
	
	public Rule addDeclaration(Declaration d){

		if(getDeclarations() == null){
			setDeclarations(new ArrayList<>());
		}
		getDeclarations().add(d);
		return this;
	}
	
	public Rule addSelector(Selector selector) {
		if(getSelectors() == null) {
			setSelectors(new ArrayList<>());
		}
		getSelectors().add(selector);
		return this;
	}
	
	public int matches(Node node) {
		return matches(node,false);
	}
	
	int matches(Node node,boolean isParent) {
		int specificity = 0;
		
		if(getSelectors() != null){
			for(Selector sel : getSelectors()){
				if(sel.matches(node,isParent)){
					int s = sel.specificity();
					if(s > specificity){
						specificity = s;
					}
				}
			}
		}
		
		if(specificity > 0 && parent != null){
			int s = parent.matches(node.parent,true);
			if (s == 0) return 0;
			
			specificity += s;
		}
		return specificity;
	}
	
	public Node createNode(Module module, Node parent) {
		Node node = new Node();
		Selector selector = getSelectors().get(0);
		node.nodeName = selector.getTag();
		node.id = selector.getId();
		node.contexts.addAll(selector.getContexts());
		parent.add(node);
		
		//Get list of matched rules ordered by priority
		SortedSet<OrderedRule> matched = module.findMatchedRules(node);
		
		OrderedRule local = new OrderedRule(this,this.matches(node, true));
		matched.add(local);
		
		List<Rule> children = null;
		for(OrderedRule or : matched){
			Rule r = or.rule;
			//System.out.println(or);
			//Apply the declarations
			if(r.getDeclarations() != null){
				for(Declaration d : r.getDeclarations()){
					node.setIfNotContains(d.getName(), d.getValue());
				}
			}
			
			if(children == null && r.getVector() != null){
				children = r.getVector();
			}
		}
		
		//Create children
		if(children != null){
			for(Rule child : children){
				node.add(child.createNode(module, node));
			}
		}
		return node;
	}
	
	 public String toString() {

    	StringBuilder sb = new StringBuilder();
    	
    	if(parent != null){
    	//	sb.append(parent.toString());
    	//	sb.append(" > ");
    	}
    	boolean first = true;
    	if(getSelectors() != null) {
    		for(Selector selector : getSelectors()){
    			if(!first){
    				sb.append(", ");
    			} else {
    				sb.append("    ");
    			}
    			sb.append(selector.toString());
    			first = false;
    		}
    	}
    	
    	if(getDeclarations() != null || getChildren() != null){
    		sb.append("{\n");
    		if(getChildren() != null) {
    			for(Rule r : getChildren()){
    				sb.append(r.toString());
    				sb.append("\n");
    			}
    		}
    		if(getDeclarations() != null) {
    			for(Declaration d : getDeclarations()){
    				sb.append(d.name);
    				sb.append(" = ");
    				sb.append(d.value);
    				sb.append(";\n");
    			}
    		}
    		sb.append("\n}");
    	}
    	
    	if(getVector() != null){
    		sb.append("[\n");
    		first = true;
    		for(Rule r : getVector()){
				if(!first){
					sb.append(",\n");
				}
				sb.append(r.toString());
				first = false;
			}
    		sb.append("]");
    	}

    	return sb.toString();
    }

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public List<Rule> getChildren() {
		return children;
	}

	public void setChildren(List<Rule> children) {
		this.children = children;
	}

	public List<Rule> getVector() {
		return vector;
	}

	public void setVector(List<Rule> vector) {
		this.vector = vector;
	}

	public List<Selector> getSelectors() {
		return selectors;
	}

	public void setSelectors(List<Selector> selectors) {
		this.selectors = selectors;
	}

	public List<Declaration> getDeclarations() {
		return declarations;
	}

	public void setDeclarations(List<Declaration> declarations) {
		this.declarations = declarations;
	}

}
