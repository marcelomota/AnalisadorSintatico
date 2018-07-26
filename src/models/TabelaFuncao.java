package models;

import java.util.ArrayList;

public class TabelaFuncao {
    
    private ArrayList<NoFuncao> funcoes;

    public TabelaFuncao() {
        this.funcoes = new ArrayList();
    }
    
    public ArrayList<NoFuncao> getFuncoes() {
        return funcoes;
    }

    public void addFuncao(NoFuncao noFuncao) {
        this.funcoes.add(noFuncao);
    }
    
    
}
