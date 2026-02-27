package com.karmorak.lib;

import java.util.ArrayList;

public class Resolutions {

	public static ArrayList<String> z3z2;		
	public static ArrayList<String> z4z3;		
	public static ArrayList<String> z8z5;		
	public static ArrayList<String> z16z9;		
	public static ArrayList<String> z21z9;		
	public static ArrayList<String> other;
	
	
	public static ArrayList<String> aspectratios;
	public static ArrayList<ArrayList<String>> resolutions;
	
	
	
	public static void initRes() {
		z3z2 = new ArrayList<String>();
		z4z3 = new ArrayList<String>();
		z8z5 = new ArrayList<String>();
		z16z9 = new ArrayList<String>();
		z21z9 = new ArrayList<String>();
		other = new ArrayList<String>();
		resolutions = new ArrayList<>();
		aspectratios = new ArrayList<>();	
		
		//3:2
		String resoulution_0 = "480:320"; // 3:2
		String resoulution_4 = "600:400"; // 3:2
		String resoulution_22 = "960:640"; // 3:2
		z3z2.add(resoulution_0);
		z3z2.add(resoulution_4);
		z3z2.add(resoulution_22);
		
	// 4:3
		String resoulution_1 = "480:360"; // 4:3
		String resoulution_5 = "640:480"; // 4:3
//		String resoulution_6 = "720:348"; // 4:3
		String resoulution_7 = "720:350"; // 4:3
		String resoulution_8 = "720:364"; // 4:3
		String resoulution_11 = "720:540"; // 4:3
		String resoulution_17 = "768:576"; // 4:3
		String resoulution_18 = "800:600"; // 4:3
		String resoulution_19 = "832:624"; // 4:3
		String resoulution_23 = "960:720"; // 4:3
		String resoulution_26 = "1024:768"; // 4:3
		String resoulution_28 = "1152:864"; // 4:3
		String resoulution_31 = "1280:960"; // 4:3
		String resoulution_32 = "1200:900"; // 4:3
		String resoulution_39= "1400:1050"; // 4:3
		String resoulution_47= "1920:1440"; // 4:3
		String resoulution_48= "2048:1563"; // 4:3
		String resoulution_54= "2800:2100"; // 4:3
		String resoulution_55= "3200:2400"; // 4:3
		String resoulution_62= "4096:3072"; // 4:3
		String resoulution_66= "6400:4800"; // 4:3
		z4z3.add(resoulution_1);
		z4z3.add(resoulution_5);
		z4z3.add(resoulution_7);
		z4z3.add(resoulution_8);
		z4z3.add(resoulution_11);
		z4z3.add(resoulution_17);
		z4z3.add(resoulution_18);
		z4z3.add(resoulution_19);
		z4z3.add(resoulution_23);
		z4z3.add(resoulution_26);
		z4z3.add(resoulution_28);
		z4z3.add(resoulution_31);
		z4z3.add(resoulution_32);
		z4z3.add(resoulution_39);
		z4z3.add(resoulution_47);
		z4z3.add(resoulution_48);
		z4z3.add(resoulution_54);
		z4z3.add(resoulution_55);
		z4z3.add(resoulution_62);
		z4z3.add(resoulution_66);
	// 16:9
		String resoulution_3 = "640:360"; // 16:9	
		String resoulution_13 = "848:480"; // 16:9
		String resoulution_14 = "852:480"; // 16:9
		String resoulution_16 = "858:484"; // 16:9
		String resoulution_20 = "960:540"; // 16:9
		String resoulution_21 = "964:544"; // 16:9
		String resoulution_24 = "1024:576"; // 16:9
		String resoulution_25 = "1024:600"; // 16:9
		String resoulution_27 = "1072:600"; // 16:9
		String resoulution_29 = "1280:720"; // 16:9
		String resoulution_33 = "1360:768"; // 16:9
		String resoulution_36 = "1376:768"; // 16:9
		String resoulution_41= "1600:900"; // 16:9
		String resoulution_43= "1680:1050"; // 16:9
		String resoulution_44= "1920:1080"; // 16:9
		String resoulution_46= "2048:1152"; // 16:9
		String resoulution_50= "2560:1440"; // 16:9
		String resoulution_53= "3200:1800"; // 16:9
		String resoulution_58= "3840:2160"; // 16:9
		String resoulution_60= "4096:2304"; // 16:9
		String resoulution_61= "4096:2304"; // 16:9
		String resoulution_63= "5120:2880"; // 16:9
		String resoulution_67= "7680:4320"; // 16:9
		String resoulution_69= "15360:8640"; // 16:9
		z16z9.add(resoulution_3);
		z16z9.add(resoulution_13);
		z16z9.add(resoulution_14);
		z16z9.add(resoulution_16);
		z16z9.add(resoulution_20);
		z16z9.add(resoulution_21);
		z16z9.add(resoulution_24);
		z16z9.add(resoulution_25);
		z16z9.add(resoulution_27);
		z16z9.add(resoulution_29);
		z16z9.add(resoulution_33);
		z16z9.add(resoulution_36);
		z16z9.add(resoulution_41);
		z16z9.add(resoulution_43);
		z16z9.add(resoulution_44);
		z16z9.add(resoulution_46);
		z16z9.add(resoulution_50);
		z16z9.add(resoulution_53);
		z16z9.add(resoulution_58);
		z16z9.add(resoulution_60);
		z16z9.add(resoulution_61);
		z16z9.add(resoulution_63);
		z16z9.add(resoulution_67);
		z16z9.add(resoulution_69);
		
		
	// 8:5
		String resoulution_30 = "1280:800"; // 8:5
		String resoulution_37 = "1440:900"; // 8:5
		String resoulution_45= "1920:2000"; // 8:5
		String resoulution_51= "2560:1600"; // 8:5
		String resoulution_59= "3840:2400"; // 8:5
		String resoulution_68= "7680:4800"; // 8:5
		z8z5.add(resoulution_30);
		z8z5.add(resoulution_37);
		z8z5.add(resoulution_45);
		z8z5.add(resoulution_51);
		z8z5.add(resoulution_59);
		z8z5.add(resoulution_68);
	// 21:9
		String resoulution_49= "2560:1080"; // 21:9	
		String resoulution_56= "3440:1440"; // 21:9
		String resoulution_57= "3840:1600"; // 21:9	
		z21z9.add(resoulution_49);
		z21z9.add(resoulution_56);
		z21z9.add(resoulution_57);
	// other		
//		String resoulution_2 = "640:350";		
		String resoulution_9 = "720:400"; // 9:5
		String resoulution_10 = "720:480"; // 3:2		
		String resoulution_12 = "800:480"; // 5:3
		String resoulution_15 = "864:480"; // 9:5
		String resoulution_34 = "1400:900"; // 14:9
		String resoulution_35 = "1366:768"; // 14:9		
		String resoulution_38 = "1440:960"; // 3:2
		String resoulution_42= "1600:1024"; // 25:16	
		String resoulution_52= "2560:2048"; // 5:4	
		String resoulution_64= "5120:4096"; // 5:4
		String resoulution_65= "6400:4096"; // 25:16
		other.add(resoulution_9);
		other.add(resoulution_10);
		other.add(resoulution_12);
		other.add(resoulution_15);
		other.add(resoulution_34);
		other.add(resoulution_35);
		other.add(resoulution_38);
		other.add(resoulution_42);
		other.add(resoulution_52);
		other.add(resoulution_64);
		other.add(resoulution_65);
		
		
		aspectratios.add("4:3");
		resolutions.add(Resolutions.z4z3);
		aspectratios.add("3:2");
		resolutions.add(Resolutions.z3z2);
		aspectratios.add("8:5");
		resolutions.add(Resolutions.z8z5);
		aspectratios.add("16:9");
		resolutions.add(Resolutions.z16z9);
		aspectratios.add("21:9");
		resolutions.add(Resolutions.z21z9);
		aspectratios.add("other");
		resolutions.add(Resolutions.other);
		
	}
}
