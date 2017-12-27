package com.example.ftp;

import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.Session;
import org.springframework.stereotype.Component;

@Component
public class FtpHelper {
	@Autowired
	CachingSessionFactory<FTPFile> factory;
	
	
	public  Session<FTPFile> getSession() {
		return factory.getSession();
	}
	
	public void close(Session<FTPFile> session){
		session.close();
	}
	
}
