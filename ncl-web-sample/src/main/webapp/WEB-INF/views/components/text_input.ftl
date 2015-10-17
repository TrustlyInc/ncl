<#macro @text_input>
	<@render_text_input .node.id!, css_classes(.node), .node.name!"", .node.label!"", .node.description!"", .node.helpEnabled!false/>
</#macro>

<#macro render_text_input id="" classes=[] name="" label="Input Label" description="Text input description" helpEnabled=false inputSize="" >
	<#assign classes = classes + [inputSize!]>
	<#if helpEnabled == true>
		<#assign classes = classes + ["help-enabled"]>
	</#if>
	<div <@print_id id!/><@print_class classes/> >
		<label for="">${label} <i class="fa fa-question-circle help-icon"></i></label>
		<input type="text" value="" autocomplete="off" autocorrect="off" autocapitalize="off" spellcheck="false" name="${name}" class=""/>
		<div class="description">${description}</div>
	</div>
</#macro>