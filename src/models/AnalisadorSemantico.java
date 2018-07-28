package models;

import java.util.ArrayList;

public class AnalisadorSemantico {
    
    private ArrayList<NoSemantico> tabelaSemantica;
    private ArrayList<String> funcoesPendentes;
    private Escopo escopo;   
    private boolean temStart; 
    private String erros;
     
    public AnalisadorSemantico() {
        
        this.tabelaSemantica = new ArrayList();
        this.erros = "";
        this.escopo = new Escopo();
        this.escopo.addEscopo("Global", "Global");
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
        no.setNomeEscopo(this.escopo.getLastNomeEscopo());
        no.setValorEscopo(this.escopo.getLastValorEscopo());
        this.tabelaSemantica.add(no);
    }
    
    public void declararVar(String tipo) {
        
        NoSemantico no = new NoSemantico();
        no.setDeclaracao("var");
        no.setTipo(tipo);
        no.setNomeEscopo(this.escopo.getLastNomeEscopo());
        no.setValorEscopo(this.escopo.getLastValorEscopo());
        this.tabelaSemantica.add(no);
    }
    
    public void declararConst() {
        
        NoSemantico no = new NoSemantico();
        no.setDeclaracao("const");
        no.setNomeEscopo(this.escopo.getLastNomeEscopo());
        no.setValorEscopo(this.escopo.getLastValorEscopo());
        this.tabelaSemantica.add(no);
    }
    
    public void declararConst(String tipo) {
        
        NoSemantico no = new NoSemantico();
        no.setDeclaracao("const");
        no.setTipo(tipo);
        no.setNomeEscopo(this.escopo.getLastNomeEscopo());
        no.setValorEscopo(this.escopo.getLastValorEscopo());
        this.tabelaSemantica.add(no);
    }
    
    public void declararStruct() {
        
        NoSemantico no = new NoSemantico();
        no.setDeclaracao("struct");
        no.setTipo("struct");
        no.setNomeEscopo(this.escopo.getLastNomeEscopo());
        no.setValorEscopo(this.escopo.getLastValorEscopo());
        this.tabelaSemantica.add(no);
    }
    
    public void declararFuncao() {
        
        NoSemantico no = new NoSemantico();
        no.setDeclaracao("function");
        no.setNomeEscopo(this.escopo.getLastNomeEscopo());
        no.setValorEscopo(this.escopo.getLastValorEscopo());
        this.tabelaSemantica.add(no);
    }
    
    public void declararProcedure() {
        
        NoSemantico no = new NoSemantico();
        no.setDeclaracao("procedure");
        no.setNomeEscopo(this.escopo.getLastNomeEscopo());
        no.setValorEscopo(this.escopo.getLastValorEscopo());
        this.tabelaSemantica.add(no);
    }
    
    public void declararTypedef() {
        
        NoSemantico no = new NoSemantico();
        no.setDeclaracao("typedef");
        no.setNomeEscopo(this.escopo.getLastNomeEscopo());
        no.setValorEscopo(this.escopo.getLastValorEscopo());
        this.tabelaSemantica.add(no);
    }
    
    public void declararVarStruct(String tipo) {
        
        NoSemantico no = new NoSemantico();
        no.setDeclaracao("varStruct");
        no.setTipo(tipo);
        no.setNomeEscopo(this.escopo.getLastNomeEscopo());
        no.setValorEscopo(this.escopo.getLastValorEscopo());
        this.tabelaSemantica.add(no);
    }
    
    public void atualizarEscopo(boolean ativar) {
        
        if(ativar)               
            this.escopo.addEscopo(this.tabelaSemantica.get(this.getLastIndex()).getDeclaracao(), this.tabelaSemantica.get(this.getLastIndex()).getNome());
        else
            this.escopo.removeLast();
    }
      
