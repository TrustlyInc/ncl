<#macro @image>
	<@render_image .node.id!, css_classes(.node), .node.src!/>
</#macro>

<#macro render_image id classes src>
	<img src="${('"'+src+'"')?eval}" <@print_id id!/><@print_class classes/> />
</#macro>