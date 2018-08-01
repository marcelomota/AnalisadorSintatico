package models;

import java.util.ArrayList;

public class TabelaSemantica {
    
    private ArrayList<NoSemantico> tabelaSemantica;
    private ArrayList<NoSemantico> funcoesPendentes;
    private ArrayList<NoSemantico> proceduresPendentes;
    private ArrayList<NoSemantico> cadeiaStructs;
    protected static int countId = 0;

    public TabelaSemantica() {
        this.tabelaSemantica = new ArrayList();
        this.funcoesPendentes = new ArrayList();
        this.proceduresPendentes = new ArrayList();
        this.cadeiaStructs = new ArrayList();
    }
    
    public boolean verificarNomeFunction(String nome) {    
        for (NoSemantico no : this.tabelaSemantica) {            
            if(no.getDeclaracao().equals("function") && no.getNome().equals(nome)) {                
                NoSemantico noAtual = this.getLastNo();
                noAtual.setIdSobrecarga(no.getId());
                this.funcoesPendentes.add(noAtual);
                return true;
            }
        }        
        return false;
    }
    
    public boolean verificarNomeProcedure(String nome) {    
        for (NoSemantico no : this.tabelaSemantica) {            
            if(no.getDeclaracao().equals("procedure") && no.getNome().equals(nome)) {                
                NoSemantico noAtual = this.getLastNo();
                noAtual.setIdSobrecarga(no.getId());
                this.proceduresPendentes.add(noAtual);
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
    
    public boolean verificarNomeTypedef(String nome) {    
        for (NoSemantico no : this.tabelaSemantica) {            
            if(no.getDeclaracao().equals("typedef") && no.getNome().equals(nome)) {                
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
    
    public boolean verificarNomeVar(String nome, String palavraReservadaEscopo, String identificadorEscopo) {        
        for (NoSemantico no : this.tabelaSemantica) {            
            if(no.getDeclaracao().equals("var")) {                
                if(no.getPalavraReservadaEscopo().equals(palavraReservadaEscopo) && no.getIdentificadorEscopo().equals(identificadorEscopo)) {
                    if(no.getNome().equals(nome)) {                        
                        boolean trava = true;
                        if(palavraReservadaEscopo.equals("function")) {                                                
                            for (NoSemantico f : this.funcoesPendentes) {                            
                                if(f.getNome().equals(identificadorEscopo)) {                                   
                                    trava = false;
                                }                             
                            }
                        } else if(palavraReservadaEscopo.equals("procedure")) {

                            for (NoSemantico p : this.proceduresPendentes) {

                                if(p.getNome().equals(identificadorEscopo)) {

                                    trava = false;
                                }                             
                            }
                        } 
                        if(trava)
                            return true; 
                    }   
                }                
            }
        }
        
        return false;
    }
    
    public boolean verificarNomeVarStruct(String nome, String palavraReservadaEscopo, String identificadorEscopo) {       
        
        ArrayList<String> extensoes = null;
        for(NoSemantico noS : this.cadeiaStructs) {
            if(noS.getNome().equals(identificadorEscopo)) {
                extensoes = noS.getExtensoes();
                break;
            }            
        }
        
        if(extensoes != null) {            
            for (NoSemantico no : this.tabelaSemantica) {    
                if(no.getDeclaracao().equals("varStruct")) {  
                    if(no.getPalavraReservadaEscopo().equals(palavraReservadaEscopo)) {
                        if(no.getIdentificadorEscopo().equals(identificadorEscopo) || extensoes.contains(no.getIdentificadorEscopo())) {
                            if(no.getNome().equals(nome)) {
                                return true;
                            } 
                        } 
                    }
                }
            }  
        } else {            
            for (NoSemantico no : this.tabelaSemantica) {    
                if(no.getDeclaracao().equals("varStruct")) {  
                    if(no.getPalavraReservadaEscopo().equals(palavraReservadaEscopo) && no.getIdentificadorEscopo().equals(identificadorEscopo)) {                       
                        if(no.getNome().equals(nome)) {
                           return true;
                        } 
                    }
                }
            }  
        }
        return false;
    }
           
    public boolean verificarNomeEscopoAtual(String nome, String palavraReservadaEscopo, String identificadorEscopo) {
        for(NoSemantico no : this.tabelaSemantica) {            
            if(no.getPalavraReservadaEscopo().equals(palavraReservadaEscopo) && no.getIdentificadorEscopo().equals(identificadorEscopo)) {
                if(no.getNome().equals(nome)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean verificarDeclaracaoFunction(String identificadorEscopo, String declaracao, String nome) {        
        for(NoSemantico no : this.tabelaSemantica) {            
            if(no.getPalavraReservadaEscopo().equals("function") && no.getIdentificadorEscopo().equals(identificadorEscopo)) {
                if(no.getDeclaracao().equals(declaracao) && no.getNome().equals(nome)) {
                    this.funcoesPendentes.add(this.getLastNo());
                    return true;
                } 
            } else if(no.getDeclaracao().equals("function") && no.getNome().equals(identificadorEscopo)) {
                for(NoParametro noP : no.getParametros()) {
                    if(noP.getNome().equals(nome)) {
                        this.funcoesPendentes.add(this.getLastNo());
                        return true;
                    }
                }   
            }
        }                     
        return false;
    }
    
    public boolean verificarDeclaracaoProcedure(String identificadorEscopo, String declaracao, String nome, int idNoDecProc) {        
        for(NoSemantico no : this.tabelaSemantica) {            
            if(no.getPalavraReservadaEscopo().equals("procedure") && no.getIdentificadorEscopo().equals(identificadorEscopo)) {
                if(no.getDeclaracao().equals(declaracao) && no.getNome().equals(nome)) {
                    return true;
                }
            } else if(no.getDeclaracao().equals("procedure") && no.getNome().equals(identificadorEscopo)) {
                for(NoParametro noP : no.getParametros()) {
                    if(noP.getNome().equals(nome)) {
                        return true;
                    }
                }   
            }
        }      
        return false;
    }
    
    public int verificarDeclaracao(String nome, String palavraReservadaEscopo, String identificadorEscopo) {
        for(NoSemantico no : this.tabelaSemantica) {
            
            if(no.getPalavraReservadaEscopo().equals(palavraReservadaEscopo) && no.getIdentificadorEscopo().equals(identificadorEscopo)) {
                if(no.getNome().equals(nome)) {
                    return no.getId();
                }
            }            
        }
        if(!palavraReservadaEscopo.equals("Global")) {
            
            for(NoSemantico no : this.tabelaSemantica) {
            
                if(no.getPalavraReservadaEscopo().equals("Global")) {
                    if(no.getNome().equals(nome)) {
                        return no.getId();
                    }
                }            
            }
        }
        
        return 0;
    }
    
    public boolean verificarDeclaracaoStruct(String nome) {
        
        NoSemantico no = this.tabelaSemantica.get(this.getLastIndex());
        if(no.getDeclaracao().equals("struct")) {
            
            for(NoSemantico s : this.tabelaSemantica) {
                if(s.getPalavraReservadaEscopo().equals(no.getDeclaracao()) && s.getIdentificadorEscopo().equals(no.getNome())) {

                    if(s.getDeclaracao().equals("varStruct") && s.getNome().equals(nome)) {
                        this.reEmpilharNo(s.getId());
                        s.setValor("");
                        return true;
                    }
                }  
            }
        }
        
        return false;
    }
    
    public void reEmpilharNo(int id) {
        
        for(int i=0; i<this.tabelaSemantica.size(); i++) {
            
            NoSemantico no = this.tabelaSemantica.get(i);
            if(no.getId() == id) {
                                  
               this.tabelaSemantica.add(this.tabelaSemantica.remove(i));  
               return;
            }
        }
    }
    
    public void addTipo(String tipo) {
        this.getLastNo().setTipo(tipo);
    }
    
    /**
     * Adiciona nome no ultimo NoSintatico da lista.
     * @param nome 
     */
    public void addNome(String nome) {
        this.getLastNo().setNome(nome);
    }
    
    public void addTipoParametro(String tipoParametro) {
        this.getLastNo().addTipoParametro(tipoParametro);
    }
    
    public boolean addNomeParametro(String nomeParametro) {
        NoSemantico no = this.getLastNo();
        boolean chave = false;
        if(!no.getParametros().isEmpty()) {
            for(NoParametro noP : no.getParametros()) {
                if(noP.getNome().equals(nomeParametro)) {
                    chave = true;
                }
            }
            no.addNomeParametro(nomeParametro);
        }        
        return chave;
    }    
    
    public void addValor(String valor, int seletor) {
        
        if(seletor == 1) {
            
            this.getLastNo().setValor(valor);
        } else if (seletor == 2) {
            
            this.getLastNo().setValor2(valor);
        } else if (seletor == 3) {
            
            this.getLastNo().setValor3(valor);
        } else {
            
            // ...
        }
    }
    
    public NoSemantico isFunction(String nome) {
        
        for(NoSemantico no : this.tabelaSemantica) {
            if(no.getDeclaracao().equals("functin") && no.getNome().equals(nome)) {
                return no;
            }
        }
        return null;
    }
    
    public boolean isStruct(String nome) {
        
        for(NoSemantico no : this.tabelaSemantica) {
            if(no.getDeclaracao().equals("struct") && no.getNome().equals(nome)) {
                return true;
            }
        }
        return false;
    }
    
    public void addExtensao(String nome) {
        
        NoSemantico noAtual = this.getLastNo();
        noAtual.addExtensao(nome);
        this.cadeiaStructs.add(noAtual);
        this.verificarCadeiaExtensao(noAtual, nome);
    }
    
    public void verificarCadeiaExtensao(NoSemantico noAtual, String extensaoAtual) {
        for(NoSemantico no : this.cadeiaStructs) {
            if(no.getNome().equals(extensaoAtual)) {
                
                no.getExtensoes().forEach(
                    (e) -> {
                        if(!noAtual.getExtensoes().contains(e)) {
                            noAtual.addExtensao(e);
                            verificarCadeiaExtensao(noAtual, e);
                        }
                    }
                );
            }
        }
    }
    
    public NoSemantico getLastFunction() {       
        for(int i = this.tabelaSemantica.size()-1; i >= 0; i--) {
            NoSemantico no = this.tabelaSemantica.get(i);  
            if(no.getDeclaracao().equals("function")) {
                return no;
            }            
        }
        return null;
    }
    
    public NoSemantico getLastProcedure() {
        for(int i = this.tabelaSemantica.size()-1; i >= 0; i--) {
            NoSemantico no = this.tabelaSemantica.get(i);  
            if(no.getDeclaracao().equals("procedure")) {
                return no;
            }            
        }
        return null;
    }
    
    public NoSemantico getLastNo() {
        return this.tabelaSemantica.get(this.getLastIndex());
    }
    
    public void addNo(NoSemantico no) {
        this.tabelaSemantica.add(no);
    }
    
    public int getLastIndex() {
        return (this.tabelaSemantica.size()-1);
    }
    
    public void addFuncaoPendente(NoSemantico no) {
        this.funcoesPendentes.add(no);
    }

    public ArrayList<NoSemantico> getFuncoesPendentes() {
        return funcoesPendentes;
    }    
            
    public void addProcedurePendente(NoSemantico no) {
        this.proceduresPendentes.add(no);
    }

    public ArrayList<NoSemantico> getProceduresPendentes() {
        return proceduresPendentes;
    }

    /**
     * Recebe o ID do NoSemantico.
     * @param id
     * @return 
     */
    public NoSemantico getNo(int id) {        
        for(NoSemantico no : this.tabelaSemantica) {
            if(no.getId() == id) {
                return no;
            }
        }
        return null;
    }
    
    public static int getId() {
       TabelaSemantica.countId++;
       return TabelaSemantica.countId;        
    }
            
    public void printSemanticTable() {
        
        this.tabelaSemantica.forEach(
            (no) -> {
                System.out.println("Id: "+no.getId());                
                System.out.println("Declaração: "+no.getDeclaracao());
                System.out.println("Linha da Declaração: "+no.getLinhaDeclaracao());
                if(!no.getTipo().isEmpty())
                    System.out.println("Tipo: "+no.getTipo());
                if(!no.getNome().isEmpty())
                    System.out.println("Nome: "+no.getNome());
                if(!no.getValor().isEmpty())
                    System.out.println("Valor: "+no.getValor());
                if(!no.getExtensoes().isEmpty()) {
                    System.out.println("Cadeia de Extensão: ");
                    no.getExtensoes().forEach(
                        (p) -> {
                            System.out.println("    "+p);
                        }
                    );
                }
                if(!no.getPalavraReservadaEscopo().isEmpty())                
                    System.out.println("Nome Escopo: "+no.getPalavraReservadaEscopo());
                if(!no.getIdentificadorEscopo().isEmpty())
                    System.out.println("Valor Escopo: "+no.getIdentificadorEscopo());                
                if(!no.getParametros().isEmpty()) {
                    System.out.println("Parametros: ");
                    no.getParametros().forEach(
                        (p) -> {
                            System.out.println("    "+p.getTipo()+" "+p.getNome());
                        }
                    );
                }
                    
                if(no.getIdSobrecarga() != 0)
                    System.out.println("ID Sobrecarga: "+no.getIdSobrecarga());
                System.out.println();
            }
        );
        
        this.printOthers();
    }
    
    public void printOthers() {
        
        System.out.println("***** Funções Pendentes *****"); 
        this.funcoesPendentes.forEach(
            (no) -> {
                System.out.println("Função: "+no.getNome());  
            }
        );        
        
        System.out.println("\n***** Procedures Pendentes *****"); 
        this.proceduresPendentes.forEach(
            (no) -> {
                System.out.println("Procedure: "+no.getNome()); 
            }
        );
    }       
    
}
