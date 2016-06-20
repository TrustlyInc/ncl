$module panel; //Define module name as panel

/* Composition rule of panel elements.
   Most panel elements will be further declared later. */
panel [  
	header [
		back_button
		header_title,
		cancel_button
	],
	scroll-pane [
		transaction_details,
		simple_error,
		content,
		cancel_confirmation
	],
	footer {
		"Secure connection by PayWithMyBank&reg;";
	}
]

simple_error {"${errorMessage!}"}

back_button [
	header-back[
		fa@fa-caret-left
	]
]

cancel_button [
	header-close[
		header-close-icon@fa@fa-times-circle
	]
]

continue_button {
	type="button";
	iconClass="fa fa-lock";
	"Continue"
}

terms_of_use {
	"By clicking 'Agree &amp; Sign In' you agree to the PayWithMyBank<sup>&reg;</sup> <a href='#' class='bt-show-terms'>Terms of Use</a>."
}

cancel_confirmation [
	mainContent [
		cancel_title {
			"Cancel Payment"
		},
		cancel_subtitle {
			"Your account will not be debited if you cancel."
		},
		button#do-cancel@non-attractive@small {"Cancel Payment"},
		button#dont-cancel@fa@fa-lock@small {"Continue Payment"}
	],
	alternative_actions
]

//Declaration rule for the panel elements having the id select_bank
panel#select_bank {
	/* Overrides the declaration rule for header_title elements having panel#select_bank as ascendant 
	   the element panel#select_bank (panel#select_bank header_title).
	   The rule set a new text for the header_title element */
	header_title { 
		"Select your bank"   //Same as -> text="Select your bank";
	}
	
	//Overrides the composition rule for content elements 
	content [
		widget [
			widget-icon [
				fa@fa-lock
			],
			widget-text {
				"<strong class='widget-text-title'>Safe and simple way to pay.</strong>
				      Your bank login is never seen or stored <br/>
					  by ${merchant}."
			}
		]
		bank_selector
	]
}

panel#credentials { 
	header_title {"Sign Into ${institutionShortName!institutionName!}"}
	
	content [
		credentials-content [
			credentials_input
		],
		continue_button#bt-post
	]
}

panel#credentials@sign_in { 
	credentials-content[
		credentials_input,
		terms_of_use
	]
	
	continue_button#bt-post {"Agree &amp; Sign In"}
}

panel#credentials@mfa { 
	credentials-content[
		label#page-text {
			"<strong>${institutionName!}</strong> needs a bit more information to confirm your identity."
		},
		credentials_input
	]
}

panel#accounts { 
	header_title {"Choose an account"}
	
	content [
		radio_account_list,
		authorization_term,
		printable_terms_button,
		continue_button#continue-account {"${finishButtonLabel!'Confirm'}"}
	]
}

panel#credentials@partial { 
	header_title {"More information needed"}
	
	credentials-content[
		more_info@page-text {"In order to complete this transaction, we need more information from you."},
		credentials_input
	]
}

panel#ech { 
	header_title {"Enter bank information"}
	
	content [
		ech-help [
			background,
			image {src="${context_path}/assets/images/2.0/check-routing.png";}
		],
		ech_inputs,
		continue_button#post-button@small
	]
}

panel#ech_confirmation { 
	header_title {"Enter bank information"}
	
	content [
		ech_info,
		authorization_term {
			showBankInfo = false;
		},
		printable_terms_button {
			fiType="ech";
		},
		continue_button#post-button@small {"Finish"}
	]
}

panel#error { 
	label#header_title {"Error"}
	
	content [
		error_type_msg [
			text {"Please try the following option(s):"}
		],
		alternative_actions
	]
}

panel#error@NoEligibleAccounts {
	error_type_msg[
		error_noeligibleaccounts [
			email_form {
				"<p class='text'>Think a mistake has been made? Give us your email and we'll investigate this and get back to you.</p>
				 <div class='email-input'>
					<input type='text' placeholder='Your email address'><button>Submit</button>
				 </div>
				 <span class='field-error'></span>"
			},
			thanks {
				"<p class='text'>Thank you for your contact information. Our team will contact you shortly.<br/><br/>
						         Additionally, you may choose one of the following options:</p>"
			}
		]
	]
}

panel#error@isTimeOutWhileAuthorizing {
	error_type_msg[
		text {"Please contact the merchant to confirm your transaction was completed."}
	]
}

panel#error@start {
	content[
		dummy
	]
}