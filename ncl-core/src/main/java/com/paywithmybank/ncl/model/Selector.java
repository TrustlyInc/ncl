package com.paywithmybank.ncl.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Selector {
	public static class Composite {
		public static enum Type {
            IMEDIATE_PARENT, // >
            PARENT, // SPC
            IMEDIATE_SIBLING, // +
            SIBLING, // ~
        }
		private Type type;
		private Selector selector;
		
		public String toString(){
			StringBuilder sb = new StringBuilder();
			sb.append(getSelector().toString());
			sb.append(" ");
			
			switch(getType()){
				case IMEDIATE_PARENT:
					sb.append(">");
					break;
				case IMEDIATE_SIBLING:
					sb.append("+");
					break;
				case PARENT:
					sb.append(" ");
					break;
				case SIBLING:
					sb.append("~");
					break;
			
			}
			
			sb.append(" ");
			return sb.toString();
		}

		public Type getType() {
			return type;
		}

		public void setType(Type type) {
			this.type = type;
		}

		public Selector getSelector() {
			return selector;
		}

		public void setSelector(Selector selector) {
			this.selector = selector;
		}
	}
	
	private String tag;
	private String id;
	private Set<String> contexts;
	private Function filter;
	Selector not;
	private Integer pos;
	List<Composite> composites;
	
	Integer specifity = null;
	
	public int specificity(){
		if(specifity == null){
			int v = 0;
			if(getId() != null) v+= 100;
			if(getTag() != null) v += 1;
			if(getContexts() != null) v += getContexts().size() * 10;
			if(filter != null) v += 10;
			if(composites != null){
				for(Composite c : composites){
					v += c.getSelector().specificity();
				}
			}
			specifity = v;
		}
		return specifity;
	}
	
	public boolean matches(Node node){
		return matches(node,composites, 0);
	}
	
	public boolean matches(Node node, boolean parentRule){
		if(parentRule){
			return matchesComposite(node,Selector.Composite.Type.PARENT,composites, 0);
		}
		
		return matches(node,composites, 0);
	}
	
	public void addComposite(Composite c){
		if(composites == null) {
			composites = new ArrayList<>();
		}
		composites.add(c);
	}
	
	public boolean isSimple(){
		return (composites == null || composites.isEmpty()) && not == null && filter == null;
	}
	
	private boolean simpleMatch(Node node){
		if(node == null) return false;
		if(getTag() != null && getTag().charAt(0) != '*' && !getTag().equals(node.nodeName)) return false;
		if(getId() != null && !getId().equals(node.id)) return false;
		if(getContexts() != null && (node.contexts == null || !node.contexts.containsAll(getContexts()))) return false;
		if(not != null && not.matches(node)) return false;
		
		return true;
	}
	private boolean matches(Node node, List<Composite> composites, int compositePos){
		
		if(!simpleMatch(node)) return false;
		
		if(composites == null || compositePos >= composites.size()) return true;
		
		Composite composite = composites.get(compositePos);
		
		return composite.getSelector().matchesComposite(node.parent,composite.getType(),composites,compositePos+1);
	}
	
	private boolean matchesComposite(Node parent, Selector.Composite.Type type,List<Composite> composites, int compositePos){		
		if(parent == null){
			return false;
		}
		
		int i;
		switch(type){
			case IMEDIATE_PARENT: 
				if(matches(parent,composites, compositePos)){
					return true;
				}
				break;
			case IMEDIATE_SIBLING:
				i = parent.children.indexOf(this);
				if(i > 0){
					if(matches(parent.children.get(i-1),composites, compositePos)){
						return true;
					}
				}
				break;
			case PARENT: 
				do {
					if(matches(parent,composites, compositePos)){
						return true;
					}
					parent = parent.parent;
				} while (parent != null);
				break;
			case SIBLING:
				i = parent.children.indexOf(this);
				for(int k = i-1 ; k >=0 ; k--) {
					if(matches(parent.children.get(k),composites, compositePos)){
						return true;
					}
				}
				break;
			default:
				break;
		
		}
		
		return false;
	}
	
	 public String toString() {
		StringBuilder sb = new StringBuilder();
    	if(composites != null){
    		for(int i = composites.size()-1; i >= 0; i--) {
    			Composite c = composites.get(i);
    			sb.append(c.toString());
    		}
    	}
    	
    	if(getTag() != null) {
    		sb.append(getTag());
    	}
    	
    	if(getId() != null) {
    		sb.append("#");
    		sb.append(getId());
    	}
    	
    	if(getContexts() != null){
    		for(String ctx : getContexts()){
    			sb.append("@");
        		sb.append(ctx);
    		}
    	}
    	
    	if(filter != null) {
    		sb.append("|");
    		sb.append(filter.toString());
    		sb.append("|");
    	}
    	
    	if(not != null){
    		sb.append("!");
    		sb.append(not.toString());
    	}
    	
    	return sb.toString();
    }

	public Integer getPos() {
		return pos;
	}

	public void setPos(Integer pos) {
		this.pos = pos;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Set<String> getContexts() {
		return contexts;
	}

	public void setContexts(Set<String> contexts) {
		this.contexts = contexts;
	}
}
