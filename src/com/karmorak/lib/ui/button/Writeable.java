//v2
package com.karmorak.lib.ui.button;

import com.karmorak.lib.Color;
import com.karmorak.lib.engine.graphic.MasterRenderer;

@Deprecated
public class Writeable extends Hang {

	
	private String prefix;
	private String content = "";
	private int max_chars;
	protected int chars = 0;
	/** 0 = all, 1 = alphabet, 2 = numbers, 3 = alphabet+number, 4 = specialchars, 5 alphabet + sonders, */
	private int writemode = 0;
	
	
	
	
	
	/** 
	 * @param prefix
	 * 0 = all, 1 = alphabet, 2 = numbers, 3 = alphabet+number, 4 = specialchars, 5 alphabet + sonders, 
	 * 
	 * */	
	public Writeable(String prefix, int max_chars) {
		this(null, prefix, max_chars, 0);	
	}
	
	/** 0 = all, 1 = alphabet, 2 = numbers, 3 = alphabet+number, 4 = specialchars, 5 alphabet + sonders, */
	public Writeable(String prefix, int max_chars, int writemode) {
		this(null, prefix, max_chars, writemode);
	}
	
	
	Writeable(Button father, String prefix, int max_chars, int writemode) {
		super(father);
		
		this.prefix = prefix;
		this.max_chars = max_chars;
		this.writemode = writemode;
		this.setSelectable(true);
		
		for (int i = 0; i < max_chars; i++) {
			content = content + "_";
		}
		
		setName(prefix + " |" + content);
		setSelectColor(Color.LIGHT_GRAY());
	}
	

	public int getchars() {
		return chars;
	}
	
	public int getmaxchars() {
		return max_chars;
	}
	
	@Override
	public void draw(MasterRenderer renderer) {
		super.draw(renderer);
	}
	
	public void write(char ch) {
		
		
		boolean found = false;
		if(writemode == 0) {
			found = true;
		} else {
			for (int i = 0; i < CHAR_SETS[writemode].length; i++) {
				char c2 = CHAR_SETS[writemode][i];
				if(c2 == ch) {
					found = true;
					break;
				}
			}
		}
		
		if(found) {
			if(max_chars == 0) {	
				
				content = content.substring(0, chars) + ch + "|";
				setName(prefix + " " + content);
				chars++;
				
			} else if(chars < max_chars) {
				
				
				content = content.substring(0, chars) + ch;
				
				if(chars+1 < max_chars) content = content + "|";
				for (int i = chars+1; i < max_chars; i++) {
					content = content + "_";
				}		
				setName(prefix + " " + content);
				chars++;
			}
		}		
	}
	
	public void write(String str) {	
		if(max_chars == 0) {
			
			
			content = content.substring(0, chars) + str + "|";
			setName(prefix + " " + content);
			chars+=str.length();
			
		} else if(chars < max_chars) {
			
			
			content = content.substring(0, chars) + str;
			
			if(chars+str.length() < max_chars) content = content + "|";
			for (int i = chars+str.length(); i < max_chars; i++) {
				content = content + "_";
			}		
			setName(prefix + " " + content);
			chars+=str.length();
		}
	}
	
	
	public void remove() {
		
		if(chars > 0) {
			content = content.replace("|", "");
			if(max_chars != 0) {
				content = content.substring(0, chars-1) + "|_" + content.substring(chars);
			} else {
				content = content.substring(0, chars-1) + "|" + content.substring(chars);
			}
			
			
			setName(prefix + " " + content);
			chars--;
		}
	}
	
	public void clear() {
		content="";
		for (int i = 0; i < max_chars; i++) content = content + "_";		
		setName(prefix + " " + content);
		chars = 0;
	}
	
	public String getContent() {
		return content.replace("_", "");
	}
	
//	public void write(String ch) {
//		Writable w = (Writable) hangs.get(hang);
//		
//		String[] names = w.getNames();
//		int count = 0;
//		
//		for(int i = 0; i < names.length; i++) {
//			for(int i2 = 0; i2 < names[i].length(); i2++) {
//				char c = names[i].charAt(i2);
//				if(c == '_') {
//					names[i] = names[i].replaceFirst("_", "" +ch.charAt(count));
//					count++;
//					if(count == ch.length()) {
//						w.setName(names[i]);
//						System.out.println(names[i]);
//						i = names.length;
//						break;
//					}
//				}			
//			}		
//		}
//	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + chars;
		result = prime * result + max_chars;
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
		Writeable other = (Writeable) obj;
		if (chars != other.chars)
			return false;
		if (max_chars != other.max_chars)
			return false;
		return true;
	}
}