<#include "/components/text_input.ftl"/>

<#macro @ech_inputs>
	<@render_ech_inputs .node.id!, css_classes(.node)/>
</#macro>

<#macro render_ech_inputs id classes>
	<div <@print_id id!/><@print_class classes/>>
		<p id="AccountType">Account type: <label><input type="radio" name="accountType" checked="" value="Checking"> Checking</label> <label><input type="radio" name="accountType" value="Savings"> Savings</label>
		<div class="description"></div>
		</p>
		<@render_text_input label="Name on account" inputSize="small" id="NameOnAccount" classes=["component","text_input"] name="ownerName" />
		<@render_text_input label="Routing number" inputSize="small" id="RoutingNumber" classes=["component","text_input"] helpEnabled=true name="routingNumber" />
		<@render_text_input label="Account number" inputSize="small" id="AccountNumber" classes=["component","text_input"] helpEnabled=true name="accountNumber"/>
		<@render_text_input label="Re-enter account number" inputSize="small" classes=["component","text_input"] id="AccountNumber2" name="accountNumber2"/>
  </div>
</#macro>