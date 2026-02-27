package com.karmorak.lib.spotify;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.util.ArrayList;
import java.net.http.HttpResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.karmorak.lib.spotify.Spotify_Manager.Spotify_Device;

public class Spotify_Manager {
	
	private static final String ROOT = "https://api.spotify.com/v1/";
	
	public static final String CMD_getInfo = "";
	public static final String CMD_getAvailableDevices = "me/player/devices";
	public static final String CMD_playSong = "me/player/play";
	public static final String CMD_queueSong = "me/player/queue";
	public static final String CMD_nextSong = "me/player/next";
	public static final String CMD_getTrack = "tracks/";
	
	/**
	 * 
	 * me/playlists = users playlists
	 * playlists/{id} = playlist infoprmation
	 * 
	 * 
	 * 
	 * 
	 * @param urlendpoint
	 * @return
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static String getInformation(String urlendpoint, String access_token) throws IOException, InterruptedException {
		
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
    		.GET()
    		.setHeader("Content-Type", "application/json")
    		.setHeader("Authorization" ,  "Bearer " + access_token)
//    		.setHeader("Authorization", access_token)
    		.uri(URI.create(ROOT + urlendpoint))
    		.build();	

		
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		return response.body();
	}
	
//	https://api.spotify.com/v1/me/player/devices
	
	public static String getAvailableDevices(String access_token) throws IOException, InterruptedException {
		
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
    		.GET()
    		.setHeader("Authorization" ,  "Bearer " + access_token)
//    		.setHeader("Authorization", access_token)
    		.uri(URI.create(ROOT + CMD_getAvailableDevices))
    		.build();	
		
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		return response.body();	
	}
	
	
	public static ArrayList<Spotify_Device> getAvailableSpotifyDevices(String access_token) throws IOException, InterruptedException {
		
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
    		.GET()
    		.setHeader("Authorization" ,  "Bearer " + access_token)
//    		.setHeader("Authorization", access_token)
    		.uri(URI.create(ROOT + CMD_getAvailableDevices))
    		.build();	
		
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		
		String[] s1 = response.body().split("\\{");
		
		ArrayList<Spotify_Device> device_list = new ArrayList<Spotify_Device>();
	
		for (int i = 2; i < s1.length; i++) {
			s1[i] = s1[i].replace(",", "").replace("\"", "").replace("}", "").replace("]", "").replace(" ", "").replace("\n", ":").replaceFirst(":", "");	
			String[] s2 = s1[i].split(":");
			if(s2.length > 14) {			
				String id = s2[1];
				boolean is_active = Boolean.parseBoolean(s2[3]);
				boolean is_private_session = Boolean.parseBoolean(s2[5]);
				boolean is_restricted = Boolean.parseBoolean(s2[7]);
				String name = s2[9];
				boolean supports_volume = Boolean.parseBoolean(s2[11]);
				String type= s2[13];
				int volume_percent = Integer.parseInt(s2[15]);	
				
				Spotify_Device d = new Spotify_Device(id, is_active, is_private_session, is_restricted, name, supports_volume, type, volume_percent);
				
				device_list.add(d);
			}
		}
		
		return device_list;	
	}
	
//	https://open.spotify.com/intl-de/album/3hA2oZbZwHU8tSPBFIZhFr?si=AFj6auMZT9Kpbca3FXBW0A
// 	https://api.spotify.com/v1/me/player/play
	
//	3T4tUhGYeRNVUGevb0wThu divide
// https://open.spotify.com/intl-de/track/2pJZ1v8HezrAoZ0Fhzby92?si=0eb89ea6d53a48ef
	
	

	public static String playSong(String access_token, String device_id, String song_id) throws IOException, InterruptedException {
		
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
    		.PUT(HttpRequest.BodyPublishers.ofString("{\"uris\": [\"spotify:track:4iV5W9uYEdYUVa79Axb7Rh\", \"spotify:track:1301WleyT98MSxVHPZCA6M\"],\"offset\": {\"position\": 0},\"position_ms\": 0}"))
    		.setHeader("Content-Type", "application/json")
    		.setHeader("Authorization" ,  "Bearer " + access_token)
//    		.setHeader("Authorization", access_token)
    		.uri(URI.create(ROOT + CMD_playSong + "?device_id=" +device_id ))
    		.build();	

		
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		System.out.println(response);
		
		return response.body();
	}
	
	public static String playSongs(String access_token, String device_id, String[] song_ids) throws IOException, InterruptedException {
		
		if(song_ids.length < 2) return null;
		
		String body = "{\"uris\": [";
		
		for (int i = 0; i < song_ids.length; i++) {
			
			
			if(i < song_ids.length -1)
				body = body + "\"spotify:track:" + song_ids[i]+ "\", ";
			else
				body = body + "\"spotify:track:" + song_ids[i]+ "\"";
		}
		
		body = body + "],\"offset\": {\"position\": 0},\"position_ms\": 0}";		
		
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
    		.PUT(HttpRequest.BodyPublishers.ofString(body))
    		.setHeader("Content-Type", "application/json")
    		.setHeader("Authorization" ,  "Bearer " + access_token)
//    		.setHeader("Authorization", access_token)
    		.uri(URI.create(ROOT + CMD_playSong + "?device_id=" +device_id ))
    		.build();	

		
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//		System.out.println("Body Response:" + response.statusCode());
		
		return response.body();
	}
	
	public static String getAlbumUribyTrack(String access_token, String song_id) throws IOException, InterruptedException {
		
		
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
    		.GET()
    		.setHeader("Content-Type", "application/json")
    		.setHeader("Authorization" ,  "Bearer " + access_token)
    		.uri(URI.create(ROOT + CMD_getTrack + song_id))
    		.build();	

		
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		System.out.println("StatusCode Response:" + response.statusCode());
		System.out.println("StatusCode Body:" + response.body());
		
		
		
		JSONParser parser = new JSONParser();
		
		
		try {
			JSONObject body = (JSONObject) parser.parse(response.body());
			JSONObject album = (JSONObject) body.get("album");
			
			 return (String) album.get("uri");
			
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
			
		}
	}
	
	

	public static String playAlbumSong(String access_token, String device_id, String song_id, String album_id) throws IOException, InterruptedException {
		
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
    		.PUT(HttpRequest.BodyPublishers.ofString("{\r\n"
    				+ "    \"context_uri\": \"spotify:album:" + album_id + "\",\r\n"
    				+ "    \"offset\": {\"uri\": \"spotify:track:" + song_id + "\"},"
    				+ "    \"position_ms\": 0\r\n"
    				+ "}"))
    		.setHeader("Content-Type", "application/json")
    		.setHeader("Authorization" ,  "Bearer " + access_token)
//    		.setHeader("Authorization", access_token)
    		.uri(URI.create(ROOT + CMD_playSong + "?device_id=" +device_id ))
    		.build();	

		
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());		
		return response.body();
	}
	
	
//	public static String playSong(String access_token, String device_id, String song_id) throws IOException, InterruptedException {
//		
//		HttpClient client = HttpClient.newHttpClient();
//		HttpRequest request = HttpRequest.newBuilder()
//    		.PUT(HttpRequest.BodyPublishers.ofString("{"
//    				+ "    \"offset\": {\"uri\": \"spotify:track:1301WleyT98MSxVHPZCA6M\"},"
//    				+ "    \"position_ms\": 0"
//    				+ "}"))
//    		.setHeader("Content-Type", "application/json")
//    		.setHeader("Authorization" ,  "Bearer " + access_token)
////    		.setHeader("Authorization", access_token)
//    		.uri(URI.create(ROOT + CMD_playSong + "?device_id=" +device_id ))
//    		.build();	
//
//		
//		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//		System.out.println(response);
//		
//		return response.body();
//	}
	
	
	public static String skipSong(String access_token, String device_id) throws IOException, InterruptedException {
		
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
    		.POST(HttpRequest.BodyPublishers.noBody())
    		.setHeader("Authorization" ,  "Bearer " + access_token)
//    		.setHeader("Authorization", access_token)
    		.uri(URI.create(ROOT + CMD_nextSong + "?device_id=" + device_id))
    		.build();	

		
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		System.out.println(response);
		
		return response.body();
	}
	
	public static String addSongtoQueue(String access_token, String device_id, String song_id) throws IOException, InterruptedException {
		
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
    		.POST(HttpRequest.BodyPublishers.noBody())
    		.setHeader("Authorization" ,  "Bearer " + access_token)
//    		.setHeader("Authorization", access_token)
    		.uri(URI.create(ROOT + CMD_queueSong +  "?uri=spotify%3Atrack%3A" + song_id+ "&device_id=" + device_id))
    		.build();	

		
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		System.out.println(response);
		
		return response.body();
	}
	
	
	
	public static class Spotify_Device {
		private final String id;
		final boolean is_active;
		final boolean is_private_session;
		final boolean is_restricted;
		final String name;
		final boolean supports_volume;
		final String type;
		final int volume_percent;	
		
		public Spotify_Device(String id, boolean is_active, boolean is_private_session, boolean is_restricted, String name, boolean supports_volume, String type, int volume_percent) {
			this.id = id;
			this.is_active = is_active;
			this.is_private_session = is_private_session;
			this.is_restricted = is_restricted;
			this.name = name;
			this.supports_volume = supports_volume;
			this.type = type;
			this.volume_percent = volume_percent;
		}
		
		public String getId() {
			return id;
		}
		
	}
	

}
