package com.example.ftp;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.Session;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.ftp.session.FtpSession;
@Configuration
@ConfigurationProperties(prefix="spring.ftp")
public class FtpConfig {
    @Autowired
    private Environment env;
	@Bean
	public CachingSessionFactory<FTPFile> ftpSessionFactory(){
		System.out.println(env.getProperty("spring.ftp.host"));
		DefaultFtpSessionFactory ds = new DefaultFtpSessionFactory();
		ds.setHost(env.getProperty("spring.ftp.host"));
		ds.setPort(Integer.parseInt(env.getProperty("spring.ftp.port")));
		ds.setUsername(env.getProperty("spring.ftp.username"));
		ds.setPassword(env.getProperty("spring.ftp.password"));
		ds.setClientMode(FTPClient.PASSIVE_LOCAL_DATA_CONNECTION_MODE);
		ds.setFileType(FTPClient.BINARY_FILE_TYPE);
		ds.setBufferSize(Integer.parseInt(env.getProperty("spring.ftp.bufferSize")));
		ds.setConnectTimeout(Integer.parseInt(env.getProperty("spring.ftp.connectTimeout")));
		ds.setDataTimeout(Integer.parseInt(env.getProperty("spring.ftp.dataTimeout")));
		ds.setControlEncoding(env.getProperty("spring.ftp.controlEncoding"));
		CachingSessionFactory<FTPFile> cachingSessionFactory = new CachingSessionFactory<FTPFile>(ds);
		cachingSessionFactory.setPoolSize(Integer.parseInt(env.getProperty("spring.ftp.poolSize")));
		cachingSessionFactory.setSessionWaitTimeout(Integer.parseInt(env.getProperty("spring.ftp.sessionWaitTimeout")));
		return cachingSessionFactory;
	} 
	
	public static void main(String[] args) {
		DefaultFtpSessionFactory ds = new DefaultFtpSessionFactory();
		ds.setHost("localhost");
		ds.setPort(21);
		ds.setUsername("admin");
		ds.setPassword("1");
		ds.setClientMode(FTPClient.PASSIVE_LOCAL_DATA_CONNECTION_MODE);
		ds.setFileType(FTPClient.BINARY_FILE_TYPE);
		ds.setBufferSize(100000);
		ds.setConnectTimeout(5000);
		ds.setDataTimeout(5000);
		ds.setControlEncoding("UTF-8");
		CachingSessionFactory<FTPFile> cachingSessionFactory = new CachingSessionFactory<FTPFile>(ds);
		cachingSessionFactory.setPoolSize(10);
		cachingSessionFactory.setSessionWaitTimeout(5000);
		Session<FTPFile> session = cachingSessionFactory.getSession();
		try {
			FTPFile[] list = session.list("/");
			for (FTPFile  f: list) {
				System.out.println(f.getName()+":"+ f.isFile());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
