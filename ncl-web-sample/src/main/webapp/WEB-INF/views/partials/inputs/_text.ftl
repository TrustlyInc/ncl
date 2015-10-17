<#escape x as x?html>
<#assign placeholder = "">
<input maxlength="256" type="text" <#if errorMessage! != "">class="svp-error"</#if> id="svp-${credential.name!}" value="${credential.value!}" autocomplete="off" autocorrect="off" autocapitalize="off" spellcheck="false" name="prompt-${credential.name!}" <#if placeholder! != "">placeholder="${placeholder!}"</#if> />
<#if errorMessage! != "" && !(genericError!false)>
  <span class="svp-error" id="svp-${credential.name!}-error">${errorMessage!}</span>
</#if>
</#escape>
