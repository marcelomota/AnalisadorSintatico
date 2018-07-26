package persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class ManipulaArquivo {
    
    /**
     * Obtem todos os arquivos .txt da pasta Arquivos.
     * @return 
     */
    public String[] getArquivos() {
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().endsWith(".txt");
            }
        };
        File dir = new File("Arquivos");
        File[] files = dir.listFiles(filter); 
        String[] arquivos = new String[files.length];
        for(int i=0; i<files.length; i++) {
            
            arquivos[i] = files[i].toString();
        }
        return arquivos;
    }
    
    /**
     * Obtem o conteudo do Arquivo.
     * @param nomeArquivo String
     * @return 
     */
    public String lerArquivo(String nomeArquivo) {
        
        try {
            
            File file = new File(nomeArquivo);
            InputStreamReader r = new InputStreamReader(new FileInputStream(file));       
            
            FileReader fileReader = new FileReader(nomeArquivo);
            BufferedReader br = new BufferedReader(fileReader);      
                        
            String texto = ""; 
            String linha = br.readLine();           
            while(linha != null){                
                texto += linha+"\n"; 
                linha = br.readLine();
            }   
            
            br.close();            
            return texto;
        } catch(IOException e) {
            return e.getMessage();
        }        
    } 
    
    /**
     * Adiciona um conteudo a um arquivo.
     * @param errosLexicos String
     * @param errosSintaticos String
     * @param errosSemanticos String
     * @param nomeArquivo String
     * @return 
     */
    public boolean salvaArquivo(String errosLexicos, String errosSintaticos, String errosSemanticos, String nomeArquivo) {
        
        try{
            File file = new File(nomeArquivo);       
            if(!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file);        
            BufferedWriter bw = new BufferedWriter(fileWriter);
            
            bw.write("** ARQUIVO COMPILADO COM SUCESSO! **");
            bw.newLine();
                        
            if(!errosLexicos.isEmpty()) {
                bw.newLine();
                bw.write("--- ERROS LÉXICOS ---");
                bw.newLine ();
                for(int i=0; i<errosLexicos.length(); i++){
                    if(errosLexicos.charAt(i) == '\n'){
                        bw.newLine ();
                    } else {
                        bw.write(errosLexicos.charAt(i));
                    }                
                }
            } else {
                bw.newLine();
                bw.write("--- ERROS LÉXICOS ---");
                bw.newLine ();
                bw.write("Não foram encontrados erros Léxicos!");
                bw.newLine ();
            }
            
            if(!errosSintaticos.isEmpty()) {
                bw.newLine ();
                bw.write("--- ERROS SINTÁTICOS ---");
                bw.newLine ();
                for(int i=0; i<errosSintaticos.length(); i++){
                    if(errosSintaticos.charAt(i) == '\n'){
                        bw.newLine ();
                    } else {
                        bw.write(errosSintaticos.charAt(i));
                    }                
                }
            } else {
                bw.newLine ();
                bw.write("--- ERROS SINTÁTICOS ---");
                bw.newLine ();
                bw.write("Não foram encontrados erros Sintáticos!");
                bw.newLine ();
            }
                        
            if(!errosSemanticos.isEmpty()) {
                bw.newLine ();
                bw.write("--- ERROS SEMÂNTICOS ---");
                bw.newLine ();
                for(int i=0; i<errosSemanticos.length(); i++){
                    if(errosSemanticos.charAt(i) == '\n'){
                        bw.newLine ();
                    } else {
                        bw.write(errosSemanticos.charAt(i));
                    }                
                }
            } else {
                bw.newLine ();
                bw.write("--- ERROS SEMÂNTICOS ---");
                bw.newLine ();
                bw.write("Não foram encontrados erros Semânticos!");
                bw.newLine ();
            }
                        
            bw.close();
            return true;
        } catch(IOException e) {
            e.getMessage();
            return false;
        }
    }
    
}
