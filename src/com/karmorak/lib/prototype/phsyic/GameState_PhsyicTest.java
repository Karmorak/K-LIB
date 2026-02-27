package com.karmorak.lib.prototype.phsyic;

import com.karmorak.lib.Color;
import com.karmorak.lib.KLIB;
import com.karmorak.lib.engine.graphic.flat.DrawMap;
import com.karmorak.lib.engine.graphic.MasterRenderer;
import com.karmorak.lib.gamestate.GameState;
import com.karmorak.lib.math.Vector2;

public class GameState_PhsyicTest implements GameState {
	
	private static Point[] points;
	private static Stick[] sticks;
	private static Form[] forms;	
	
	Engine engine;
	
	DrawMap d;
	
	@Override
	public void init() {
		
		engine = new Engine();
		
		points = new Point[] {
				new Point(100, 800, 5, 50, true), 
				new Point(200, 800, 0, 0, true), 
				new Point(200, 700, 0, 0, true),
				new Point(100, 700, 0, 0, true), 
				
				new Point(100, 900, 0, 0, true),
				new Point(100, 1000, 0, 0, true)
				};
		
		
		sticks = new Stick[] {
			new Stick(points[0], points[1], true),	
			new Stick(points[1], points[2], true),
			new Stick(points[2], points[3], true),	
			new Stick(points[3], points[0], true),
			new Stick(points[0], points[2], true),
			new Stick(points[1], points[3], true),	
			
			new Stick(points[0], points[4], false),
			new Stick(points[4], points[5], false),
			new Stick(points[5], engine, false)	
		};
		
		forms = new Form[] {
			new Form(points[0], points[1], points[2], points[3],(int) sticks[4].length)	
		};
		
		
		d = new DrawMap(new Vector2(64, 64), Color.BLACK());
//		d.fillTriangle(new Vector2(128, 128), new Vector2(228, 128), new Vector2(128, 28), Color.RED);
//		d.fillTriangle(new Vector2(63, 63), new Vector2(1, 1), new Vector2(63, 1), Color.RED);
		d.fillTriangle(new Vector2(52, 20), new Vector2(50, 1), new Vector2(36, 45), Color.RED());
		
	}
	
	boolean updated = false;
	int y = 0; 
	
	int state = 0;
	
	@Override
	public void update(float deltaTime) {
		
		engine.updateEngine();
		updatePoints();
		for(int i = 0 ; i < 3; i++) {
			updateSticks();
			constrainPoints();
		}	
	
		y++;
//		d.clearDrawMap(Color.BLACK);
//		d.drawLine2(Color.GREEN, 64,32, 32 , 32);
		
//		if(state == 0) {
//			d.drawLine(Color.GREEN, 32, 32, 64, y);	
//		} else if(state == 1) {
//			d.drawLine(Color.GREEN, 32, 32, 64-y, 64);	
//		} else if(state == 2) {
//			d.drawLine(Color.GREEN, 32, 32, 0, 64-y);	
//		} else if(state == 3) {
//			d.drawLine(Color.GREEN, 32, 32, y, 0);	
//		} else if(state == 4) {
//			d.drawLine(Color.GREEN, 64, y, 32 , 32);	
//		} else if(state == 5) {
//			d.drawLine(Color.GREEN, 64-y, 64, 32 , 32);		
//		} else if(state == 6) {
//			d.drawLine(Color.GREEN, 0, 64-y, 32 , 32);		
//		} else if(state == 7) {
//			d.drawLine(Color.GREEN, y, 0, 32 , 32);		
//		}
//		
//		
//		
		if(y > 64) {
			y = 0;
			state ++;
			if(state > 7) state =0;
		}
		
		
	}
	
	

	@Override
	public void draw(MasterRenderer renderer) {

		for (int i = 0; i < points.length; i++) {
			points[i].draw(renderer);
		}
		
		for (int i = 0; i < forms.length; i++) {		
			forms[i].drawShape(renderer);
			forms[i].drawTexture(renderer);			
		}
		
		for (int i = 0; i < sticks.length; i++) {
			sticks[i].draw();
		}
		
		engine.draw(renderer);
		
	}
	
	
	void updatePoints() {
		for (int i = 0; i < points.length; i++) {
			Point p = points[i];
			if(!p.pinned) {
				float vel_x = (p.x - p.old_x) * Point.friction;
				float vel_y = p.y - p.old_y;
				
				p.old_x = p.x;
				p.old_y = p.y;
				p.x += vel_x;
				p.y += vel_y;
				p.y -= Point.gravity;
				
				if(p.x > KLIB.graphic.Width()) {
					p.x = (int) (KLIB.graphic.Width());
					p.old_x = p.x + vel_x * p.bounce;
				} else if(p.x < 0) {
					p.x = 0;
					p.old_x = p.x + vel_x * p.bounce;
				} 
				
				if(p.y > KLIB.graphic.Height()) {
					p.y = (int) (KLIB.graphic.Height());
					p.old_y = p.y + vel_y * p.bounce;
				} else if(p.y < 0) {
					p.y = 0;
					p.old_y = p.y + vel_y * p.bounce;
				} 
			}
		}
	}
	
	void constrainPoints() {
		for (int i = 0; i < points.length; i++) {
			Point p = points[i];
			if(!p.pinned) {
				float vel_x = (p.x - p.old_x) * Point.friction;
				float vel_y = p.y - p.old_y;
				
				if(p.x > KLIB.graphic.Width()) {
					p.x = (int) (KLIB.graphic.Width());
					p.old_x = p.x + vel_x * p.bounce;
				} else if(p.x < 0) {
					p.x = 0;
					p.old_x = p.x + vel_x * p.bounce;
				} 
				
				if(p.y > KLIB.graphic.Height()) {
					p.y = (int) (KLIB.graphic.Height());
					p.old_y = p.y + vel_y * p.bounce;
				} else if(p.y < 0) {
					p.y = 0;
					p.old_y = p.y + vel_y * p.bounce;
				} 
			}
		}
	}
	
	void updateSticks() {
		for (int i = 0; i < sticks.length; i++) {
			Stick s = sticks[i];
			
			float dx = s.p1.x - s.p0.x;
			float dy = s.p1.y - s.p0.y;
			
			float distance = (float) Math.sqrt(dx*dx + dy*dy);
			float difference = s.length - distance;
			float percent = difference / distance / 2;
			
			float offsetX = dx * percent;
			float offsetY = dy * percent;
			
			if(!s.p0.pinned) {
				s.p0.x -= offsetX;
				s.p0.y -= offsetY;
			}
			if(!s.p1.pinned) {
				s.p1.x += offsetX;
				s.p1.y += offsetY;
			}
			
			if(!s.isHidden) {
				s.drawMap.clearDrawMap();
				s.drawMap.drawLine2(Color.RED(), (int) s.length ,(int) s.length,(int)(s.length+dx),(int)(s.length+dy));
			}
		}
	}
	


	
	@Override
	public void tap(float x, float y, int count, int button) {
		
		if(points[6].pinned) points[6].pinned = false;
		else points[6].pinned = true;
		
	}
	

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
