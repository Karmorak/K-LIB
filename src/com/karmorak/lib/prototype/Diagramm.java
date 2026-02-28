//v1
package com.karmorak.lib.prototype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.karmorak.lib.Color;
import com.karmorak.lib.ColorPreset;
import com.karmorak.lib.Colorable;
import com.karmorak.lib.engine.graphic.flat.DrawMap;
import com.karmorak.lib.engine.graphic.flat.Texture;
import com.karmorak.lib.engine.graphic.MasterRenderer;
import com.karmorak.lib.math.MathTools;
import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.ui.button.Button;

public class Diagramm {
	
	ArrayList<Float> vertical;
	ArrayList<Float> horizontal;
	
	ArrayList<Button> vertical_b;
	ArrayList<Button> horizontal_b;
	
	ArrayList<Button> index_buttons;
	
	
	
	HashMap<String, ArrayList<Vector2>> datas;
	HashMap<String, Color> colors;
	
	
	
	float min_vertical;
	float max_vertical;
	
	float min_horizontal;
	float max_horizontal;
	
	
	int x_width = 900;
	int y_height = 300;
	
	int background_abs_left = 60;
	int background_abs_right = 50;
	int background_abs_top = 50;
	int background_abs_bottom = 60;
	
	int line_inbound_left = 15;
	int line_inbound_right = 15;
	int line_inbound_bottom = 15;
	int line_inbound_top = 15;
	
	int button_abs = 10;
	
	
	Vector2 pos = new Vector2(200, 200);
	
	ArrayList<DrawMap> lines;
	
	
//	DrawMap lines;
	DrawMap d_columns;
	
	int font_size = 12;
	
	Texture background;
	Texture columns;
	
	
	
	public Diagramm() {
		
		vertical = new ArrayList<>();
		horizontal = new ArrayList<>();
		
		vertical_b = new ArrayList<>();
		horizontal_b = new ArrayList<>();
		
		index_buttons = new ArrayList<>();
		
		lines = new ArrayList<>();
		
		
		datas = new HashMap<>();
		colors = new HashMap<>();
		
//		lines = new DrawMap(x_width, y_height, Color.ALPHA);
        d_columns = new DrawMap(x_width, y_height, ColorPreset.ALPHA);
//		d_columns.fill(Color.LIGHT_GRAY);
		
		
		DrawMap d = new DrawMap(x_width + background_abs_left + background_abs_right, y_height +background_abs_bottom + background_abs_top , ColorPreset.LIGHT_GRAY);
		d.fill(ColorPreset.LIGHT_GRAY);
		background = new Texture(d);
		d.destroy();
		
	}
	
	public Vector2 getSize() {
		return new Vector2(x_width + background_abs_left + background_abs_right, y_height +background_abs_bottom + background_abs_top);
	}
	
	public void setPosition(float x, float y) {
		pos.set(x, y);
	}
	
	public void clearData() {
		datas.clear();
		colors.clear();
		
		vertical.clear();
		horizontal.clear();
		
		vertical_b.clear();
		horizontal_b.clear();
		
		index_buttons.clear();
		
		lines.clear();
		
		d_columns.destroy();
		d_columns = new DrawMap(x_width, y_height, Color.ALPHA());
		
	}
	
	public int getDataSize() {
		return datas.size();
	}
	
	
	int biggest_data = 0;
	
	
	public void addData(Colorable lineColor, String name, ArrayList<Vector2> data)  {
		datas.put(name, data);
		colors.put(name, lineColor.toColor());
	
		
		for (Vector2 vector2 : data) {
            int horizontal = (int) vector2.getX();
			if(horizontal_counts.get(horizontal) != null) {
                horizontal_counts.put(horizontal, horizontal_counts.get(horizontal) + 1);
			} else {
                horizontal_counts.put(horizontal, 1);
			}
		}
		
		index_buttons.add(new Button("- " + name, 1.5f, lineColor.toColor()));

	}
	
