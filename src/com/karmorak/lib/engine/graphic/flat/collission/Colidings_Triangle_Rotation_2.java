package com.karmorak.lib.engine.graphic.flat.collission;


class Colidings_Triangle_Rotation_2 {
		
	static float calc_X(float a_pos_y, float a_width, float b_pos_y, float b_height, float b_pos_x, float b_width) {
		float t_y = a_pos_y-b_pos_y;
//		if(t_y >= 0) {
//			if(t_y > b_height+a_width) return -1;
//		} else return -1;
		float x = (b_pos_x + (b_width * (t_y / b_height)));
		return x;		
	}
	static float calc_Y(float a_pos_x, float b_pos_y, float b_height, float b_pos_x, float b_width) {
		float t_x = a_pos_x-b_pos_x;
//		if(t_x >= 0) {
//			if(t_x > b_width) return -1; 
//		} else return -1;		
		float y = b_pos_y + (b_height * (t_x / b_width));	
		return y;
	}
	
	static void colides_Up_Side_t(Colideable p, Colideable tri, float move) {
		boolean firstX = false;		
		
		float a_minwidth = p.getMinCOLbounds().getWidth();
		float a_width = p.getMaxCOLbounds().getWidth();
		float a_height = p.getMaxCOLbounds().getHeight();
		float a_pos_x = p.getPosition().getX() + a_minwidth;	
		float a_pos_y = p.getPosition().getY()+a_height;
		float b_pos_x = tri.getPosition().getX();
		float b_pos_y = tri.getPosition().getY();
		
		if(a_pos_x >= b_pos_x && a_pos_x < b_pos_x+1) {
			if(a_pos_y < b_pos_y+a_height && a_pos_y > b_pos_y) {
				a_pos_y = b_pos_y;
			}
		}
		p.setPosition(a_pos_x - a_minwidth, a_pos_y-a_height);
		
		//OBEN LINKS
		a_pos_x = p.getPosition().getX() + a_minwidth;
		a_pos_y = p.getPosition().getY() + a_height;	
		float b_width = tri.getMaxCOLbounds().getWidth();
		float b_height = tri.getMaxCOLbounds().getHeight();	

		float x = calc_X(a_pos_y, a_width, b_pos_y, b_height, b_pos_x, b_width);
		float y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);	
		
		if(a_pos_y > y && a_pos_x < x && x > 0 && y > 0)	
			if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)
				if(a_pos_x <= b_pos_x + b_width&& a_pos_x > b_pos_x)		
					if(firstX) {
						if(p.isMoveUp()) {
						a_pos_y = a_pos_y-move;				
						x = calc_X(a_pos_y, a_width,b_pos_y, b_height, b_pos_x, b_width);
						a_pos_x = x;
						} else a_pos_x = x;		
						x = calc_X(a_pos_y, a_width,b_pos_y, b_height, b_pos_x, b_width);
						y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);		
						if(a_pos_y > y && a_pos_x < x && x > 0 && y > 0)	
							if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)
								if(a_pos_x <= b_pos_x + b_width&& a_pos_x > b_pos_x)	
									a_pos_y = y;
					} else {
						a_pos_y = y;
						x = calc_X(a_pos_y, a_width,b_pos_y, b_height, b_pos_x, b_width);
						y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);		
						if(a_pos_y > y && a_pos_x < x && x > 0 && y > 0)
							if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)
								if(a_pos_x <= b_pos_x + b_width&& a_pos_x > b_pos_x)		
									a_pos_x = x+1;
					}
		p.setPosition(a_pos_x - a_minwidth, a_pos_y-a_height);	
		
		//OBEN RECHTS
		a_pos_x = p.getPosition().getX() + a_width;
		a_pos_y = p.getPosition().getY() + a_height;	
		
		x = calc_X(a_pos_y, a_width, b_pos_y, b_height, b_pos_x, b_width);
		y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);
		
