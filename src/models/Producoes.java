package models;

import java.util.ArrayList;

public class Producoes {

    private String nao_terminal;
    private ArrayList<String> derivacoes;

    public Producoes(String nao_terminal) {
        this.nao_terminal = nao_terminal;
        this.derivacoes = new ArrayList();
    }
    
    public void addDerivacao(String derivacao) {
        this.derivacoes.add(derivacao);
    }

    public String getNaoTerminal() {
        return this.nao_terminal;
    }

    public ArrayList<String> getDerivacoes() {
        return this.derivacoes;
    }
    
}
