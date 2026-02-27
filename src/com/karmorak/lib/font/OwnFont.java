//v1.5.0
package com.karmorak.lib.font;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import com.karmorak.lib.Color;
import com.karmorak.lib.KLIB;
import com.karmorak.lib.engine.graphic.flat.DrawMap;
import com.karmorak.lib.engine.io.images.ImageLoader;
import com.karmorak.lib.font.identifier.Identifier;
import com.karmorak.lib.font.ownchar.OwnCharData;
import com.karmorak.lib.math.Vector2;

public class OwnFont {
		
	/* v.1.3.1
	 *  - removed libgdx
	 * v.1.4.0
	 *  - added new font attributes
	 *  - font can now take just the raw fontData
	 *  - OwnFont can now be saved in the KLIB library path
	 *  - you can now get a Array of Files with all of the windows fonts
	 *  - you can now load a font from  whether a direct path or the font name 
	 * v.1.5.0
	 *  - reworked in order to use special chars
	 *  - extracted fontData into an extra class
	 */
	
	float scale = 1f;
	int char_spacing = 0;
		
	public final fontData DATA;

//	private fontCache cache;	
		
	static final int DEFAULT_FONT_SIZE = 72;
	private static final ArrayList<OwnFont> different_fonts = new ArrayList<>();
	private static final ArrayList<String> new_warned_ids = new ArrayList<>();

//	static HashMap<String, Color> all_font_cache_colors = new HashMap<>();
	
	public static int cachesize = 3;

	public static class FontFilePathHandle {
		
		public URL font, font_txt;

		public FontFilePathHandle(URL font, URL font_txt) {
			this.font = font;
			this.font_txt = font_txt;
		}
		
	}
	
	public URL getURLPath() {
		return DATA.path;
	}	
	
	public String getPath() {
		return DATA.path.toString();
	}
	
	
//	public OwnFont(String pathorname) {
		
//		loadFont(pathorname, FONT_SIZE, false);
		
		
//	}
	
	
	OwnFont(fontData data) {	
		this.DATA = data;
	}
	
	public OwnFont(String pathorname) {		
		this.DATA = loadFontData(pathorname, DEFAULT_FONT_SIZE);		
	}
	
	
	public OwnFont(String name, DrawMap t, HashMap<Character, String> fontData, Color c) {
		this.DATA = new fontData(name, DEFAULT_FONT_SIZE, t, fontData, null);
	}
	
	public OwnFont(FontFilePathHandle paths) {
		this(paths, Color.WHITE());
	}
	
	public OwnFont(FontFilePathHandle paths, Color c) {
		URL handle = paths.font;
			
		String name = "";
		int f_size = DEFAULT_FONT_SIZE;
		
		HashMap<Character, String> raw_data = new HashMap<>();
		HashMap<String, String> raw_special_data = new HashMap<>();

		URL f = paths.font_txt;

		try {
			String line;
			BufferedReader br = new BufferedReader(new InputStreamReader(f.openStream()));

			while ((line = br.readLine()) != null) {
				if(line.startsWith("name")) {
					name = line.split(":")[1];
				} else if (line.startsWith("opt_spacing")) {
                    char_spacing = Integer.parseInt(line.split(":")[1]);
				} else if (line.startsWith("opt_defscale")) {
                    scale = Float.parseFloat(line.split(":")[1]);
				} else if (line.startsWith("font_size")) {
					f_size = Integer.parseInt(line.split(":")[1]);
				}else if (line.startsWith("§")) {
					line = line.split("//")[0].replace(" ", "").replace("§", "");
					String[] lines = line.split("_", 2);
					raw_special_data.put(lines[0], lines[1]);
				} else {
					if (!line.startsWith("//")) {
						line = line.split("//")[0].replace(" ", "");
						String[] lines = line.split("_", 2);
						raw_data.put((char) Integer.parseInt(lines[0]), lines[1]);
					}
				}
			}
			br.close();
		} catch (NumberFormatException | IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
		}

        different_fonts.add(this);
		this.DATA = new fontData(name, f_size, handle, raw_data, raw_special_data);
	}
	
	



	public DrawMap colorize(Color color) {
		return new DrawMap(DATA.texture).colorize(color, 0);
	}



	//* scaled char size
	public Vector2 getCharSize(Identifier ident) {
		float x = 0;
		float y = 0;

		float[] info = DATA.getInfo(ident);
		Vector2 bounds = DATA.getBounds(ident);
		x = bounds.getWidth()*info[0];
		y = bounds.getHeight()*info[0];

		return new Vector2(x*scale, y*scale);
	}
	
