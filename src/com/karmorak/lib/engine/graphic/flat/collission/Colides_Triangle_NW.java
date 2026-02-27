package com.karmorak.lib.engine.graphic.flat.collission;

class Colides_Triangle_NW {
	
	
	static float calc_X(float a_pos_y, float a_width, float b_pos_y, float b_height, float b_pos_x, float b_width) {
		float t_y = a_pos_y-b_pos_y;
//		if(t_y >= 0) {
//			if(t_y > b_height+a_width) return -1;
//		} else return -1;
		float x = (b_pos_x + b_width * (t_y / b_height));
		return x;		
	}
	static float calc_Y(float a_pos_x, float b_pos_y, float b_height, float b_pos_x, float b_width) {
		float t_x = a_pos_x-b_pos_x;
//		if(t_x >= 0) {
//			if(t_x > b_width) return -1; 
//		} else return -1;		
		float y = b_pos_y + b_height * (t_x / b_width);	
		return y;
	}
	
	protected static short intersects_NW(Colideable rec, Colideable tri) {
	//Kolidiert UNTERE LINKE ecke des vierecks mit dreieck
		float a_pos_y = rec.getPosition().getY();			
		float a_pos_x = rec.getPosition().getX();
		float a_width = rec.getMaxCOLbounds().getWidth();
		float a_height = rec.getMaxCOLbounds().getHeight();
		
		float b_width = tri.getMaxCOLbounds().getWidth();
		float b_height = tri.getMaxCOLbounds().getHeight();		
		float b_pos_x = tri.getPosition().getX();
		float b_pos_y = tri.getPosition().getY();

		float x = calc_X(a_pos_y, a_width, b_pos_y, b_height, b_pos_x, b_width);
		float y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);		
		if(x  < 0|| y < 0) {
			if(a_pos_y < y && a_pos_x > x) {
				if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)  {
					if(a_pos_x <= b_pos_x + b_width) {	
						return 0;
					}
				}
			}
		}
	//Kolidiert UNTER RECHTE ecke des vierecks mit dreieck
		a_pos_x = rec.getPosition().getX() + a_width;	
		
		x = calc_X(a_pos_y, a_width, b_pos_y, b_height, b_pos_x, b_width);
		y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);		
		if(x  < 0|| y < 0) {		
			if(a_pos_y < y && a_pos_x > x) {
				if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)  {
					if(a_pos_x <= b_pos_x + b_width) {	
						return 1;
					}
				}
			}
		}
	//Kolidiert OBERE LINKE ecke des vierecks mit dreieck
		a_pos_x = rec.getPosition().getX();
		a_pos_y = rec.getPosition().getY()+a_height;	

		x = calc_X(a_pos_y, a_width, b_pos_y, b_height, b_pos_x, b_width);
		y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);		
		if(x  < 0|| y < 0) {
		
			if(a_pos_y < y && a_pos_x > x) {
				if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)  {
					if(a_pos_x <= b_pos_x + b_width) {
						return 2;
					}
				}
			}
		}
	//Kolidiert OBERE RECHTE ecke des vierecks mit dreieck	
		a_pos_x = rec.getPosition().getX() + a_width;
		a_pos_y = rec.getPosition().getY()+a_height;
		
		x = calc_X(a_pos_y, a_width, b_pos_y, b_height, b_pos_x, b_width);
		y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);		
		if(x  < 0|| y < 0) {	
			if(a_pos_y < y && a_pos_x > x) {
				if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)  {
					if(a_pos_x <= b_pos_x + b_width) {	
						return 3;
					}
				}
			}	
		}
		return -1;
	}
	
	

}
