<#macro @bank_selector>
	<@render_bank_selector .node.id!, css_classes(.node), .node.pinned!""/>
</#macro>

<#macro render_bank_selector id classes pinned>
	<#if id?has_content><#assign bindclass = id></#if>
	<div <@print_id id!/><@print_class classes/> >
		<div class="search">
			<input type="text" class="search-input" value="" placeholder="Select your bank" autocomplete="off"/>
		</div>
		<div class="empty-message">Your search did not match any banks.</div>
		<div class="content">
			<#list paymentProviders as pp>
				<#assign paymentProviderName=pp.name>
				<#if pp.shortName?? && pp.shortName! != "">
					<#assign paymentProviderName=pp.shortName>
				</#if>
				<div class="fic" data-fic="${pp.paymentProviderId!}">${paymentProviderName!}</div>
			</#list>
		</div>
	</div>
</#macro>