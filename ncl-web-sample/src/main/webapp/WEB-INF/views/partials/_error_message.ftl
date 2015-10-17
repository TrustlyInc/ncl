<#if errorMessage?? && errorMessage != "">
	<#assign showErrorClass = "show-error"/>
</#if>
<span class="svp-error svp-error-generic ${showErrorClass!}" id="svp-generic-error">${errorMessage!}</span>