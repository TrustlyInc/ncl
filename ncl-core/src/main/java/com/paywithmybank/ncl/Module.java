package com.paywithmybank.ncl;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import com.paywithmybank.ncl.model.Declaration;
import com.paywithmybank.ncl.model.Node;
import com.paywithmybank.ncl.model.OrderedRule;
import com.paywithmybank.ncl.model.Rule;
import com.paywithmybank.ncl.model.Selector;
import com.paywithmybank.ncl.model.Selector.Composite;
import com.paywithmybank.ncl.model.Source;
import com.paywithmybank.ncl.parser.Parser;
import com.paywithmybank.ncl.parser.nodes.ASTNode;

public class Module {
	String name;
	Source source;
	List<String> imports;
	int pos = 0;
	NCL ncl;
	
	List<Rule> otherRules;
	Map<String,List<Rule>> rulesById; //Has id
	Map<String,List<Rule>> rulesByContext; //Has context
	Map<String,List<Rule>> rulesByTag; //Has tag
	
	public Module(Source source, NCL ncl) {
		this.ncl = ncl;
		this.source = source;
		ASTNode ast = new Parser(source).parse();
		processExpressions(ast);
		
	}
	
	public Module(String s, NCL ncl) {
		this(new Source("String", new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8))),ncl);
	}
	
	private void processExpressions(ASTNode ast) {
		for(ASTNode expression : ast.children) {
			
			switch(expression.type){
				case DIRECTIVE:
					processDirective(expression);
				break;
				
				case RULE:
					processRule(expression);
				break;
				
				default:
			}
		}
	}

	private void processRule(ASTNode astRule) {
		addRule(createRule(astRule));
		
	}
	
	private Rule createRule(ASTNode astRule) {
		Rule rule = new Rule(pos++);
		
		processSelectorGroup(rule,astRule.get(ASTNode.Type.SELECTORS_GROUP).children);
		
		ASTNode block = astRule.get(ASTNode.Type.BLOCK);
		processRuleBlock(rule,block);
		
		ASTNode vectorNode = astRule.get(ASTNode.Type.VECTOR);
		processRuleVector(rule, vectorNode);
		
		return rule;
	}
	
	private void processRuleBlock(Rule rule, ASTNode block) {
		if(block != null){
			for(ASTNode blockItemNode : block.children){
				switch(blockItemNode.type){
					case DECLARATION:
						rule.addDeclaration(createDeclaration(blockItemNode));
					break;
					case RULE:
						rule.addChildrenRule(createRule(blockItemNode));
					default:
				}
			}
		}
	}

	private Declaration createDeclaration(ASTNode blockItemNode) {
		Object value = null;
		String name = (String) blockItemNode.getChildNodeData(ASTNode.Type.ATTRIBUTE);
		
		ASTNode v = blockItemNode.child(1);
		switch(v.type){
			case VALUE:
				value = v.data;
			break;
			case VECTOR:
				value = createVector(v);
			break;
		}
		
		return new Declaration(name,value);
	}

	private Object createVector(ASTNode v) {
		List<Object> vector = new ArrayList<>();
		for(ASTNode child : v.children){
			switch(child.type){
				case VALUE:
					vector.add(child.data);
				break;
				case VECTOR:
					vector.add(createVector(child));
				break;
			}
		}
		return vector;
	}

	private void processRuleVector(Rule rule, ASTNode vectorNode) {
		
		if(vectorNode != null){
			for(ASTNode simpleRuleNode : vectorNode.children){
				rule.addVectorRule(createSimpleRule(simpleRuleNode));
			}
		}
	}

	private Rule createSimpleRule(ASTNode simpleRuleNode) {
		Rule rule = new Rule(pos++);
		
		switch(simpleRuleNode.type){
			case SIMPLE_RULE:
				rule.addSelector(createSimpleSelector(simpleRuleNode.get(ASTNode.Type.SIMPLE_SELECTOR)));
				
				ASTNode block = simpleRuleNode.get(ASTNode.Type.BLOCK);
				processRuleBlock(rule,block);
				
				ASTNode vectorNode = simpleRuleNode.get(ASTNode.Type.VECTOR);
				processRuleVector(rule, vectorNode);
			break;
			case SIMPLE_SELECTOR:
				rule.addSelector(createSimpleSelector(simpleRuleNode));
			break;
			default:
		}
		
		
		return rule;
	}

	private void processSelectorGroup(Rule rule, List<ASTNode> selectorsGroupNodes){
		for(ASTNode selectorNode : selectorsGroupNodes) {
			rule.addSelector(createSelector(selectorNode));
		}
	}

	private Selector createSelector(ASTNode selectorNode) {
		Selector selector = createSimpleSelector(selectorNode.children.get(selectorNode.children.size()-1));
		
		for(int i = selectorNode.children.size()-2; i >= 0; i--){
			Composite composite = new Composite();
			
			composite.setType(Composite.Type.PARENT);
			
			ASTNode n = selectorNode.children.get(i);
			
			if(n.type == ASTNode.Type.COMBINATOR){
				switch((String) n.data) {
					case "+":
						composite.setType(Composite.Type.IMEDIATE_SIBLING);
						break;
					case ">":
						composite.setType(Composite.Type.IMEDIATE_PARENT);
						break;
					case "~":
						composite.setType(Composite.Type.SIBLING);
						break;
				}
				i--;
				n = selectorNode.children.get(i);
			}
			
			
			if(n.type == ASTNode.Type.SIMPLE_SELECTOR){
				composite.setSelector(createSimpleSelector(n));
			}

			selector.addComposite(composite);
		}
		
		return selector;
	}
	
	private Selector createSimpleSelector(ASTNode node){
		Selector selector = new Selector();
		selector.setPos(pos++);
		selector.setTag((String) node.getChildNodeData(ASTNode.Type.TAG));
		selector.setId((String) node.getChildNodeData(ASTNode.Type.HASH));
		
		List<ASTNode> contexts = node.list(ASTNode.Type.CONTEXT);
		
		if(contexts != null){
			selector.setContexts(new HashSet<>());
			for(ASTNode n : contexts){
				selector.getContexts().add((String)n.data);
			}
		}
		return selector;
	}

	private void processDirective(ASTNode directive) {
		for(ASTNode d : directive.children) {
			
			switch(d.type){
				case MODULE:
					processModule(d);
				break;
				
				case IMPORT:
					processImport(d);
				break;
				
				default:
			}
		}
		
	}

	private void processModule(ASTNode d) {
		if(name != null){
			error("Module already has a name");
		}
		name = (String) d.data;
		
	}

	private void error(String msg) {
		throw new RuntimeException(msg + ". Source: " + source.getName());
		
	}

	private void processImport(ASTNode d) {
		String moduleName = (String)d.data;
		Module m = ncl.getModule(moduleName);
		if(m == null){
			error("Could not find module " + moduleName);
			return;
		}
		
		if(imports == null){
			imports = new ArrayList<>();
		}
		imports.add(moduleName);
		
		int count = 0;
		if(m.rulesById != null){
			for(List<Rule> lst : m.rulesById.values()){
				for(Rule r : lst){
					count++;
					addRule(new Rule(r,pos + r.getOrder() ));
				}
			}
		}
		
		if(m.rulesByContext != null){
			for(List<Rule> lst : m.rulesByContext.values()){
				for(Rule r : lst){
					count++;
					addRule(new Rule(r,pos + r.getOrder() ));
				}
			}
		}
		
		if(m.rulesByTag != null){
			for(List<Rule> lst : m.rulesByTag.values()){
				for(Rule r : lst){
					count++;
					addRule(new Rule(r,pos + r.getOrder() ));
				}
			}
		}
		
		if(m.otherRules != null){
			for(Rule r : m.otherRules){
				count++;
				addRule(new Rule(r,pos + r.getOrder() ));
			}
		}
		
		pos += count;
	}

	Module addRule(Rule rule) {
		if(rule.getChildren() != null){
			for(Rule r : rule.getChildren()){
				addRule(r);
			}
		}
		
		if(rule.getVector() != null){
			for(Rule r : rule.getVector()) {
				if(r.getChildren() != null){
					for(Rule r2 : r.getChildren()){
						addRule(r2);
					}
				}
			}
		}
		
		for(Selector selector : rule.getSelectors()){
			if(selector != null){
				if(selector != null){
					if(selector.getId() != null){
						if(rulesById == null) {
							rulesById = new HashMap<>();
						}
						List<Rule> idRules = rulesById.get(selector.getId());
						if(idRules == null){
							idRules = new ArrayList<>();
							rulesById.put(selector.getId(), idRules);
						}
						if(!idRules.contains(rule)){
							idRules.add(rule);
						}
					}
					else if(selector.getTag() != null && selector.getTag().charAt(0) != '*'){
						if(rulesByTag == null) {
							rulesByTag = new HashMap<>();
						}
						List<Rule> tagRules = rulesByTag.get(selector.getTag());
						if(tagRules == null){
							tagRules = new ArrayList<>();
							rulesByTag.put(selector.getTag(), tagRules);
						}
						if(!tagRules.contains(rule)){
							tagRules.add(rule);
						}
					}
					else if(selector.getContexts() != null && !selector.getContexts().isEmpty()){
						if(rulesByContext == null) {
							rulesByContext = new HashMap<>();
						}
						
						for(String context : selector.getContexts()) {
							List<Rule> contextRules = rulesByContext.get(context);
							if(contextRules == null){
								contextRules = new ArrayList<>();
								rulesByContext.put(context, contextRules);
							}
							if(!contextRules.contains(rule)){
								contextRules.add(rule);
							}
						}
					}
					else {
						if(otherRules == null){
							otherRules = new ArrayList<>();
						}
						otherRules.add(rule);
					}
				}
			}
		}
		return this;
	}
	
	public SortedSet<OrderedRule> findMatchedRules(Node node) {
		TreeSet<OrderedRule> matched = new TreeSet<>();
		
		HashSet<Rule> candidates = new HashSet<>();
		if(node.getId() != null && rulesById != null) {
			List<Rule> l = rulesById.get(node.getId());
			if(l != null) {
				candidates.addAll(l);
			}
		}
		
		if(node.getNodeName() != null && rulesByTag != null) {
			List<Rule> l = rulesByTag.get(node.getNodeName());
			if(l != null) {
				candidates.addAll(l);
			}
		}
		
		if(node.getContexts() != null && rulesByContext != null && !node.getContexts().isEmpty()){
			for(String c : node.getContexts()){
				List<Rule> l = rulesByContext.get(c);
				if(l != null) {
					candidates.addAll(l);
				}
			}
		}
		
		if(otherRules != null){
			candidates.addAll(otherRules);
		}
		
		for(Rule r : candidates){
			int s = r.matches(node);
			if(s > 0){
				OrderedRule or = new OrderedRule(r, s);
				matched.add(or);
			}
		}
		
		return matched;
	}
	
	public Node getNode(String selector){
		Node node =  new Node(selector);
		applyRules(node);
		return node;
	}
	
	public void applyRules(Node node){
		//Get list of matched rules ordered by priority
		SortedSet<OrderedRule> matched = findMatchedRules(node);
		
		List<Rule> children = null;
		for(OrderedRule or : matched){
			Rule r = or.rule;
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
				node.add(child.createNode(this, node));
			}
		}
	}
	
	public String toString(){

		StringBuilder sb = new StringBuilder();
		sb.append("[module ");
		sb.append(name);
		sb.append("\n");

		if(rulesByTag != null) {
			for(Map.Entry<String, List<Rule>> l : rulesByTag.entrySet()){
				sb.append("  ");
				sb.append(l.getKey());
				sb.append(" -> ");
				for(Rule r : l.getValue()){
					sb.append(r.toString());
					sb.append("\n");
				}
			}
		}
		
		if(rulesById != null) {
			for(Map.Entry<String, List<Rule>> l : rulesById.entrySet()){
				sb.append("  ");
				sb.append("#");
				sb.append(l.getKey());
				sb.append(" -> ");
				for(Rule r : l.getValue()){
					sb.append(r.toString());
					sb.append("\n");
				}
			}
		}
		
		if(rulesByContext != null) {
			for(Map.Entry<String, List<Rule>> l : rulesByContext.entrySet()){
				sb.append("  ");
				sb.append("@");
				sb.append(l.getKey());
				sb.append(" -> ");
				for(Rule r : l.getValue()){
					sb.append(r.toString());
					sb.append("\n");
				}
			}
		}
		
		if(otherRules != null) {
			for(Rule r : otherRules){
				sb.append("  ");
				sb.append(r.toString());
				sb.append("\n");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	public String getName(){
		return name;
	}
}
