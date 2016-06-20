$module experiment_1;

$import panel;

panel#select_bank { 
	header_title {"This is experiment 1"}
}

panel#credentials@sign_in { 
	content [
		recaptcha_input,
		credentials-content[
			block-group [
				block@grid-30 [ //Column 1 - Width 30%
					tile {"This is experiment 1"}
				],
				block@grid-70[ //Column 2 - Width 70%
					credentials_input,
					terms_of_use
				]
			]
		],
		continue_button_exp_1#bt-post@grid-30{ 
		    type="button";
			
			"Experiment 1"
		}
	]
}