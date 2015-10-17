<#macro @ech_info>
	<@render_ech_info .node.id!, css_classes(.node)/>
</#macro>

<#macro render_ech_info id classes>
	<div <@print_id id!/><@print_class classes/>>
	  <h3>Confirm your information:</h3>
	  <p>
	    Name on account: ${ownerName!}<br>
	    Account type: ${accountType!}<br>
	    Bank routing number: ${routingNumber!}<br>
	    Bank account number: ${accountNumber!}<br>
	  </p>
	</div>
</#macro>