	public Vector2 getWordBounds_Raw(String s) {
		
		float width = 0;
		float y = 0;
		OwnCharData[] data = OwnCharData.createOwnChars(s, this);

		for (int i = 0; i < data.length; i++) {
			OwnCharData c = data[i];
			
			float[] info = DATA.getInfo(c.getIdentifier());
			Vector2 bounds = DATA.getBounds(c.getIdentifier());
			
			width += bounds.getWidth() * info[0];
			
			if(i < data.length-2)
				width += char_spacing;
			
			int ay = (int) info[1];
			if(ay < 0) ay = 0;			
			
			if(bounds.getHeight() * info[0] + ay  > y) 
				y = bounds.getHeight() * info[0] + ay;
			
		}
		return new Vector2(width, y);
	}

	public int getWordWidth_Raw(OwnCharData[] data) {
		int width = 0;

		for (OwnCharData c : data) {
			float[] info = DATA.getInfo(c.getIdentifier());
			Vector2 bounds = DATA.getBounds(c.getIdentifier());

			width += (int) (bounds.getWidth() * info[0]);

		}
		width += char_spacing * (data.length-1);

		return width;
	}

	public int getWordHeight_Raw(OwnCharData[] data) {
		int y = 0;

		for (OwnCharData c : data) {
			float[] info = DATA.getInfo(c.getIdentifier());
			Vector2 bounds = DATA.getBounds(c.getIdentifier());

			int ay = (int) info[1];
			if (ay < 0) ay = 0;

			if (bounds.getHeight() * info[0] + ay > y)
				y = (int) (bounds.getHeight() * info[0] + ay);
		}
		return y;
	}

	public Vector2 getWordBounds_Raw(OwnCharData[] data) {
		float width = 0;
		float y = 0;

        for (OwnCharData c : data) {
            float[] info = DATA.getInfo(c.getIdentifier());
            Vector2 bounds = DATA.getBounds(c.getIdentifier());

            width += bounds.getWidth() * info[0];

            int ay = (int) info[1];
            if (ay < 0) ay = 0;

            if (bounds.getHeight() * info[0] + ay > y)
                y = bounds.getHeight() * info[0] + ay;

        }
		width += char_spacing * (data.length-1);

		return new Vector2(width, y);
	}

	public int getWordWidth_Raw(OwnCharData[] data, int startIndex, int endIndex) {
		float width = 0;

		for (int i = startIndex; i < endIndex; i++) {
			OwnCharData c = data[i];
			float[] info = DATA.getInfo(c.getIdentifier());
			Vector2 bounds = DATA.getBounds(c.getIdentifier());

			width += bounds.getWidth() * info[0];
		}

		if(endIndex - startIndex > 1) {
			width += char_spacing * (endIndex-1 - startIndex);
		}
		return (int) width;
	}

	public Vector2 getWordBounds_Raw(OwnCharData[] data, int startIndex, int endIndex) {
		float width = 0;
		float y = 0;

		for (int i = startIndex; i < endIndex; i++) {
			OwnCharData c = data[i];
			float[] info = DATA.getInfo(c.getIdentifier());
			Vector2 bounds = DATA.getBounds(c.getIdentifier());

			width += bounds.getWidth() * info[0];

			int ay = (int) info[1];
			if (ay < 0) ay = 0;

			if (bounds.getHeight() * info[0] + ay > y)
				y = bounds.getHeight() * info[0] + ay;


		}

		if(endIndex - startIndex > 1) {
			width += char_spacing * (endIndex - startIndex);
		}
		return new Vector2(width, y);
	}


	


