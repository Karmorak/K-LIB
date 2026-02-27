package com.karmorak.lib.engine.graphic.flat.collission;

import java.util.ArrayList;
import com.karmorak.lib.KLIB;
import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.math.Vector4;

public abstract class Colideable {
	
	
	public static final short SHAPE_RECTANGLE = 0;
	public static final short SHAPE_CIRCLE = 1;
	public static final short SHAPE_TRIANGLE = 2;
	
	public short type = 0;
	public short rotation;
	
	private static final short MOVE_UP = 0;
	private static final short MOVE_RIGHT = 1;
	private static final short MOVE_DOWN = 2;
	private static final short MOVE_LEFT = 3;
	private boolean[] moving = new boolean[4];
	
	protected Vector2 pos;
	protected Vector2 bounds;
	protected Vector4 hit_box;
	protected boolean iscatched = true;
	
	@SuppressWarnings("exports")
	public Pushable pushable;

	class Pushable {
		float push_strength;
		float weight;
		
		public Pushable(float strength, float weight) {
			this.push_strength = strength;
			this.weight = weight;
		}
		
	}
	
	
	public void setMoveAll(boolean bool) {
		moving[MOVE_UP] = bool;
		moving[MOVE_RIGHT]  = bool;
		moving[MOVE_DOWN] = bool;
		moving[MOVE_LEFT] = bool;
	}
	
	public void setMoveUp(boolean bool) {
		moving[MOVE_UP] = bool;
	}
	public void setMoveRight(boolean bool) {
		moving[MOVE_RIGHT]  = bool;
	}
	public void setMoveDown(boolean bool) {
		moving[MOVE_DOWN] = bool;
	}
	public void setMoveLeft(boolean bool) {
		moving[MOVE_LEFT] = bool;
	}
	
	public boolean isMoveUp() {
		return moving[MOVE_UP];
	}
	public boolean isMoveRight() {
		return moving[MOVE_RIGHT];
	}
	public boolean isMoveDown() {
		return moving[MOVE_DOWN];
	}
	public boolean isMoveLeft() {
		return moving[MOVE_LEFT];
	}
	

	
	public Colideable() {
		this.type = SHAPE_RECTANGLE;
		this.pos = new Vector2(0, 0);
		this.bounds = new Vector2(0, 0);
		this.hit_box = new Vector4(0, 0, 0, 0);
		
	}
	
	public Colideable(Vector2 pos, Vector2 boundsmax) {
		this.type = SHAPE_RECTANGLE;
		this.pos = new Vector2(pos.getX(), pos.getY());
		this.bounds = new Vector2(boundsmax.getWidth(), boundsmax.getHeight());
		this.hit_box = new Vector4(0,0, boundsmax.getWidth(), boundsmax.getHeight());
	}
	public Colideable(Vector2 pos, Vector2 boundsmax, short type_id) {
		this.pos = new Vector2(pos.getX(), pos.getY());
		this.bounds = new Vector2(boundsmax.getWidth(), boundsmax.getHeight());
		this.hit_box = new Vector4(0,0, boundsmax.getWidth(), boundsmax.getHeight());
		this.type = type_id;
	}
	
	public Colideable(Vector2 pos, Vector2 boundsmin, Vector2 boundsmax) {
		this.pos = new Vector2(pos.getX(), pos.getY());
		this.bounds = new Vector2(boundsmax.getWidth(), boundsmax.getHeight());
		this.hit_box = new Vector4(boundsmin.getX(), boundsmin.getY(), boundsmax.getWidth(), boundsmax.getHeight());
		this.type = SHAPE_RECTANGLE;
	}
	
	public Colideable(Vector2 pos, Vector2 boundsmin, Vector2 boundsmax, short type_id) {
		this.pos = new Vector2(pos.getX(), pos.getY());
		this.bounds = new Vector2(boundsmax.getWidth(), boundsmax.getHeight());
		this.hit_box = new Vector4(boundsmin.getX(), boundsmin.getY(), boundsmax.getWidth(), boundsmax.getHeight());
		this.type = type_id;
	}
	
