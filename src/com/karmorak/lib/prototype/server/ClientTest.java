package com.karmorak.lib.prototype.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientTest {
	
	
	String encoding = "UTF-8";
	String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13";
	
	Socket s;
	
	public ClientTest(String url, int port) {
//		try {				//localhost, 4999
//			
//						
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
	public String sendRequest(String url) {	
		StringBuilder builder = new StringBuilder();
		
		try {			
			Socket s = new Socket(url, 80);	
			PrintWriter pr = new PrintWriter(s.getOutputStream());
			
		    pr.println("GET /watch?v=NyVYXRD1Ans HTTP/1.1");
		    pr.println("User-Agent: " + userAgent);
//		    pr.println("Content-Type: application/x-www-form-urlencoded");
		    pr.println("Host: " + url);
		    pr.println("");
		    pr.flush();
		    
		    
		    //Creates a BufferedReader that contains the server response
	        BufferedReader bufRead = new BufferedReader(new InputStreamReader(s.getInputStream()));
	        String outStr;
	        

	        
	        while((outStr = bufRead.readLine()) != null){
	        	System.out.println(outStr);
	        	builder.append(outStr);
	        }
	        
	        
	        
	        //Closes out buffer and writer
	        bufRead.close();
	        pr.close();
	        s.close();

	        return builder.toString();		    
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
	}

}

