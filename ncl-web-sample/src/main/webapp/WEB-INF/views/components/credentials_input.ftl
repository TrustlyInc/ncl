<#macro @credentials_input>
	<@render_credentials_input .node.id!, css_classes(.node)/>
</#macro>

<#macro render_credentials_input id classes showPlaceHolders=false bigLabels=false isLogin=false>
<div <@print_id id!/><@print_class classes/>>
		<#assign cssClasses="">

		<#if showPlaceHolders>
			<#assign cssClasses=cssClasses + " show-place-holders">
		<#else>
			<#assign cssClasses=cssClasses + " no-place-holders">
		</#if>

		<#if bigLabels>
			<#assign cssClasses=cssClasses + " big-labels">
		<#else>
			<#assign cssClasses=cssClasses + " normal-labels">
		</#if>

		<#if errorMessage?? && errorMessage != "">
			<#assign cssClasses=cssClasses + " form-with-error">
		</#if>

		<#if credentials??>
		<div class="component credentials_input${cssClasses!} <#if !isLogin>is-not-login</#if>" >
			<!-- auth-fields -->
			<input style="display:none" type="text" name="fakeusernameremembered"/>
			<input style="display:none" type="password" name="fakepasswordremembered"/>
			<div style="display: none;">
				<input type="text" name="prompt-paywithmybank" value="PayWithMyBank">
			</div>
			<div class="auth-fields auth-fields-${type!"default"}" data-type="${type!"default"}">
				<!-- fake fields are a workaround for chrome autofill getting the wrong fields -->
				<#assign credentialsLength=0 />
				<#list credentials as credential>
					<#if credential.name != "remember">
						<#assign credentialsLength=credentialsLength+1 />
					</#if>
				</#list>
				<#assign useReplacer=false />

				<div class="field field-Replacer <#if useReplacer==false>disabled</#if>" data-credential-name="Replacer">
					<select class="replace-for-many-radios">
						<option value="">Select an item</option>
					<#list credentials as credential>
						<option value="prompt-${credential.name!}">${credential.metadata.label!}</option>
					</#list>
					</select>
				</div>

				<#if keyImageText! != "" && keyImageData! != "">
					<@render partial="/partials/inputs/image" />
				</#if>

				<#assign firstRadio=true />
				<#assign firstFieldClass="first" />
				<#assign lastFieldClass="" />
				<#assign credentialsSize=credentials?size />
				<#assign index=0 />
				<#assign credentialsLength=0 />

				<#list credentials as credential>

					<#if index==(credentialsSize - 1)>
						<#assign lastFieldClass="last" />
					</#if>

					<#if credential.name != "remember">
					<div class="field field-${credential.metadata.type.name()?html} <#if credential_index == 0>field-First<#else><#if !credential_has_next>field-Last</#if></#if> ${firstFieldClass} ${lastFieldClass} <#if useReplacer>disabled</#if>" data-credential-name="${credential.name!}">
						<#if credential.metadata.type.name() == "Label">
							<p>${credential.metadata.label!}</p>
		          <p><b>${credential.value!}</b></p>
						<#else>
							<#if credential.metadata.type.name() == "Password">
								<input maxlength="256" value="" type="password" class="validation-forms" id="svp-${credential.name!}" autocomplete="off" name="prompt-${credential.name!}" <#if showPlaceHolders>placeholder="${credential.metadata.label!}"</#if> />
							<#elseif (credential.metadata.type.name() == "Select")>
								<@render partial="/partials/inputs/select" />
							<#elseif (credential.metadata.type.name() == "AccountLocation")>
								<#assign optionsSize=credential.metadata.optionsList?size />
								
								<#if (optionsSize lte 7)>
									<ul class="radio-options">
									<#assign optionIndex=0 />
									<#assign firstOptionClass="first" />
									<#assign lastOptionClass="" />
									<#list credential.metadata.optionsList as option>
										<#if "Select Bank Account Location" != "${option.label!}">
											<#if (optionsSize-2) == optionIndex>
												<#assign lastOptionClass="last" />
											</#if>
											<li class="radio-option ${firstOptionClass} ${lastOptionClass}">
												<input type="radio" name="prompt-${credential.name!}" value="${option.value!}"/> 
												<label for="prompt-${credential.name!}">${option.label!}</label>
											</li>
											<#assign optionIndex=optionIndex+1 />
											<#assign firstOptionClass="" />
										</#if>
									</#list>
									</ul>
								<#else>
									<@render partial="/partials/inputs/select" />
								</#if>

							<#elseif credential.metadata.type.name() == "Radio">
								<#if firstRadio>
									<#assign firstRadio=false>
									<input type="hidden" id="selectedRadioButton" value="" />
								</#if>
								<@render partial="/partials/inputs/radio" />
							<#elseif credential.metadata.type.name() == "Checkbox">
								<@render partial="/partials/inputs/checkbox" />
							<#elseif credential.metadata.type.name() == "Text">
								<input maxlength="256" type="text" <#if errorMessage! != "">class="svp-error"</#if> id="svp-${credential.name!}" value="${credential.value!}" autocomplete="off" autocorrect="off" autocapitalize="off" spellcheck="false" name="prompt-${credential.name!}" <#if showPlaceHolders>placeholder="${credential.metadata.label!}"</#if> />
							<#elseif credential.metadata.type.name() == "Hidden">
								<@render partial="/partials/inputs/hidden" />
							<#elseif credential.metadata.type.name() == "Link">
								<@render partial="/partials/inputs/link" />
							<#elseif credential.metadata.type.name() == "Refresh">
								<@render partial="/partials/inputs/refresh" />
							</#if>
						</#if>
					</div><!-- div.field -->

					<#assign firstFieldClass="" />
					<#assign index=index+1 />
					</#if>
				</#list>
				<@render partial="/partials/error_message" />
			</div><!-- auth-fields -->
		</div>
	</#if>
</div>
</#macro>
