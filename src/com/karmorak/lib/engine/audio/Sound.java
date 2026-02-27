package com.karmorak.lib.engine.audio;

import static org.lwjgl.openal.AL11.*;

import com.karmorak.lib.utils.file.FileUtils;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.libc.LibCStdlib;

import static org.lwjgl.system.MemoryStack.*;

import java.io.FileNotFoundException;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class Sound {
	
	private final int bufferId, sourceId;
	
	
	private final String filepath;
	private final int sampleLength, frequency;
	
	private boolean isPlaying = false;
	
	public Sound(String file) {
		this(file, false);
	}
	
    public Sound(String file, boolean loops){
    	this.filepath = file;
    	
    	stackPush();
    	IntBuffer channelsBuffer = stackMallocInt(1);
    	stackPush();
    	IntBuffer sampleRateBuffer = stackMallocInt(1);

		ShortBuffer rawAudioBuffer = STBVorbis.stb_vorbis_decode_filename(filepath, channelsBuffer, sampleRateBuffer);

    	
    	if(rawAudioBuffer == null) {
    		System.err.println("Could not load sound '" + filepath + "'");
    		stackPop();
    		stackPop();
    		bufferId = -1;
    		sourceId = -1;
    		sampleLength = 0;
    		frequency = 0;
    		return;
    	}
    	
    	//Retrieve the extra information that was stored in the buffers by stb
    	int channels = channelsBuffer.get();
    	int sampleRate = sampleRateBuffer.get();
    	//free
    	stackPop();
		stackPop();
		
		//Fine the correct openAL format
		int format = -1;
		if(channels == 1) {
			format = AL_FORMAT_MONO16;
		} else if (channels == 2) {
			format = AL_FORMAT_STEREO16;
		}
		
		bufferId = alGenBuffers();
		alBufferData(bufferId, format, rawAudioBuffer, sampleRate);
		
		
		int sizeInBytes;
		int bits;

		sizeInBytes = alGetBufferi(bufferId, AL_SIZE);
		bits = alGetBufferi(bufferId, AL_BITS);

		sampleLength = sizeInBytes * 8 / (channels * bits);
		
		frequency = alGetBufferi(bufferId, AL_FREQUENCY);

//		float durationInSeconds = (float)sampleLength / (float)frequency;		
//		System.out.println((int) (durationInSeconds/60) + ":" + (int)(durationInSeconds%60));
    	
		//Generate the source
		sourceId = alGenSources();
    	
		
		alSourcei(sourceId, AL_BUFFER, bufferId);
		alSourcei(sourceId, AL_LOOPING, loops ? 1 : 0);
		alSourcei(sourceId, AL_POSITION, 0);
		alSourcef(sourceId, AL_GAIN, 0.3f);
		
		//Free stb raw audio buffer
		LibCStdlib.free(rawAudioBuffer);
    }

    public int getBufferId() {
        return this.bufferId;
    }

    public void destroy() {
    	alDeleteSources(sourceId);
        alDeleteBuffers(this.bufferId);
    }
    
    
    public void play() {
    	int state = alGetSourcei(sourceId, AL_SOURCE_STATE);
    	if(state == AL_STOPPED) {
    		isPlaying = false;
    		alSourcei(sourceId, AL_POSITION, 0);
    	}
    	
    	if(!isPlaying) {

    		alSourcePlay(sourceId);
    		isPlaying = true;
    	}    	
    }
    
    public void pause() {
		alSourcePause(sourceId);
    }
    
    public void resume() {
    	int state = alGetSourcei(sourceId, AL_SOURCE_STATE);
    	if(state == AL_STOPPED) {
    		isPlaying = false;
    		alSourcei(sourceId, AL_POSITION, 0);
    	}
    	
    	if(!isPlaying) { 
    		alSourcePlay(sourceId);
    		isPlaying = true;
    	}  
    }
    
	public void setVolume(float  vol) {
		alSourcef(sourceId, AL_GAIN, vol);	
	}
	
	public float getVolume() {
		return alGetSourcef(sourceId, AL_GAIN);	
	}
    
    public void setDuration(float progress) {		
		alSourcei(sourceId, AL_SAMPLE_OFFSET, (int) (sampleLength*progress));
    }
    
    public float getDurationinSeconeds() {	    	
    	float f = (float)alGetSourcei(sourceId, AL_SAMPLE_OFFSET) / (float)frequency;
    	return f;
    }
    
    public void stop() {
    	if(isPlaying) {
    		alSourceStop(sourceId);
    		isPlaying = false;
    	}    	
    }


    public boolean isPlaying() {
    	int state = alGetSourcei(sourceId, AL_SOURCE_STATE);
    	if(state == AL_STOPPED) {
    		isPlaying = false;
    	}
    	return isPlaying;
    }


    
    
    
}