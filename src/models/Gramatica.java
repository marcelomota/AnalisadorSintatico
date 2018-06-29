package models;

import java.util.ArrayList;
import java.util.Iterator;

public class Gramatica {

    private ArrayList<Producoes> gramatica;
    
    
    public Gramatica(String texto_gramatica) {
        this.gramatica = new ArrayList();
        this.carregar(texto_gramatica);
        //this.printGramatica();
    }
    
    private void carregar(String texto_gramatica) {
            
        int i = 0;        
        char atual;                
        while(i < texto_gramatica.length()) {
            // Obtem o caractere da posicao atual.
            atual = texto_gramatica.charAt(i);
            String linha = "";
            while(atual != '\n') {                 
                linha += atual;
                i++;
                atual = texto_gramatica.charAt(i);
            }            
            String[] aux = linha.split("::=");  
            Producoes prod = new Producoes(aux[0]);
            if(aux[1].contains("|")) {
                
                String[] subString = aux[1].split("(\\|)");
                for(int j=0; j<subString.length; j++) {                   
                    
                    if(subString[j].trim().equals("'vazio'")) {
                        
                        prod.addDerivacao("'£'");
                    } else if(subString[j].contains("'~'")) {
                        prod.addDerivacao("'||' <LogicalAndExpr> <LogicalOrExpr 1>");                        
                    } else {
                        
                        prod.addDerivacao(subString[j].trim());
                    }                    
                }
            } else {
                prod.addDerivacao(aux[1].trim());
            }                        
            this.gramatica.add(prod);
            i++;
        }         
    }
    
    public ArrayList<Producoes> getGramatica() {
        return gramatica;
    }
    
     public void printGramatica() {
        
        Iterator it = this.gramatica.iterator();
        while(it.hasNext()) {
            Producoes atual = (Producoes) it.next();
            System.out.println("Nao-Terminal: "+atual.getNaoTerminal());
            ArrayList<String> list = atual.getDerivacoes();
            list.forEach((derivacao) -> {
                System.out.println("    Derivacao: "+derivacao);
            });
        }
    }  
}
