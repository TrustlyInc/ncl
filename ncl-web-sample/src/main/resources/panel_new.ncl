$module panel;
$import ui;

/*
 * Layout
 */

panel [
	header,
	page#panel_page,
	footer,
	cancel_confirmation,
	loader
]

header [
	header_title,
	back_button_arrow@back-button-action,
	cancel_button@panel-close-action [
		header-close-icon@icon-cancel-circled
	]
]

page [
	page_row_top,
	page_row_complement [
		scrollable
	]
]

page_row_top [
	transaction_details
]

scrollable [
	simple_error,
	page-padding#panel_content
]

footer {
	"Secure connection by PayWithMyBank&reg;"
}

panel@mobile {
	 header [
		header_title,
		back_button_arrow@back-button-action
	]
}

panel@hybrid {
	page[ 
		scrollable 
	]
	
	page_title {
		"${institutionShortName!institutionName!}"
	}
}[
	page#panel_page,
	footer,
	cancel_confirmation,
	loader
]

/*
 * Cancel confirmation
 */

cancel_confirmation  {
	scrollable [
		simple_error,
		page-padding
	]

	page-padding [
		cancel_title {
			"Cancel"
		},
		cancel_subtitle {
			"Your account will not be debited if you cancel."
		},
		button#do-cancel@non-attractive@small {
			label = "Cancel";
		},
		button#dont-cancel@small {
			label = "Continue";
		},
		alternative_actions
	]
}[
	page
]

/*
 * Select Bank
 */

// change layout just for hybrid select bank page
panel#select_bank {
	header_title {
		"Select your bank"
	}
	
	page#panel_page [
		page_row_top,
		searchbank_input,
		bank_selector,
		bank_not_listed
	]	
}

panel#select_bank@hybrid{
	header [
		header_title,
		back_button@back-button-action
	]

	page#panel_page [
		searchbank_input {
			placeholder = "Search";
		},
		bank_selector,
		bank_not_listed
	]
}[
	header,
	page#panel_page,
	footer,
	cancel_confirmation,
	loader 
]

/*
 * Credentials
 */

secure_login_warning {
	"Your bank login is never seen or stored by ${merchantName!}."
}

panel#credentials { 
	header_title {
		"Sign into ${institutionShortName!institutionName!}"
	}
	
	credentials_page_context [
		credentials_input
	]
	
	more_info@page-text {
		"<strong>${institutionName!}</strong> needs a bit more information to confirm your identity."
	}
}

panel#credentials@hybrid {
	#panel_content [
		recaptcha_input,
		more_info@page-text,
		credentials_page_context,
		continue_button#bt-post,
		back_button@back-button-action
	]
}

panel#credentials@sign_in  {
	#panel_content [
		recaptcha_input,
		credentials_page_context,
		terms_of_use,
		continue_button#bt-post,
		secure_login_warning
	]
	
	continue_button#bt-post {
		label="Agree &amp; Sign In";
	}
}

panel#credentials@sign_in@hybrid  {
	#panel_content [
		page_title,
		recaptcha_input,
		credentials_page_context,
		terms_of_use,
		continue_button#bt-post,
		back_button@back-button-action
	]
}

panel#credentials@mfa  {
	#panel_content [
		recaptcha_input,
		more_info@page-text,
		credentials_page_context,
		continue_button#bt-post,
		secure_login_warning
	]
}

panel#credentials@partial  {
	header_title {
		"More information needed"
	}
	
	#panel_content [
		more_info@page-text{
			"In order to complete this transaction, we need more information from you."
		},
		credentials_page_context,
		continue_button#bt-post,
		secure_login_warning
	]
}

/*
 * Accounts
 */

panel#accounts  {
	header_title {
		"Choose an account"
	}

	#panel_content [
		radio_account_list,
		authorization_term,
		printable_terms_button,
		continue_button#continue-account
	]
	
	continue_button#continue-account {
		label="${finishButtonLabel!'Confirm'}";
	}
}

panel#accounts@hybrid  {
	#panel_content [
		page_title,
		radio_account_list,
		authorization_term,
		printable_terms_button,
		continue_button#continue-account,
		back_button@back-button-action
	]
}

/*
 * Echeck
 */
panel#echeck {
 	header_title {
		"Enter bank information"
	}
	
	page#panel_page {
		scrollable [
			simple_error,
			echeck-help,
			page-padding#panel_content
		]
	}
	
	#panel_content [
		echeck_inputs,
		continue_button#post-button@small
	]
	
	echeck-help [
		background,
		image {src="${context_path}/assets/images/2.0/check-routing.png";}
	]
}

panel#echeck@hybrid {
	page#panel_page {
		scrollable [
			simple_error,
			page-padding#panel_content
		]
	}
	
	#panel_content [
		page_title {
			"Enter bank information"
		},
		echeck-help,
		echeck_inputs,
		continue_button#post-button@small,
		back_button@back-button-action
	]
}

panel#echeck_confirmation {
	header_title {
		"Enter bank information"
	}
	
	page#panel_page {
		scrollable [
			page-padding#panel_content
		]
	}
	
	#panel_content [
		echeck_info,
		authorization_term {
			showBankInfo = false;
		},
		printable_terms_button {
			fiType="echeck";
		},
		continue_button#post-button@small {
			label="Finish";
		}
	]
}

panel#echeck_confirmation@hybrid {
	page#panel_page [
		scrollable [
			simple_error,
			page-padding#panel_content
		]
	]
}

/*
 * Error
 */

panel#error {
	header_title {
		"Pay with online banking"
	}
	
	#panel_content [
		error_type_msg,
		alternative_actions
	]
	
	error_type_msg [
		text {
			"Please try the following option(s):"
		}
	]
}

panel#error@NoEligibleAccounts {
	error_type_msg [
		error_noeligibleaccounts [
			email_form {
				"<p class='text'>Think a mistake has been made? Give us your email and we'll investigate this and get back to you.</p>
				  <div class='email-input'>
					<input type='text' placeholder='Your email address'><button>Submit</button>
				  </div>
				  <span class='field-error'></span>"
			},
			thanks {
				"<p class='text'>Thank you for your contact information.
					Our team will contact you shortly.
					<br/><br/>
					Additionally, you may choose one of the following options:</p>"
			}
		]
	]
}

panel#error@isTimeOutWhileAuthorizing {
	error_type_msg [
		text {
			"Please contact the merchant to confirm your transaction was completed."
		}
	]
}