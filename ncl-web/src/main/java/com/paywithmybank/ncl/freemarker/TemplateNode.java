package com.paywithmybank.ncl.freemarker;

import com.paywithmybank.ncl.model.Node;

import freemarker.template.AdapterTemplateModel;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNodeModel;
import freemarker.template.TemplateSequenceModel;
import freemarker.template.WrappingTemplateModel;

public class TemplateNode extends WrappingTemplateModel implements TemplateNodeModel, TemplateHashModel, AdapterTemplateModel   {
	Node node;

	@Override
	public TemplateNodeModel getParentNode() throws TemplateModelException {
		return new TemplateNode(node.getParentNode());
	}

	public TemplateNode(Node node) {
		super();
		this.node = node;
	}

	@Override
	public TemplateSequenceModel getChildNodes() throws TemplateModelException {
		return new TemplateSequenceModel() {
			
			@Override
			public int size() throws TemplateModelException {
				return node.getChildren() != null ? node.getChildren().size() : 0;
			}
			
			@Override
			public TemplateModel get(int index) throws TemplateModelException {
				return node.getChildren() != null ? new TemplateNode(node.getChildren().get(index)) : null;
			}
		};
	}

	@Override
	public String getNodeName() throws TemplateModelException {
		return node.getNodeName();
	}

	@Override
	public String getNodeType() throws TemplateModelException {
		return node.getType();
	}

	@Override
	public String getNodeNamespace() throws TemplateModelException {
		return node.getNodeNamespace();
	}

	@Override
	public TemplateModel get(String key) throws TemplateModelException {
		if(node == null) return null;
		
		Object obj = null;
		
		switch(key){
			case "id":
			return wrap(node.getId());
			
			case "name":
			return wrap(getNodeName());
			
			case "type":
			return wrap(getNodeType());
			
			case "contexts":
			return wrap(node.getContexts());
		}

		obj = node != null ? node.get(key) : null;
		return obj != null ? wrap(obj) : null;
	}

	@Override
	public boolean isEmpty() throws TemplateModelException {
		return node.getChildren() == null || node.getChildren().isEmpty();
	}

	@Override
	public Object getAdaptedObject(Class hint) {
		return node;
	}
}
