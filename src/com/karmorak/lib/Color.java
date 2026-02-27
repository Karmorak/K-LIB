package com.karmorak.lib;

import com.karmorak.lib.math.Vector3;
import com.karmorak.lib.math.Vector4;

@SuppressWarnings("unused")
public class Color implements Colorable {


	private static final Color BLACK = new Color(0, 0, 0),
			WHITE = new Color(255, 255, 255),
			RED = new Color(255, 0, 0),
			CYAN = new Color(0, 255, 255),
			GREEN = new Color(0, 255, 0),
			BROWN = new Color(139, 69, 19),
			PINK = new Color(255, 20, 147),
			ORANGE = new Color(238, 154, 0),
			BLUE = new Color(0, 0, 255),
			YELLOW = new Color(255, 255, 0),
			LIGHT_GRAY = new Color(190, 190, 190),
			GRAY = new Color(130, 130, 130),
			DARK_GRAY = new Color(70, 70, 70),
			NEUTRAL = new Color(1, 1, 1),
			ALPHA = new Color(0, 0, 0, 0);

	@Deprecated
	public static Color BLACK() { return BLACK; }
	@Deprecated
	public static Color WHITE() { return WHITE; }
	@Deprecated
	public static Color RED() { return RED; }
	@Deprecated
	public static Color CYAN() { return CYAN; }
	@Deprecated
	public static Color GREEN() { return GREEN; }
	@Deprecated
	public static Color PINK() { return PINK; }
	@Deprecated
	public static Color ORANGE() { return ORANGE; }
	@Deprecated
	public static Color BLUE() { return BLUE; }
	@Deprecated
	public static Color YELLOW() { return YELLOW; }
	@Deprecated
	public static Color LIGHT_GRAY () { return LIGHT_GRAY; }
	@Deprecated
	public static Color ALPHA() { return ALPHA; }


	private int R, G, B, A;
	
	public static int BGRtoRGBA(int BGR) {
		
		int R = BGR & 255; 
		int G = (BGR >> 8) & 255; 
		int B = (BGR >> 16) & 255; 
		
		return new Color(R, G, B, 255).toInt();
	}
	
	public static Color BGRtoColor(int BGR) {		
		int R = BGR & 255; 
		int G = (BGR >> 8) & 255; 
		int B = (BGR >> 16) & 255; 
		
		return new Color(R, G, B, 255);
	}
	
	public static Color RGBtoColor(int RGB) {		
		int B = RGB & 255; 
		int G = (RGB >> 8) & 255; 
		int R = (RGB >> 16) & 255; 
		
		return new Color(R, G, B, 255);
	}
	
	public static Color invert(Color c) {
		return new Color(255-c.R, 255-c.G, 255-c.B, c.A);		
	}

	public Color(Color color) {
		this.R = color.R;
		this.G = color.G;
		this.B = color.B;
		this.A = color.A;
	}

	public Color(int color_code) {
		A = (color_code >> 24) & 0xFF;
		B = (color_code >> 16) & 0xFF;
		G = (color_code >> 8) & 0xFF;
		R = (color_code) & 0xFF;
	}

	public Color(ColorPreset color) {
		this.R = color.R;
		this.G = color.G;
		this.B = color.B;
		this.A = color.A;
	}
	
	public Color(int color_code, boolean alpha) {
		if(alpha) {
			A = (color_code >> 24) & 0xFF;
        } else {
			A = 0xFF;
        }
        B = (color_code >> 16) & 0xFF;
        G = (color_code >> 8) & 0xFF;
        R = (color_code) & 0xFF;
    }
	
	public Color(int r, int g, int b) {
		R = r;
		G = g;
		B = b;
		A = 255;
		
	}
	
	public Color(int r, int g, int b, int a) {
		R = r;
		G = g;
		B = b;
		A = a;
	}

	public Color(int[] values) {
		R = values[0];
		G = values[1];
		B = values[2];
		A = values[3];
	}

	public Color(float r, float g, float b) {
		R = (int) r;
		G = (int) g;
		B = (int) b;
		A = 255;
	}

	public Color(float r, float g, float b, float a) {
		R = (int) r;
		G = (int) g;
		B = (int) b;
		A = (int) a;
	}

	public int getRed() {
		return R;
	}

