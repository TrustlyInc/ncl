 <#-- Default macro. Routes if type is not defined.
      It will try to use a macro for a type with same name as the node.
      If not found will render a div and recurse the inner elements -->
<#macro @default>
	<#if .vars['@' + .node.name + '$' + .node.id! ]??>
		<#assign m = .vars['@' + .node.name + '$' + .node.id! ] >
		<@m/>
	<#else>
		<#if .vars['@'+.node.name]??>
			<#assign m = .vars['@'+.node.name] >
			<@m/>
		<#else>
			<div <@attributes .node!/>><#if .node.text??>${('"'+.node.text+'"')?eval}</#if><#recurse/></div>
		</#if>
	</#if>
</#macro>


<#-- Helper macro. 
     Prints the id and class names for an element based on node id, name and contexts -->
<#function css_classes node additional...>
	<#assign cls = ["component"]>
	<#if node.type??>
		<#assign cls = cls + [node.type]>
		<#if node.type != node.name>
			<#assign cls = cls + [node.name]>
		</#if>
	<#else>
		<#assign cls = cls + [node.name]>
	</#if>
	
	<#if node.contexts?is_sequence>
		<#list node.contexts as class>
			<#assign cls = cls + [class]>
		</#list>
	</#if>
	
	<#if additional?is_sequence>
		<#list additional as class>
			<#if class?is_sequence>
				<#assign cls = cls + class>
			<#else>
				<#assign cls = cls + [class]>
			</#if>
		</#list>
	</#if>
	<#return cls>
</#function>

<#macro print_id id><#if id?has_content> id='${id}' </#if></#macro>
<#macro print_class cl><#if cl?? > class='${cl?join(" ")}' </#if></#macro>

<#macro attributes n cl... ><@print_id n.id!/><@print_class css_classes(n,cl)/></#macro>