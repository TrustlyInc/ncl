$module panel-zelle;
$import panel;

panel#accounts@retrieval {
	header_title {
		text="${institutionShortName!institutionName!}";
	}
	
	#panel_content [
		label@choose_deposit_account {
			"Choose a deposit account:"
		},
		radio_account_list,
		label@choose_email_or_mobile {
			"Choose an email or mobile to link to the deposit account:"
		},
		email_or_phone_list,
		continue_button#continue-account {
			label="${finishButtonLabel!'Confirm'}";
		}
	]
}