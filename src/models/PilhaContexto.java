package models;

import java.util.ArrayList;

public class PilhaContexto {
    
    private ArrayList<String> pilhaContexto;

    public PilhaContexto() {
        this.pilhaContexto = new ArrayList();
    }

    public int getLastIndex() {
        return (this.pilhaContexto.size()-1);
    }
    
    public void addContexto(String contexto) {
        this.pilhaContexto.add(contexto);
    }
    
    public String getLastContexto() {
        if(this.pilhaContexto.isEmpty())
            return "";
        return this.pilhaContexto.get(this.getLastIndex());
    }
    
    public void removerLast() {
        if(!this.pilhaContexto.isEmpty()) {
            this.pilhaContexto.remove(this.getLastIndex());
        }
    }
   
}
