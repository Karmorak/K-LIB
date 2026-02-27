package com.karmorak.lib.prototype.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;

import com.karmorak.lib.KLIB;
import com.karmorak.lib.Running;

public class HttpServerTest extends Running {

	private static final String CLRF = "\r\n";
	
	public boolean ready = false;
	private ServerSocket server;
	int log_level = 2;
	
	private response MAIN_SITE  = new response();

	
	private static final String _404String = "<!DOCTYPE html>\r\n"
	+ "<html>\r\n"
	+ "	<head>\r\n"
	+ "	<title>Success</title>\r\n"
	+ "	</head>	\r\n"
	+ "	<body>\r\n"
	+ "		<p>Error 404 Not Found.</p>\r\n"
	+ "	</body>\r\n"
	+ "</html>";
	
	private static final String _404Header = 
			
			"HTTP/1.1 404 Not Found" + CLRF 
			+ "Content-type: text/html" + CLRF
			+ "Content-length: " + _404String.length() + CLRF
			+ CLRF;
	
	private static final response _404 = new response(_404Header, _404String);
	
	private static class response {
		byte[] header;
		byte[] body;
		
		public response() {
			// TODO Auto-generated constructor stub
		} 
		public response(String Header, String Body) {
			header = Header.getBytes();
			body = Body.getBytes();
		} 		
		
	}
	
	@Override
	public void init() {
		System.out.println("starting server...");
		try {
			server = new ServerSocket(4999);	
			
			
			
			if(log_level == 1) {
				System.out.println("Server Ready!");
			} else if(log_level == 2) {
				System.out.println(server.getInetAddress());
				System.out.println("Waiting for client at " + server.getLocalPort());
			} else if(log_level == 3) {
				System.out.println("SERVER SOCKET TESTS:");
				System.out.println("getChannel: " + server.getChannel());
				System.out.println("getInetAddress: " + server.getInetAddress());
				System.out.println("getLocalPort: " + server.getLocalPort());
				System.out.println("getLocalSocketAddress: " + server.getLocalSocketAddress());
			}
			
			File f = null;
			try {
				f = new File(KLIB.class.getResource("spotify2.html").toURI());				
				if(f.exists()) {
					FileInputStream file = new FileInputStream(f.getAbsolutePath());
					
					String response =
							"HTTP/1.1 200 OK" + CLRF
							+ CLRF;
					
					MAIN_SITE.header = response.getBytes();
					MAIN_SITE.body = file.readAllBytes();
					
					
					file.close();
				} else {					
					MAIN_SITE = _404;
				}
				
			} catch (URISyntaxException | NullPointerException e) {
				e.printStackTrace();
				MAIN_SITE = _404;
			}
			
			
			ready = true;
//	            System.out.println("URI - GET INFORMATION:");
//	            URI uri = new URI("httpscheme://world.hello.com/thismy?parameter=value");
//	            System.out.println(uri.getHost());
//	            System.out.println(uri.getPath());
//	            System.out.println(uri.getQuery());
//	            System.out.println(uri.getScheme());
			
			
					
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	boolean info = false, status = false;
	

	
	
	@Override
	public void update() {		
	
		
		
		try(Socket client = server.accept()) {		
			if(log_level == 1) {
				System.out.println("");
				System.out.println("client connected");
			} else if (log_level == 2) {
				System.out.println(client.toString());	
			} else if (log_level == 3) {
				System.out.println("");
	            System.out.println("CLIENT SOCKET TESTS:");
	            System.out.println("getChannel: " + client.getChannel());
	            System.out.println("getLocalAddress: " + client.getLocalAddress());
	            System.out.println("getLocalPort: " + client.getLocalPort());
	            System.out.println("getLocalSocketAddress: " + client.getLocalSocketAddress());
	            System.out.println("getRemoteSocketAddress: " + client.getRemoteSocketAddress());
	            System.out.println("getInetAddress: " + client.getInetAddress());
	            System.out.println("getInputStream: " + client.getInputStream());
	            System.out.println("getOutputStream: " + client.getOutputStream());
			}
			
			
			
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
					
			String line = in.readLine();
			String[] firstline = line.split(" ");
			
			String method = firstline[0];
			String request = firstline[1];
			
			StringBuilder header = new StringBuilder();				
			while(!line.isBlank()) {
				header.append(line + "\r\n");
				line = in.readLine();
			}
			
			
			if(log_level == 2) {
				System.out.println(method + " " + request);
			} else if(log_level == 3) {
				System.out.println("---REQUEST---");
				System.out.println(header);
				System.out.println("");
			}
			
			
			OutputStream clientOutput = client.getOutputStream();
			
			if(method.equals("GET") || method.equals("HEADER")) {			
				
				if(request.equals("/")) {
					
					
					clientOutput.write(MAIN_SITE.header);
					clientOutput.write(MAIN_SITE.body);
					clientOutput.write((CLRF + CLRF).getBytes()); 
					
					clientOutput.flush();
					
				} else  if (request.equals("/edshape.jpg")) {
					
					File f2 = new File(KLIB.class.getResource(request.replaceFirst("/", "")).toURI());
					FileInputStream image = new FileInputStream(f2.getAbsolutePath());
					
					
					String response =
							"HTTP/1.1 200 OK" + CLRF +
							"Content-Type: image/jpeg" +  CLRF
							+ CLRF
							;
					
					clientOutput.write(response.getBytes());
					clientOutput.write(image.readAllBytes());
					clientOutput.write((CLRF + CLRF).getBytes()); 
				} else {
					
					clientOutput.write(MAIN_SITE.header);
					clientOutput.write(MAIN_SITE.body);
					clientOutput.write((CLRF + CLRF).getBytes()); 
					
					clientOutput.flush();
				}
					  
					  
					  
					  
//					  System.err.println("Client connection closed!");
					  in.close();
					  clientOutput.close();
					  client.close();
					  
			}		  
				  
				 
		
		} catch (IOException | URISyntaxException  e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}
	
	// return supported MIME Types
	private String getContentType(String fileRequested) {
		if (fileRequested.endsWith(".htm")  ||  fileRequested.endsWith(".html"))
			return "text/html";
		else
			return "text/plain";
	}
	
	private byte[] readFileData(File file) throws IOException {
		FileInputStream fileIn = null;
		byte[] fileData = new byte[(int) file.length()];
		
		try {
			fileIn = new FileInputStream(file);
			fileIn.read(fileData);
		} finally {
			if (fileIn != null) 
				fileIn.close();
		}
		
		return fileData;
	}
	
	
	
	
}
	
	