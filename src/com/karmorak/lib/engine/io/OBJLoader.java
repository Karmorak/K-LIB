package com.karmorak.lib.engine.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.karmorak.lib.engine.graphic.roomy.Mesh;
import com.karmorak.lib.engine.graphic.roomy.Vertex;
import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.math.Vector3;

public class OBJLoader {

	static List<Vector3> verticies = new ArrayList<>();
	static List<Vector2> textures = new ArrayList<>();
	static List<Vector3> normals = new ArrayList<>();
	static List<Integer> indicies = new ArrayList<>();
	
	
	public static Mesh RawModel(URL url) {		
		return RawModel(url, null);
	}
	
	public static Mesh RawModel(URL url, URL texturePath) {
		
		Vertex[] verticesArray  = null;
		int[] indicesArray = null;
		
		String line = "";
		String[] c_line;
		
		try {
			InputStream input = url.openStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			
		
			while(true) {
				line = reader.readLine();
				c_line = line.split(" ");
				if(line.startsWith("v ")) {
					Vector3 vertex = new Vector3(Float.parseFloat(c_line[1]), Float.parseFloat(c_line[2]), Float.parseFloat(c_line[3]));
					verticies.add(vertex);
				} else if(line.startsWith("vt ")) {
					Vector2 coords = new Vector2(Float.parseFloat(c_line[1]), Float.parseFloat(c_line[2]));
					textures.add(coords);
				} else if(line.startsWith("vn ")) {
					Vector3 normal = new Vector3(Float.parseFloat(c_line[1]), Float.parseFloat(c_line[2]), Float.parseFloat(c_line[3]));
					normals.add(normal);
				} else if(line.startsWith("f ")) {
					break;
				}				
			}
			verticesArray = new Vertex[verticies.size()];
			while(line != null) {
				if(!line.startsWith("f ")) {
					line = reader.readLine();
					continue;
				}
				c_line = line.split(" ");
				String[] vertex_1 = c_line[1].split("/");
				String[] vertex_2 = c_line[2].split("/");
				String[] vertex_3 = c_line[3].split("/");
				
								
				createVertex(vertex_1, verticesArray);
				createVertex(vertex_2, verticesArray);
				createVertex(vertex_3, verticesArray);				
				
				line = reader.readLine();
			}
			reader.close();
			
			indicesArray = new int[indicies.size()];
			for (int i = 0; i < indicies.size(); i++) {
				indicesArray[i] = indicies.get(i);
			}
			
			verticies.clear(); 
			normals.clear();
			indicies.clear(); 
			textures.clear();
			
			if(texturePath != null) return new Mesh(verticesArray, indicesArray, texturePath);
			else return new Mesh(verticesArray, indicesArray);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println(line);
			e.printStackTrace();
		}
		return null;		
	}
	
	private static void createVertex(String[] data, Vertex[] verts) {
		int vertexIndex = Integer.parseInt(data[0]) -1;
		int textureCoordsIndex = Integer.parseInt(data[1]) -1;
		int normalsIndex = Integer.parseInt(data[2]) -1;
		
		
		indicies.add(vertexIndex);		
		Vector3 verteciespos = verticies.get(vertexIndex);
		Vector2 coords = new Vector2(textures.get(textureCoordsIndex).getX(),1- textures.get(textureCoordsIndex).getY());
		Vector3 normal = normals.get(normalsIndex);
		
		
		verts[vertexIndex] = new Vertex(-1, verteciespos, coords, normal);		
	}
	
}

