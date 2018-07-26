package models;

import java.util.ArrayList;

public class NoFuncao {
    
    private String nome;
    private ArrayList<String> parametros;

    public NoFuncao(String nome) {
        this.nome = nome;
        this.parametros = new ArrayList();
    }

    public NoFuncao() {
        this.parametros = new ArrayList();
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<String> getParametros() {
        return parametros;
    }

    public void addParametro(String parametro) {
        this.parametros.add(parametro);
    }
        
}
