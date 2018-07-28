package models;

import java.util.ArrayList;

public class Escopo {
 
    private ArrayList<NoEscopo> listaEscopo;

    public Escopo() {
        this.listaEscopo = new ArrayList();
    }

    public ArrayList<NoEscopo> getListaEscopo() {
        return listaEscopo;
    }

    public void addEscopo(String nome, String valor) {
        this.listaEscopo.add(new NoEscopo(nome, valor));
    }
    
    public int getLastIndex() {
        return (this.listaEscopo.size()-1);
    }
    
    public String getLastNomeEscopo() {
        return this.listaEscopo.get(this.getLastIndex()).getNome();
    }
    
    public String getLastValorEscopo() {
        return this.listaEscopo.get(this.getLastIndex()).getValor();
    }
    
    public void removeLast() {
        if(!this.listaEscopo.get(this.getLastIndex()).getNome().equals("Global")) {
         
            this.listaEscopo.remove(this.getLastIndex());
        }        
    }
   
}
