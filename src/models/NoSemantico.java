package models;

public class NoSemantico {

    private String declaracao;         
    private String tipo;
    private String nome;      
    private String escopo;
    private String valor; 

    public NoSemantico() {
        this.nome = null;
        this.valor = null;    
        this.tipo = null;
        this.escopo = null;
    }

     public String getDeclaracao() {
        return declaracao;
    }

    public void setDeclaracao(String declaracao) {
        this.declaracao = declaracao;
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
   
    public String getEscopo() {
        return this.escopo;
    }

    public void setEscopo(String escopo) {
        this.escopo = escopo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

}