	//not tested
	public void addCustomValues(float[] vertical_values, float[] horizontal_values) {
		
		for(int i = 0; i < vertical_values.length; i++) {
			
			
			float val = vertical_values[i];
			
			Button vertical = new Button("" + (int)val);
			vertical.setHeight(font_size);
			vertical.setPosition(pos.getX() - vertical.getWidth() -15, pos.getY() + (int) (i/vertical_values.length  * y_height - font_size*0.5f));
			
			vertical_b.add(vertical);				
		}
		
		for(int i = 0; i < horizontal_values.length; i++) {
			
			
			float val = horizontal_values[i];
			
			Button horizontal = new Button("" + (int)val);
			horizontal.setHeight(font_size);
			int x = (int) (i/horizontal_values.length * x_width);
			
			horizontal.setPosition(pos.getX() +  x - horizontal.getWidth()*0.5f, pos.getY() - font_size - 15);
			
			horizontal_b.add(horizontal);
		}
		
		
		for (String key : datas.keySet()) {
			float max_horizontal_raster = horizontal_values[horizontal_values.length-1];
			float max_vertical_raster = vertical_values[vertical_values.length-1];
			
			ArrayList<Vector2> data = datas.get(key);


            float x = data.getFirst().getHorizontal();
			x = (float) (x / max_horizontal_raster * x_width);

            float y = data.getFirst().getVertical();
			y = (float) (y / max_vertical_raster * y_height);

            DrawMap line = new DrawMap(x_width, y_height, ColorPreset.ALPHA);
			
			for (int i = 1; i < data.size(); i++) {
				float x2 = data.get(i).getHorizontal();
//				x2 = (float) (max_horizontal_raster / 10f *x2 / max_horizontal_raster * x_width);
				
				x2 = (float) (x2 / max_horizontal_raster * x_width);
				
				float y2 = data.get(i).getVertical();
				y2 = (float) (y2 / max_vertical_raster * y_height);
				
				line.drawLine(colors.get(key),(int) x, y_height -(int) y,(int) x2, y_height -(int) y2);
				
				x = x2;
				y = y2;
			}
			
			
			lines.add(line);
		}
		
	
		
		d_columns.drawLine(Color.BLACK(), 0, line_inbound_bottom, x_width, line_inbound_bottom);
		d_columns.drawLine(Color.BLACK(), line_inbound_left, 0, line_inbound_left, y_height);
		
		for(int i = 1; i < 5; i++) {
			int y =  y_height/5*i + line_inbound_bottom;			
			d_columns.drawLine(Color.BLACK(), 0, y, x_width, y);
		}
		
		columns = new Texture(d_columns);	
		
	}
	
