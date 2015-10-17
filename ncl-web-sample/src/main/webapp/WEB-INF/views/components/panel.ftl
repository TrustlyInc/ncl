<#macro @panel>
	<@render_panel .node.id!, css_classes(.node),(.node.action)!"",(.node.cancel_action)!/>
</#macro>

<#macro @panel$accounts>
	<@content for="title">Choose an account</@content>
	<@render_panel .node.id!, css_classes(.node),"${context_path}/aa1","/step/${fiCode!}/authentication/cancel">
	</@render_panel>
</#macro>

<#macro @panel$credentials>
	<#if .node.contexts?seq_contains("partial")>
		<@content for="title">More information needed</@content>
		<@render_panel .node.id!, css_classes(.node),"${context_path}/aa2"/>
	<#else>
		<@content for="title">Log in to your bank</@content>
		<@render_panel .node.id!, css_classes(.node),"${context_path}/aa3"/>
	</#if>
</#macro>

<#macro @panel$ech>
	<@content for="title">Enter bank information</@content>
	<@render_panel .node.id!, css_classes(.node),"${context_path}/aa4">
	</@render_panel>
</#macro>

<#macro @panel$ech_confirmation>
	<@content for="title">Enter bank information</@content>
	<@render_panel .node.id!, css_classes(.node),"${context_path}/aa5">
	</@render_panel>
</#macro>

<#macro @panel$select_bank>
	<@content for="title">Select your Bank</@content>
	<@render_panel .node.id!, css_classes(.node),"${context_path}/aa6">
	</@render_panel>
</#macro>

<#macro render_panel id classes action="" cancel_action="" >
<div <@print_id id!/><@print_class classes/>>
<form class="component ${id}_page" id="pageForm" <#if action?has_content>action="${action}"</#if> method="post" <#if cancel_action?has_content>data-cancel-action="${cancel_action}"</#if> method="post" autocomplete="off">
		<#assign errorClass="" />
		<#if errorMessage! != "">
			<#assign errorClass="error" />
		</#if>
		<div class="component base_page ${errorClass}" data-app-name="${appName!}">
			<#nested>
			<#recurse/>
		</div>
</form>
</div>
</#macro>