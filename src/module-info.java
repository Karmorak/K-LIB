module lwjgl_lib {
	
	requires transitive org.lwjgl.glfw;
	requires org.lwjgl.openal;
	requires org.lwjgl.opengl;
	requires org.lwjgl;
	requires java.desktop;
	requires java.net.http;
	requires org.lwjgl.par;
	requires jdk.compiler;
	requires org.lwjgl.stb;
	requires org.lwjgl.opengles;
	requires com.github.kwhat.jnativehook;
	requires java.xml;
	requires com.sun.jna;
	requires com.sun.jna.platform;
	requires json.simple;
    requires humble.video.noarch;
    requires xercesImpl;
    requires org.lwjgl.tinyfd;
    requires jdk.jfr;


    exports com.karmorak.lib;
	exports com.karmorak.lib.ui;	
	exports com.karmorak.lib.ui.button;
	exports com.karmorak.lib.ui.button.events;
	exports com.karmorak.lib.animation;
	exports com.karmorak.lib.gamestate;
	exports com.karmorak.lib.engine.objects;
	exports com.karmorak.lib.math;
	exports com.karmorak.lib.engine.terrain;
	exports com.karmorak.lib.engine.io;
	exports com.karmorak.lib.engine.io.images;
	exports com.karmorak.lib.utils;
	exports com.karmorak.lib.utils.file;
	exports com.karmorak.lib.font;
	exports com.karmorak.lib.engine.graphic;
	exports com.karmorak.lib.engine.graphic.roomy;
	exports com.karmorak.lib.engine.graphic.flat;
	exports com.karmorak.lib.engine.graphic.flat.collission;
	exports com.karmorak.lib.engine.audio;
	exports com.karmorak.lib.spotify;
	exports com.karmorak.lib.prototype;
	exports com.karmorak.lib.prototype.server;
	exports com.karmorak.lib.prototype.phsyic;
    exports com.karmorak.lib.font.ownchar;
	exports com.karmorak.lib.font.identifier;
    exports com.karmorak.lib.math.vector;


}