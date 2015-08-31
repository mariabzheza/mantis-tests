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
		
		try {
			// открываем хранилище почты по протоколу pop3
			Store store = session.getStore("pop3");
			//store.connect(mailserver, user, password);
			store.connect(manager.getProperty("mailserver"), user, password);
			//получаем доступ к почтовому ящику inbox и открываем его с режимом чтения read-write
			//Folder folder = store.getDefaultFolder().getFolder("INBOX");
			
			Folder root = store.getDefaultFolder();
		    Folder folder = root.getFolder("inbox");
			
			System.out.println("!!! Folder name is "+ folder.getFullName() +"");
			
			folder.open(Folder.READ_WRITE);
			if (folder.isOpen()) {
				System.out.println("!!! Folder is Open!!!");
			} else {
				System.out.println("!!! Folder is NOT Open!!!");
			}
			int count_message = folder.getMessageCount();
			System.out.println("!!! Message count = " + count_message + " ");
			
			int unread_count_message = folder.getUnreadMessageCount();
			System.out.println("!!! Unread Message count = " + unread_count_message + " ");
			
			if (folder.getMessageCount() != 1) {
				System.out.println("There is No Letter!!!");
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
			System.out.println("*******************");
			System.out.println("!!! MSG Text = "+ msg);
			System.out.println("*******************");
			
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
