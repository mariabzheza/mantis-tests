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
		
		/* Створюємо Адміністративний Інтерфейс admin, де будемо перевіряти, чи існує юзер
		* (в даному випадку якщо він існує, то ми його видаляємо)
		* для цього можна використовувати безбраузерний http client, org.apache.http.client із селеніуму
		* але ми будемо юзати net.sourceforge.htmlunit.corejs.classfile (це є надбудова над org.apache.http.client)
		* html unit включає в себе http client а також дає додаткові можливості, зокрема працювати з html
		* тобто за допомогою htmlunit ми зможемо працювати як з селеніумом, наприклад, завантажити сторінку, знайти потрібну форму
		*  та потрібні елементи форми, полів вводу даних, заповнити їх значеннями, відправити цю форму і т.д.
		*  В даному випадку таким шляхом ми будемо виконувати лише допоміжні дії по протоколу http 
		*  (це ті дії, які ми не будемо тестувати, а вони лише допомогають пришвидшити роботу за допомогою даного протоколу)
		*  це, наприклад, якби ми звертались напряму до бази даних а не через сайт, щоб зекономити час, щось типу того. 
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
