package com.karmorak.lib.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import com.karmorak.lib.KLIB;

public class MonitorNative {

    private static final MethodHandle getMonitorInfoJsonHandle;

    // DLL laden
    static {
        try {
            // 1. DLL laden (aus Pfad oder über deine Entpack-Logik)
            // Lade beispielsweise über deinen Temp-Ordner-Loader oder direkt
            SymbolLookup lib = loadNativeLibrary();
            Linker linker = Linker.nativeLinker();

            // 2. C-Funktion getMonitorInfoJson in der DLL suchen
            MemorySegment functionAddress = lib.find("getMonitorInfoJson")
                    .orElseThrow(() -> new UnsatisfiedLinkError("Funktion 'getMonitorInfoJson' in DLL nicht gefunden."));

            // 3. Signatur definieren: const char* getMonitorInfoJson()
            // ValueLayout.ADDRESS entspricht einem C-Pointer (char*)
            FunctionDescriptor descriptor = FunctionDescriptor.of(ValueLayout.ADDRESS);

            // 4. MethodHandle für Aufruf erzeugen
            getMonitorInfoJsonHandle = linker.downcallHandle(functionAddress, descriptor);

        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Initialisieren der Native-Schnittstelle", e);
        }
    }

    private static SymbolLookup loadNativeLibrary() {
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
            return SymbolLookup.libraryLookup(temp.toPath(), Arena.global());

        } catch (IOException e) {
            throw new RuntimeException("Failed to load native library", e);
        }
    }

    public static String getMonitorInfoJson() {
        try {
            // Funktion aufrufen -> Gibt MemorySegment (Pointer auf char*) zurück
            MemorySegment resultPointer = (MemorySegment) getMonitorInfoJsonHandle.invokeExact();

            if (resultPointer.equals(MemorySegment.NULL)) {
                return "[]";
            }


            // Den C-String (UTF-8) auslesen und in Java String konvertieren
            return resultPointer.reinterpret(Long.MAX_VALUE, Arena.global(), null).getString(0, StandardCharsets.UTF_8);

        } catch (Throwable e) {
            e.printStackTrace();
            return "[]";
        }
    }
}