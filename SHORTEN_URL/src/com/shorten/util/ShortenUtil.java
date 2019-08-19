package com.shorten.util;

import java.util.HashMap;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ShortenUtil {
	
	Logger logger = LoggerFactory.getLogger(ShortenUtil.class);
	
	public static String shortenUrlFileDrive = "C:/";
	public static String shortenUrlFileName = "Temp/ShortenUrlList.txt";
	private HashMap<String, String> keyMap; 
	private HashMap<String, String> valueMap;
	private String domain; 
	private char myChars[]; 
	private Random myRand;
	private int keyLength;

	// Default Constructor
	public ShortenUtil() {
		keyMap = new HashMap<String, String>();
		valueMap = new HashMap<String, String>();
		myRand = new Random();
		keyLength = 7;
		myChars = new char[52];
		
		int idx = 0;
		for (int i = 10; i < 62; i++) {
			int j = 0;
			if (i < 10) {
				j = i + 48;
			} else if (i > 9 && i <= 35) {
				j = i + 55;
			} else {
				j = i + 61;
			}
			myChars[idx++] = (char) j;
		}
		
		domain = "http://localhost";
	}

	/**
	 * ID길이와 서버주소를 받는 생성자
	 * @param length
	 * @param newDomain
	 */
	public ShortenUtil(int length, String newDomain) {
		this();
		this.keyLength = length-1;
		if (!newDomain.isEmpty()) {
			newDomain = sanitizeURL(newDomain);
			domain = newDomain;
		}
	}

	/**
	 * 원래의 URL로 부터 단축 URL을 리턴한다.
	 * @param longURL
	 * @return
	 */
	public String shortenURL(String longURL) {
		String shortURL = "";
		//if (validateUrlCheck(longURL)) {
		//if (true) {
			longURL = sanitizeURL(longURL);
			if (valueMap.containsKey(longURL)) {
				shortURL = domain + "/" + valueMap.get(longURL);
			} else {
				shortURL = domain + "/" + getKey(longURL);
			}
		//}
		// add http part
		return shortURL;
	}


	/**
	 * URL 의 유효여부 체크한다.
	 * @param url
	 * @return
	 */
	public boolean validateUrlCheck(String url){
		 try {
			 
			 String[] schemes = {"http","https"};
			 UrlValidator urlValidator = new UrlValidator(schemes);
			 if (urlValidator.isValid(url)) {
				   System.out.println("URL is valid");
				   return true;
			 } else {
				   System.out.println("URL is invalid");
				   return false;
			 }
			 
      } catch (Exception e) {
             e.printStackTrace();
             return false;
      }		
	}




	/**
	 * http, https 를 제외한 유효한 URL을 가져온다.
	 * @param url
	 * @return
	 */
	String sanitizeURL(String url) {
		if (url.substring(0, 7).equals("http://"))
			url = url.substring(7);

		if (url.substring(0, 8).equals("https://"))
			url = url.substring(8);

		if (url.charAt(url.length() - 1) == '/')
			url = url.substring(0, url.length() - 1);
		return url;
	}

	//
	private String getKey(String longURL) {
		String key;
		key = generateKey();
		keyMap.put(key, longURL);
		valueMap.put(longURL, key);
		return key;
	}

	// 랜덤문자생성
	private String generateKey() {
		String key = "";
		boolean flag = true;
		while (flag) {
			key = "";
			for (int i = 0; i <= keyLength; i++) {
				key += myChars[myRand.nextInt(52)];
			}
			// System.out.println("Iteration: "+ counter + "Key: "+ key);
			if (!keyMap.containsKey(key)) {
				flag = false;
			}
		}
		return key;
	}

	/**
	 * 입력된 URL이 기존재하는 주소이면 해당URL에 대한 단축URL을 리턴한다.
	 * @param urlText
	 * @return
	 */
	public static String existShortenUrl(String urlText){
		File file = new File(shortenUrlFileDrive,shortenUrlFileName);
		BufferedReader br = null;
		String lineText = null;
		String existShortenUrl = null;
		try {
			br = new BufferedReader(new FileReader(file));
			while((lineText = br.readLine()) != null){
				String[] urlAry = lineText.split("\t");
				if(urlAry[0].equals(urlText)) return urlAry[1];
			}
		} catch (Exception e) {
	            e.getStackTrace();
		} finally {
            try {
                if(br != null) br.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
		return existShortenUrl;
	}

	/**
	 * ID를 기준으로 원래의 URL을 리턴한다.
	 * @param id
	 * @return
	 */
	public static String existOriginUrl(String id){
		File file = new File(shortenUrlFileDrive,shortenUrlFileName);
		BufferedReader br = null;
		String lineText = null;
		String existOriginUrl = null;
		try {
			br = new BufferedReader(new FileReader(file));
			while((lineText = br.readLine()) != null){
				String[] urlAry = lineText.split("\t");
				if(urlAry[1].indexOf(id) > 0) return urlAry[0];
			}
		} catch (Exception e) {
	            e.getStackTrace();
		} finally {
            try {
                if(br != null) br.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
		return existOriginUrl;
	}
	
	
	//테스트용
	public static void main(String args[]) {
		//System.out.println(existShortenUrl("www.google.com"));
		ShortenUtil u = new ShortenUtil(8, "localhost/");
		
		String urlText = "daum.net";
		String returnShortenUrl = existShortenUrl(urlText);
		if(returnShortenUrl == null) {
			String newShortenUrl = u.shortenURL(urlText);
			File file = new File(shortenUrlFileDrive,shortenUrlFileName);
		    BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new FileWriter(file, true));
				bw.write(urlText + "\t" + newShortenUrl);
				bw.newLine();
	            bw.flush();					
			} catch (Exception e) {
		            e.getStackTrace();
			} finally {
	            try {
	                if(bw != null) bw.close();
	            } catch(IOException e) {
	                e.printStackTrace();
	            }
	        }
		}
	}
	
}
