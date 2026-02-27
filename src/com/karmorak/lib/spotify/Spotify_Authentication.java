package com.karmorak.lib.spotify;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import com.karmorak.lib.KLIB;
import com.karmorak.lib.utils.Encodings;

public abstract class Spotify_Authentication {
	
	
	public static final String API_TOKEN_URL = "https://accounts.spotify.com/api/token";
	
	/** returns s String list with {access_token} */
	public static String[] Authv1() throws IOException, InterruptedException {
		String client_id = "75b84efdd12942aea58177fb442cb77a";
		String client_secret = "e943bbdea71a4f3699d5088dcbebc89c";
		String clientandsecret = client_id + ":" + client_secret;
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()			
    		.POST(BodyPublishers.ofString("grant_type=client_credentials"))    	
			.setHeader("Content-Type", "application/x-www-form-urlencoded")
			.setHeader("Authorization" ,  "Basic " +  Base64.getEncoder().encodeToString(clientandsecret.getBytes()))
			.uri(URI.create("https://accounts.spotify.com/api/token"))
    		.build();
		
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());;
		String responsebody = response.body();
		String[] list = responsebody.split("\"");
		return new String[] {list[3]};
	}
	
	public static String[] Authv2() throws NoSuchAlgorithmException, IOException, URISyntaxException, InterruptedException {
		String codeverifier = "veryveryverylooooooooooooooooooooooooooooooooooooooooooooooooooooooooooongcodechallenge";
		
		String text = "test";
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
		
		byte[] encodedBytes = Base64.getEncoder().encode(hash);
		String codechallenge = new String(encodedBytes);
//		
		String CLRF = "\r\n";
		String header =
				"HTTP/1.1 200 OK" + CLRF
				+ CLRF;
		
		String redirectpath = "http://localhost:4999";
		
		String client_id = "75b84efdd12942aea58177fb442cb77a";
		String client_secret = "e943bbdea71a4f3699d5088dcbebc89c";
		String clientandsecret = client_id + ":" + client_secret;
		String scopes = "user-read-private user-read-email user-read-playback-state user-modify-playback-state user-read-currently-playing app-remote-control streaming";
		String redirect_uri = redirectpath;
		
		String POSTS_API_URL = "https://accounts.spotify.com/authorize" + 
				"?response_type=code" + 
				"&client_id=" + client_id + 
				"&scope="  + Encodings.encodeURIComponent(scopes) +
				"&redirect_uri=" + Encodings.encodeURIComponent(redirect_uri);
		
		
		ServerSocket s = new ServerSocket(4999);
		
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
		    Desktop.getDesktop().browse(new URI(POSTS_API_URL));
		}	
		Socket client = s.accept();
		BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));		
		String response = in.readLine();
		
		OutputStream clientOutput = client.getOutputStream();
		


		
		clientOutput.write(header.getBytes());
		clientOutput.write(	("<!DOCTYPE html>\r\n"
				+ "<html>\r\n"
				+ "	<head>\r\n"
				+ "	<title>Success</title>\r\n"
				+ "	<link rel=\"icon\" href=\"data:;base64,iVBORw0KGgo=\">\r\n"
				+ "	</head>	\r\n"
				+ "	\r\n"
				+ "	<body>\r\n"
				+ "		<h1>Authentication Complete</h1>\r\n"
				+ "		<p>You can close the browser.</p>\r\n"
				+ "	</body>\r\n"
				+ "</html>").getBytes());
		clientOutput.flush();
		client.close();
		
		String code = response.split(" ")[1].replace("/?code=", "");
		s.close();
		
		//ALTENATIVE CLIENT ERSTELLEN		
