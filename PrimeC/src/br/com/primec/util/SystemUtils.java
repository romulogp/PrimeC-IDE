package br.com.primec.util;

import java.io.IOException;

/**
 *
 * @author Rômulo Göelzer Portolann
 */
public class SystemUtils {
 
    public static void openNotepad(String filePath) {
        String command = "notepad.exe '" + filePath;
        try {
            Runtime r = Runtime.getRuntime();
            r.exec(command);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
}