	public void dispose() {
//		cache.textures[0].dispose();
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public float getScale() {
		return scale;
	}

	public static float getHeight(String name, OwnFont font) {
		float biggest = 0;
		OwnCharData[] data = OwnCharData.createOwnChars(name, font);
		for(OwnCharData c : data) {
			Vector2 b = font.DATA.getBounds(c.getIdentifier());
			if(b.getHeight() >= biggest) {
				biggest = b.getHeight();
			}
		}
		return biggest;
	}
	
	public static float getWidth(String name, OwnFont font) {
		float biggest = 0;
		OwnCharData[] data = OwnCharData.createOwnChars(name, font);
		for(OwnCharData c : data) {
			Vector2 b = font.DATA.getBounds(c.getIdentifier());
			if(b.getHeight() >= biggest) {
				biggest = biggest + b.getWidth();
			}
		}
		return biggest;
	}
	
	public static File[] getAvailableWindowsFonts() {
		File fontDirectory = new File(KLIB.WIN_DIR_PATH, "Fonts");

//		for (int i = 0; i < fontDirectory.listFiles().length; i++) {
//			System.out.println(fontDirectory.listFiles()[i].getName());
//		}
		return fontDirectory.listFiles();
	}
	
	public void setCharSpacing(int i) {
		char_spacing = i;		
	}
	
	public int getCharSpacing() {
		return (int) (char_spacing * scale);		
	}
	
	public int getCharSpacing_Raw() {
		return char_spacing;		
	}
	
	public String getTITLE_NAME() {
		return DATA.TITLE_NAME;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((DATA == null) ? 0 : DATA.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OwnFont other = (OwnFont) obj;
		if (DATA == null) {
            return other.DATA == null;
		} else return DATA.equals(other.DATA);
    }

	
	private static fontData loadFontData(String pathorname, int fontSize) {

		HashMap<Character, String> fontData = new HashMap<Character, String>();
		Font font = null;
		
		if(pathorname.contains("/")) {
			File ttfFilePath = new File(pathorname);
	        // Schriftart aus der TTF-Datei erstellen
			Font c_font;
			try {
				c_font = Font.createFont(Font.TRUETYPE_FONT, ttfFilePath);
				 // Schriftart und Größe setzen
				font = c_font.deriveFont(Font.PLAIN, fontSize);
			} catch (FontFormatException | IOException _) {
				System.err.println(pathorname + " couldnt be loaded.");
			}	       
		} else {
			font = new Font(pathorname, Font.PLAIN, fontSize);
		}

		String availableChars = "";
		
        // GraphicsEnvironment initialisieren
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

		// Schriftart zur Umgebung hinzufügen
		ge.registerFont(font);

		// Alle verfügbaren Zeichen der Schriftart erhalten
		char startChar = 0; // Startzeichen (z.B. Leerzeichen)
		char endChar = 260; // Endzeichen (z.B. '~')

		StringBuilder availableCharsBuilder = new StringBuilder();

		for (char c = startChar; c <= endChar; c++) {
		    if (font.canDisplay(c)) {
		        availableCharsBuilder.append(c);
		    }
		}

		availableChars = availableCharsBuilder.toString();


        // Informationen über die Schriftart ausgeben
		System.out.println("Schriftartname: " + font.getFontName());
		System.out.println("Schriftartstil: " + font.getStyle());
		System.out.println("Schriftartgröße: " + font.getSize());
		
		
		// Textur mit dem gewünschten Text zeichnen		
		
		HashMap<Character, int[]> raw_data = new HashMap<Character, int[]>();
        int numChars = availableChars.length();
        
        int squareSize = (int) Math.ceil(Math.sqrt(numChars)); // Größe des Quadrats berechnen
        // x , width, y
        
        // BufferedImage erstellen, um die Textur zu speichern
        BufferedImage texture = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        // Graphics2D-Objekt zum Zeichnen auf das BufferedImage erhalten
        Graphics2D g2d = texture.createGraphics();
        g2d.setFont(font);

        FontMetrics metrics = g2d.getFontMetrics();
        int font_descent = metrics.getDescent();
        
        
        g2d.dispose();
        texture = new BufferedImage(squareSize * (fontSize), squareSize * (fontSize+font_descent), BufferedImage.TYPE_INT_ARGB);
        g2d = texture.createGraphics();
        g2d.setFont(font);

        int x_ = 0;
        int y_ = 1;       
        
        for (int i = 0; i < numChars; i++) {
            char c = availableChars.charAt(i);
            
            int char_width = metrics.charWidth(c);
//            int char_height = (int) metrics.getMaxCharBounds(g2d).getHeight();
            
            int char_x = (int) (x_ * fontSize);
            int char_y = (y_-1) * (fontSize + font_descent);
                        
            raw_data.put(c, new int[]{char_x, char_width});    
   
            
            String charString = String.valueOf(c);

//            if(c == '!' || c == 'g' || c == '@' || c == ' ') {
//            	System.out.println(c + " " + char_x + " " + (char_y) + " " + char_width + " " + char_height + " desc" +  metrics.getDescent() + " asc" + metrics.getAscent() + " " + metrics.getLeading());          	 
//            }    
            
            int pix_y = y_ * (fontSize + metrics.getDescent()) - metrics.getDescent();
            g2d.drawString(charString, x_ * fontSize, pix_y);
            raw_data.put(c, new int[]{char_x, char_width, char_y});
            
            x_++;
            if (x_ >= squareSize) {
            	x_ = 0;
            	y_++;
            }
        }
        
        
			

		// Pixelinformationen in einem Integer-Array im RGBA-Format extrahieren
		int width = texture.getWidth();
		int height = texture.getHeight();
		int[] pixels = new int[width * height];
		texture.getRGB(0, 0, width, height, pixels, 0, width);

		DrawMap m = new DrawMap(width, height, ImageLoader.IntArrayToBuffer(width, height, pixels));
         
         
         for(char c : raw_data.keySet()) {
		   
		   int s_x = raw_data.get(c)[0];
		   int s_width = raw_data.get(c)[1];
		   int s_y = raw_data.get(c)[2];
		   int s_height = fontSize + font_descent;
		   
		   boolean hit = false;
		   int hit_top = s_y + s_height;        
		   int hit_bottom = -1;
		   
		   int hit_left = -1;
		   int hit_right = 0;
		   
		   int abs_y = 0;
		   
		   for(int x = s_x; x < s_x + s_width; x++) {
			   
			  for(int y = s_y; y < s_y + s_height; y++) {
				  
				  Color color = m.getPixel(x, y);
				  if(color.getAlpha() != 0) {
					  if(y < hit_top) {
						  hit_top = y;			  
					  }       		
					  if(hit == false)
						  hit = true;  
					  if(hit_left == -1) {
						  hit_left = x;
					  }
					  if(x > hit_right) {
						  hit_right = x;
					  }
					  break;
				  }
			  }     
			  
			  if(hit) {
				  for(int y = s_y + s_height-1; y > s_y; y--)  {
					  Color color = m.getPixel(x, y);
					  if(color.getAlpha() != 0) {
						  if(y+1 > hit_bottom) {
							  hit_bottom = y+1;
						  }
						  
					  }        				  
				  }    
			  }
			  hit = false;
		   }

		   String data;
		   
		   if(c == ' ' || hit_bottom == -1) { //da chars wie leerzeichen keine texture haben
			   
			   data = "x" + s_x + "_y" + s_y + "_w" + s_width + "_h" + fontSize;
			   
		   } else {
			   int height_r = hit_bottom - s_y - (hit_top-s_y);
			   int width_r = hit_right - s_x - (hit_left-s_x);
			   
			   data = "x" + hit_left + "_y" + hit_top + "_w" + width_r + "_h" + height_r;
			   
			   if(hit_bottom - s_y != fontSize) {
				   abs_y = fontSize - (hit_bottom - s_y);
				   data = data+"_ay" + abs_y;
			   }
		     
		   }           
		
		   fontData.put(c, data);
		   
         }
         
         raw_data = null;


        return new fontData(font.getFontName(), fontSize, m, fontData, null);
	}
	
		
	public static OwnFont loadFont(String pathorname, int fontSize, boolean save) {
		
		OwnFont newFont = new OwnFont(loadFontData(pathorname, fontSize));
		
        if(save) {
       	 	newFont.DATA.saveFont();
        }   
        
        return newFont;			
	}

	public int getFontSize() {
		return DATA.FONT_SIZE;
	}
	
    static BufferedImage createImageWithText(Font font, String text, int fontSize) {

        int numChars = text.length();
        
        int squareSize = (int) Math.ceil(Math.sqrt(numChars)); // Größe des Quadrats berechnen
        // x , width, y
        
        // BufferedImage erstellen, um die Textur zu speichern
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        // Graphics2D-Objekt zum Zeichnen auf das BufferedImage erhalten
        Graphics2D g2d = image.createGraphics();
        g2d.setFont(font);

        FontMetrics metrics = g2d.getFontMetrics();
        int font_descent = metrics.getDescent();
        
        
        g2d.dispose();
        image = new BufferedImage(squareSize * (fontSize), squareSize * (fontSize+font_descent), BufferedImage.TYPE_INT_ARGB);
        g2d = image.createGraphics();
        g2d.setFont(font);

        int x = 0;
        int y = 1;

        
        
        for (int i = 0; i < numChars; i++) {
            char c = text.charAt(i);
                                      
            
            String charString = String.valueOf(c);
            
            int pix_y = y * (fontSize + metrics.getDescent()) - metrics.getDescent();
            g2d.drawString(charString, x * fontSize, pix_y);
            
            x++;
            if (x >= squareSize) {
                x = 0;
                y++;
            }
        }

        return image;
    }
    
	public static String[] getAvailableFontFamilyNames() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		return ge.getAvailableFontFamilyNames();
	}
	
	public static String[] getAvailableFontNames() { // get all available windows font names
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Font[] fonts = ge.getAllFonts();

		String[] fontNames = new String[fonts.length];
		for (int i = 0; i < fonts.length; i++) {
			fontNames[i] = fonts[i].getFontName();
		}

		return fontNames;
	}

}
