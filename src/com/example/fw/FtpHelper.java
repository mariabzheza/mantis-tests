package com.example.fw;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class FtpHelper extends HelperBase{

	public static Logger log = Logger.getLogger(FtpHelper.class.getName());
	
	public FtpHelper(ApplicationManager manager) {
		super(manager);
	}

	private FTPClient ftp;

/*	public FtpHelper(AppManager app) {
		this.app = app;
	}
*/
	
	private void initFtpConnection() {
		// ftp.host = localhost ���� ���������� ���������� �� �������� �����, ������ �� ���� ������ �������, �� ����� ����������� �������� ��� ����������
		String ftpserver = manager.getProperty("ftp.host");
		// ��� �����������
		String login = manager.getProperty("ftp.login");
		String password = manager.getProperty("ftp.password");
		// Path to application.
		// ������ ��������, �� ���������� � ����� �������� �������� �� ��������� C:\wamp\www � �� ��� ������ �������� �������
		// ���� ��������, ���� � application.properties �� ������� ��� ���� /mantisbt-1.2.19
		String appPath = manager.getProperty("ftp.appPath");

		if (ftp == null) {
			ftp = new FTPClient();
		}
		if (ftp.isConnected()) {
			return;
		}

		try {
			ftp.connect(ftpserver);
		    ftp.login(login, password);
		    log.info("Connected to " + ftpserver +": " + ftp.getReplyString());
			ftp.changeWorkingDirectory(appPath);

	    } catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// ��������� ����� ���������������� ����� (���� �� ���� ������) � ���� ����� ����������� �������� ��� ������ ����
	// � ����� �������� ������������ ��� ����, ��� �� ���� Captcha �� ��� ��������� �����
	public void instalConfigWithoutCaptcha() {
		String configFile = manager.getProperty("ftp.configFile");
		initFtpConnection();
		try {
			// Check if there is backup of config file
			boolean backupExists = false;
			FTPFile[] files = ftp.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().equals(configFile +".bak")) {
					backupExists = true;
					break;
				}
			}
			if (!backupExists) {
				ftp.rename(configFile, configFile +".bak");
				//pause(20000);
			}
			// ����������� ����� ��������������� ����
			InputStream in = this.getClass().getResourceAsStream("/" + configFile);
			ftp.storeFile(configFile, in);

		} catch (Exception e) {
			e.printStackTrace();
		}
		closeFtpConnection();
	}

	// �������� �������� ������ ����, ���� � ��� � ����� ����������� � ��� � ������ ���������� ����������� ������
	public void restoreConfig() {
		String configFile = manager.getProperty("ftp.configFile");
		
		initFtpConnection();
		try {
			// Check if there is backup of config file
			boolean backupExists = false;
			FTPFile[] files = ftp.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().equals(configFile+ ".bak")) {
					backupExists = true;
					break;
				}
			}
			if (backupExists) {
				ftp.deleteFile(configFile);
				//pause(20000);
				ftp.rename(configFile+ ".bak", configFile);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		closeFtpConnection();
	}
	
	// Original from file
	/*
		public void backupFile(String file, String fileBackup) {
			initFtpConnection();
			try {
				// Check if there is backup of config file
				boolean backupExists = false;
				FTPFile[] files = ftp.listFiles();
				for (int i = 0; i < files.length; i++) {
					if (files[i].getName().equals(fileBackup)) {
						backupExists = true;
						break;
					}
				}
				if (!backupExists) {
					ftp.rename(file, fileBackup);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	*/
	
	/* Original from file
	public void restoreFile(String fileBackup, String file) {
		initFtpConnection();
		try {
			// Check if there is backup of config file
			boolean backupExists = false;
			FTPFile[] files = ftp.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().equals(fileBackup)) {
					backupExists = true;
					break;
				}
			}
			if (backupExists) {
				ftp.deleteFile(file);
				ftp.rename(fileBackup, file);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
*/

	public void uploadFile(InputStream in, String targetFile) {
		initFtpConnection();
		try {
			ftp.storeFile(targetFile, in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void closeFtpConnection() {
		try {
			ftp.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
