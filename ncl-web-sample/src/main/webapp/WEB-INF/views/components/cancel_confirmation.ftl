<#macro @cancel_confirmation>
	<@render_cancel_confirmation .node.id!, css_classes(.node)/>
</#macro>

<#macro render_cancel_confirmation id classes>
<div <@print_id id!/><@print_class classes/> >
    <#recurse/>
</div>
</#macro>