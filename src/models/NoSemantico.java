package models;

public class NoSemantico {

    private String declaracao;         
    private String tipo;
    private String nome;      
    private String nomeEscopo;
    private String valorEscopo;
    private String valor; 
    protected boolean chave;

    public NoSemantico() {
        this.nome = "";
        this.valor = "";    
        this.tipo = "";
        this.nomeEscopo = "";
        this.valorEscopo = "";
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
       
    public String getNomeEscopo() {
        return this.nomeEscopo;
    }

    public void setNomeEscopo(String nomeEscopo) {
        this.nomeEscopo = nomeEscopo;
    }
    
    public String getValorEscopo() {
        return this.valorEscopo;
    }

    public void setValorEscopo(String valorEscopo) {
        this.valorEscopo = valorEscopo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
    
    public void setValor2(String valor) {
        
        if(this.chave) {
            this.valor += ", "+valor;
            this.chave = false;
        } else {
            this.valor += " "+valor;
            this.chave = true;
        }
        
    }
    
    public void setValor3(String valor) {
        
        this.valor += " "+valor;
    }

}
