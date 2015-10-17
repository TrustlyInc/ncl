$module panel;

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
		text="Secure connection by PayWithMyBank&reg;";
	}
]

simple_error {
	text = "${errorMessage!}";
}

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
	label="Continue";
}

terms_of_use {
	text = "By clicking 'Agree &amp; Sign In' you agree to the PayWithMyBank<sup>&reg;</sup> <a href='#' class='bt-show-terms'>Terms of Use</a>.";
}

cancel_confirmation [
	mainContent [
		cancel_title {
			text = "Cancel Payment";
		},
		cancel_subtitle {
			text = "Your account will not be debited if you cancel.";
		},
		button#do-cancel@non-attractive@small {
			label = "Cancel Payment";
		},
		button#dont-cancel@fa@fa-lock@small {
			label = "Continue Payment";
		}
	],
	alternative_actions
]

panel#select_bank { 
	header_title {
		text="Select your bank";
	}
	
	content [
		widget [
			widget-icon [
				fa@fa-lock
			],
			widget-text {
				text="<strong class='widget-text-title'>Safe and simple way to pay.</strong>
				      Your bank login is never seen or stored <br/>
					  by ${merchant}.";
			}
		]
		bank_selector
	]
}

panel#credentials { 
	header_title {
		text="Sign Into ${institutionShortName!institutionName!}";
	}
	
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
	
	continue_button#bt-post {
		label="Agree &amp; Sign In";
	}
}

panel#credentials@mfa { 
	credentials-content[
		label#page-text {
			text="<strong>${institutionName!}</strong> needs a bit more information to confirm your identity.";
		},
		credentials_input
	]
}

panel#accounts { 
	header_title {
		text="Choose an account";
	}
	
	content [
		radio_account_list,
		authorization_term,
		printable_terms_button,
		continue_button#continue-account {
			label="${finishButtonLabel!'Confirm'}";
		}
	]
}

panel#credentials@partial { 
	header_title {
		text="More information needed";
	}
	
	credentials-content[
		more_info@page-text {
			text="In order to complete this transaction, we need more information from you.";
		},
		credentials_input
	]
}

panel#ech { 
	header_title {
		text="Enter bank information";
	}
	
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
	header_title {
		text="Enter bank information";
	}
	
	content [
		ech_info,
		authorization_term {
			showBankInfo = false;
		},
		printable_terms_button {
			fiType="ech";
		},
		continue_button#post-button@small {
			label="Finish";
		}
	]
}

panel#error { 
	label#header_title {
		text="Error";
	}
	
	content [
		error_type_msg [
			text {
				text="Please try the following option(s):";
			}
		],
		alternative_actions
	]
}

panel#error@NoEligibleAccounts {
	error_type_msg[
		error_noeligibleaccounts [
			email_form {
				text="<p class='text'>Think a mistake has been made? Give us your email and we'll investigate this and get back to you.</p>
					  <div class='email-input'>
						<input type='text' placeholder='Your email address'><button>Submit</button>
					  </div>
					  <span class='field-error'></span>";
			},
			thanks {
				text="<p class='text'>Thank you for your contact information.
						Our team will contact you shortly.
						<br/><br/>
						Additionally, you may choose one of the following options:</p>";
			}
		]
	]
}

panel#error@isTimeOutWhileAuthorizing {
	error_type_msg[
		text {
			text="Please contact the merchant to confirm your transaction was completed.";
		}
	]
}

panel#error@start {
	content[
		dummy
	]
}
