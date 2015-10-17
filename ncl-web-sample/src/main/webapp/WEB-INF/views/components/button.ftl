<#macro @button>
	<@render_button .node.id!, css_classes(.node), .node.label!, (.node.iconClass)!/>
</#macro>

<#macro render_button id classes label="Button Label" iconClass="">
	<button type="submit" <@print_id id!/><@print_class classes/>>
	<#if iconClass != ""><i class="${iconClass}"></i> </#if>${('"'+label+'"')?eval}
	</button>
</#macro>