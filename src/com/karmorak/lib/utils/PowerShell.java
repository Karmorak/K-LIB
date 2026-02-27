package com.karmorak.lib.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class PowerShell {
	
	// Privater Konstruktor verhindert Instanziierung
    private PowerShell() {}
		
	
	public static String execute(String command) {
		// ProcessBuilder mit den notwendigen Flags
		ProcessBuilder processBuilder = new ProcessBuilder(
	            "powershell.exe",
	            "-NoProfile",
	            "-ExecutionPolicy", "Bypass",
	            "-Command",
	            command
	        );
		
        // Fehler-Stream zum Output-Stream leiten, um alles in einem Rutsch zu lesen
        processBuilder.redirectErrorStream(true);
        
        try {
            Process process = processBuilder.start();

            // Liest den gesamten Output-Stream direkt in einen String
            String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            // Warten, bis der Prozess fertig ist
            int exitCode = process.waitFor();
            
            if (exitCode != 0) {
                return "Fehler beim Ausführen (Exit Code " + exitCode + "): " + output;
            }

            return output.trim(); // trim() entfernt unnötige Leerzeilen am Ende

        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt(); // Best Practice bei InterruptedException
            return "Exception aufgetreten: " + e.getMessage();
        }
	}

	
	public static String execute(InputStream scriptStream) {
        if (scriptStream == null) return "Fehler: InputStream ist null";

        Path tempScript = null;
        try {
            // 1. Skript in temporäre Datei kopieren
            tempScript = Files.createTempFile("simple_ps_", ".ps1");
            Files.copy(scriptStream, tempScript, StandardCopyOption.REPLACE_EXISTING);

            // 2. Prozess starten
            Process process = new ProcessBuilder(
                "powershell.exe", "-NoProfile", "-ExecutionPolicy", "Bypass",
                "-File", tempScript.toAbsolutePath().toString()
            ).redirectErrorStream(true).start();

            // 3. Ergebnis lesen (Java 21 readAllBytes)
            String result = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            process.waitFor();
            
            return result.trim();

        } catch (Exception e) {
            return "Fehler: " + e.getMessage();
        } finally {
            // 4. Temporäre Datei löschen
            if (tempScript != null) {
                try { Files.deleteIfExists(tempScript); } catch (IOException ignored) {}
            }
        }
    }
	
}
