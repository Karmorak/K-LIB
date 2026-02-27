package com.karmorak.lib.engine.graphic.flat.collission;

class Colidings_Triangle_Rotation_3 {
		
	static float calc_X(float a_y, float b_y, float b_height, float b_x, float b_width) {
		float t_y = a_y-b_y;
		float x = (b_x + (b_width-b_width * (t_y / b_height)));
		return x;		
	}
	static float calc_Y(float a_x, float b_y, float b_height, float b_x, float b_width) {
		float t_x = a_x-b_x;	
		float y = b_y - (b_height * (t_x / b_width));	
		return y;
	}
	
	static void colides_Up_Side_t(Colideable p, Colideable tri, float move) {
		
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
		
		//(KOLLIDIERT OBERE SEITE)
		if(		a_y + a_height > b_y &&
				a_y + a_height < b_y + b_height &&
				a_x + a_width >= b_x + b_width &&
				a_x + a_minwidth < b_x + b_width) {
			p.setPosition(a_x, b_y - a_height);
		}
		
		//OBEN RECHTS
		float x = calc_X(a_y + a_height, b_y, b_height, b_x, b_width);
		float y = calc_Y(a_x + a_width, b_y, b_height, b_x, b_width);	
		
		if(a_x + a_width > x && a_y + a_height > y && x > 0 && y > 0)
			if(a_y + a_height > b_y && a_y + a_height < b_y + b_height)				
				if(a_x + a_width < b_x + b_width && a_x + a_width > b_x)	
						p.setPosition(a_x, y);
		
		//OBEN LINKS
//		x = calc_X(a_y, b_y, b_height, b_x, b_width);
//		y = calc_Y(a_x, b_y, b_height, b_x, b_width);		
		
//		if(p.isMoveUp() && !p.isMoveLeft()) {
//			colides_Up_Right_t(p, firstX, tri, move);
//			return;
//		} 	
//		
//		if(a_y > y && a_x > x && x > 0 && y > 0)
//			if(a_y > b_y && a_y <= b_y + b_height)
//				if(a_x <= b_x + b_width&& a_x > b_x)	{
//						a_y = y ;
//						x = calc_X(a_y,b_y, b_height, b_x, b_width);
//						y = calc_Y(a_x, b_y, b_height, b_x, b_width);		
//						if(a_y > y && a_x > x && x > 0 && y > 0)
//							if(a_y > b_y && a_y <= b_y + b_height)
//								if(a_x <= b_x + b_width&& a_x > b_x)			
//									a_x = b_x+b_width;
//				
//					}
//		p.setPosition(a_x - a_minwidth, a_y-a_height);		
//		
	}
	
	static void colides_Right_Side_t(Colideable p, Colideable tri, float move) {		
		
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
		
		
		//RECHTE SEITE(obere rechte ecke) mit der linken ecke des dreiecks
		if(a_x + a_width > b_x &&
				a_x + a_minwidth < b_x + b_width &&
				a_y + a_height > b_y + b_height && 
				a_y + a_minheight < b_y + b_height) {
					p.setPosition(b_x- a_width, a_y);	
		}
		
		
		//OBEN RECHTS		
		float x = calc_X(a_y + a_height, b_y, b_height, b_x, b_width);
		float y = calc_Y(a_x + a_width, b_y, b_height, b_x, b_width);	
		
		if(a_x + a_width > x && a_y + a_height > y && x > 0 && y > 0)
			if(a_y + a_height > b_y && a_y + a_height < b_y + b_height)				
				if(a_x + a_width < b_x + b_width && a_x + a_width > b_x)	
						p.setPosition(x - a_width, a_y);
		
		
//		//UNTEN RECHTS		
//		x = calc_X(a_y, b_y, b_height, b_x, b_width);
//		y = calc_Y(a_x + a_width, b_y, b_height, b_x, b_width);	
//		
//		if(a_x + a_width > x && a_y + a_minheight > y && x > 0 && y > 0)
//			if(a_y + a_minheight > b_y && a_y + a_minheight < b_y + b_height)				
//				if(a_x + a_width < b_x + b_width && a_x + a_width > b_x)		
//						p.setPosition(x - a_width, a_y);
	}
	
	static void colides_Left_Side_t(Colideable p, Colideable tri, float move) {		

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
		
		float x = calc_X(a_y + a_height, b_y, b_height, b_x, b_width);
		
		//UNTEN LINKS
		if(a_x + a_minwidth > b_x &&
			a_x + a_minwidth < b_x + b_width &&
			a_y + a_minheight > b_y &&
			a_y + a_minheight < b_y + b_height &&
			a_x + a_minwidth > x) {
			p.setPosition(b_x + b_width, a_y);	
		//OBEN LINKS
		} else if(a_x + a_minwidth > b_x &&
				a_x + a_minwidth < b_x + b_width &&
				a_y + a_height > b_y &&
				a_y + a_height < b_y + b_height&&
				a_x + a_minwidth > x) {
			p.setPosition(b_x + b_width, a_y);
		}
		
		
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
		
		//UNTEN LINKS
		if(a_x + a_minwidth > b_x &&
				a_x + a_minwidth < b_x + b_width &&
				a_y + a_minheight > b_y &&
				a_y + a_minheight < b_y + b_height) {
				p.setPosition(a_x, b_y + b_height);			
		//UNTEN RECHTS
		} else if(a_x + a_width > b_x &&
				a_x + a_width < b_x + b_width &&
				a_y + a_minheight > b_y &&
				a_y + a_minheight < b_y + b_height) {
				p.setPosition(a_x, b_y + b_height);
		}
		
	}
}
