package br.com.primec.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 
 * @author Rômulo Göelzer Portolann
 */
public class FileSaveUtil {

    private String name;
    private String path;

    /**
     * Gera um arquivo com o nome informado dentro da pasta principal do programa.
     * @param name nome do arquivo
     */
    public FileSaveUtil(String name) {
        this(name, new File("").getPath());
    }
    
    /**
     * Gera um arquivo com o nome informado dentro do caminho especificado
     * @param name nome do arquivo
     * @param path caminho do arquivo
     */
    public FileSaveUtil(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public void save(String content) {
        String fullPath = path + name;
        
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fullPath, false);
            fos.write(content.getBytes()); //Escreve no objeto
            fos.flush(); //Persiste no arquivo
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        } finally {
            //try catch para tratar exclusivamente o método close.
            try {
                //Fecha o arquivo (esta ação É necessária e deverá ser 
                //executada não importa que exceções ocorram)
                fos.close();
            } catch (Exception ex) {
                //Erro no fechamento do arquivo. Não há porque subir esta exceção... Apenas trate-a
            }
        }
    }
    
    public String getFileFullPath() {
        return path + name;
    }
    
}
