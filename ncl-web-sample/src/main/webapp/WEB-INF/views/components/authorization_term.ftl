<#macro @authorization_term>
	<@render_authorization_term .node.id!, css_classes(.node), .node.showBankInfo!true/>
</#macro>
	
<#macro render_authorization_term id classes showBankInfo=true>
  <div <@print_id id!/><@print_class classes/> >

      <p>
        I authorize ${merchantName!} ...
        <br/><br/>
      </p>
      <p>
  	    Reference Number: ${merchantReference!}<br/>
  		</p>
  </div>
</#macro>