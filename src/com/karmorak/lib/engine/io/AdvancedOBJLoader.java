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

public class AdvancedOBJLoader {

	static List<Vertex> verticies = new ArrayList<>();
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
					verticies.add(new Vertex(verticies.size(), vertex));
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
			
			
			while(line != null && line.startsWith("f ")) {
				c_line = line.split(" ");
				String[] vertex_1 = c_line[1].split("/");
				String[] vertex_2 = c_line[2].split("/");
				String[] vertex_3 = c_line[3].split("/");				
								
				createVertex(vertex_1);
				createVertex(vertex_2);
				createVertex(vertex_3);				
				
				line = reader.readLine();				
				
			}
			reader.close();
			removeUnusedVertices(verticies);
						
			float furthestpoint = 0;
			verticesArray = new Vertex[verticies.size()];
			for (int i = 0; i < verticies.size(); i++) {
				float length = verticies.get(i).getLength();				
				if(length > furthestpoint) furthestpoint = length;
				verticesArray[i] = verticies.get(i);
			}
			
			indicesArray = new int[indicies.size()];
			for (int i = 0; i < indicies.size(); i++) {
				indicesArray[i] = indicies.get(i);
			}
			
			verticies.clear();
			textures.clear();
			normals.clear();
			indicies.clear();				
			
			if(texturePath != null)	return new Mesh(verticesArray, indicesArray, texturePath);
			else return new Mesh(verticesArray, indicesArray);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println(line);
			e.printStackTrace();
		}
		return null;	
	}
	
	

	
	
	
	private static void createVertex(String[] data) {
		int vertexIndex = Integer.parseInt(data[0]) -1;
		int textureCoordsIndex = Integer.parseInt(data[1]) -1;
		int normalsIndex = Integer.parseInt(data[2]) -1;
		
		Vector2 coords = new Vector2(textures.get(textureCoordsIndex).getX(),1- textures.get(textureCoordsIndex).getY());
		Vector3 normal = normals.get(normalsIndex);
		
		Vertex vertex = verticies.get(vertexIndex);
		
		if(!vertex.isSet()) {
			vertex.setTextureCoord(coords);
			vertex.setNormal(normal);
			indicies.add(vertexIndex);	
		} else {
			handleDublicates(vertex, coords, normal);		
		}
	}
	
	private static void handleDublicates(Vertex current, Vector2 coords, Vector3 normal) {
		if(current.isSimilar(coords, normal)) {
			indicies.add(current.getIndex());
		} else {
			Vertex nextVertex = current.getDublicate();
			if(nextVertex != null) {
				handleDublicates(nextVertex, coords, normal);
			} else {
				Vertex dubliacte = new Vertex(verticies.size(), current.getPosition(), coords, normal);
				current.setDublicate(dubliacte);
				verticies.add(dubliacte);
				indicies.add(dubliacte.getIndex());
			}			
		}		
		
	}
    private static void removeUnusedVertices(List<Vertex> vertices){
        for(Vertex vertex:vertices){
            if(!vertex.isSet()){
                vertex.setTextureCoord(textures.get(0));
                vertex.setNormal(normals.get(0));
            }
        }
    }
	
	
}

