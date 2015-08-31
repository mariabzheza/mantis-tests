package com.example.tests;

import static org.testng.Assert.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.example.fw.AccountHelper;
import com.example.fw.JamesHelper;
import com.example.fw.User;

public class SignupTest extends TestBase{
	
	public User user = new User().setLogin("testuser3")
		.setPassword("123456")
		.setEmail("testuser3@localhost.localdomain");
		//.setEmail("testuser1@localhost.localdomain");
	
	private JamesHelper james;

	private AccountHelper accHelper;
	
	@BeforeClass
	public void createMailUser() {
		james = app.getJamesHelper();
		accHelper = app.getAccountHelper();
		if (!james.doesUserExist(user.login)) {
			james.createUser(user.login, user.password);
		}
	}
	
	@AfterClass
	public void deleteMailUser() {
		if (james.doesUserExist(user.login)) {
			james.deleteUser(user.login);
		}
	}
	
	//@Test
	public void  newUserShouldSignup() {
		accHelper.signup(user);
		accHelper.login(user);
		
		assertThat(accHelper.loggedUser(), equalTo(user.login));
	}
	
	@Test
	public void  existingUserShouldNotSignup() {
		try {
			accHelper.signup(user);
		} catch (Exception e) {
			assertThat(e.getMessage(), containsString("That username is already being used"));
			return;
		}
		fail("Exception expected");
		
	}
	
}
