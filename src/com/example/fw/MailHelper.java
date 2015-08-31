package com.example.fw;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

public class MailHelper extends HelperBase{
	
	private String mailserver;
	
/*	public class Msg {
		String text;
		public Msg(String text) {
			this.text = text;
		}
	}
*/

	public MailHelper(ApplicationManager applicationManager) {
		super(applicationManager);
	}
	
	public String getNewMail(String user, String password) {
		//устанавливаем новое соединение с сервером
		Properties props = System.getProperties();
		Session session = Session.getDefaultInstance(props);
		
		//Store store;
		try {
			// открываем хранилище почты по протоколу pop3
			Store store = session.getStore("pop3");
			//store.connect(mailserver, user, password);
			store.connect(manager.getProperty("mailserver"), user, password);
			//получаем доступ к почтовому ящику inbox и открываем его с режимом чтения read-write
			//Folder folder = store.getDefaultFolder().getFolder("INBOX");
			
			Folder root = store.getDefaultFolder();
		    Folder folder = root.getFolder("inbox");
			
			System.out.println("6_01. Folder name is "+ folder.getFullName() +"");
			//Folder folder = store.getFolder("D:\\Trennings\\_Trainings\\Trening_3\\Lesson_9\\james-2.3.2\\apps\\james\\var\\mail\\INBOX");
			folder.open(Folder.READ_WRITE);
			if (folder.isOpen()) {
				System.out.println("6. Folder is Open!!!");
			} else {
				System.out.println("7. Folder is NOT Open!!!");
			}
			int count_message = folder.getMessageCount();
			System.out.println("8. Message count = " + count_message + " ");
			
			int unread_count_message = folder.getUnreadMessageCount();
			System.out.println("9. Unread Message count = " + unread_count_message + " ");
			
			if (folder.getMessageCount() != 1) {
				System.out.println("8. No Letter!!!");
				return null;
			}
			// извлекаем письмо
			Message message = folder.getMessage(1);
			
			//отмечаем как подлежащее удалению
			message.setFlag(Flags.Flag.DELETED, true);
			//Message msg = new Message((String) message.getContent());
			String msg = (String) message.getContent();
			folder.close(true);
			store.close();
			System.out.println("666 MSG = "+ msg);
			
			return msg;
			
/*			if(message.getContentType().startsWith("text/plain")) {
				return msg;
			} else {
				return null;
			}
*/
		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
/*	original code:
 * 
 * +	public String getNewMail(String user, String password) {
		+		Properties props = System.getProperties();
		+		Session session = Session.getDefaultInstance(props);
		+		
		+		Store store;
		+		try {
		+			store = session.getStore("pop3");
		+			store.connect(manager.getProperty("mailserver"), user, password);
		+			Folder folder = store.getDefaultFolder().getFolder("INBOX");
		+			folder.open(Folder.READ_WRITE);
		+			if (folder.getMessageCount() != 1) {
		+				return null;
		+			}
		+			Message message = folder.getMessage(1);
		+			
		+			message.setFlag(Flags.Flag.DELETED, true);
		+			//Msg msg = new Msg((String) message.getContent());
		+			String msg = (String) message.getContent();
		+			folder.close(true);
		+			store.close();
		+
		+			return msg;
		+		
		+		} catch (Exception e) {
		+			e.printStackTrace();
		+			return null;
		+		}
		+	}
*/
	
}