	public void update() {
		
		float min_v = 0; 
		float max_v = 0; 
		float min_h = 0; 
		float max_h = 0; 
		
		ArrayList<Float> horizontal_values = new ArrayList<>();
		
		int max_vertical_values = 0;
		
		boolean init = false;
		
		vertical_b.clear();
		horizontal_b.clear();
		lines.clear();
		d_columns.clearDrawMap(Color.ALPHA());
		
		
		for (String key : datas.keySet()) {
			ArrayList<Vector2> data = datas.get(key);			
			Collections.sort(data);		
			
			min_vertical = data.get(0).getY();
			max_vertical = data.get(0).getVertical();
			
			min_horizontal = data.get(0).getX();
			max_horizontal = data.get(0).getHorizontal();		
			
			for (int i = 1; i < data.size(); i++) {			
				float f = data.get(i).getVertical();
				if(f > max_vertical) max_vertical = f;
				if(f < min_vertical) min_vertical = f;
			}	
			
			if(!init) {
				min_v = min_vertical;
				max_v = max_vertical;
				
				min_h = min_horizontal;
				max_h = max_horizontal;		
				
				for(int i = 0; i < data.size(); i++) {
					horizontal_values.add(data.get(i).getHorizontal());
				}
				
				init=true;
			} else {
				if(max_vertical > max_v) max_v = max_vertical;
				if(min_vertical < min_v) min_v = min_vertical;
				
				if(max_horizontal > max_h) max_h = max_horizontal;
				if(min_horizontal < min_h) min_h = min_horizontal;
				
				for(int i = 0; i < data.size(); i++) {
					if(!horizontal_values.contains(data.get(i).getHorizontal()))					
						horizontal_values.add(data.get(i).getHorizontal());
				}
			}
		}		
		Collections.sort(horizontal_values);	
		
		min_vertical = min_v;
		max_vertical = max_v;
		
		min_horizontal = min_h;
		max_horizontal = max_h;		
		
		
		
		double min_vertical_raster = MathTools.getMinPlacevalue(min_vertical);
		
		double max_vertical_digits = MathTools.getMinPlacevalue(max_vertical);
		double max_vertical_raster = Math.ceil(max_vertical / max_vertical_digits) * max_vertical_digits;	//Math.ceil (aufrunden)		
		
		double min_horizontal_raster = MathTools.getMinPlacevalue(min_horizontal);
		
		double max_horizontal_digits = MathTools.getMinPlacevalue(max_horizontal) +1;
		double max_horizontal_raster = Math.ceil(max_horizontal / max_horizontal_digits) * max_horizontal_digits;	
		
		
		
		for(float i = 0.2f; i <= 1f; i+=0.2) {
			
			
			float val = (float) (i*max_vertical_raster);
			
			Button vertical = new Button("" + (int)val);
			vertical.setHeight(font_size);
			
			vertical_b.add(vertical);
				
		}
		
		//Quer zahlen
		
		double horizontal_span = max_horizontal_raster - min_horizontal_raster;
		
		if(horizontal_values.size() <= 10) {
			
					
			for (int i = 0; i < horizontal_values.size(); i++) {			
				float f = horizontal_values.get(i);		
								
				int x = (int) ((i / (horizontal_values.size()-1f) * x_width));		
				Button horizontal = new Button("" + (int) f);
				horizontal.setHeight(font_size);											
				horizontal_b.add(horizontal);					
				
				d_columns.drawLine(Color.BLACK(), x + line_inbound_left , 0, x + line_inbound_left, y_height);
			}		
			
			
			
			
			
			
			for (String key : datas.keySet()) {
				ArrayList<Vector2> data = datas.get(key);			 
				
				
				int f = 0;
				float x = 0;
				float y = 0;
				boolean first = true;
				
				DrawMap line = new DrawMap(x_width, y_height, Color.ALPHA());
				
				for (int i = 0; i < horizontal_values.size(); i++) {
					float h_value = horizontal_values.get(i);
					
					
					
					if(f < data.size() && data.get(f).getX() == h_value) {
						if(first) {
							x = (i / (horizontal_values.size() -1f) * x_width);
							y = (float) (data.get(f).getVertical() / max_vertical_raster * y_height);
							first = false;
						} else {
							
							float x2 = data.get(f).getHorizontal();
							//x2 = (float) (max_horizontal_raster / 10f *x2 / max_horizontal_raster * x_width);
								
							x2 = (float) (i / (horizontal_values.size() -1f) * x_width);
								
							float y2 = data.get(f).getVertical();
							y2 = (float) (y2 / max_vertical_raster * y_height);
								
							line.drawLine(colors.get(key),(int) x, y_height -(int) y+1,(int) x2, y_height -(int) y2+1);
							line.drawLine(colors.get(key),(int) x, y_height -(int) y,(int) x2, y_height -(int) y2);
							line.drawLine(colors.get(key),(int) x, y_height -(int) y-1,(int) x2, y_height -(int) y2-1);
								
							x = x2;
							y = y2;

					}						
					f++;
				} else {
					
					if(first) {
						x = (i / (horizontal_values.size() -1f) * x_width);
						y = (float) (0 / max_vertical_raster * y_height);
						first = false;
					} else {
						
						float x2 = h_value;
						//x2 = (float) (max_horizontal_raster / 10f *x2 / max_horizontal_raster * x_width);
							
						x2 = (float) (i / (horizontal_values.size() -1f) * x_width);
							
						float y2 = 0;
						y2 = (float) (y2 / max_vertical_raster * y_height);
							
						line.drawLine(colors.get(key),(int) x, y_height -(int) y+1,(int) x2, y_height -(int) y2+1);
						line.drawLine(colors.get(key),(int) x, y_height -(int) y,(int) x2, y_height -(int) y2);
						line.drawLine(colors.get(key),(int) x, y_height -(int) y-1,(int) x2, y_height -(int) y2-1);
							
						x = x2;
						y = y2;

					}	
					
				}
			}
			lines.add(line);
		}
			
		} else {
			int t = 6;		
			if(horizontal_span < 10)			
				t = (int) (horizontal_span) + 1;
			
			
				
			for(int i = 1; i < t; i++) {
						
					
					
				//horizontal-----------------
				//cut the decimals
				float val;
				if(t != 6) val =  (float) ((i) * 100);
				else val =  (float) ((i*2) * 100);
				int val2 = (int) val;
				val = val2 / 100f;
					
				//round up or down
				float n = val;
				float dec = n - (int) n;
				if(dec < 0.5) val = (int) val;
				else if (dec > 0.5) val = (int)val+1;
					
				
				Button horizontal = new Button("" + (int) val);
				horizontal.setHeight(font_size);
				int x = (int) (val / horizontal_span * x_width);
					
					
					
				d_columns.drawLine(Color.BLACK(), x + line_inbound_left, 0, x + line_inbound_left, y_height);
					
					
					
				horizontal_b.add(horizontal);	
					
			}
			
			
			
			
			for (String key : datas.keySet()) {
				ArrayList<Vector2> data = datas.get(key);			
				
			
				float x = data.get(0).getX();							//jahreszahlen
				x = (float) (x / max_horizontal_raster * x_width);
				
				float y = data.get(0).getY();
				y = (float) (y / max_vertical_raster * y_height);
				
				DrawMap line = new DrawMap(x_width, y_height, Color.ALPHA());
				
				for (int i = 1; i < data.size(); i++) {
					float x2 = data.get(i).getHorizontal();
//					x2 = (float) (max_horizontal_raster / 10f *x2 / max_horizontal_raster * x_width);
					
					x2 = (float) (x2 / max_horizontal_raster * x_width);
					
					float y2 = data.get(i).getVertical();
					y2 = (float) (y2 / max_vertical_raster * y_height);
					
					line.drawLine(colors.get(key),(int) x, y_height -(int) y+1,(int) x2, y_height -(int) y2+1);
					line.drawLine(colors.get(key),(int) x, y_height -(int) y,(int) x2, y_height -(int) y2);
					line.drawLine(colors.get(key),(int) x, y_height -(int) y-1,(int) x2, y_height -(int) y2-1);
					
					x = x2;
					y = y2;
				}
				
				
				lines.add(line);
			}
			
			
			
			
		}	
		
			
		d_columns.drawLine(Color.BLACK(), 0, line_inbound_bottom, x_width, line_inbound_bottom);
//		d_columns.drawLine(Color.BLACK, line_inbound_left, 0, line_inbound_left, y_height);
		
		for(int i = 1; i < 5; i++) {
			int y =  y_height/5*i + line_inbound_bottom;			
			d_columns.drawLine(Color.BLACK(), 0, y, x_width, y);
		}
		
		
		columns = new Texture(d_columns);		
		
	}
	
