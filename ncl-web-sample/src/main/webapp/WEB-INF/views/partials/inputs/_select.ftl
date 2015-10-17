<#escape x as x?html>
<select <#if errorMessage! != "">class="svp-error"</#if> id="svp-${credential.name!}" value="" autocomplete="off" name="prompt-${credential.name!}">
	<#list credential.metadata.optionsList as option>
		<option value="${option.value!}" <#if credential.value?? && option.value == credential.value>selected='selected'</#if>>${option.label!}</option>
	</#list>
</select>
</#escape>
