package models;

public class NoParametro {

    private String tipo;
    private String nome;

    public NoParametro() {
        this.tipo = "";
        this.nome = "";        
    }

    public NoParametro(String tipo) {
        this.tipo = tipo;
        this.nome = ""; 
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
   
}
