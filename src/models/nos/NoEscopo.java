package models.nos;

public class NoEscopo {
    
    private String palavraReservada;
    private String identificador;

    public NoEscopo() {        
    }

    public NoEscopo(String palavraReservada, String identificador) {
        this.palavraReservada = palavraReservada;
        this.identificador = identificador;
    }

    public String getPalavraReservada() {
        return palavraReservada;
    }

    public void setPalavraReservada(String palavraReservada) {
        this.palavraReservada = palavraReservada;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }
    
}