//		if(p.isMoveUp() && !p.isMoveRight()) {
//			colides_Up_Left_t(p, firstX, tri, move);
//			return;
//		} 		
		
		if(a_pos_y > y && a_pos_x < x && x > 0 && y > 0)
			if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)
				if(a_pos_x <= b_pos_x + b_width&& a_pos_x > b_pos_x)		
					if(firstX) {
						a_pos_x = b_pos_x;					
						x = calc_X(a_pos_y, a_width,b_pos_y, b_height, b_pos_x, b_width);
						y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);		
						if(a_pos_y > y && a_pos_x < x && x > 0 && y > 0)
							if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)
								if(a_pos_x <= b_pos_x + b_width&& a_pos_x > b_pos_x)	
									a_pos_y = y - a_height;
					} else {
						a_pos_y = y - a_height;
						x = calc_X(a_pos_y, a_width,b_pos_y, b_height, b_pos_x, b_width);
						y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);		
						if(a_pos_y > y && a_pos_x < x && x > 0 && y > 0)
							if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)
								if(a_pos_x <= b_pos_x + b_width&& a_pos_x > b_pos_x)	
									a_pos_x = x + a_width;
					}
		p.setPosition(a_pos_x-a_width, a_pos_y-a_height);		
		
	}
	
	static void colides_Left_Side_t(Colideable p, Colideable tri, float move) {
		boolean firstX = true;
		
		float a_minwidth = p.getMinCOLbounds().getWidth();
		float a_minheight = p.getMinCOLbounds().getHeight();
		float a_width = p.getMaxCOLbounds().getWidth();
		float a_height = p.getMaxCOLbounds().getHeight();
		float a_pos_y = p.getPosition().getY() + a_height;			
		float a_pos_x = p.getPosition().getX() + a_minwidth;
		float b_pos_x = tri.getPosition().getX();
		float b_pos_y = tri.getPosition().getY();
		float b_width = tri.getMaxCOLbounds().getWidth();
		float b_height = tri.getMaxCOLbounds().getHeight();	
		
		if(a_pos_x < b_pos_x+b_width && a_pos_x > b_pos_x+b_width-a_width) {
			if(a_pos_y > b_pos_y+b_height-1 && a_pos_y <= b_pos_y + b_height) {
				a_pos_x = b_pos_x + b_width;
			}
		}
		p.setPosition(a_pos_x - a_minwidth, a_pos_y-a_height);
		
		//OBEN LINKS
		a_pos_x = p.getPosition().getX() + a_minwidth;
		a_pos_y = p.getPosition().getY() + a_height;

		float x = calc_X(a_pos_y, a_width, b_pos_y, b_height, b_pos_x, b_width);
		float y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);	
		
		if(a_pos_y > y && a_pos_x < x && x > 0 && y > 0)	
			if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)
				if(a_pos_x <= b_pos_x + b_width&& a_pos_x > b_pos_x)		
					if(firstX) {
						if(p.isMoveUp()) {
						a_pos_y = a_pos_y-move;				
						x = calc_X(a_pos_y, a_width,b_pos_y, b_height, b_pos_x, b_width);
						a_pos_x = x;
						} else a_pos_x = x;		
						x = calc_X(a_pos_y, a_width,b_pos_y, b_height, b_pos_x, b_width);
						y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);		
						if(a_pos_y > y && a_pos_x < x && x > 0 && y > 0)	
							if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)
								if(a_pos_x <= b_pos_x + b_width&& a_pos_x > b_pos_x)	
									a_pos_y = y;
					} else {
						a_pos_y = y;
						x = calc_X(a_pos_y, a_width,b_pos_y, b_height, b_pos_x, b_width);
						y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);		
						if(a_pos_y > y && a_pos_x < x && x > 0 && y > 0)
							if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)
								if(a_pos_x <= b_pos_x + b_width&& a_pos_x > b_pos_x)		
									a_pos_x = x;
					}
		p.setPosition(a_pos_x - a_minwidth, a_pos_y-a_height);	
		
		//UNTEN LINKS
		a_pos_y = p.getPosition().getY() + a_minheight;			
		a_pos_x = p.getPosition().getX() + a_minwidth;

		x = calc_X(a_pos_y, a_width, b_pos_y, b_height, b_pos_x, b_width);
		y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);		
		if(x  < 0|| y < 0) return;
				
		if(a_pos_y > y && a_pos_x < x && x > 0 && y > 0)		
			if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)
				if(a_pos_x <= b_pos_x + b_width && a_pos_x > b_pos_x)	
					if(firstX) {
						a_pos_x = x;
						x = calc_X(a_pos_y, a_width,b_pos_y, b_height, b_pos_x, b_width);
						y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);		
						if(a_pos_y > y && a_pos_x < x && x > 0 && y > 0)		
							if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)
								if(a_pos_x <= b_pos_x + b_width && a_pos_x > b_pos_x)	
									a_pos_y = b_pos_y + b_height;
					} else {
						a_pos_y = b_pos_y + b_height+1;
						x = calc_X(a_pos_y, a_width,b_pos_y, b_height, b_pos_x, b_width);
						y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);		
						if(a_pos_y > y && a_pos_x < x && x > 0 && y > 0)		
							if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)
								if(a_pos_x <= b_pos_x + b_width && a_pos_x > b_pos_x)	
									a_pos_x = x+a_width;
					}
		p.setPosition(a_pos_x - a_minwidth, a_pos_y - a_minheight);		
		
	}
	
	static void colides_Right_Side_t(Colideable p, Colideable tri, float move) {
		boolean firstX = true;
		
		//OBEN RECHTS
		float a_width = p.getMaxCOLbounds().getWidth();		
		float a_height = p.getMaxCOLbounds().getHeight();
		float a_pos_x = p.getPosition().getX() + a_width;
		float a_pos_y = p.getPosition().getY() + a_height;	
		
		float b_pos_x = tri.getPosition().getX();
		float b_pos_y = tri.getPosition().getY();	
		float b_width = tri.getMaxCOLbounds().getWidth();
		float b_height = tri.getMaxCOLbounds().getHeight();		
		
		float x = calc_X(a_pos_y, a_width, b_pos_y, b_height, b_pos_x, b_width);
		float y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);	
		
