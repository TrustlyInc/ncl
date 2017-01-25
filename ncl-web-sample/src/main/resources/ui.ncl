/*
 * Ui components definitions
 */

bank_not_listed {
	text = "<a href='#' data-reason='bank-not-listed'>My bank is not on the list</a>";
}

simple_error {
	text = "${errorMessage!}";
}

back_button_arrow [
	icon-left-dir
]

back_button [
	inner {
		text = "<a src=#>Back</a>";
	}
]

continue_button {
	type="button";
	iconClass="icon-lock";
	label="Continue";
}

terms_of_use {
	text = "By clicking 'Agree &amp; Sign In' you agree to the PayWithMyBank<sup>&reg;</sup> <a href='#' class='bt-show-terms'>Terms of Use</a>.";
}