	HashMap<Integer, Integer> horizontal_counts = new HashMap<>();
	

	public void draw(MasterRenderer renderer, int layer) {
		
		int b_add  = 0;
		
		
		float x = button_abs;
		float y = background_abs_bottom - font_size - button_abs*2;
		int c = 0;
		
		for (int i = 0; i < index_buttons.size(); i++) {
			Button b = index_buttons.get(i);
			
			if(c == 0 && i != 0) {
				b_add += b.getHeight() + button_abs*2;
			}
			
			c++;
			
			
			
			b.setPosition(pos.getX() + x, pos.getY() + y - b.getHeight() + b_add*2);
			
			x += b.getWidth() + button_abs;
			
			if(c == 4) {
				c = 0;
				y -= b.getHeight() + button_abs;
				x = button_abs;
			}
			
			b.draw(renderer, layer+1);
		}
		
		y = 0.2f;
		
		for (int i = 0; i < vertical_b.size(); i++) {
			Button vertical = vertical_b.get(i);
			
			
			vertical.setPosition(pos.getX() - vertical.getWidth() + background_abs_left - button_abs, pos.getY() + (int) (y  * y_height - font_size*0.5f) + line_inbound_bottom + b_add);
			vertical.draw(renderer, layer +1);
			
			y += 0.2f;
		}
		
		
		float d = 1f/(horizontal_b.size()-1);
		x = 0;
		
		for (int i = 0; i < horizontal_b.size(); i++) {		
			Button horizontal = horizontal_b.get(i);
			
			
			horizontal.setPosition(pos.getX() +  x*x_width - horizontal.getWidth()*0.5f + background_abs_left + line_inbound_left, pos.getY() + background_abs_top + b_add  - font_size);
			
			horizontal_b.get(i).draw(renderer, layer+1);
			
			x += d;
		}		
		
		

		
		for (int i = 0; i < lines.size(); i++) {
			renderer.processDrawMap(lines.get(i), pos.getX() + background_abs_left + line_inbound_left, pos.getY() + background_abs_bottom + b_add + line_inbound_bottom, layer + 2 + i);
		}
		
		if(columns != null) {
			renderer.processTexture(columns, pos.getX() + background_abs_left, pos.getY() + background_abs_bottom + b_add, layer +1);
		}
		
		if(background!= null)
			renderer.processTexture(background, pos.getX(), pos.getY(), background.getWidth(), background.getHeight() + b_add, layer);
		
		
	}
	
		
	
	
}
