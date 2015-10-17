<#escape x as x?html>
<input type="hidden" id="svp-${credential.name!}" name="prompt-${credential.name!}" value="" />
<a href="#" data-fic-link="svp-${credential.name!}" data-fic-value="${credential.value!''}">${credential.metadata.label!}</a>
</#escape>
