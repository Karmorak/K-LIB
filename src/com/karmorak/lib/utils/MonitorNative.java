package com.karmorak.lib.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import com.karmorak.lib.KLIB;


public class MonitorNative {
	
    // DLL laden
    static {
    	loadNativeLibrary();
    }
    
    private static void loadNativeLibrary() {
        try {
            // Pfad innerhalb des Jar
            String dllName = "NativeMonitor.dll";
            InputStream in = KLIB.class.getClassLoader().getResourceAsStream(dllName);
            

            if (in == null) throw new RuntimeException("DLL nicht im Jar gefunden!");

            // Temp-File erzeugen
            File temp = File.createTempFile("NativeMonitor", ".dll");
            temp.deleteOnExit(); // wird beim Beenden gelöscht

            // Kopieren
            Files.copy(in, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Laden
            System.load(temp.getAbsolutePath());

        } catch (IOException e) {
            throw new RuntimeException("Failed to load native library", e);
        }
    }    

    public static native String getMonitorInfoJson();

}
