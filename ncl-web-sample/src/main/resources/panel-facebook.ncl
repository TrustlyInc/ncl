$module panel-facebook;

panel#accounts [
	container#view-main-content@panel-main-content [
		container@inner [
			header_accounts {
				title="Choose Your Account";
			},
			error_container,
			account_list,
			partial_account_numbers
		]
	],
	cancel_facebook,
	partial_account_numbers2,
	print_frame,
	panel_container_actions_accounts
]

panel#echeck [
	panel_side_content_echeck,
	view_main_content_echeck,
	cancel_facebook,
	panel_container_actions_echeck
]

panel#echeck_confirmation [
	container#view-main-content@panel-main-content [
		container@inner [
			header_echeck_confirmation,
			confirmation_fields_echeck
		]
	],
	cancel_facebook,
	panel_container_actions_echeck_confirmation
]

panel#credentials [
	container#view-main-content@panel-main-content [
		container@inner [
			header_credentials,
			formerror_facebook,
			auth_fields,
			captcha_markup,
			credentials_text
		]
	],
	cancel_facebook,
	panel_container_actions_credentials
]

panel#select_bank [
	container#view-main-content@panel-main-content [
		container@inner [
			bank_list
		]
	]
]

panel#select_bank@mobile container@inner [
	bank_list_mobile
]

panel#error [
	view_main_content_error,
	panel_container_actions_error	
]