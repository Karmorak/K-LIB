package com.karmorak.lib.prototype;

import com.karmorak.lib.math.Vector2;
import io.humble.video.*;
import io.humble.video.awt.MediaPictureConverter;
import io.humble.video.awt.MediaPictureConverterFactory;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Video {


	final String PATH;
	public Vector2 size;
	long duration;
	
	long streamStartTime;
	int videoStreamId;
	Demuxer demuxer;
	Decoder videoDecoder;
	MediaPictureConverter converter;
	MediaPicture picture;
	
	public static BufferedImage image;
	
	public Video(String path) {
		this.PATH = path;
	}


	public void loadVideo() throws InterruptedException, IOException {
//	     Start by creating a container object, in this case a demuxer since
//	     we are reading, to get video data from.
	    demuxer = Demuxer.make();

//	    Open the demuxer with the filename passed on.
	    demuxer.open(PATH, null, false, true, null, null);

	    /*
	     * Query how many streams the call to open found
	     */
	    int numStreams = demuxer.getNumStreams();

	    /*
	     * Iterate through the streams to find the first video stream
	     */
	    videoStreamId = -1;
	    streamStartTime = Global.NO_PTS;
	    videoDecoder = null;
	    for(int i = 0; i < numStreams; i++)
	    {
	      final DemuxerStream stream = demuxer.getStream(i);
	      streamStartTime = stream.getStartTime();
	      final Decoder decoder = stream.getDecoder();
	      if (decoder != null && decoder.getCodecType() == MediaDescriptor.Type.MEDIA_VIDEO) {
	    	 size = new Vector2( decoder.getHeight() , decoder.getWidth());

	        videoStreamId = i;
	        videoDecoder = decoder;
	        // stop at the first one.
	        break;
	      }
	    }
	    if (videoStreamId == -1) {
			throw new RuntimeException("could not find video stream in container: "+ PATH);
		}

	    /*
	     * Now we have found the audio stream in this file.  Let's open up our decoder so it can
	     * do work.
	     */
	    videoDecoder.open(null, null);

	    picture = MediaPicture.make(
	        videoDecoder.getWidth(),
	        videoDecoder.getHeight(),
	        videoDecoder.getPixelFormat());



	    /** A converter object we'll use to convert the picture in the video to a BGR_24 format that Java Swing
	     * can work with. You can still access the data directly in the MediaPicture if you prefer, but this
	     * abstracts away from this demo most of that byte-conversion work. Go read the source code for the
	     * converters if you're a glutton for punishment.
	     */
	    converter = MediaPictureConverterFactory.createConverter(MediaPictureConverterFactory.HUMBLE_BGR_24, picture);
	}
	
	public void playVideo() throws InterruptedException, IOException {
	    image = null;

	    /**
	     * This is the Window we will display in. See the code for this if you're curious, but to keep this demo clean
	     * we're 'simplifying' Java AWT UI updating code. This method just creates a single window on the UI thread, and blocks
	     * until it is displayed.
	     */
//	    final ImageFrame window = ImageFrame.make();
//	    if (window == null) {
//	      throw new RuntimeException("Attempting this demo on a headless machine, and that will not work. Sad day for you.");
//	    }

	    /**
	     * Media playback, like comedy, is all about timing. Here we're going to introduce <b>very very basic</b>
	     * timing. This code is deliberately kept simple (i.e. doesn't worry about A/V drift, garbage collection pause time, etc.)
	     * because that will quickly make things more complicated.
	     *
	     * But the basic idea is there are two clocks:
	     * <ul>
	     * <li>Player Clock: The time that the player sees (relative to the system clock).</li>
	     * <li>Stream Clock: Each stream has its own clock, and the ticks are measured in units of time-bases</li>
	     * </ul>
	     *
	     * And we need to convert between the two units of time. Each MediaPicture and MediaAudio object have associated
	     * time stamps, and much of the complexity in video players goes into making sure the right picture (or sound) is
	     * seen (or heard) at the right time. This is actually very tricky and many folks get it wrong -- watch enough
	     * Netflix and you'll see what I mean -- audio and video slightly out of sync. But for this demo, we're erring for
	     * 'simplicity' of code, not correctness. It is beyond the scope of this demo to make a full fledged video player.
	     */

	    // Calculate the time BEFORE we start playing.
	    long systemStartTime = System.nanoTime();
	    // Set units for the system time, which because we used System.nanoTime will be in nanoseconds.
	    final Rational systemTimeBase = Rational.make(1, 1000000000);
	    // All the MediaPicture objects decoded from the videoDecoder will share this timebase.
	    final Rational streamTimebase = videoDecoder.getTimeBase();

	    /**
	     * Now, we start walking through the container looking at each packet. This
	     * is a decoding loop, and as you work with Humble you'll write a lot
	     * of these.
	     *
	     * Notice how in this loop we reuse all of our objects to avoid
	     * reallocating them. Each call to Humble resets objects to avoid
	     * unnecessary reallocation.
	     */
	    final MediaPacket packet = MediaPacket.make();
	    while(demuxer.read(packet) >= 0) {
	      /**
	       * Now we have a packet, let's see if it belongs to our video stream
	       */
	      if (packet.getStreamIndex() == videoStreamId) {
	    	  
	    	 System.out.println(" 1" + packet.getTimeStamp());
	    	 System.out.println(" 2" +  packet.getDuration());
	    	 System.out.println(" 3" +  packet.getDts());
	    	  
	        /**
	         * A packet can actually contain multiple sets of samples (or frames of samples
	         * in decoding speak).  So, we may need to call decode  multiple
	         * times at different offsets in the packet's data.  We capture that here.
	         */
	        int offset = 0;
	        int bytesRead = 0;
	        do {
	          bytesRead += videoDecoder.decode(picture, packet, offset);

	          if (picture.isComplete()) {
	            image = displayVideoAtCorrectTime(streamStartTime, picture, converter, image, systemStartTime, systemTimeBase, streamTimebase);

//	            Thread.sleep(42);
	          }
	          offset += bytesRead;
	        } while (offset < packet.getSize());
	      }
	    }

	    // Some video decoders (especially advanced ones) will cache
	    // video data before they begin decoding, so when you are done you need
	    // to flush them. The convention to flush Encoders or Decoders in Humble Video
	    // is to keep passing in null until incomplete samples or packets are returned.
	    do {
	      videoDecoder.decode(picture, null, 0);


	      if (picture.isComplete()) {
	        image = displayVideoAtCorrectTime(streamStartTime, picture, converter, image, systemStartTime, systemTimeBase, streamTimebase);
	      }
	    } while (picture.isComplete());

	    // It is good practice to close demuxers when you're done to free
	    // up file handles. Humble will EVENTUALLY detect if nothing else
	    // references this demuxer and close it then, but get in the habit
	    // of cleaning up after yourself, and your future girlfriend/boyfriend
	    // will appreciate it.
	    demuxer.close();

	    // similar with the demuxer, for the windowing system, clean up after yourself.
//	    window.dispose();
	}
	
	
	


	private void printMetaInformation(Demuxer demuxer) throws InterruptedException, IOException {
	    // There are a few other key pieces of information that are interesting for
	    // most containers; The duration, the starting time, and the estimated bit-rate.
//	     This code extracts all three.
	    final String formattedDuration = formatTimeStamp(demuxer.getDuration());
	    System.out.printf("Duration: %s, start: %f, bitrate: %d kb/s\n",
	        formattedDuration,
	        demuxer.getStartTime() == Global.NO_PTS ? 0 : demuxer.getStartTime() / 1000000.0, demuxer.getBitRate()/1000);
	    // Many programs that make containers, such as iMovie or Adobe Elements, will
	    // insert meta-data about the container. Here we extract that meta data and print it.
	    KeyValueBag metadata = demuxer.getMetaData();
	    System.out.println("MetaData:");
	    for(String key: metadata.getKeys()) {
			System.out.printf("  %s: %s\n", key, metadata.getValue(key));
		}
	    
	    int numStreams = demuxer.getNumStreams();
	    // Finally, a container consists of several different independent streams of
	    // data called Streams. In Humble there are two objects that represent streams:
	    // DemuxerStream (when you are reading) and MuxerStreams (when you are writing).

	    // First find the number of streams in this container.

	    // Now, let's iterate through each of them.
	    for (int i = 0; i < numStreams; i++) {
	      DemuxerStream stream = demuxer.getStream(i);
	
	      metadata = stream.getMetaData();
	      // Language is usually embedded as metadata in a stream.
	      final String language = metadata.getValue("language");

	      // We will only be able to make a decoder for streams we can actually
	      // decode, so the caller should check for null.
	      Decoder d = stream.getDecoder();
	
	      System.out.printf(" Stream #0.%1$d (%2$s): %3$s\n", i, language, d != null ? d.toString() : "unknown coder");
	      System.out.println("  Metadata:");
	      for(String key: metadata.getKeys())
	        System.out.printf("    %s: %s\n", key, metadata.getValue(key));
	    }
	}


	  /**
	   * Takes the video picture and displays it at the right time.
	   */
	  private static BufferedImage displayVideoAtCorrectTime(long streamStartTime, final MediaPicture picture, final MediaPictureConverter converter, BufferedImage image, long systemStartTime,  final Rational systemTimeBase, final Rational streamTimebase)
	      throws InterruptedException {
	    long streamTimestamp = picture.getTimeStamp();
	    // convert streamTimestamp into system units (i.e. nano-seconds)
	    streamTimestamp = systemTimeBase.rescale(streamTimestamp-streamStartTime, streamTimebase);
	    // get the current clock time, with our most accurate clock
	    long systemTimestamp = System.nanoTime();
	    // loop in a sleeping loop until we're within 1 ms of the time for that video frame.
	    // a real video player needs to be much more sophisticated than this.
	    while (streamTimestamp >= (systemTimestamp - systemStartTime + 1000000)) {
	      Thread.sleep(1);
	      systemTimestamp = System.nanoTime();
	    }
	    // finally, convert the image from Humble format into Java images.
	    image = converter.toImage(image, picture);
	    // And ask the UI thread to repaint with the new image.
//	    window.setImage(image);
	    return image;
	  }
	
	private static String formatTimeStamp(long duration) {
		if (duration == Global.NO_PTS) {
			return "00:00:00.00";
		}
		double d = 1.0 * duration / Global.DEFAULT_PTS_PER_SECOND;
		int hours = (int) (d / (60 * 60));
		int mins = (int) ((d - hours * 60 * 60) / 60);
		int secs = (int) (d - hours * 60 * 60 - mins * 60);
		int subsecs = (int) ((d - (hours * 60 * 60.0 + mins * 60.0 + secs)) * 100.0);
		return String.format("%1$02d:%2$02d:%3$02d.%4$02d", hours, mins, secs, subsecs);
	}
	
	

}
