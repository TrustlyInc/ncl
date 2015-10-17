<#macro @radio_account_list>
	<@render_radio_account_list .node.id!, css_classes(.node), accounts/>
</#macro>
	
<#macro render_radio_account_list id classes accounts="">
	<div <@print_id id!/><@print_class classes/> >
		<#if accounts??>

			<div class="field field-Replacer">
				<select class="replace-for-many-radios" name="account">
					<#list accounts as acc>
					<#assign disabled="">
					<option data-account-type="${acc.type!}" data-account-name="${acc.name!}" data-account-number="${acc.number!}" data-account-index="${acc.indexAccount}" ${disabled}>${acc.name!} - ${acc.number!}</option>
					</#list>
				</select>
			</div>
			<#assign firstClass="first" />
			<#assign lastClass="" />
			<#assign accountsSize=accounts?size />
			<#assign index=0 />
			<div class="labels">
			<#list accounts as acc>

				<#if index == (accountsSize - 1)>
					<#assign lastClass="last" />
				</#if>

				<#assign noFundsClass="">
				<#assign disabled="">

				<label class="${firstClass} ${lastClass} "><input ${disabled} class="${noFundsClass}" type="radio" name="account" data-account-type="${acc.type!}" data-account-name="${acc.name!}" data-account-number="${acc.number!}" data-account-index="${acc.indexAccount}"> ${acc.name!} - ${acc.number!}</label>
				
				<#assign firstClass="" />
				<#assign index=index+1 />
			</#list>
			</div>
		</#if>
	</div>
</#macro>