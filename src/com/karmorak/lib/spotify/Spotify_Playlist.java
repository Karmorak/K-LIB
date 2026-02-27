package com.karmorak.lib.spotify;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
public class Spotify_Playlist {
	
	
	private static final String PLAYLIST = "https://api.spotify.com/v1/playlists/"; 
	
	public static String getPlaylist(String access_token, String id) throws IOException, InterruptedException {
		
	
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
    		.GET()
    		.setHeader("Content-Type", "application/json")
    		.setHeader("Authorization" ,  "Bearer " + access_token)
//    		.setHeader("Authorization", access_token)
    		.uri(URI.create(PLAYLIST+id))
    		.build();	

		
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		return response.body();
		
	}
	
	
	//https://open.spotify.com/playlist/0QkYe3o4hkKahdBK1YcQVx?si=13cc671e47db4f52
	
	public static String getIDfromURL(String url) {
		
		String[] in = url.split("/");
		
		
		String out = in[in.length-1];
		if(out.contains("?si=")) {
			out = out.split("\\?")[0];
		}
		
		
		return out;
	}
	

}