	public Colideable(Vector2 pos, Vector2 boundsmin, Vector2 boundsmax, Vector2 bounds) {
		this.pos = new Vector2(pos.getX(), pos.getY());
		this.bounds = new Vector2(bounds.getWidth(), bounds.getHeight());
		this.hit_box = new Vector4(boundsmin.getX(), boundsmin.getY(), boundsmax.getWidth(), boundsmax.getHeight());
		this.type = SHAPE_RECTANGLE;
	}
	
	public Colideable(Vector2 pos, Vector2 boundsmin, Vector2 boundsmax, Vector2 bounds, short type_id) {
		this.pos = new Vector2(pos.getX(), pos.getY());
		this.bounds = new Vector2(bounds.getWidth(), bounds.getHeight());
		this.hit_box = new Vector4(boundsmin.getX(), boundsmin.getY(), boundsmax.getWidth(), boundsmax.getHeight());
		this.type = type_id;
	}
	
	
	public Vector2 getPosition() {
		return pos;
	}
	public void setPosition(Vector2 pos) {
		this.pos.setPosition(pos);
	}	
	public void setPosition(float x, float y) {
		pos.setPosition(x, y);
	}
	
	public Vector2 getMinCOLbounds() {
		return hit_box.getPosition();
	}
	public Vector2 getMaxCOLbounds() {
		return hit_box.getBounds();
	}
	
	public void setMinCOLbounds(Vector2 vec) {
		hit_box.setPosition(vec);
	}
	public void setMaxCOLbounds(Vector2 vec) {
		hit_box.setBounds(vec);
	}
	
	
	public Vector2 getBounds() {
		return new Vector2(bounds.getWidth(), bounds.getHeight());
	}
	
	public void setBounds(Vector2 vector2) {
		bounds.setWidth(vector2.getWidth());	
		bounds.setHeight(vector2.getHeight());	
	}
	
	public void setCatched(boolean bool) {
		iscatched = bool;
	}
	
	public short getType() {
		return type;
	}
		
	public void moveX(float value, ArrayList<Colideable> cols) {
		pos.setX(pos.getX() + value);
		float a_minwidth = getMinCOLbounds().getWidth();
		float a_minheight = getMinCOLbounds().getHeight();
		float a_pos_x = getPosition().getX() + a_minwidth;
		float a_pos_y = getPosition().getY() + a_minheight;			
		float a_width = getMaxCOLbounds().getWidth();		
		for(Colideable c : cols) {			
			if(c != null) {
				float b_width = c.getMaxCOLbounds().getWidth();	
				float b_pos_x = c.getPosition().getX();
				
				if(c.getType() == SHAPE_TRIANGLE) {
					
					if(value < 0) {
						if(c.rotation == Triangle.FACING_NORTH_WEST) {
							Colidings_Triangle_Rotation_0.colides_Left_Side_t(this, c, -4);
						} else if (c.rotation == Triangle.FACING_NORTH_EAST) {
							Colidings_Triangle_Rotation_1.colides_Left_Side_t(this, c, -4);
						} else if (c.rotation == Triangle.FACING_SOUTH_EAST) {
							Colidings_Triangle_Rotation_2.colides_Left_Side_t(this, c, -4);
						} else if(c.rotation == Triangle.FACING_SOUTH_WEST) {
							Colidings_Triangle_Rotation_3.colides_Left_Side_t(this, c, -4);
						}
					} else if (value  > 0) {						
						if(c.rotation == Triangle.FACING_NORTH_WEST) {
							Colidings_Triangle_Rotation_0.colides_Right_Side_t(this, c, 4);
						} else if (c.rotation == Triangle.FACING_NORTH_EAST) {
							Colidings_Triangle_Rotation_1.colides_Right_Side_t(this, c, 4);
						} else if (c.rotation == Triangle.FACING_SOUTH_EAST) {
							Colidings_Triangle_Rotation_2.colides_Right_Side_t(this, c, 4);
						} else if(c.rotation == Triangle.FACING_SOUTH_WEST) {
							Colidings_Triangle_Rotation_3.colides_Right_Side_t(this, c, 4);
						}
					}
				} else {
					if(Colideable.intersects(this, c)) {						
						if(c.Is_pushable()) {
							if(value < 0) {
								c.pushX(this, value);
							} else {
								c.pushX(this, value);
							}							
						} else {
							if(value < 0) {
								a_pos_x = b_pos_x + b_width;	
								setPosition(a_pos_x - a_minwidth, a_pos_y - a_minheight);
							} else if (value  > 0) {			
								setPosition(c.getPosition().getX() - a_width + c.getMinCOLbounds().getX(), a_pos_y - a_minheight);							
							}
						}
					}
				}
			}
		}
	}
	
