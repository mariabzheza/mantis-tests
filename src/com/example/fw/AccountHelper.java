package com.example.fw;

//import javax.mail.Message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class AccountHelper extends WebDriverHelperBase{

	public AccountHelper(ApplicationManager applicationManager) {
		super(applicationManager);
	}

	public void signup(User user) {
		openUrl("/");
		//click(By.cssSelector("span.bracket-link"));
		//old!!! click(By.xpath("//span/a[.='Signup for a new account']"));
		//click(By.xpath("//span[1]/a"));
		//***************
		driver.findElement(By.linkText("Signup for a new account")).click();
		
	    type(By.name("username"), user.login);
	    type(By.name("email"), user.email);
	    click(By.cssSelector("input.button"));
	    	    
	    pause(25000);
	    String msg = manager.getMailHelper().getNewMail(user.login, user.password);
	    //***Print Letter's content
	    //System.out.println("****************Letter "+ msg);
	    
	    String confirmationLink = getConfirmationLink(msg);
	    
	  //***************the next 1 string added
	    System.out.println("***************************");
	    System.out.println("!!!!!!!!*******Letter "+ msg);
	    System.out.println("***************************");
	    
	    openAbsoluteUrl(confirmationLink);
	    
	    type(By.name("password"), user.password);
	    type(By.name("password_confirm"), user.password);
	    click(By.cssSelector("input.button"));
	    
	}
	//**the next should private or public???
	public String getConfirmationLink(String text) {
		Pattern regex = Pattern.compile("http\\S*");
		Matcher matcher = regex.matcher(text);
		if (matcher.find()) {
			//System.out.println("REG_EXPR = "+ regex);
			return matcher.group();
		} else {
			System.out.println("!!! Link Not founded!!!");
			return "";
		}
		
	}

	public void login(User user) {
		openUrl("/");
		type(By.name("username"), user.login);
		type(By.name("password"), user.password);
		click(By.cssSelector("input.button"));
	}
	
	public String loggedUser() {
		WebElement element = findElement(By.cssSelector("td.login-info-left span"));
		return element.getText();
	}

	/*private WebElement findElement(By cssSelector) {
		// TODO Auto-generated method stub
		return null;
	}
	*/

}
