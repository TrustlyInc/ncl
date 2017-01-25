$module panel;
$import ui;

/*
 * Layout
 */

panel [
	header,
	page#panel_page,
	footer {
		text="Secure connection by PayWithMyBank&reg;";
	},
	cancel_confirmation,
	loader
]

panel@mobile@hybrid [
	page#panel_page,
	footer {
		text="Secure connection by PayWithMyBank&reg;";
	},
	cancel_confirmation,
	loader
]


panel > header [
	header_title,
	back_button_arrow@back-button-action,
	cancel_button@panel-close-action [
		header-close-icon@icon-cancel-circled
	]
]

panel@mobile > header [
	header_title,
	back_button_arrow@back-button-action
]

/*
 * Cancel confirmation
 */

cancel_confirmation [
	page
]

cancel_confirmation page [
	page_row_top [
		transaction_details
	],
	page_row_complement [
		scrollable [
			simple_error,
			page-padding [
				cancel_title {
					text = "Cancel";
				},
				cancel_subtitle {
					text = "Your account will not be debited if you cancel.";
				},
				button#do-cancel@non-attractive@small {
					label = "Cancel";
				},
				button#dont-cancel@small {
					label = "Continue";
				},
				alternative_actions
			]
		]
	]
]

@mobile@hybrid cancel_confirmation page [
	scrollable [
		simple_error,
		page-padding [
			cancel_title {
				text = "Cancel";
			},
			cancel_subtitle {
				text = "Your account will not be debited if you cancel.";
			},
			button#do-cancel@non-attractive@small {
				label = "Cancel";
			},
			button#dont-cancel@small {
				label = "Continue";
			},
			alternative_actions
		]
	]
]

/*
 * Select Bank
 */

// change layout just for hybrid select bank page
panel#select_bank@mobile@hybrid [
	header,
	page#panel_page,
	footer {
		text="Secure connection by PayWithMyBank&reg;";
	},
	cancel_confirmation,
	loader
]
panel#select_bank@mobile@hybrid > header [
	header_title,
	back_button@back-button-action
]

panel#select_bank header header_title {
	text="Select your bank";
}

panel#select_bank > page#panel_page [
	page_row_top [
		transaction_details
	],
	searchbank_input,
	bank_selector,
	bank_not_listed
]

panel#select_bank@mobile@hybrid > page#panel_page [
	searchbank_input {
		placeholder = "Search";
	},
	bank_selector,
	bank_not_listed
]

/*
 * Credentials
 */

secure_login_warning {
	text="Your bank login is never seen or stored by ${merchantName!}.";
}

panel#credentials header header_title {
	text="Sign into ${institutionShortName!institutionName!}";
}

panel#credentials@sign_in > page#panel_page [
	page_row_top [
		transaction_details
	],
	page_row_complement [
		scrollable [
			simple_error,
			page-padding#panel_content [
				recaptcha_input,
				credentials_page_context [
					credentials_input
				],
				terms_of_use,
				continue_button#bt-post {
					label="Agree &amp; Sign In";
				},
				secure_login_warning
			]
		]
	]
]

panel#credentials@mobile@hybrid@sign_in > page#panel_page [
	scrollable [
		simple_error,
		page-padding#panel_content [
			page_title {
				text="${institutionShortName!institutionName!}";
			},
			recaptcha_input,
			credentials_page_context [
				credentials_input
			],
			terms_of_use,
			continue_button#bt-post {
				label="Agree &amp; Sign In";
			},
			back_button@back-button-action
		]
	]
]

panel#credentials@mfa > page#panel_page [
	page_row_top [
		transaction_details
	],
	page_row_complement [
		scrollable [
			simple_error,
			page-padding#panel_content [
				recaptcha_input,
				more_info@page-text {
					text="<strong>${institutionName!}</strong> needs a bit more information to confirm your identity.";
				},
				credentials_page_context [
					credentials_input
				],
				continue_button#bt-post,
				secure_login_warning
			]
		]
	]
]

