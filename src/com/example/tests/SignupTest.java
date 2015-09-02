package com.example.tests;

import static org.testng.Assert.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.example.fw.AccountHelper;
import com.example.fw.AdminHelper;
import com.example.fw.JamesHelper;
import com.example.fw.User;

public class SignupTest extends TestBase{
	
	public User user = new User().setLogin("testuser3")
		.setPassword("123456")
		.setEmail("testuser3@localhost.localdomain");
		//.setEmail("testuser1@localhost.localdomain");
	
	private JamesHelper james;
	private AccountHelper accHelper;
	private AdminHelper admin;
	
	@BeforeClass
	public void createMailUser() {
		
		accHelper = app.getAccountHelper();
		admin = app.getAdminHelper();
		james = app.getJamesHelper();
		
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
	
	@Test
	public void  newUserShouldSignup() throws Exception {
		
		/* ��������� �������������� ��������� admin, �� ������ ���������, �� ���� ����
		* (� ������ ������� ���� �� ����, �� �� ���� ���������)
		* ��� ����� ����� ��������������� ������������� http client, org.apache.http.client �� ��������
		* ��� �� ������ ����� net.sourceforge.htmlunit.corejs.classfile (�� � ��������� ��� org.apache.http.client)
		* html unit ������ � ���� http client � ����� �� �������� ���������, ������� ��������� � html
		* ����� �� ��������� htmlunit �� ������� ��������� �� � ���������, ���������, ����������� �������, ������ ������� �����
		*  �� ������ �������� �����, ���� ����� �����, ��������� �� ����������, ��������� �� ����� � �.�.
		*  � ������ ������� ����� ������ �� ������ ���������� ���� ������� 䳿 �� ��������� http 
		*  (�� � 䳿, �� �� �� ������ ���������, � ���� ���� ����������� ����������� ������ �� ��������� ������ ���������)
		*  ��, ���������, ���� �� ���������� ������� �� ���� ����� � �� ����� ����, ��� ���������� ���, ���� ���� ����. 
		*/
		
		admin.deleteUserIfExists(user);
		accHelper.signup(user);
		accHelper.login(user);
		
		assertThat(accHelper.loggedUser(), equalTo(user.login));
	}
	
	//@Test
	public void  existingUserShouldNotSignup() {
		//if (! admin.userExist(user)) {
		//	admin.createUser(user);
		//}
		try {
			accHelper.signup(user);
		} catch (Exception e) {
			assertThat(e.getMessage(), containsString("That username is already being used"));
			return;
		}
		fail("Exception expected");
		
	}
	
}
