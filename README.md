NCL - New Configuration Language
================================

NCL is a new configuration and composition language inspired on CSS.

The main idea is to use CSS like rules to create the data. So it is possible to use rules/templates and override the configuration and the composition of components or any other kind of data.

For a complete example see the file [panel.ncl](https://github.com/PayWithMyBank/ncl/blob/master/ncl-web-sample/src/main/resources/panel.ncl).


# Why

NCL is used inside PayWithMyBank for UI A/B testing and to offer custom versions of our authentication panel.

We wanted a easy way to create variations on the UI and not rely on custom program code or  slight different copies of a template that you have to maintain.

CSS can be used to customize some elements of the UI but not all

# Syntax


## Module

Every NCL module must have a module name. 

A module defined the set of rules that can be used to create an element.

```
 $module my_module;
```

## Rule

A rule is defined by a selector followed by a declaration block and/or a composition block.

### Selector

NCL selectors are used to select elements for rules. The follow most CSS selector rules with a few exceptions (there is no classes but contexts).

Pattern       | Matches              | Example 
------------- | -------------------- | -------------
E             | An element of type E |  panel //Selects a panel element  
E#myid        | An E element with ID equals to "myid" | panel#select_bank //Selects a panel element with ID "select_bank"
E@my_context  | An E element with context "my_context" | panel#select_bank@mobile //Selects a panel element with ID "select_bank" and a "mobile" context.
E F          | An F element descendant of an E element |  panel header //Selects a header element that is inside a panel
E > F          | An F element child of an E element |  panel > header //Selects a header element that is children of a panel

### Declaration Block

A declaration block is used to set properties to an element and/or create nested rules.

A declaration block consists of a list of declarations or nested rules in braces {}.
```
panel#select_bank {
    title = "My title";
    
    header {
        text = 
"   String values can expand to multiple lines.
    This is the second paragraph.";
    }
}
```

```
    name = value;
```

A declaration consists of a property name, an equal (=), a value, and ends on a semicolon (;).

Values can be:

Type    | Format 
--------|--------
string  | "This is a string" 
boolean | true
integer | 1234
float   | 12.56


### Composition Block

A composition block is used to define the element children list.

A composition block consists of a list of simple selectors or simple rules in brackets [] separated by commas (,).

```
$module panel;

panel [
	header,
	section [
		title,
		subtitle,
		content
	],
	footer
]

panel#select_bank {
	content [
		paragraph { text = "My first paragraph.";},
		bank_selector
	]
}
``` 

## Module Import

A module can import other modules to extend or override the rules.

```
 $module panel_experiment_1;
 $import panel;
 
 panel {
	title = "My title";
 }
 
 panel#select_bank {
	content [
		paragraph { text = "My first paragraph on experiment 1. ";}
	]
}
```

## Nested Rules

Nested rules matches descendants of parent rule selector. 

```
panel#select_bank {
    title = "My title";
    
    header {
        text = 
"   String values can expand to multiple lines.
    This is the second paragraph.";
    }
}
```

So the header rule above is the same as:
```
panel#select_bank header {
    text = 
"   String values can expand to multiple lines.
    This is the second line.";
}
```

# Object Creation and Rule Evaluation

The language main power comes when getting configuration objects based on the module defined rules.

Rules are evaluated as templates to create the configuration object.

```
NCL ncl = new NCL();
		
Module module = ncl.getModule("panel_experiment_1");

Node panel = ncl.getNode("page#select_bank");

System.out.println(panel);
```

On this example we are getting the node configuration for "page#select_bank" in the module "panel_experiment_1".

The language will create a node element "page" with ID select_bank" and apply all the rules on the module "panel_experiment_1" and module "panel" (that is imported by module "panel_experiment_1").

The printed result is:

```
panel#select_bank {title="My title";} [
	header,
	section [
		title,
		subtitle,
		content [
			paragraph { text = "My first paragraph on experiment 1. ";},
			bank_selector
		]
	],
	footer
]
```

A correspondent XML would be:
```
<panel id="select_bank" title="My title">
	<header/>
	<section>
		<title/>
		<subtitle/>
		<content>
			<paragraph text ="My first paragraph on experiment 1."/>
			<bank_selector />
		</content>
	</section>
</panel>