	public void moveY(float value, ArrayList<Colideable> cols) {
		pos.setY(pos.getY()+ value);
		float a_minheight = getMinCOLbounds().getHeight();
		float a_pos_y = getPosition().getY() + a_minheight;
		float a_height = getMaxCOLbounds().getHeight();
		
		for(Colideable c : cols) {
			if(c != null) {
				float b_height = c.getMaxCOLbounds().getHeight();	
				float b_pos_y = c.getPosition().getY();
				if(c.getType() == SHAPE_TRIANGLE) {
					if(value < 0) {
						if(c.rotation == Triangle.FACING_NORTH_WEST) {
							Colidings_Triangle_Rotation_0.colides_Down_Side_t(this, c, -4);
						} else if (c.rotation == Triangle.FACING_NORTH_EAST) {
							Colidings_Triangle_Rotation_1.colides_Down_Side_t(this, c, -4);
						} else if (c.rotation == Triangle.FACING_SOUTH_EAST) {
							Colidings_Triangle_Rotation_2.colides_Down_Side_t(this, c, -4);
						} else if(c.rotation == Triangle.FACING_SOUTH_WEST) {
							Colidings_Triangle_Rotation_3.colides_Down_Side_t(this, c, -4);
						}
					} else if (value  > 0) {
						if(c.rotation == Triangle.FACING_NORTH_WEST) {
							Colidings_Triangle_Rotation_0.colides_Up_Side_t(this, c, 4);
						} else if (c.rotation == Triangle.FACING_NORTH_EAST) {
							Colidings_Triangle_Rotation_1.colides_Up_Side_t(this, c, 4);
						} else if (c.rotation == Triangle.FACING_SOUTH_EAST) {
							Colidings_Triangle_Rotation_2.colides_Up_Side_t(this, c, 4);
						} else if(c.rotation == Triangle.FACING_SOUTH_WEST) {
							Colidings_Triangle_Rotation_3.colides_Up_Side_t(this, c, 4);
						}
					}
				} else {
					if(Colideable.intersects(this, c)) {
						if(c.Is_pushable()) {
							
							if(value < 0) {
								c.pushY(this, value);
							} else {
								c.pushY(this, value);
							}						
						} else {
							if(value < 0) {
								a_pos_y = b_pos_y + b_height;	
								setPosition(getPosition().getX(), a_pos_y - a_minheight);
							} else if (value  > 0) {
								a_pos_y = b_pos_y - a_height;
								setPosition(getPosition().getX(), a_pos_y - a_minheight);
							}
						}
					}
				}
			}
		}
	}
	
	public boolean isOut() {
		if(iscatched) {
			if(pos.getX() + hit_box.getX() <= 0) {
				return true;			
			} else if (pos.getX() + hit_box.getWidth() >= KLIB.graphic.Width()) {
				return true;
			}			
			if(pos.getY() + hit_box.getY() <= 0 ) {
				return true;
			} else if (pos.getY()+ hit_box.getHeight() >= KLIB.graphic.Height()) {
				return true;
			}	
		}
		return false;		
	}
	
	public void keepIn() {
		if(iscatched) {
			if(pos.getX() + hit_box.getX() < 0) {
				pos.setPosition(0-hit_box.getX(), pos.getY());					
			} else if (pos.getX() + hit_box.getWidth() > KLIB.graphic.Width()) {
				pos.setPosition(KLIB.graphic.Width() - hit_box.getWidth(), pos.getY());
			}			
			if(pos.getY() + hit_box.getY() < 0 ) {
				pos.setPosition(pos.getX(), 0-hit_box.getY());
			} else if (pos.getY()+ hit_box.getHeight() > KLIB.graphic.Height()) {
				pos.setPosition(getPosition().getX(), KLIB.graphic.Height() - hit_box.getHeight());
			}
		}
	}

	
	
