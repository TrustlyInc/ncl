<#macro @alternative_actions>
	<@render_alternative_actions .node.id!, css_classes(.node)/>
</#macro>

<#macro render_alternative_actions id classes>
	<ul <@print_id id!/><@print_class classes/> >
		<li>
			<a href="${context_path}/${fiCode!}/authentication/changeBank?ppTransactionId=${ppTransactionId!}&paymentType=${paymentType!}&deviceType=${deviceType!}&widgetId=${widgetId!}&merchantId=${merchantId!}&paymentFlow=${paymentFlow!}&theme=${theme!}">Try another bank</a>
		</li>
	</ul>
</#macro>