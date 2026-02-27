package com.karmorak.lib.engine.graphic.flat.collission;

class Colidings_Triangle_Rotation_0 {
		
//�ndere evtl reihenfolgen bei col left side
//       ##
//     #  #
//   #    #
// #   	  #
//# # # # #
	
	
	static float calc_X(float a_pos_y, float b_pos_y, float b_height, float b_pos_x, float b_width) {
		float t_y = a_pos_y - b_pos_y;
		float x = (b_pos_x + b_width * (t_y / b_height));
		return x;		
	}
	static float calc_Y(float a_pos_x, float b_pos_y, float b_height, float b_pos_x, float b_width) {
		float t_x = a_pos_x - b_pos_x;
		float y = b_pos_y + b_height * (t_x / b_width);	
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
		
		
		if(a_x + a_minwidth < b_x + b_width && a_x + a_width >= b_x + b_width)
			if(a_y + a_minheight < b_y + b_height && a_y + a_minheight > b_y)
				p.setPosition(a_x, b_y + b_height - a_minheight); 
		
		//Kollidiert unten Rechts
		float x = calc_X(a_y + a_minheight, b_y, b_height, b_x, b_width);
		float y = calc_Y(a_x + a_width, b_y, b_height, b_x, b_width);	
		
		if(a_x + a_width > x && a_y + a_minheight < y && x > 0 && y > 0) {
			if(a_y + a_minheight < b_y + b_height)
				if(a_x + a_width < b_x +b_width)
				p.setPosition(a_x, y);
		}
		
		//Kollidiert unten Links
		
		
	}
	
	static void colides_Right_Side_t(Colideable p, Colideable tri, float move) {	

		float a_x = p.getPosition().getX();
		float a_y = p.getPosition().getY();			
//		float a_minwidth = p.getMinCOLbounds().getWidth();
		float a_minheight = p.getMinCOLbounds().getHeight();
		float a_width = p.getMaxCOLbounds().getWidth();
		float a_height = p.getMaxCOLbounds().getHeight();			

		float b_x = tri.getPosition().getX();
		float b_y = tri.getPosition().getY();
		float b_width = tri.getMaxCOLbounds().getWidth();
		float b_height = tri.getMaxCOLbounds().getHeight();	
		
	
		//SEITE RECHTS
		if(a_x + a_width > b_x && a_x + a_width < b_x + b_width) {
			if(a_y + a_height > b_y && a_y + a_minheight <= b_y) {
				p.setPosition(b_x, a_y);
			}
		}
				
		
		//UNTEN RECHTS		
		float x = calc_X(a_y + a_minheight, b_y, b_height, b_x, b_width);
		float y = calc_Y(a_x + a_width, b_y, b_height, b_x, b_width);
		
		if(a_x + a_width > x && x > 0) {
			if(a_y + a_minheight < y && y > 0) {
				if(a_x + a_width > b_x && a_y + a_minheight > b_y) {
					if(a_x + a_width < b_x + b_width && a_y + a_minheight < b_y + b_height) {
						p.setPosition(x - a_width, a_y);
					}
				}
			}
		}
	}
	
	static void colides_Left_Side_t(Colideable p, Colideable tri, float move) {		
		
//		//UNTEN LINKS
		float a_x = p.getPosition().getX();
		float a_y = p.getPosition().getY();			
		float a_minwidth = p.getMinCOLbounds().getWidth();
		float a_minheight = p.getMinCOLbounds().getHeight();
//		float a_width = p.getMaxCOLbounds().getWidth();
		float a_height = p.getMaxCOLbounds().getHeight();			

		float b_x = tri.getPosition().getX();
		float b_y = tri.getPosition().getY();
		float b_width = tri.getMaxCOLbounds().getWidth();
		float b_height = tri.getMaxCOLbounds().getHeight();	

		
		float x = calc_X(a_y + a_minheight, b_y, b_height, b_x, b_width);
		float x2 = calc_X(a_y + a_height, b_y, b_height, b_x, b_width);
		
		//UNTEN LINKS
		if(	a_x + a_minwidth < b_x + b_width && 
			a_x + a_minwidth > x && 
			a_y + a_minheight < b_y + b_height && 
			a_y + a_minheight > b_y) {
			p.setPosition(b_x + b_width, a_y);
		//OBEN LINKS	
		} else if (	a_x + a_minwidth < b_x + b_width && 
					a_x + a_minwidth > x2 && 
					a_y + a_height > b_y && 
					a_y + a_height < b_y + b_height) {
			p.setPosition(b_x + b_width, a_y);		
		}
	}
	
	static void colides_Up_Side_t(Colideable p, Colideable tri, float move) {
		
//		//OBEN RECHTS
		float a_x = p.getPosition().getX();
		float a_y = p.getPosition().getY();			
		float a_minwidth = p.getMinCOLbounds().getWidth();
//		float a_minheight = p.getMinCOLbounds().getHeight();
		float a_width = p.getMaxCOLbounds().getWidth();
		float a_height = p.getMaxCOLbounds().getHeight();			

		float b_x = tri.getPosition().getX();
		float b_y = tri.getPosition().getY();
		float b_width = tri.getMaxCOLbounds().getWidth();
		float b_height = tri.getMaxCOLbounds().getHeight();		
	
		float y = calc_Y(a_x + a_minwidth, b_y, b_height, b_x, b_width);	
		float y2 = calc_Y(a_x + a_width, b_y, b_height, b_x, b_width);	
	
		
		//OBEN LINKS
		if(	a_x + a_minwidth < b_x + b_width && 
			a_x + a_minwidth > b_x && 
			a_y + a_height > b_y && 
			a_y + a_height < y) {
			p.setPosition(a_x, b_y - a_height);
		//OBEN RECHTS	
		} else if (	a_x + a_width < b_x + b_width && 
					a_x + a_width > b_x && 
					a_y + a_height > b_y && 
					a_y + a_height < y2) {
			p.setPosition(a_x, b_y - a_height);		
		}	
	}
}
