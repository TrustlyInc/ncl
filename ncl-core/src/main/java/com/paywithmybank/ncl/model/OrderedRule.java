package com.paywithmybank.ncl.model;

public class OrderedRule  implements Comparable<OrderedRule>{
	public Rule rule;
	public int specificity;
	
	public OrderedRule(Rule rule, int specificity) {
		super();
		this.rule = rule;
		this.specificity = specificity;
	}

	@Override
	public int compareTo(OrderedRule or) {
		int result = -(specificity - or.specificity);
		
		if (result == 0){
			if(!rule.equals(or.rule)){
				result = or.rule.getOrder() - rule.getOrder();
				
				if(result == 0) {
					result = 1;
				}
			}
		}
		
		return result != 0 ? result : 1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rule == null) ? 0 : rule.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderedRule other = (OrderedRule) obj;
		if (rule == null) {
			if (other.rule != null)
				return false;
		} else if (!rule.equals(other.rule))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "specificity: " + specificity + " -> " + rule;
	}
	
	
	
	
}
