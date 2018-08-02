package models;

import models.nos.NoEscopo;
import java.util.ArrayList;

public class PilhaEscopo {
 
    private ArrayList<NoEscopo> pilhaEscopo;

    public PilhaEscopo() {
        this.pilhaEscopo = new ArrayList();
        this.addEscopo("Global", "Global");
    }

    public void addEscopo(String palavraReservada, String identificador) {
        this.pilhaEscopo.add(new NoEscopo(palavraReservada, identificador));
    }
    
    public int getLastIndex() {
        return (this.pilhaEscopo.size()-1);
    }
    
    public String getLastPalavraReservada() {
        return this.pilhaEscopo.get(this.getLastIndex()).getPalavraReservada();
    }
    
    public String getLastIdentificador() {
        return this.pilhaEscopo.get(this.getLastIndex()).getIdentificador();
    }
    
    public void removerLast() {        
        if(!this.getLastIdentificador().equals("Global")) {
            this.pilhaEscopo.remove(this.getLastIndex());
        }
    }
   
}