    public void addTipo(String tipo) {
        
        if(this.escopo.getLastNomeEscopo().equals("struct")) {
            
            this.declararVarStruct(tipo);
        } else if(this.tabelaSemantica.get(this.getLastIndex()).getDeclaracao().equals("function")) {
            
            if(this.tabelaSemantica.get(this.getLastIndex()).getTipo().isEmpty()) {
                this.tabelaSemantica.get(this.getLastIndex()).setTipo(tipo);
            } else if(this.tabelaSemantica.get(this.getLastIndex()).getValor().isEmpty()) {
                this.tabelaSemantica.get(this.getLastIndex()).setValor(tipo);
            } else {
                this.tabelaSemantica.get(this.getLastIndex()).setValor2(tipo);
            }
        } else {
            
            if(this.tabelaSemantica.get(this.getLastIndex()).getTipo().isEmpty()) {
                this.tabelaSemantica.get(this.getLastIndex()).setTipo(tipo);
            } else {
                this.erros += "ERRO AO ATUALIZAR TIPO\n";
            }   
        }        
    }
    
    public void addNome(String nome, String linha) {
        
        NoSemantico no = this.tabelaSemantica.get(this.getLastIndex());
        if(no.getDeclaracao().equals("var")) {
            
            if(no.getNome().isEmpty()) {
                
                this.verificarNome(no, nome, linha);
                this.tabelaSemantica.get(this.getLastIndex()).setNome(nome);
            } else {
                 
                this.declararVar(this.tabelaSemantica.get(this.getLastIndex()).getTipo());
                this.verificarNome(no, nome, linha);
                this.tabelaSemantica.get(this.getLastIndex()).setNome(nome);
            }            
        } else if(no.getDeclaracao().equals("const")) {
            
            if(no.getNome().isEmpty()) {
                
                this.verificarNome(no, nome, linha);
                this.tabelaSemantica.get(this.getLastIndex()).setNome(nome);
            } else {
                 
                this.declararConst(this.tabelaSemantica.get(this.getLastIndex()).getTipo());
                this.verificarNome(no, nome, linha);
                this.tabelaSemantica.get(this.getLastIndex()).setNome(nome);
            }  
        } else if(no.getDeclaracao().equals("varStruct")) {
            
            if(no.getNome().isEmpty()) {
                
                this.verificarNome(no, nome, linha);
                this.tabelaSemantica.get(this.getLastIndex()).setNome(nome);
            } else {
                 
                this.declararVarStruct(this.tabelaSemantica.get(this.getLastIndex()).getTipo());
                this.verificarNome(no, nome, linha);
                this.tabelaSemantica.get(this.getLastIndex()).setNome(nome);
            }  
        } else if(no.getDeclaracao().equals("function")) {
            
            if(no.getNome().isEmpty()) {
                
                this.verificarNome(no, nome, linha);
                this.tabelaSemantica.get(this.getLastIndex()).setNome(nome);
            } else {
                
                if(this.tabelaSemantica.get(this.getLastIndex()).getValor().isEmpty()) {
                    
                    this.tabelaSemantica.get(this.getLastIndex()).setValor(nome);
                } else {
                    
                    this.tabelaSemantica.get(this.getLastIndex()).setValor2(nome);
                }                
            }
        } else {
            
            if(no.getNome().isEmpty()) {
                // Acao pos verificacao ?
                this.verificarNome(no, nome, linha);
                this.tabelaSemantica.get(this.getLastIndex()).setNome(nome);
            } else {
                this.erros += "ERRO AO ATUALIZAR NOME - Linha "+linha+"\n";
            }
        }        
    }
    
