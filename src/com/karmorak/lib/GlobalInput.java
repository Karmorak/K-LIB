package com.karmorak.lib;

import java.util.ArrayList;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.karmorak.lib.gamestate.GSM;

public class GlobalInput implements NativeKeyListener {
	
	public static ArrayList<Integer> keys = new ArrayList<Integer>();
	
	
	
	public void nativeKeyPressed(NativeKeyEvent e) {
//		System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
		
//		if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
//			try {
//				GlobalScreen.unregisterNativeHook();
//			} catch (NativeHookException nativeHookException) {
//				nativeHookException.printStackTrace();
//				}
//			}
		
		
		keys.add((Integer)e.getKeyCode());
		GSM.globalkeyDown(e);
	}

		public void nativeKeyTyped(NativeKeyEvent e) {
//			System.out.println("Key Typed: " + e.getKeyText(e.getKeyCode()));			
		}
	
		public void nativeKeyReleased(NativeKeyEvent e) {
//			System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
			keys.remove((Integer)e.getKeyCode());
			GSM.globalkeyUp(e);
		}

	
		public static boolean isKey(int keyCode) {
			if(keys.contains(keyCode)) return true;
			return false;
		}
		
	}
