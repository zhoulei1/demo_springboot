package com.example.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;

public class CommonUtil {
	/**
	 * 获取文件的后缀名
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileExt(String fileName) {
		int index = fileName.lastIndexOf(".");
		if (index <= 0) {
			return "";
		}
		return fileName.substring(index);
	}
	
	
	/**
	 * 获取csv文件的编码
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String getCsvCharset(BufferedInputStream in) throws IOException {
		String charsetName = "UTF-8";
		int p = (in.read() << 8) + in.read();

		switch (p) {
		case 0xefbb:
			charsetName = "UTF-8";
			break;
		case 0xfffe:
			charsetName = "Unicode";
			break;
		case 0xfeff:
			charsetName = "UTF-16BE";
			break;
		default:
			charsetName = "GBK";
		}

		return charsetName;
	}
	
	//下载获取附件名称
	public static String getAttachmentName(HttpServletRequest request, String filename) throws UnsupportedEncodingException {
		String user_agent = request.getHeader("user-agent");
		boolean firefox = false;
		if (user_agent != null && user_agent.toLowerCase().contains("firefox"))
			firefox = true;
		else
			firefox = false;
		if (firefox)
			return "=?UTF-8?B?"+ new String(Base64.encodeBase64(filename.getBytes("UTF-8"))) + "?=";
		else
			return "\"" + URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20") + "\";filename*=UTF-8''"
					+ URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
	}
}
