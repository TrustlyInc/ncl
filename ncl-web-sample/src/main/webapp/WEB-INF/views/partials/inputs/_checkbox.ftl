<#escape x as x?html>
<input type="checkbox" id="svp-${credential.name!}" name="prompt-${credential.name!}" <#if credential.value == "on">checked="checked"</#if> />
</#escape>