//		header = 
//				"POST / HTTP/1.1" + CLRF + 
//				"accept:  application/json" + CLRF +
//				"Content-Type application/x-www-form-urlencoded" + CLRF +
//				"Authorization Basic " + Base64.getEncoder().encodeToString(clientandsecret.getBytes()) + CLRF + CLRF + CLRF
//				;
				
		
		String body = "grant_type=authorization_code" +
				"&code=" + code + 
				"&redirect_uri=" + Encodings.encodeURIComponent(redirect_uri) +
				"&client_id=" + client_id + 
				"&client_secret=" + client_secret + CLRF;	

//		client = new Socket("https://accounts.spotify.com/api/token", 80);
//		
//		OutputStream out = client.getOutputStream();
//		out.write(header.getBytes());
//		out.write(body.getBytes());
//		
//		out.flush();
//				
//		BufferedReader bf = new BufferedReader(new InputStreamReader(client.getInputStream()));
//		
//		String responseString = bf.readLine();
//		String[] firstline = responseString.split(" ");
//		
//		
//		StringBuilder sb = new StringBuilder();				
//		while(!responseString.isBlank()) {
//			sb.append(responseString + "\r\n");
//			responseString = bf.readLine();
//		}
//		
//		System.out.println(responseString);

	
		HttpRequest request2 = HttpRequest.newBuilder()
				.POST(BodyPublishers.ofString(body))
				.header("accept", "application/json")
				.setHeader("Content-Type", "application/x-www-form-urlencoded")
				.setHeader("Authorization" ,  "Basic " +  Base64.getEncoder().encodeToString(clientandsecret.getBytes()))
				.uri(URI.create(API_TOKEN_URL))
				.build();
		
		HttpClient client2 = HttpClient.newHttpClient();
		HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());
		
		String responseString = response2.body();
		
		String[] list = responseString.split("\"");
		String access_token = list[3];
		String refresh_token = list[13];
		
				
		return new String[] {access_token, refresh_token};
	}
	
	public static String[] Auth_RefreshToken(String refresh_token) throws NoSuchAlgorithmException, IOException, URISyntaxException, InterruptedException {
		
		String CLRF = "\r\n";
		String header =
				"HTTP/1.1 200 OK" + CLRF
				+ CLRF;
		
		String redirectpath = "http://localhost:4999";
		
		String client_id = "75b84efdd12942aea58177fb442cb77a";
		String client_secret = "e943bbdea71a4f3699d5088dcbebc89c";
		String clientandsecret = client_id + ":" + client_secret;
		String scopes = "user-read-private user-read-email user-read-playback-state user-modify-playback-state user-read-currently-playing app-remote-control streaming";
		String redirect_uri = redirectpath;
		
		String body = "grant_type=refresh_token" +
				"&refresh_token=" + refresh_token + 
				"&client_id=" + client_id + CLRF;	
		
		
		HttpRequest request2 = HttpRequest.newBuilder()
				.POST(BodyPublishers.ofString(body))
				.header("accept", "application/json")
				.setHeader("Content-Type", "application/x-www-form-urlencoded")
				.setHeader("Authorization" ,  "Basic " +  Base64.getEncoder().encodeToString(clientandsecret.getBytes()))
				.uri(URI.create(API_TOKEN_URL))
				.build();
		
		HttpClient client2 = HttpClient.newHttpClient();
		HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());
		
		String responseString = response2.body();
		
		String[] list = responseString.split("\"");
		String access_token = list[3];
		
		String new_refresh_token;
		
		if(responseString.contains(refresh_token)) {
			new_refresh_token = list[13];
			KLIB.TEMP_DATA.setValue("spotify_refresh_token", new_refresh_token);
		}else 
			new_refresh_token = refresh_token;
		
		
		KLIB.TEMP_DATA.setValue("spotify_access_token", access_token);
		KLIB.TEMP_DATA.setValue("spotify_auth_time", "" + KLIB.system.Date() + " " + KLIB.system.Time());
		
		return new String[] {access_token, new_refresh_token};
	}
	
	
	public static String[] Authv3() throws NoSuchAlgorithmException, IOException, URISyntaxException, InterruptedException {
		
		String access_token = KLIB.TEMP_DATA.getValue("spotify_access_token");
		String refresh_token = KLIB.TEMP_DATA.getValue("spotify_refresh_token");
		
		System.out.println("Start Auth v3");
		if(access_token == null || access_token.equals("")) {	
			System.out.println("no access token");
			if(refresh_token == null || refresh_token.equals("")) {
				System.out.println("no refresh token");
				
				String[] s = Authv2();
				KLIB.TEMP_DATA.setValue("spotify_refresh_token", s[1]);
				KLIB.TEMP_DATA.setValue("spotify_access_token", s[0]);
				KLIB.TEMP_DATA.setValue("spotify_auth_time", "" + KLIB.system.Date() + " " + KLIB.system.Time());
				
				
				return s;
			} else {
				System.out.println("get by refresh token");
				return Auth_RefreshToken(refresh_token);			
			}			
		} else {
			System.out.println("access token");
			
			String l = KLIB.TEMP_DATA.getValue("spotify_auth_time");
			
			
			String[] previous_date = l.split(" ")[0].split("\\.");
			String[] previous_time = l.split(" ")[1].split(":");
			
			long days_in_sec = Long.parseLong(previous_date[0]) * 86400;
			long hours_in_sec = Long.parseLong(previous_time[0]) * 3600;
			long minutes_in_sec = Long.parseLong(previous_time[1]) * 60;
			long previous_seconds = Long.parseLong(previous_time[2]) + minutes_in_sec + hours_in_sec + days_in_sec;						
			
			String[] current_date = KLIB.system.Date().split("\\.");
			String[] current_time = KLIB.system.Time().split(":");
			
			days_in_sec = Long.parseLong(current_date[0]) * 86400;
			hours_in_sec = Long.parseLong(current_time[0]) * 3600;
			minutes_in_sec = Long.parseLong(current_time[1]) * 60;
			long current_seconds = Long.parseLong(current_time[2]) + minutes_in_sec + hours_in_sec + days_in_sec;
			
			
			if(previous_date[1].equals(current_date[1]) && previous_date[2].equals(current_date[2])) {			
				System.out.println("same month");
				if(current_seconds - previous_seconds < 3600) {
					System.out.println("in time");
					return new String[] {access_token, refresh_token};					
				} else {
					System.out.println("not in time");
					return Auth_RefreshToken(refresh_token);
				}
			} else {
				System.out.println("not in month");
				return Auth_RefreshToken(refresh_token); 	
			}
			
		}	
	}
	
	
	
	
	public static String getFinalURL(String url) throws IOException {
	    HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
	    con.setInstanceFollowRedirects(false);
	    con.connect();
	    con.getInputStream();

	    if (con.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM || con.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
	        String redirectUrl = con.getHeaderField("Location");
	        return getFinalURL(redirectUrl);
	    }
	    return url;
	}
	
	public static String getFinalRedirectedUrl(String url) {

	    HttpURLConnection connection;
	    String finalUrl = url;
	    try {
	        do {
	            connection = (HttpURLConnection) new URL(finalUrl)
	                    .openConnection();
	            connection.setInstanceFollowRedirects(false);
	            connection.setUseCaches(false);
	            connection.setRequestMethod("GET");
	            connection.connect();
	            int responseCode = connection.getResponseCode();
	            if (responseCode >= 300 && responseCode < 400) {
	                String redirectedUrl = connection.getHeaderField("Location");
	                if (null == redirectedUrl)
	                    break;
	                finalUrl = redirectedUrl;
	                System.out.println("redirected url: " + finalUrl);
	            } else
	                break;
	        } while (connection.getResponseCode() != HttpURLConnection.HTTP_OK);
	        connection.disconnect();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return finalUrl;
	}

	@SuppressWarnings("unused")
	static URL URL(String path) {
		return Spotify_Authentication.class.getResource(path);
	}

}
