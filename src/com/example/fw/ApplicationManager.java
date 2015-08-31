package com.example.fw;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class ApplicationManager {


	private  WebDriver driver;
	public  String baseUrl;
	
	private Properties properties;
	private HibernateHelper hibernateHelper;
	private AccountHelper accountHelper;
	private MailHelper mailHelper;
	private JamesHelper jamesHelper;
		
	public ApplicationManager(Properties properties) {
		this.properties = properties;
	}
	
	public void stop() {
		driver.quit();
	}
	
	public WebDriver getDriver() {
		String browser = properties.getProperty("browser");
		if (driver == null){
			if ("firefox".equals(browser)) {
				driver = new FirefoxDriver();
			} else if ("ie".equals(browser)) {
				System.setProperty("webdriver.ie.driver", "D:\\Trennings\\_Trainings\\Trening_3\\Preparations\\IEDriverServer.exe");
				driver = new InternetExplorerDriver();
			} else if ("chrome".equals(browser)) {
				System.setProperty("webdriver.chrome.driver", "D:\\Trennings\\_Trainings\\Trening_3\\Preparations\\chromedriver.exe");
				driver = new ChromeDriver();
			} else {
				throw new Error("Unsupported browser: " + browser);
			}
		    baseUrl = properties.getProperty("baseUrl");
		    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		    driver.get(baseUrl);
		}
		return driver;
	}

	public void openUrl(String string) {
		driver.get(baseUrl+ string);
	}
	
	public void openAbsoluteUrl(String string) {
		driver.get(string);
	}
	
	public void setProperty(Properties props) {
		this.properties = props;
	}
	
	public String getProperty(String key) {
		return properties.getProperty(key);
	}
	
	public String getProperty(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	public HibernateHelper getHibernateHelper() {
		if (hibernateHelper == null) {
			hibernateHelper = new HibernateHelper(this);
		}
		
		return hibernateHelper;		
	}

/*	public AccountHelper getAccountHelper() {
		
		return null;
	}
*/	

	public AccountHelper getAccountHelper() {
		if (accountHelper == null) {
			accountHelper = new AccountHelper(this);
		}
		
		return accountHelper;
	}
	
	public MailHelper getMailHelper() {
		if (mailHelper == null) {
			mailHelper = new MailHelper(this);
		}
		
		return mailHelper;
	}
	
	public JamesHelper getJamesHelper() {
		if (jamesHelper == null) {
			jamesHelper = new JamesHelper(this);
		}
		return jamesHelper;
	}
	
}