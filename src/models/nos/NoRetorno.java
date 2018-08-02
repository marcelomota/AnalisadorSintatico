package models.nos;

public class NoRetorno {
    
    private String classe;
    private String valor;

    public NoRetorno(String classe, String valor) {
        this.classe = classe;
        this.valor = valor;
    }

    public NoRetorno() {
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
    
}
