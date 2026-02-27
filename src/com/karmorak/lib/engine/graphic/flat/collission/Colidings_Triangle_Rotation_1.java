package com.karmorak.lib.engine.graphic.flat.collission;

class Colidings_Triangle_Rotation_1 {
		
	static float calc_X(float a_y, float b_x, float b_y, float b_width, float b_height) {
		float t_y = a_y-b_y;
		float x = (b_x + (b_width-b_width * (t_y / b_height)));
		return x;		
	}
	static float calc_Y(float a_x, float b_x, float b_y, float b_width, float b_height) {
		float t_x = a_x-b_x;	
		float y = b_y + (b_height-b_height * (t_x / b_width));	
		return y;
	}
	
	static void colides_Down_Side_t(Colideable p, Colideable tri, float move) {
		
		float a_x = p.getPosition().getX();
		float a_y = p.getPosition().getY();			
		float a_minwidth = p.getMinCOLbounds().getWidth();
		float a_minheight = p.getMinCOLbounds().getHeight();
		float a_width = p.getMaxCOLbounds().getWidth();
//		float a_height = p.getMaxCOLbounds().getHeight();			

		float b_x = tri.getPosition().getX();
		float b_y = tri.getPosition().getY();
		float b_width = tri.getMaxCOLbounds().getWidth();
		float b_height = tri.getMaxCOLbounds().getHeight();	
		

		//UNTERE SEITE
		if(a_x + a_minwidth <= b_x && a_x + a_width > b_x) {
			if(a_y + a_minheight < b_y + b_height) {
				p.setPosition(a_x, b_y + b_height);
			}
		}
		
		
		float x = calc_X(a_y + a_minheight, b_x, b_y, b_width, b_height);
		float y = calc_Y(a_x + a_minwidth, b_x, b_y, b_width, b_height);
		
		//UNTERE LINKE
		if(a_x + a_minwidth > b_x && x > 0 && a_x + a_minwidth < x) {
			if(a_y + a_minheight < y && a_y + a_minheight > b_y && y > 0) {
				p.setPosition(a_x, y);
			}
		}
	}
	
	static void colides_Left_Side_t(Colideable p, Colideable tri, float move) {
		float a_x = p.getPosition().getX();
		float a_y = p.getPosition().getY();			
		float a_minwidth = p.getMinCOLbounds().getWidth();
		float a_minheight = p.getMinCOLbounds().getHeight();
		float a_width = p.getMaxCOLbounds().getWidth();
		float a_height = p.getMaxCOLbounds().getHeight();			

		float b_x = tri.getPosition().getX();
		float b_y = tri.getPosition().getY();
		float b_width = tri.getMaxCOLbounds().getWidth();
		float b_height = tri.getMaxCOLbounds().getHeight();	
		
		//LINKE SEITE
		if(a_x + a_minwidth < b_x + b_width) {
			if(a_y + a_minheight <= b_y && a_y + a_height <= b_y + b_height) {
				p.setPosition(b_x + b_width, a_y);
			}
		}
		
		
		float x = calc_X(a_y + a_minheight, b_x, b_y, b_width, b_height);
		float y = calc_Y(a_x + a_minwidth, b_x, b_y, b_width, b_height);		
		//UNTERE LINKE
		if(a_x + a_minwidth > b_x && x > 0 && a_x + a_minwidth < x) {
			if(a_y + a_minheight < y && a_y + a_minheight > b_y && y > 0) {
				p.setPosition(x - a_minwidth, a_y);
			}
		}
		
		
			
	}
	
	static void colides_Right_Side_t(Colideable p, Colideable tri, float move) {
		
		
		//UNTEN RECHTS
		float a_x = p.getPosition().getX();
		float a_y = p.getPosition().getY();			
		float a_minwidth = p.getMinCOLbounds().getWidth();
		float a_minheight = p.getMinCOLbounds().getHeight();
		float a_width = p.getMaxCOLbounds().getWidth();
//		float a_height = p.getMaxCOLbounds().getHeight();			

		float b_x = tri.getPosition().getX();
		float b_y = tri.getPosition().getY();
		float b_width = tri.getMaxCOLbounds().getWidth();
		float b_height = tri.getMaxCOLbounds().getHeight();	
		
		
		
		
	}
	
	static void colides_Up_Side_t(Colideable p, Colideable tri, float move) {
		
		float a_x = p.getPosition().getX();
		float a_y = p.getPosition().getY();			
		float a_minwidth = p.getMinCOLbounds().getWidth();
		float a_minheight = p.getMinCOLbounds().getHeight();
		float a_width = p.getMaxCOLbounds().getWidth();
//		float a_height = p.getMaxCOLbounds().getHeight();			

		float b_x = tri.getPosition().getX();
		float b_y = tri.getPosition().getY();
		float b_width = tri.getMaxCOLbounds().getWidth();
		float b_height = tri.getMaxCOLbounds().getHeight();	
		
	}
	

}