	public static boolean intersects(Colideable a, Colideable b) {
		if(a.type == SHAPE_RECTANGLE && b.type == SHAPE_RECTANGLE) {	
			float a_pos_x = a.getPosition().getX();
			float a_pos_y = a.getPosition().getY();		
			float a_x = a.getMinCOLbounds().getX();
			float a_y = a.getMinCOLbounds().getY();		
			float a_width = a.getMaxCOLbounds().getWidth();
			float a_height = a.getMaxCOLbounds().getHeight();
//			
			float b_pos_x = b.getPosition().getX();
			float b_pos_y = b.getPosition().getY();
			float b_x = b.getMinCOLbounds().getX();
			float b_y = b.getMinCOLbounds().getY();		
			float b_width = b.getMaxCOLbounds().getWidth();
			float b_height = b.getMaxCOLbounds().getHeight();
			
			if(a_pos_x + a_width > b_pos_x + b_x && a_pos_x + a_x < b_pos_x+ b_width ) {
				if(a_pos_y + a_height > b_pos_y + b_y &&  a_pos_y + a_y < b_pos_y+ b_height) {
					return true;
				}
			}
		} else if (a.type == SHAPE_RECTANGLE && b.type == SHAPE_TRIANGLE) {
			if(b.rotation == Triangle.FACING_NORTH_WEST) {
				Colides_Triangle_NW.intersects_NW(a, b);
			} else if(b.rotation == Triangle.FACING_NORTH_EAST) {
				
			} else if(b.rotation == Triangle.FACING_SOUTH_WEST) {
				
			} else if(b.rotation == Triangle.FACING_SOUTH_WEST) {
				
			}
		}
		return false;
	}
	

	
	public static boolean intersects(Vector2 a_pos, Vector2 a_mincolbounds, Vector2 a_colbounds, Colideable b) {
		float a_pos_x = a_pos.getX();
		float a_pos_y = a_pos.getY();		
		float a_x = a_mincolbounds.getX();
		float a_y = a_mincolbounds.getY();		
		float a_width = a_colbounds.getWidth();
		float a_height = a_colbounds.getHeight();
		
		float b_pos_x = b.pos.getX();
		float b_pos_y = b.pos.getY();
		float b_x = b.hit_box.getX();
		float b_y = b.hit_box.getY();		
		float b_width = b.hit_box.getWidth();
		float b_height = b.hit_box.getHeight();
			
		
		if(a_pos_x + a_width > b_pos_x + b_x && a_pos_x + a_x < b_pos_x+ b_width ) {
			if(a_pos_y + a_height > b_pos_y + b_y &&  a_pos_y + a_y < b_pos_y + b_height) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean intersects(Vector2 a_pos, Vector2 a_bounds, Vector2 b_pos, Vector2 b_bounds) {
		float a_pos_x = a_pos.getX();
		float a_pos_y = a_pos.getY();		
		float a_width = a_bounds.getWidth();
		float a_height = a_bounds.getHeight();
		
		float b_pos_x = b_pos.getX();
		float b_pos_y = b_pos.getY();
		float b_width = b_bounds.getWidth();
		float b_height = b_bounds.getHeight();
			
		
		if(a_pos_x + a_width > b_pos_x && a_pos_x < b_pos_x+ b_width ) {
			if(a_pos_y + a_height > b_pos_y &&  a_pos_y < b_pos_y + b_height) {
				return true;
			}
		}
		
		return false;
	}
	

	
	@Deprecated
	public static boolean Colides(Colideable b1, Colideable b2) {
		if(b1.type == SHAPE_RECTANGLE && b2.type == SHAPE_RECTANGLE) {	
			float a_pos_x = b1.pos.getX();
			float a_pos_y = b1.pos.getY();		
			float a_width = b1.bounds.getWidth();
			float a_height = b1.bounds.getHeight();
			
			float b_pos_x = b2.pos.getX();
			float b_pos_y = b2.pos.getY();
			float b_width = b2.bounds.getWidth();
			float b_height = b2.bounds.getHeight();
			
			if(a_pos_x + a_width > b_pos_x && a_pos_x  < b_pos_x+ b_width )
				if(a_pos_y + a_height > b_pos_y &&  a_pos_y < b_pos_y+ b_height)
					return true;
			
		} else if (b1.type == SHAPE_CIRCLE && b2.type == SHAPE_CIRCLE) {
			float a_pos_x = b1.pos.getX();
			float a_pos_y = b1.pos.getY();		
			float a_width = b1.bounds.getWidth();
			
			float b_pos_x = b2.pos.getX();
			float b_pos_y = b2.pos.getY();
			float b_width = b2.bounds.getWidth();
			
			
			float abs_x = 0;
			if(a_pos_x < b_pos_x)
				abs_x = b_pos_x - a_pos_x;
			else 
				abs_x = a_pos_x - b_pos_x;
			
			float abs_y = 0;
			if(a_pos_y< b_pos_y)
				abs_y = b_pos_y - a_pos_y;
			else 
				abs_y = a_pos_y - b_pos_y;
				
			
			
			if(abs_x < a_width + b_width && abs_y < a_width + b_width)
				return true;
		} else if ((b1.type == SHAPE_CIRCLE && b2.type == SHAPE_RECTANGLE) || (b2.type == SHAPE_CIRCLE && b1.type == SHAPE_RECTANGLE)) {
			if(b1.type == SHAPE_CIRCLE) {
			
				float a_pos_x = b1.pos.getX();
				float a_pos_y = b1.pos.getY();		
				float a_width = b1.bounds.getWidth();
				
				float circ_m_x  = a_pos_x+a_width;
				float circ_m_y = a_pos_y+ a_width;
				
				float b_pos_x = b2.pos.getX();
				float b_pos_y = b2.pos.getY();
				float b_width = b2.bounds.getWidth();
				float b_height = b2.bounds.getHeight();
				
				//ist Kreis mittelpunkt innerhalb von rechteck
				if((circ_m_x > b_pos_x && circ_m_x < b_pos_x + b_width) && (circ_m_y > b_pos_y && circ_m_y < b_pos_y + b_height)) return true;
				//kolidiert kreis seite mit rechteckseite auf X
				if((circ_m_x + a_width > b_pos_x && circ_m_x - a_width < b_pos_x + b_width) && (circ_m_y > b_pos_y && circ_m_y < b_pos_y + b_height)) return true;
				
//				//kolidiert kreis seite mit rechteckseite auf X
//				if((circ_m_x + a_width > b_pos_x && circ_m_x - a_width < b_pos_x + b_width) && (circ_m_y > b_pos_y && circ_m_y < b_pos_y + b_height)) return true;
				
				float x = Math.abs(circ_m_x - b_pos_x);
				float y = Math.abs(circ_m_y - b_pos_y);				
				double c =  Math.sqrt(x*x + y*y); 
				//unten links
				if(c < a_width) return true;				
				x = Math.abs(circ_m_x - (b_pos_x + b_width));	
				c =  Math.sqrt(x*x + y*y); 
				// unten rechts
				if(c < a_width) return true;				
				y = Math.abs(circ_m_y - (b_pos_y + b_width));			
				c =  Math.sqrt(x*x + y*y); 
				// oben rechts
				if(c < a_width) return true;				
				x = Math.abs(circ_m_x - b_pos_x);		
				c =  Math.sqrt(x*x + y*y); 
				//oben links
				if(c < a_width) return true;
			
				
				
				
			} else {
				float a_pos_x = b1.pos.getX();
				float a_pos_y = b1.pos.getY();		
				float a_width = b1.bounds.getWidth();
				float circ_m_x  = a_pos_x+a_width;
				float circ_m_y = a_pos_y+ a_width;
				
				float b_pos_x = b2.pos.getX();
				float b_pos_y = b2.pos.getY();
				float b_width = b2.bounds.getWidth();
				float b_height = b2.bounds.getHeight();
				
				
				if((circ_m_x > b_pos_x && circ_m_x < b_pos_x + b_width) && (circ_m_y > b_pos_y && circ_m_y < b_pos_y + b_height)) {
					return true;
				}	
				
				
			}			
		}	
		
		
		return false;
	}
	
	public void pushX(Colideable pushing, float value){};
	public void pushY(Colideable pushing, float value){};
	
	public boolean Is_pushable() {
		if(pushable != null) {
			return true;
		} else {
			return false;
		}
		
	}

	public void set_Pushable(float strength, float weight) {
		if(pushable == null) {
			this.pushable = new Pushable(strength, weight);
		}
	}
	
	
	

}