//		if(p.isMoveUp() && !p.isMoveRight()) {
//			colides_Up_Left_t(p, firstX, tri, move);
//			return;
//		} 		
		
		if(a_pos_y > y && a_pos_x < x && x > 0 && y > 0) 
			if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)
				if(a_pos_x <= b_pos_x + b_width && a_pos_x > b_pos_x)		
					if(firstX) {
						a_pos_x = b_pos_x;					
						x = calc_X(a_pos_y, a_width,b_pos_y, b_height, b_pos_x, b_width);
						y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);		
						if(a_pos_y > y && a_pos_x < x && x > 0 && y > 0) 
							if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)
								if(a_pos_x <= b_pos_x + b_width && a_pos_x > b_pos_x)		
									a_pos_y = y - a_height;
					} else {
						a_pos_y = y - a_height;
						x = calc_X(a_pos_y, a_width,b_pos_y, b_height, b_pos_x, b_width);
						y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);		
						if(a_pos_y > y && a_pos_x < x && x > 0 && y > 0) 
							if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)
								if(a_pos_x <= b_pos_x + b_width && a_pos_x > b_pos_x)			
									a_pos_x = x + a_width;
					}
		p.setPosition(a_pos_x-a_width, a_pos_y-a_height);	
		
		//UNTEN RECHTS
		float a_minheight = p.getMinCOLbounds().getHeight();
		a_pos_x = p.getPosition().getX() + a_width;
		a_pos_y = p.getPosition().getY() + a_minheight;	
		
		x = calc_X(a_pos_y, a_width, b_pos_y, b_height, b_pos_x, b_width);
		y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);
		
		if(a_pos_y > y && a_pos_x < x && x > 0 && y > 0)
			if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)
				if(a_pos_x <= b_pos_x + b_width&& a_pos_x > b_pos_x)
					if(firstX) {
						a_pos_x = b_pos_x;				
						x = calc_X(a_pos_y, a_width,b_pos_y, b_height, b_pos_x, b_width);
						y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);		
						if(a_pos_y > y && a_pos_x < x && x > 0 && y > 0)
							if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)
								if(a_pos_x <= b_pos_x + b_width&& a_pos_x > b_pos_x)	
									a_pos_y = b_pos_y + b_height+1;
					} else {
						a_pos_y = b_pos_y + b_height+1;
						x = calc_X(a_pos_y, a_width,b_pos_y, b_height, b_pos_x, b_width);
						y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);		
						if(a_pos_y > y && a_pos_x < x && x > 0 && y > 0)
							if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)
								if(a_pos_x <= b_pos_x + b_width&& a_pos_x > b_pos_x)
									a_pos_x = b_pos_x;
					}
		p.setPosition(a_pos_x-a_width, a_pos_y - a_minheight);	
	}
	
	static void colides_Down_Side_t(Colideable p, Colideable tri, float move) {
		boolean firstX = false;
		
		//UNTEN LINKS
		float a_minheight = p.getMinCOLbounds().getHeight();
		float a_minwidth = p.getMinCOLbounds().getWidth();
		float a_pos_y = p.getPosition().getY() + a_minheight;			
		float a_pos_x = p.getPosition().getX() + a_minwidth;
		float a_width = p.getMaxCOLbounds().getWidth();
		
		float b_width = tri.getMaxCOLbounds().getWidth();
		float b_height = tri.getMaxCOLbounds().getHeight();		
		float b_pos_x = tri.getPosition().getX();
		float b_pos_y = tri.getPosition().getY();

		float x = calc_X(a_pos_y, a_width, b_pos_y, b_height, b_pos_x, b_width);
		float y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);	
				
		if(a_pos_y > y && a_pos_x < x && x > 0 && y > 0)			
			if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)
				if(a_pos_x <= b_pos_x + b_width&& a_pos_x > b_pos_x)	
					if(firstX) {
						a_pos_x = x+a_width;
						x = calc_X(a_pos_y, a_width,b_pos_y, b_height, b_pos_x, b_width);
						y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);		
						if(a_pos_y > y && a_pos_x < x && x > 0 && y > 0)			
							if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)
								if(a_pos_x <= b_pos_x + b_width&& a_pos_x > b_pos_x)	
									a_pos_y = b_pos_y + b_height;
					} else {
						a_pos_y = b_pos_y + b_height+1;
						x = calc_X(a_pos_y, a_width,b_pos_y, b_height, b_pos_x, b_width);
						y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);		
						if(a_pos_y > y && a_pos_x < x && x > 0 && y > 0)			
							if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)
								if(a_pos_x <= b_pos_x + b_width&& a_pos_x > b_pos_x)
									a_pos_x = x+a_width;
					}
		p.setPosition(a_pos_x - a_minwidth, a_pos_y -a_minheight);
		
		
		//UNTEN RECHTS		
		a_pos_x = p.getPosition().getX() + a_width;
		a_pos_y = p.getPosition().getY() + a_minheight;	
		
		x = calc_X(a_pos_y, a_width, b_pos_y, b_height, b_pos_x, b_width);
		y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);	
		
		if(a_pos_y > y && a_pos_x < x && x > 0 && y > 0)			
			if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)
				if(a_pos_x <= b_pos_x + b_width&& a_pos_x > b_pos_x)	
					if(firstX) {
						a_pos_x = b_pos_x;				
						x = calc_X(a_pos_y, a_width,b_pos_y, b_height, b_pos_x, b_width);
						y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);		
						if(a_pos_y > y && a_pos_x < x && x > 0 && y > 0)			
							if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)
								if(a_pos_x <= b_pos_x + b_width&& a_pos_x > b_pos_x)	
									a_pos_y = b_pos_y + b_height+1;
					} else {
						a_pos_y = b_pos_y + b_height+1;
						x = calc_X(a_pos_y, a_width,b_pos_y, b_height, b_pos_x, b_width);
						y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);		
						if(a_pos_y > y && a_pos_x < x && x > 0 && y > 0)			
							if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)
								if(a_pos_x <= b_pos_x + b_width&& a_pos_x > b_pos_x)	
									a_pos_x = b_pos_x;
					}
		p.setPosition(a_pos_x-a_width, a_pos_y - a_minheight);	
		
	}
	
	
	
	static void colides_Down_Left_t(Colideable p, boolean firstX, Colideable tri, float move) {
		float a_pos_y = p.getPosition().getY();			
		float a_pos_x = p.getPosition().getX();
		float a_width = p.getMaxCOLbounds().getWidth();
		
		float b_width = tri.getMaxCOLbounds().getWidth();
		float b_height = tri.getMaxCOLbounds().getHeight();		
		float b_pos_x = tri.getPosition().getX();
		float b_pos_y = tri.getPosition().getY();

		float x = calc_X(a_pos_y, a_width, b_pos_y, b_height, b_pos_x, b_width);
		float y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);		
		if(x  < 0|| y < 0) return;
				
		if(a_pos_y > y && a_pos_x < x) {			
			if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)  {
				if(a_pos_x <= b_pos_x + b_width&& a_pos_x > b_pos_x) {		
					if(firstX) {
						a_pos_x = x+a_width;
						x = calc_X(a_pos_y, a_width,b_pos_y, b_height, b_pos_x, b_width);
						y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);		
						if(x  < 0|| y < 0) return;
						if(a_pos_y > y && a_pos_x < x) {
							if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)  {
								if(a_pos_x <= b_pos_x + b_width&& a_pos_x > b_pos_x) {		
									a_pos_y = b_pos_y + b_height;
								}
							}
						}
					} else {
						a_pos_y = b_pos_y + b_height+1;
						x = calc_X(a_pos_y, a_width,b_pos_y, b_height, b_pos_x, b_width);
						y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);		
						if(x  < 0|| y < 0) return;
						if(a_pos_y > y && a_pos_x < x) {
							if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)  {
								if(a_pos_x <= b_pos_x + b_width&& a_pos_x > b_pos_x) {	
									a_pos_x = x+a_width;
								}
							}
						}
					}
				}
			}
		}
		p.setPosition(a_pos_x, a_pos_y);		
	}
	
	static void colides_Down_Right_t(Colideable p, boolean firstX, Colideable tri, float move) {
		float a_width = p.getMaxCOLbounds().getWidth();
		float a_pos_x = p.getPosition().getX() + a_width;
		float a_pos_y = p.getPosition().getY();	
		
		float b_pos_x = tri.getPosition().getX();
		float b_pos_y = tri.getPosition().getY();	
		float b_width = tri.getMaxCOLbounds().getWidth();
		float b_height = tri.getMaxCOLbounds().getHeight();		
		
		float x = calc_X(a_pos_y, a_width, b_pos_y, b_height, b_pos_x, b_width);
		float y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);		
		if(x  < 0|| y < 0) return;
		
		if(a_pos_y > y && a_pos_x < x) {	
			if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)  {
				if(a_pos_x <= b_pos_x + b_width&& a_pos_x > b_pos_x) {	
					if(firstX) {
						a_pos_x = b_pos_x;				
						x = calc_X(a_pos_y, a_width,b_pos_y, b_height, b_pos_x, b_width);
						y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);		
						if(x  < 0|| y < 0) return;
						if(a_pos_y > y && a_pos_x < x) {	
							if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)  {
								if(a_pos_x <= b_pos_x + b_width&& a_pos_x > b_pos_x) {		
									a_pos_y = b_pos_y + b_height+1;
								}
							}
						}
					} else {
						a_pos_y = b_pos_y + b_height+1;
						x = calc_X(a_pos_y, a_width,b_pos_y, b_height, b_pos_x, b_width);
						y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);		
						if(x  < 0|| y < 0) return;
						if(a_pos_y > y && a_pos_x < x) {	
							if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)  {
								if(a_pos_x <= b_pos_x + b_width&& a_pos_x > b_pos_x) {		
									a_pos_x = b_pos_x;
								}
							}
						}
					}
				}
			}
		}
		p.setPosition(a_pos_x-a_width, a_pos_y);		
	}
	
	static void colides_Up_Left_t(Colideable p, boolean firstX, Colideable tri, float move) {
		float a_pos_x = p.getPosition().getX();
		float a_width = p.getMaxCOLbounds().getWidth();
		float a_height = p.getMaxCOLbounds().getHeight();
		float a_pos_y = p.getPosition().getY()+a_height;			
		
		
		float b_width = tri.getMaxCOLbounds().getWidth();
		float b_height = tri.getMaxCOLbounds().getHeight();		
		float b_pos_x = tri.getPosition().getX();
		float b_pos_y = tri.getPosition().getY();

		float x = calc_X(a_pos_y, a_width, b_pos_y, b_height, b_pos_x, b_width);
		float y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);		
		if(x  < 0|| y < 0) return;
		
		if(a_pos_y > y && a_pos_x < x) {	
			if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)  {
				if(a_pos_x <= b_pos_x + b_width&& a_pos_x > b_pos_x) {		
					if(firstX) {
						if(p.isMoveUp()) {
						a_pos_y = a_pos_y-move;				
						x = calc_X(a_pos_y, a_width,b_pos_y, b_height, b_pos_x, b_width);
						a_pos_x = x;
						} else a_pos_x = x;		
						x = calc_X(a_pos_y, a_width,b_pos_y, b_height, b_pos_x, b_width);
						y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);		
						if(x  < 0|| y < 0) return;
						if(a_pos_y > y && a_pos_x < x) {	
							if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)  {
								if(a_pos_x <= b_pos_x + b_width&& a_pos_x > b_pos_x) {		
									a_pos_y = y;
								}
							}
						}
					} else {
						a_pos_y = y;
						x = calc_X(a_pos_y, a_width,b_pos_y, b_height, b_pos_x, b_width);
						y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);		
						if(x  < 0|| y < 0) return;
						if(a_pos_y > y && a_pos_x < x) {	
							if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)  {
								if(a_pos_x <= b_pos_x + b_width&& a_pos_x > b_pos_x) {			
									a_pos_x = x+1;
								}
							}
						}
					}
				}
			}
		}
		p.setPosition(a_pos_x, a_pos_y-a_height);		
	}
	
	static void colides_Up_Right_t(Colideable p, boolean firstX, Colideable tri, float move) {
		float a_width = p.getMaxCOLbounds().getWidth();
		float a_pos_x = p.getPosition().getX() + a_width;
		float a_height = p.getMaxCOLbounds().getHeight();
		float a_pos_y = p.getPosition().getY()+a_height;	
		
		float b_pos_x = tri.getPosition().getX();
		float b_pos_y = tri.getPosition().getY();	
		float b_width = tri.getMaxCOLbounds().getWidth();
		float b_height = tri.getMaxCOLbounds().getHeight();		
		
		float x = calc_X(a_pos_y, a_width, b_pos_y, b_height, b_pos_x, b_width);
		float y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);		
		if(x  < 0|| y < 0) return;
		
		if(p.isMoveUp() && !p.isMoveRight()) {
			colides_Up_Left_t(p, firstX, tri, move);
			return;
		} 		
		
		if(a_pos_y > y && a_pos_x < x) {
			if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)  {
				if(a_pos_x <= b_pos_x + b_width&& a_pos_x > b_pos_x) {			
					if(firstX) {
						a_pos_x = b_pos_x;					
						x = calc_X(a_pos_y, a_width,b_pos_y, b_height, b_pos_x, b_width);
						y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);		
						if(x  < 0|| y < 0) return;
						if(a_pos_y > y && a_pos_x < x) {
							if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)  {
								if(a_pos_x <= b_pos_x + b_width&& a_pos_x > b_pos_x) {		
									a_pos_y = y - a_height;
								}
							}
						}
					} else {
						a_pos_y = y - a_height;
						x = calc_X(a_pos_y, a_width,b_pos_y, b_height, b_pos_x, b_width);
						y = calc_Y(a_pos_x, b_pos_y, b_height, b_pos_x, b_width);		
						if(x  < 0|| y < 0) return;
						if(a_pos_y > y && a_pos_x < x) {
							if(a_pos_y > b_pos_y && a_pos_y <= b_pos_y + b_height)  {
								if(a_pos_x <= b_pos_x + b_width&& a_pos_x > b_pos_x) {		
									a_pos_x = x + a_width;
								}
							}
						}
					}
				}
			}
		}
		p.setPosition(a_pos_x-a_width, a_pos_y-a_height);		
	}

}