    public void verificarNome(NoSemantico no, String nome, String linha) {
        
        if(no.getDeclaracao().equals("var")) {
            
            if(this.verificarNomeVar(nome, no.getNomeEscopo(), no.getValorEscopo())) {
                this.erros += "Erro 02 - Erro na linha "+linha+", já existe uma variável declarada como '"+nome+"'.\n";
            }            
        } else if(no.getDeclaracao().equals("const")){
            
            if(this.verificarNomeConst(nome)) {
                this.erros += "Erro 03 - Erro na linha "+linha+", já existe uma constante declarada como '"+nome+"'.\n";
            } 
        } else if(no.getDeclaracao().equals("struct")){
            
            if(this.verificarNomeStruct(nome)) {
                this.erros += "Erro 04 - Erro na linha "+linha+", já existe uma struct declarada como '"+nome+"'.\n";
            } 
        } else if(no.getDeclaracao().equals("function")){
            
            if(this.verificarNomeFuncao(nome)) {
                this.erros += "Erro 05 - Erro na linha "+linha+", já existe uma funcao declarada como '"+nome+"'.\n";
            } 
        } else if(no.getDeclaracao().equals("procedure")){
            
            if(this.verificarNomeProcedure(nome)) {
                this.erros += "Erro 06 - Erro na linha "+linha+", já existe uma procedure declarada como '"+nome+"'.\n";
            } 
        } else if(no.getDeclaracao().equals("typedef")){
            
            if(this.verificarNomeTypedef(nome)) {
                this.erros += "Erro 07 - Erro na linha "+linha+", já existe um tipo declarado como '"+nome+"'.\n";
            } 
        } else if(no.getDeclaracao().equals("varStruct")){
            
            if(this.verificarNomeVarStruct(nome, no.getNomeEscopo(), no.getValorEscopo())) {
                this.erros += "Erro 08 - Erro na linha "+linha+", já existe uma variável declarada como '"+nome+"'.\n";
            } 
        } else {
            this.erros += "ERRO AO VERIFICAR NOME\n";
        }
                
    }
    
    public boolean verificarNomeVar(String nome, String nomeEscopo, String valorEscopo) {
        
        for (NoSemantico no : this.tabelaSemantica) {
            
            if(no.getDeclaracao().equals("var") && no.getNomeEscopo().equals(nomeEscopo) && no.getValorEscopo().equals(valorEscopo) && no.getNome().contains(nome)) {
                
                return true;
            }
        }
        
        return false;
    }
    
    public boolean verificarNomeConst(String nome) {
    
        for (NoSemantico no : this.tabelaSemantica) {
            
            if(no.getDeclaracao().equals("const") && no.getNome().equals(nome)) {
                
                return true;
            }
        }
        
        return false;
    }
    
    public boolean verificarNomeStruct(String nome) {
    
        for (NoSemantico no : this.tabelaSemantica) {
            
            if(no.getDeclaracao().equals("struct") && no.getNome().equals(nome)) {
                
                return true;
            }
        }
        
        return false;
    }
    
    public boolean verificarNomeFuncao(String nome) {
                
        for (NoSemantico no : this.tabelaSemantica) {
            
            if(no.getDeclaracao().equals("function") && no.getNome().equals(nome)) {
                
                this.funcoesPendentes.add(nome);
                return false;
            }
        }
        
        return false;
    }
    
    public boolean verificarNomeProcedure(String nome) {
    
        for (NoSemantico no : this.tabelaSemantica) {
            
            if(no.getDeclaracao().equals("procedure") && no.getNome().equals(nome)) {
                
                this.funcoesPendentes.add(nome);
                return false;
            }
        }
        
        return false;
    }
    
    public boolean verificarNomeTypedef(String nome) {
    
        for (NoSemantico no : this.tabelaSemantica) {
            
            if(no.getDeclaracao().equals("typedef") && no.getNome().equals(nome)) {
                
                return true;
            }
        }
        
        return false;
    }
    
    public boolean verificarNomeVarStruct(String nome, String nomeEscopo, String valorEscopo) {
        
        for (NoSemantico no : this.tabelaSemantica) {
            
            if(no.getDeclaracao().equals("varStruct") && no.getNomeEscopo().equals(nomeEscopo) && no.getValorEscopo().equals(valorEscopo) && no.getNome().contains(nome)) {
                
                return true;
            }
        }
        
        return false;
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
                System.out.println("Valor: "+no.getValor());
                System.out.println("Nome Escopo: "+no.getNomeEscopo());
                System.out.println("Valor Escopo: "+no.getValorEscopo());                
                System.out.println();
            }
        );
    }
    
}
