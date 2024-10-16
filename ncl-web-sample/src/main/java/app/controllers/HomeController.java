package app.controllers;

import java.util.ArrayList;
import java.util.List;

import org.javalite.activeweb.AppController;
import org.javalite.activeweb.annotations.GET;

import com.paywithmybank.ncl.freemarker.Module;
import com.paywithmybank.ncl.freemarker.NCL;

import app.controllers.model.AccountView;
import app.controllers.model.PaymentProvider;
import app.controllers.model.Subject;
import app.controllers.model.Subject.Credential.InputMetadata;
import app.controllers.model.Subject.Credential.InputMetadata.InputType;

public class HomeController  extends AppController{
	
	
	/**
	 * Some links to test:
	 * 
	 * http://localhost:8800/ncl/?module=panel&page=panel%23select_bank
	 * http://localhost:8800/ncl/?module=panel&page=panel%23credentials@sign_in
	 * http://localhost:8800/ncl/?module=panel&page=panel%23credentials@sign_in@error
	 * http://localhost:8800/ncl/?module=experiment_1&page=panel%23credentials@sign_in
	 * http://localhost:8800/ncl/?module=panel&page=panel%23credentials@mfa
	 * http://localhost:8800/ncl/?module=panel&page=panel%23accounts
	 * http://localhost:8800/ncl/?module=panel&page=panel%23credentials@partial
	 * http://localhost:8800/ncl/?module=panel&page=panel%23credentials@partial@error
	 * http://localhost:8800/ncl/?module=panel&page=panel%23ech
	 * http://localhost:8800/ncl/?module=panel&page=panel%23ech_confirmation
	 */
	@GET
	public void index() {
		NCL ncl = new NCL();
		try {
			
			String moduleName = param("module");
			String page = param("page");
			
			if(page == null){
				page = "panel#select_bank";
			}
			
			if(moduleName == null || moduleName.isEmpty()){
				moduleName = "panel";
			}
			Module module = ncl.getModule(moduleName);
			view("ncl_module",module);
			
			//Mock data
			view("institutionName","Bank");
			view("merchant","My Bussiness Name");
			view("base_url","http://localhost:8800/ncl");
			view("fiCode","000000000");
			view("amount","0.00");
			
			Subject subject = new Subject();
			List<Subject.Credential> credentials = new ArrayList<>();
			subject.setCredentialsList(credentials);
			view("subject",subject);
			
			switch(page){
				case "panel#select_bank":
					List<PaymentProvider> paymentProviders = new ArrayList<>();
					paymentProviders.add(new PaymentProvider().setPaymentProviderId("000000000").setName("A Bank"));
					paymentProviders.add(new PaymentProvider().setPaymentProviderId("000000000").setName("Bank of A"));
					paymentProviders.add(new PaymentProvider().setPaymentProviderId("000000000").setName("Bank of M"));
					paymentProviders.add(new PaymentProvider().setPaymentProviderId("000000000").setName("Bank of the W"));
					paymentProviders.add(new PaymentProvider().setPaymentProviderId("000000000").setName("BT"));
					paymentProviders.add(new PaymentProvider().setPaymentProviderId("000000000").setName("C O"));
					paymentProviders.add(new PaymentProvider().setPaymentProviderId("000000000").setName("Ch"));
					paymentProviders.add(new PaymentProvider().setPaymentProviderId("000000000").setName("City"));
					paymentProviders.add(new PaymentProvider().setPaymentProviderId("000000000").setName("C Bank"));
					paymentProviders.add(new PaymentProvider().setPaymentProviderId("000000000").setName("Demo Bank"));
					paymentProviders.add(new PaymentProvider().setPaymentProviderId("000000000").setName("F T Bank"));
					paymentProviders.add(new PaymentProvider().setPaymentProviderId("000000000").setName("F R Bank"));
					paymentProviders.add(new PaymentProvider().setPaymentProviderId("000000000").setName("KeyBank"));
					paymentProviders.add(new PaymentProvider().setPaymentProviderId("000000000").setName("M Bank"));
					paymentProviders.add(new PaymentProvider().setPaymentProviderId("000000000").setName("N Fl"));
					paymentProviders.add(new PaymentProvider().setPaymentProviderId("000000000").setName("P Bank"));
					paymentProviders.add(new PaymentProvider().setPaymentProviderId("000000000").setName("Reg"));
					paymentProviders.add(new PaymentProvider().setPaymentProviderId("000000000").setName("Sun"));
					paymentProviders.add(new PaymentProvider().setPaymentProviderId("000000000").setName("T Bank"));
					paymentProviders.add(new PaymentProvider().setPaymentProviderId("000000000").setName("U Bank"));
					paymentProviders.add(new PaymentProvider().setPaymentProviderId("000000000").setName("U"));
					paymentProviders.add(new PaymentProvider().setPaymentProviderId("000000000").setName("U Bank"));
					paymentProviders.add(new PaymentProvider().setPaymentProviderId("000000000").setName("Wel"));
					view("paymentProviders",paymentProviders);
				break;
				
				case "panel#credentials@sign_in@error":	
					view("errorMessage","Sign in error. Please try again.");
				case "panel#credentials@sign_in":
					credentials.add(new Subject.Credential().setName("email").setMetadata(new InputMetadata().setLabel("Email").setType(InputType.Text)));
					credentials.add(new Subject.Credential().setName("token").setMetadata(new InputMetadata().setLabel("Token").setType(InputType.Password)));
					view("credentials",credentials);
					view("inputCount",2);
				break;
				
				case "panel#credentials@mfa@error":	
					view("errorMessage","Invalid answer!");
				case "panel#credentials@mfa":
					credentials.add(new Subject.Credential().setName("date").setMetadata(new InputMetadata().setLabel("When do you first eat an apple?").setType(InputType.Text)));
					view("credentials",credentials);
					view("inputCount",2);
				break;
				
				case "panel#credentials@partial@error":
					view("errorMessage","Invalid number!");
				case "panel#credentials@partial":
					credentials.add(new Subject.Credential().setName("number").setMetadata(new InputMetadata().setLabel("Enter your number:").setType(InputType.Text)));
					view("credentials",credentials);
					view("inputCount",2);
				break;
				
				case "panel#accounts":
					List<AccountView> accounts = new ArrayList<>();
					AccountView account = new AccountView();
					account.setName("My super account");
					account.setNumber("0000");
					accounts.add(account);
					account = new AccountView();
					account.setName("My long term account");
					account.setNumber("0000");
					accounts.add(account);
					view("accounts",accounts);
				break;
				
				case "panel#ech":
				case "panel#ech_confirmation":
					view("eCh",1);
				break;
			}
			
			if(page.startsWith("panel#error")){
				view("errorMessage","Sorry, some error happened.");
			}
			
			view("body_classes","paywithmybank instant select-bank index");
			view("page",page + "@paywithmybank@instant@select-bank@index");
		} catch (Exception e) {
			// e.printStackTrace();
		}
		render("/page");
	}

}
