package models;

public class NoSemantico {

    private int id;
    private String declaracao;  
    private String linhaDeclaracao;
    private String tipo;
    private String nome;      
    private String nomeEscopo;
    private String valorEscopo;
    private String valor; 
    private int idSobrecarga;
    private boolean sobrescrita;
    protected boolean chave;

    public NoSemantico() {
        this.id = AnalisadorSemantico.getId();
        this.nome = "";
        this.valor = "";    
        this.tipo = "";
        this.nomeEscopo = "";
        this.valorEscopo = "";
        this.idSobrecarga = 0;
    }

    public int getId() {
        return id;
    }
    
    public String getDeclaracao() {
        return declaracao;
    }

    public void setDeclaracao(String declaracao) {
        this.declaracao = declaracao;
    }      

    public String getLinhaDeclaracao() {
        return linhaDeclaracao;
    }

    public void setLinhaDeclaracao(String linhaDeclaracao) {
        this.linhaDeclaracao = linhaDeclaracao;
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

    public int getIdSobrecarga() {
        return idSobrecarga;
    }

    public void setIdSobrecarga(int idSobrecarga) {
        this.idSobrecarga = idSobrecarga;
    }

    public boolean isSobrescrita() {
        return sobrescrita;
    }

    public void setSobrescrita(boolean sobrescrita) {
        this.sobrescrita = sobrescrita;
    }

}
