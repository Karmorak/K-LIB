package com.karmorak.lib.engine.audio;


import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

import static org.lwjgl.openal.ALC11.*;


import org.lwjgl.openal.AL;

public class AudioMaster {
	
	private static long audioDevice = -1, audioContext;
	
	public static void init() {
	
		
		String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
		
		audioDevice = alcOpenDevice(defaultDeviceName);
		
		int[] attributes = {0};		
		audioContext = alcCreateContext(audioDevice, attributes); 
		alcMakeContextCurrent(audioContext); 
		
		
		ALCCapabilities alcCapabilities = ALC.createCapabilities(audioDevice);
		ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);
		
		
		if(!alCapabilities.OpenAL10) {
			assert false : "Audio Library not Supported!";
		}
		
		// Check for EAX 2.0 support 
//		g_bEAX = alIsExtensionPresent("EAX2.0"); 
//		
//		// Generate Buffers 
//		AL11.alGetError(); // clear error code
//		
//		AL11.alGenBuffers(); 
//		int error = AL11.alGetError();
//		if (error != AL11.AL_NO_ERROR) { 
//			System.out.println("alGenBuffers :" + error); 
//			return; 
//		}
		
	}
	
	
	public static void destroy() {
		alcDestroyContext(audioContext);
		alcCloseDevice(audioDevice);
	}

}