	public Color setRed(int r) {
		R = r;
		return this;
	}

	public int getGreen() {
		return G;
	}

	public Color setGreen(int g) {
		G = g;
		return this;
	}

	public int getBlue() {
		return B;
	}

	public Color setBlue(int b) {
		B = b;
		return this;
	}

	public int getAlpha() {
		return A;
	}

	@Override
	public int Red() {
		return R;
	}

	@Override
	public int Green() {
		return G;
	}

	@Override
	public int Blue() {
		return B;
	}

	@Override
	public int Alpha() {
		return A;
	}

	public Color setAlpha(int a) {	A = a;	return this; }

	public void set(int r, int g, int b, int a) {
		R = r;
		G = g;
		B = b;
		A = a;
	}

	public void set(Colorable c) {
		R = c.Red();
		G = c.Green();
		B = c.Blue();
		A = c.Alpha();
	}
	
	public void add(int r, int g, int b, int a) {
		R += r;
		if(R > 255) R = 255;		
		G += g;
		if(G > 255) G = 255;	
		B += b;
		if(B > 255) B = 255;	
		A += A;
		if(A > 255) A = 255;	
	}
	

	@Override
	public int toInt() {
		return (A << 24) + (B << 16) + (G << 8) + R; //RGBA
	}

	@Override
	public Color toColor() {
		return this;
	}

	public static int toInt(int R, int G, int B) {
		return (255 << 24) + (B << 16) + (G << 8) + R; //RGBA
	}

	public static int toInt(int R, int G, int B, int A) {
		return (A << 24) + (B << 16) + (G << 8) + R; //RGBA
	}

    public Vector3 Vec3() {
		return new Vector3(R, G, B);
	}
	
	
	public Vector4 Vec4() {
		return new Vector4(R, G, B, A);
	}
	
	/**relikt aus libGDX*/
	@SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
	public static String getColorName(float color_value) {
		float f = color_value * 6;
		if(f <= 0) {
			return "no";
		} else if(f <= 0.5f) {
			return "red";
		} else if (f <= 1f) {
			return "yellow";
		} if(f <= 1.5f) {
			return "yellow";
		} else if (f <= 2f) {
			return "green";
		} if(f <= 2.5f) {
			return "green";
		} else if (f <= 3f) {
			return "cyan";
		} if(f <= 3.5f) {
			return "cyan";
		} else if (f <= 4f) {
			return "blue";
		} if(f <= 4.5f) {
			return "blue";
		} else if (f <= 5f) {
			return "pink";
		} if(f <= 5.5f) {
			return "pink";
		} else if (f <= 6f) {
			return "red";
		} else {
			return "no";
		}	
	}
	
	public static Color getRandomColor() {
		float f = (float) (Math.random());		
		return FloattoColor(f);
	}	
	
	
	/**
	 * 
	 * @param color color_float
	 * @return
	 * 
	 * the number shouldn't be greater than 1f but if its greater its always 1f
	 * if it's smaller than 0f the value will be 0f
	 */
	public static Color FloattoColor(float color) {
				
		Color c;
				
		float six = 1f/6f;
		float m;
		
		if(color < six) {
			//rot -> orange -> gelb
			m = color * 6f;
			c =  new Color(255, m*255f, 0);		
		} else if(color < six*2) {
			//gelb -> grün
			m = (color - six) * 6f;
			c =  new Color(255f-m*255f, 255, 0);	
		} else if(color < six*3) {
			//grün -> cyan
			m = (color - six*2f) * 6f;
			c =  new Color(0, 255, m*255f);		
		} else if(color < six*4) {
			//cyan -> blau
			m = (color - six*3f) * 6f;
			c =  new Color(0, 255-m*255f, 255);		
		} else if(color < six*5) {
			//blau -> pink
			m = (color - six*4f) * 6f;
			c =  new Color(m*255f, 0, 255);	
		} else {
			//pink -> rot
			m = (color - six*5f) * 6f;
			c =  new Color(255, 0, 255-m*255);	
		}
		
		return c;		
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + A;
		result = prime * result + B;
		result = prime * result + G;
		result = prime * result + R;
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
		Color other = (Color) obj;
		if (A != other.A)
			return false;
		if (B != other.B)
			return false;
		if (G != other.G)
			return false;
        return R == other.R;
    }



}
