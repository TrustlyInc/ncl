package app.controllers;

public class MainTest {
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
	public static void main(String[] args) {
		com.paywithmybank.ncl.NCL ncl = new com.paywithmybank.ncl.NCL();
		try {
			
			com.paywithmybank.ncl.Module panelOrig = ncl.getModule("panel_orig");
			com.paywithmybank.ncl.Module panelNew = ncl.getModule("panel_new");
			compare(panelOrig,panelNew,"panel#select_bank");
			compare(panelOrig,panelNew,"panel#credentials@sign_in");
			compare(panelOrig,panelNew,"panel#credentials@sign_in@error");
			compare(panelOrig,panelNew,"panel#credentials@mfa");
			compare(panelOrig,panelNew,"panel#credentials@mfa@error");
			compare(panelOrig,panelNew,"panel#credentials@partial");
			compare(panelOrig,panelNew,"panel#credentials@partial@error");
			compare(panelOrig,panelNew,"panel#accounts");
			compare(panelOrig,panelNew,"panel#echeck");
			compare(panelOrig,panelNew,"panel#echeck_confirmation");
			compare(panelOrig,panelNew,"panel#error");
			compare(panelOrig,panelNew,"panel#error@NoEligibleAccounts");
			compare(panelOrig,panelNew,"panel#error@isTimeOutWhileAuthorizing");
			System.out.println("Equivalent!");
			
		} catch (Exception e) {
			// e.printStackTrace();
		}
		
	}
	
	private static void compare(com.paywithmybank.ncl.Module panelOrig,com.paywithmybank.ncl.Module panelNew, String selector){

		compare2(panelOrig,panelNew,selector);
		compare2(panelOrig,panelNew,selector + "@mobile");
		compare2(panelOrig,panelNew,selector + "@mobile@hybrid");
	}
	
	private static void compare2(com.paywithmybank.ncl.Module panelOrig,com.paywithmybank.ncl.Module panelNew, String selector){
		com.paywithmybank.ncl.model.Node origNode = panelOrig.getNode(selector);
		com.paywithmybank.ncl.model.Node newNode = panelNew.getNode(selector);
		
		if(!origNode.equals(newNode)){
			throw new RuntimeException("Nodes are different for selector "+selector+". \nOrig=" + origNode + "\nNew=" + newNode);
		}
	}

}
