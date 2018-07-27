package models;

import java.util.ArrayList;
import java.util.Iterator;

public class AnalisadorSemantico {
    
    private ArrayList<NoSemantico> tabelaSemantica;
    private boolean temStart; 
    private String erros;
    private String escopoAtual;    

    public AnalisadorSemantico() {
        
        this.tabelaSemantica = new ArrayList();
        this.erros = "";
        this.escopoAtual = "Global";
    }
        
    public void declararStart(String linha) {
        
        if(this.temStart) {
           this.erros += "Erro 01 - Mais de um metodo principal 'Start' foi declarado na linha "+linha+".\n";
        } else {
           this.temStart = true;
        }
    }
    
    public void declararVar() {
        
        NoSemantico no = new NoSemantico();
        no.setDeclaracao("var");
        no.setEscopo(this.escopoAtual);
        this.tabelaSemantica.add(no);
    }
    
    public void declararConst() {
        
        NoSemantico no = new NoSemantico();
        no.setDeclaracao("const");
        no.setEscopo(this.escopoAtual);
        this.tabelaSemantica.add(no);
    }
    
    public void declararStruct() {
        
        NoSemantico no = new NoSemantico();
        no.setDeclaracao("struct");
        no.setEscopo(this.escopoAtual);
        this.tabelaSemantica.add(no);
    }
    
    public void declararFuncao() {
        
        NoSemantico no = new NoSemantico();
        no.setDeclaracao("function");
        no.setEscopo(this.escopoAtual);
        this.tabelaSemantica.add(no);
    }
    
    public void declararProcedure() {
        
        NoSemantico no = new NoSemantico();
        no.setDeclaracao("procedure");
        no.setEscopo(this.escopoAtual);
        this.tabelaSemantica.add(no);
    }
    
    public void declararTypedef() {
        
        NoSemantico no = new NoSemantico();
        no.setDeclaracao("typedef");
        no.setEscopo(this.escopoAtual);
        this.tabelaSemantica.add(no);
    }
    
    public void atualizarEscopo(boolean ativar) {
        
        if(ativar)        
            this.escopoAtual = this.tabelaSemantica.get(this.getLastIndex()).getNome();
        else
            this.escopoAtual = "Global";
    }
      
    public void addTipo(String tipo) {
        
        if(this.tabelaSemantica.get(this.getLastIndex()).getTipo() == null) {
            this.tabelaSemantica.get(this.getLastIndex()).setTipo(tipo);
        } else {
            this.erros += "ERRO AO ATUALIZAR TIPO\n";
        }
    }
    
    public void addNome(String nome, String linha) {
        
        NoSemantico no = this.tabelaSemantica.get(this.getLastIndex());
        if(no.getNome() == null) {
            // Acao pos verificacao ?
            this.verificarNome(no, nome, linha);
            this.tabelaSemantica.get(this.getLastIndex()).setNome(nome);
        } else {
            this.erros += "ERRO AO ATUALIZAR NOME\n";
        }
    }
    
    public void verificarNome(NoSemantico no, String nome, String linha) {
        
        if(no.getDeclaracao().equals("var")) {
            
            if(this.verificarNomeVar(nome, no.getEscopo())) {
                this.erros += "Erro 02 - Erro na linha "+linha+", já existe uma variável declarada como '"+nome+"'.\n";
            }            
        } else if(no.getDeclaracao().equals("const")){
            
            if(this.verificarNomeConst(nome)) {
                this.erros += "Erro 03 - Erro na linha "+linha+", já existe uma constante declarada como '"+nome+"'.\n";
            } 
        } else if(no.getDeclaracao().equals("struct")){
            
            if(this.verificarNomeStruct(nome)) {
                this.erros += "Erro 04 - Erro na linha "+linha+", já existe uma variável declarada como '"+nome+"'.\n";
            } 
        } else if(no.getDeclaracao().equals("function")){
            
            if(this.verificarNomeFuncao(nome)) {
                this.erros += "Erro 05 - Erro na linha "+linha+", já existe uma variável declarada como '"+nome+"'.\n";
            } 
        } else if(no.getDeclaracao().equals("procedure")){
            
            if(this.verificarNomeProcedure(nome)) {
                this.erros += "Erro 06 - Erro na linha "+linha+", já existe uma variável declarada como '"+nome+"'.\n";
            } 
        } else if(no.getDeclaracao().equals("typedef")){
            
            if(this.verificarNomeTypedef(nome, no.getEscopo())) {
                this.erros += "Erro 07 - Erro na linha "+linha+", já existe uma variável declarada como '"+nome+"'.\n";
            } 
        } else {
            this.erros += "ERRO AO VERIFICAR NOME\n";
        }
                
    }
    
    public boolean verificarNomeVar(String nome, String escopo) {
        
        for (NoSemantico no : this.tabelaSemantica) {
            
            if(no.getDeclaracao().equals("var") && no.getEscopo().equals(escopo) && no.getNome().equals(nome)) {
                
                return true;
            }
        }
        
        return false;
    }
    
    public boolean verificarNome(String nome) {
    
        return false;
    }
    
    public void verificarFuncao(String nome) {
        
        this.tabelaSemantica.forEach(
            (no) -> {
                if(no.getDeclaracao().equals("function") && no.getNome().equals(nome)){
                    
                }
            }
        );
    }
     
    public String getErros() {
        return this.erros;
    }
    
    public ArrayList<NoSemantico> getTabelaSemantica() {
        return tabelaSemantica;
    }
    
    public int getLastIndex() {
        return (this.tabelaSemantica.size()-1);
    }
             
    public void printSemanticTable() {
        
        this.tabelaSemantica.forEach(
            (no) -> {
                System.out.println("Declaração: "+no.getDeclaracao());
                System.out.println("Tipo: "+no.getTipo());
                System.out.println("Nome: "+no.getNome());  
                System.out.println("Escopo: "+no.getEscopo());
                System.out.println("Valor: "+no.getValor());
                System.out.println();
            }
        );
    }
    
}
