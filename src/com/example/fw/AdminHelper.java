package com.example.fw;

import java.util.List;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.util.logging.Logger;

import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WaitingRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class AdminHelper extends HelperBase{
	
	// class AdminHelper розширяє HelperBase, а не WebDriverHelperBase, 
	// тому-що він буде працювати напряму по протоколу http і браузер йому ніякий не потрібен
	
    Logger log =  Logger.getLogger("AdminHelper");

	private WebClient web;
	private String baseUrl;

	public AdminHelper(ApplicationManager applicationManager) {
		super(applicationManager);
		web = new WebClient();
		// далі потрібно встановити refresh handler ручками, тому-що дефолтний рефреш хендлер є поганий!!!
		// в даному випадку можна використовувати або WaitingRefreshHandler() або ThreadedRefreshHandler()
		//web.setRefreshHandler(new WaitingRefreshHandler());
		web.setRefreshHandler(new ThreadedRefreshHandler());
		baseUrl = manager.getProperty("baseUrl");
	}
	
	// адмін хелпер повинен сочатку зайти в систему як користувач з правами адміна і після цього повинен виконати всі дії з потрібним нам користувачем
	

	public boolean userExist(User user) throws Exception {
		HtmlPage userPage = openUserPage(user);
		HtmlForm deleteForm = findUsewrremovalForm(userPage);
		return (deleteForm != null);
	}

	public void deleteUserIfExists(User user) throws Exception {
		log.info("Deleting user " + user.login);
		HtmlPage userPage = openUserPage(user);
		log.info("userPage opened");
		HtmlForm deleteForm = findUsewrremovalForm(userPage);
		log.info("deleteForm = " + deleteForm);
		if (deleteForm != null) {
			HtmlPage commitPage = (HtmlPage) deleteForm.getInputByValue("Delete User").click();
			log.info("commitPage opened");
			HtmlForm commitForm = findUsewrremovalForm(commitPage);
			log.info("commitForm = " + commitForm);
			commitForm.getInputByValue("Delete Account").click();
			log.info("commitForm submitted");
		}
		//log.info("PAUSE For 25 sec!!! for verify manually if testuser was deleted from mantisbt");
		//pause(25000);
	}
	
	private HtmlPage openUserPage(User user) throws Exception {
		loginAdminIfRequired();
		String userId = manager.getHibernateHelper().getUserId(user.login);
		log.info("userId = " + userId);
		HtmlPage userPage = (HtmlPage) web.getPage(baseUrl + "/manage_user_edit_page.php?user_id=" + userId);
		return userPage;
	}

	private void loginAdminIfRequired() throws Exception {
		String adminLogin = manager.getProperty("admin.login");
		String adminPassword = manager.getProperty("admin.password");
		log.info("logging with " + adminLogin + "/" + adminPassword);
		
		HtmlPage mainPage = (HtmlPage) web.getPage(baseUrl);
		log.info("mainPage opened");
		HtmlForm loginForm = mainPage.getFormByName("login_form");
		log.info("loginForm = " + loginForm);
		if (loginForm != null) {
			loginForm.getInputByName("username").setValueAttribute(adminLogin);
			loginForm.getInputByName("password").setValueAttribute(adminPassword);
			loginForm.getInputByValue("Login").click();
			log.info("loginForm submitted");
		}
	}

	private HtmlForm findUsewrremovalForm(HtmlPage userPage) {
		List<HtmlForm> forms = userPage.getForms();
		for (HtmlForm form : forms) {
			if (form.getActionAttribute().endsWith("manage_user_delete.php")) {
				return form;
			}
		}
		return null;
	}

}