panel#credentials@mobile@hybrid@mfa > page#panel_page [
	scrollable [
		simple_error,
		page-padding#panel_content [
			recaptcha_input,
			more_info@page-text {
				text="<strong>${institutionName!}</strong> needs a bit more information to confirm your identity.";
			},
			credentials_page_context [
				credentials_input
			],
			continue_button#bt-post,
			back_button@back-button-action
		]
	]
]

panel#credentials@partial header header_title {
	text="More information needed";
}

panel#credentials@partial > page#panel_page [
	page_row_top [
		transaction_details
	],
	page_row_complement [
		scrollable [
			simple_error,
			page-padding#panel_content [
				more_info@page-text {
					text="In order to complete this transaction, we need more information from you.";
				},
				credentials_page_context [
					credentials_input
				],
				continue_button#bt-post,
				secure_login_warning
			]
		]
	]
]

panel#credentials@mobile@hybrid@partial > page#panel_page [
	scrollable [
		simple_error,
		page-padding#panel_content [
			recaptcha_input,
			more_info@page-text {
				text="<strong>${institutionName!}</strong> needs a bit more information to confirm your identity.";
			},
			credentials_page_context [
				credentials_input
			],
			continue_button#bt-post,
			back_button@back-button-action
		]
	]
]

/*
 * Accounts
 */

panel#accounts header header_title {
	text="Choose an account";
}

panel#accounts > page#panel_page [
	page_row_top [
		transaction_details
	],
	page_row_complement [
		scrollable [
			simple_error,
			page-padding#panel_content [
				radio_account_list,
				authorization_term,
				printable_terms_button,
				continue_button#continue-account {
					label="${finishButtonLabel!'Confirm'}";
				}
			]
		]
	]
]

panel#accounts@mobile@hybrid > page#panel_page [
	scrollable [
		simple_error,
		page-padding#panel_content [
			page_title {
				text="${institutionShortName!institutionName!}";
			},
			radio_account_list,
			authorization_term,
			printable_terms_button,
			continue_button#continue-account {
				label="${finishButtonLabel!'Confirm'}";
			},
			back_button@back-button-action
		]
	]
]

/*
 * Echeck
 */

panel#echeck header header_title {
	text="Enter bank information";
}

panel#echeck > page#panel_page [
	page_row_top [
		transaction_details
	],
	page_row_complement [
		scrollable [
			simple_error,
			echeck-help [
				background,
				image {src="${context_path}/assets/images/2.0/check-routing.png";}
			],
			page-padding#panel_content [
				echeck_inputs,
				continue_button#post-button@small
			]
		]
	]
]

panel#echeck@mobile@hybrid > page#panel_page [
	scrollable [
		simple_error,
		page-padding#panel_content [
			page_title {
				text="Enter bank information";
			},
			echeck-help [
				background,
				image {src="${context_path}/assets/images/2.0/check-routing.png";}
			],
			echeck_inputs,
			continue_button#post-button@small,
			back_button@back-button-action
		]
	]
]


panel#echeck_confirmation header header_title {
	text="Enter bank information";
}

panel#echeck_confirmation > page#panel_page [
	page_row_top [
		transaction_details
	],
	page_row_complement [
		scrollable [
			page-padding#panel_content [
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
		]
	]
]

panel#echeck_confirmation@mobile@hybrid > page#panel_page [
	scrollable [
		simple_error,
		page-padding#panel_content [
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
	]
]

/*
 * Error
 */

panel#error header header_title {
	text="Pay with online banking";
}

panel#error {
	error_type_msg [
		text {
			text="Please try the following option(s):";
		}
	]
}

panel#error > page#panel_page [
	page_row_top [
		transaction_details
	],
	page_row_complement [
		scrollable [
			simple_error,
			page-padding#panel_content [
					error_type_msg,
					alternative_actions
				]
		]
	]
]

panel#error@mobile@hybrid > page#panel_page [
	scrollable [
		simple_error,
		page-padding#panel_content [
			error_type_msg,
			alternative_actions
		]
	]
]

panel#error@NoEligibleAccounts error_type_msg [
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

panel#error@isTimeOutWhileAuthorizing error_type_msg [
	text {
		text="Please contact the merchant to confirm your transaction was completed.";
	}
]