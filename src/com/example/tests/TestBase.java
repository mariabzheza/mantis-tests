package com.example.tests;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

import org.seleniumhq.jetty7.util.log.Log;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;

import com.example.fw.ApplicationManager;

public class TestBase {

	protected ApplicationManager app;
	
	@BeforeTest
		public void setUp() throws Exception {
	    /*Properties properties = new Properties();
	    properties.load(new FileReader (new File( "application.properties")) );
		app=new ApplicationManager(properties); */
		
		String configFile = System.getProperty("configFile","application.properties");
		Properties properties = new Properties();
		properties.load(new FileReader (new File(configFile))); 
		app = new ApplicationManager(properties);
		// метод instalConfigWithoutCaptcha() створює копію файла (*.bak) а потім прописує свій конфіг-файл з налаштуваннями для mantisbt
		// для того, щоб під час створення юзера не потрібно було вводити каптчу
		app.getFtpHelper().instalConfigWithoutCaptcha();
	  }
	 
	@AfterTest
	public void tearDown() throws Exception {
		// тут до методу stop() додано відновлення попередньої конфігурації для mantisbt ftpHelper.restoreConfig();
		// файл config_inc.php витираємо і з бак-файлу відновлюємо оригінальний файл
		app.stop();
	  }  

	

	
}