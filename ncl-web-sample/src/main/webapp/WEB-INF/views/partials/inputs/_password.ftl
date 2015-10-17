<#escape x as x?html>
<#assign placeholder = "">
<input maxlength="256" value="" type="password" class="validation-forms" <#if errorMessage! != "">class="svp-error" </#if>id="svp-${credential.name!}" autocomplete="off" name="prompt-${credential.name!}" <#if placeholder! != "">placeholder="${placeholder!}"</#if> />
<#if errorMessage! != "" && !(genericError!false)>
  <span class="svp-error" id="svp-${credential.name!}-error">${errorMessage!}</span>
</#if>
</#escape>
