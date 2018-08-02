package controllers;

import java.util.ArrayList;
import models.nos.NoParametro;
import models.nos.NoSemantico;
import models.PilhaContexto;
import models.PilhaEscopo;
import models.TabelaSemantica;
import models.Token;
import models.nos.NoRetorno;

public class ControllerAnalisadorSemantico {
    
    private TabelaSemantica tabelaSemantica;
    private PilhaContexto pilhaContexto;
    private PilhaEscopo pilhaEscopo;
    private String errosSemanticos; // 7
    private boolean temStart;
    private boolean initializer;
    private boolean initializer2;
    private boolean ignorarAtribuicao;
    private Token tokens;

    public ControllerAnalisadorSemantico(Token tokens) {
        
        this.tabelaSemantica = new TabelaSemantica();
        this.pilhaContexto = new PilhaContexto();
        this.pilhaEscopo = new PilhaEscopo();
        this.errosSemanticos = "";
        this.tokens = tokens;
    }
    
    /**
     * Declaracao de um metodo Start().
     * @param linha 
     */
    public void declararStart(String linha) {  
        
        if(this.temStart) {
            
           this.errosSemanticos += "Erro #01 - Numero de métodos principais 'Start' declarados excedido na linha "+linha+".\n";
        } else {
            
           this.temStart = true;
        }
    }
    
    /**
     * Declaracao de uma variavel sem tipo.
     * @param linha 
     */
    public void declararVar(String linha) {
        
        NoSemantico no = new NoSemantico();
        no.setDeclaracao("var");
        no.setLinhaDeclaracao(linha);
        no.setPalavraReservadaEscopo(this.pilhaEscopo.getLastPalavraReservada());
        no.setIdentificadorEscopo(this.pilhaEscopo.getLastIdentificador());
        this.tabelaSemantica.addNo(no);
    }
    
    /**
     * Declaracao de uma variavel com o tipo.
     * @param tipo
     * @param linha 
     */
    public void declararVar(String tipo, String linha) {
        
        NoSemantico no = new NoSemantico();
        no.setDeclaracao("var");
        no.setLinhaDeclaracao(linha);
        no.setTipo(tipo);
        no.setPalavraReservadaEscopo(this.pilhaEscopo.getLastPalavraReservada());
        no.setIdentificadorEscopo(this.pilhaEscopo.getLastIdentificador());
        this.tabelaSemantica.addNo(no);
    }
    
    /**
     * Declaracao de uma constante sem o tipo.
     * @param linha 
     */
    public void declararConst(String linha) {    

        NoSemantico no = new NoSemantico();
        no.setDeclaracao("const");
        no.setLinhaDeclaracao(linha);
        no.setPalavraReservadaEscopo(this.pilhaEscopo.getLastPalavraReservada());
        no.setIdentificadorEscopo(this.pilhaEscopo.getLastIdentificador());
        this.tabelaSemantica.addNo(no);
    }
    
    /**
     * Declaracao de uma constante com o tipo.
     * @param tipo
     * @param linha 
     */
    public void declararConst(String tipo, String linha) {
        
        NoSemantico no = new NoSemantico();
        no.setDeclaracao("const");
        no.setLinhaDeclaracao(linha);
        no.setTipo(tipo);
        no.setPalavraReservadaEscopo(this.pilhaEscopo.getLastPalavraReservada());
        no.setIdentificadorEscopo(this.pilhaEscopo.getLastIdentificador());
        this.tabelaSemantica.addNo(no);
    }
    
    /**
     * Declaracao de uma struct.
     * @param linha 
     */
    public void declararStruct(String linha) {
        
        NoSemantico no = new NoSemantico();
        no.setDeclaracao("struct");
        no.setLinhaDeclaracao(linha);
        no.setPalavraReservadaEscopo(this.pilhaEscopo.getLastPalavraReservada());
        no.setIdentificadorEscopo(this.pilhaEscopo.getLastIdentificador());
        this.tabelaSemantica.addNo(no);
    }
    
    /**
     * Declaracao de uma funcao.
     * @param linha 
     */
    public void declararFuncao(String linha) {
        
        NoSemantico no = new NoSemantico();
        no.setDeclaracao("function");
        no.setLinhaDeclaracao(linha);
        no.setPalavraReservadaEscopo(this.pilhaEscopo.getLastPalavraReservada());
        no.setIdentificadorEscopo(this.pilhaEscopo.getLastIdentificador());
        this.tabelaSemantica.addNo(no);
    }
    
    /**
     * Declaracao de uma procedure.
     * @param linha 
     */
    public void declararProcedure(String linha) {
        
        NoSemantico no = new NoSemantico();
        no.setDeclaracao("procedure");
        no.setLinhaDeclaracao(linha);
        no.setPalavraReservadaEscopo(this.pilhaEscopo.getLastPalavraReservada());
        no.setIdentificadorEscopo(this.pilhaEscopo.getLastIdentificador());
        this.tabelaSemantica.addNo(no);
    }
    
    public void declararParametrosFuncao() {
        NoSemantico noS = this.tabelaSemantica.getLastFunction();
        for(NoParametro noP : noS.getParametros()) {
            NoSemantico no = new NoSemantico();
            no.setDeclaracao("parametro");
            no.setLinhaDeclaracao(noS.getLinhaDeclaracao());
            no.setTipo(noP.getTipo());
            no.setNome(noP.getNome());
            no.setPalavraReservadaEscopo("function");
            no.setIdentificadorEscopo(noS.getNome());
            this.tabelaSemantica.addNo(no);
        }
    }
    
    public void declararParametrosProcedure() {
        NoSemantico noS = this.tabelaSemantica.getLastProcedure();
        for(NoParametro noP : noS.getParametros()) {
            NoSemantico no = new NoSemantico();
            no.setDeclaracao("parametro");
            no.setLinhaDeclaracao(noS.getLinhaDeclaracao());
            no.setTipo(noP.getTipo());
            no.setNome(noP.getNome());
            no.setPalavraReservadaEscopo("procedure");
            no.setIdentificadorEscopo(noS.getNome());
            this.tabelaSemantica.addNo(no);
        }
    }
    
    /**
     * Declaracao de um typedef.
     * @param linha 
     */
    public void declararTypedef(String linha) {
        
        NoSemantico no = new NoSemantico();
        no.setDeclaracao("typedef");
        no.setLinhaDeclaracao(linha);
        no.setPalavraReservadaEscopo(this.pilhaEscopo.getLastPalavraReservada());
        no.setIdentificadorEscopo(this.pilhaEscopo.getLastIdentificador());
        this.tabelaSemantica.addNo(no);
    }
    
    /**
     * Declaracao de uma variavel dentro de uma struct.
     * @param tipo
     * @param linha 
     */
    public void declararVarStruct(String tipo, String linha) {
        
        NoSemantico no = new NoSemantico();
        no.setDeclaracao("varStruct");
        no.setLinhaDeclaracao(linha);
        no.setTipo(tipo);
        no.setPalavraReservadaEscopo(this.pilhaEscopo.getLastPalavraReservada());
        no.setIdentificadorEscopo(this.pilhaEscopo.getLastIdentificador());
        this.tabelaSemantica.addNo(no);
    }    
     
    public void addTipo(String tipo, String linha) {
        
        if(this.pilhaEscopo.getLastPalavraReservada().equals("struct")) {
                        
            this.declararVarStruct(tipo, linha);
        } else if(this.pilhaContexto.getLastContexto().equals("function")) {
            
            if(this.tabelaSemantica.getLastNo().getTipo().isEmpty()) {
                
                this.tabelaSemantica.addTipo(tipo);
            } else {
                
                this.tabelaSemantica.addTipoParametro(tipo);
            }            
        } else if(this.pilhaContexto.getLastContexto().equals("procedure")) {
            
            this.tabelaSemantica.addTipoParametro(tipo);        
        } else if(this.pilhaContexto.getLastContexto().equals(this.tabelaSemantica.getLastNo().getDeclaracao())) {
            
            this.tabelaSemantica.addTipo(tipo);
        } else {
            
            // ...
        }
    }
    
    public void addExtensao(String nome, String linha) {
        
        if(this.tabelaSemantica.isStruct(nome)) {
            
            this.tabelaSemantica.addExtensao(nome);
        } else {
            
            this.errosSemanticos += "Erro #02 - Erro na linha "+linha+", Struct '"+nome+"' não declarada.\n";
        }
    }
    
    public void addNomeProcedure(String nome, String linha) {
        
        if(!this.tabelaSemantica.verificarNomeProcedure(nome)) {
            
            if(this.tabelaSemantica.verificarNomeEscopoAtual(nome, this.pilhaEscopo.getLastPalavraReservada(), this.pilhaEscopo.getLastIdentificador())) {
                
                this.errosSemanticos += "Erro #03 - Erro na linha "+linha+", já existe uma declaração no escopo atual com o nome '"+nome+"'.\n";
                this.tabelaSemantica.getLastNo().setErro(true);  
            }
        } else {
            
          this.tabelaSemantica.getLastNo().setErro(true);  
        }
        this.tabelaSemantica.addNome(nome);
    }
    
    public void addNomeStruct(String nome, String linha) {
        
        if(this.tabelaSemantica.verificarNomeStruct(nome)) {
            
            this.tabelaSemantica.getLastNo().setErro(true);  
            this.errosSemanticos += "Erro #04 - Erro na linha "+linha+", já existe uma struct declarada como '"+nome+"'.\n";
        } else {
            
            if(this.tabelaSemantica.verificarNomeEscopoAtual(nome, this.pilhaEscopo.getLastPalavraReservada(), this.pilhaEscopo.getLastIdentificador())) {
                
                this.errosSemanticos += "Erro #05 - Erro na linha "+linha+", já existe uma declaração no escopo atual com o nome '"+nome+"'.\n";
                this.tabelaSemantica.getLastNo().setErro(true);  
            }
        }
        this.tabelaSemantica.addNome(nome);
    }
    
    public void addNomeTypedef(String nome, String linha) {
        
        if(this.tabelaSemantica.verificarNomeTypedef(nome)) {
            
            this.tabelaSemantica.getLastNo().setErro(true);  
            this.errosSemanticos += "Erro #06 - Erro na linha "+linha+", já existe um typedef declarado como '"+nome+"'.\n";
        } else {
            
            if(this.tabelaSemantica.verificarNomeEscopoAtual(nome, this.pilhaEscopo.getLastPalavraReservada(), this.pilhaEscopo.getLastIdentificador())) {
            
                this.errosSemanticos += "Erro #07 - Erro na linha "+linha+", já existe uma declaração no escopo atual com o nome '"+nome+"'.\n";
                this.tabelaSemantica.getLastNo().setErro(true);  
            }
        }
        this.tabelaSemantica.addNome(nome);
    }
       
    public void addNome(String nome, String linha) {
        
        NoSemantico no = this.tabelaSemantica.getLastNo();
        if(this.pilhaContexto.getLastContexto().equals("function")) {
            
            if(this.tabelaSemantica.getLastNo().getNome().isEmpty()) {
                
                if(!this.tabelaSemantica.verificarNomeFunction(nome)){
                    
                    if(this.tabelaSemantica.verificarNomeEscopoAtual(nome, this.pilhaEscopo.getLastPalavraReservada(), this.pilhaEscopo.getLastIdentificador())) {
                        
                        this.errosSemanticos += "Erro #08 - Erro na linha "+linha+", já existe uma declaração no escopo atual com o nome '"+nome+"'.\n";
                        no.setErro(true);  
                    }
                }
                this.tabelaSemantica.addNome(nome);
            } else {        
                
                if(this.tabelaSemantica.addNomeParametro(nome))
                    this.errosSemanticos += "Erro #09 - Erro na linha "+linha+", já existe uma parametro o nome '"+nome+"'.\n";      
            } 
        } else if(this.pilhaContexto.getLastContexto().equals("procedure")) {
            
            if(this.tabelaSemantica.addNomeParametro(nome)) {
                this.errosSemanticos += "Erro #10 - Erro na linha "+linha+", já existe uma parametro o nome '"+nome+"'.\n";
                no.setErro(true);
            }
        } else if(this.pilhaEscopo.getLastPalavraReservada().equals("struct")) {
          
            if(this.tabelaSemantica.verificarNomeVarStruct(nome, this.pilhaEscopo.getLastPalavraReservada(), this.pilhaEscopo.getLastIdentificador())) {
                this.errosSemanticos += "Erro #11 - Erro na linha "+linha+", já existe uma variável declarada como '"+nome+"'.\n";;
                no.setErro(true);
            }
            this.tabelaSemantica.addNome(nome);
        } else {
            
            if(no.getDeclaracao().equals("var")) {
               
                if(this.tabelaSemantica.verificarNomeVar(nome, this.pilhaEscopo.getLastPalavraReservada(), this.pilhaEscopo.getLastIdentificador())){
                    
                    if(this.tabelaSemantica.isParametro(nome, this.pilhaEscopo.getLastPalavraReservada(), this.pilhaEscopo.getLastIdentificador())) {
                        this.errosSemanticos += "Erro #12 - Erro na linha "+linha+", já existe uma variável declarada como '"+nome+"' nos parametros.\n";                   
                    } else {
                        this.errosSemanticos += "Erro #13 - Erro na linha "+linha+", já existe uma variável declarada como '"+nome+"'.\n";                   
                    }
                    
                    this.tabelaSemantica.removeLastNo();
                } else if(this.pilhaEscopo.getLastPalavraReservada().equals("Global")) {
                    
                    if(this.tabelaSemantica.verificarNomeEscopoAtual(nome, this.pilhaEscopo.getLastPalavraReservada(), this.pilhaEscopo.getLastIdentificador())) {    
                        this.errosSemanticos += "Erro #14 - Erro na linha "+linha+", já existe uma declaração no escopo atual com o nome '"+nome+"'.\n";
                        this.tabelaSemantica.removeLastNo();
                    }    
                } else {
                    
                    if(no.getNome().isEmpty()) {
                    
                        this.tabelaSemantica.addNome(nome);
                    } else {

                        this.declararVar(no.getTipo(), linha);
                        this.tabelaSemantica.addNome(nome);
                    } 
                }         
            } else if(no.getDeclaracao().equals("const")) {
                
                if(this.tabelaSemantica.verificarNomeConst(nome)) {
                    
                    no.setErro(true);
                    this.errosSemanticos += "Erro #15 - Erro na linha "+linha+", já existe uma constante declarada como '"+nome+"'.\n";
                } else if(this.tabelaSemantica.verificarNomeEscopoAtual(nome, this.pilhaEscopo.getLastPalavraReservada(), this.pilhaEscopo.getLastIdentificador())){
                    
                    no.setErro(true);
                    this.errosSemanticos += "Erro #16 - Erro na linha "+linha+", já existe uma declaração no escopo atual com o nome '"+nome+"'.\n";
                }
                this.tabelaSemantica.addNome(nome);
            } else {
              
                // ...
            } 
        }
    }
    
    public void addValor(String valor, String linha, String classe) {
        
        NoSemantico no = this.tabelaSemantica.getLastNo();
        if(this.initializer) {
                                    
            if(no.getValor().isEmpty()) {

                this.tabelaSemantica.addValor(valor, 1); 
            } else {

                this.tabelaSemantica.addValor(valor, 2);
            }         
        } else if(this.initializer2 && !this.ignorarAtribuicao) {
                        
            if(no.getDeclaracao().equals("const")) {
                
                this.errosSemanticos += "Erro #17 - Erro na linha "+linha+", a constante '"+no.getNome()+"' não pode ter o valor alterado.\n";
                this.ignorarAtribuicao = true;
            } else {
                
                if(classe.equals("Identificador") && no.getDeclaracao().equals("var")) {
                    NoSemantico noF = this.tabelaSemantica.isFunction(valor);
                    if(noF != null) {
                     
                        if(!no.getTipo().equals(noF.getTipo())) {
                            this.errosSemanticos += "Erro #18 - Erro na linha "+linha+", o tipo de retorno da função '"+noF.getNome()+"' não corresponde com o tipo da variável '"+no.getNome()+"'.\n";
                        }
                    }                    
                }
                if(no.getValor().isEmpty()) {

                    this.tabelaSemantica.addValor(valor, 1); 
                } else {

                    this.tabelaSemantica.addValor(valor, 2);
                }  
            }
        } else if(!this.ignorarAtribuicao) {
        
            if(classe.equals("Identificador")) {
                int id = this.tabelaSemantica.verificarDeclaracao(valor, this.pilhaEscopo.getLastPalavraReservada(), this.pilhaEscopo.getLastIdentificador()); 
                if(id == 0) {

                    this.errosSemanticos += "Erro #19 - Erro na linha "+linha+", o identificador '"+valor+"' não foi declarado.\n";
                    this.initializer2 = false;
                } else {

                    this.tabelaSemantica.reEmpilharNo(id);
                    if(!this.tabelaSemantica.getLastNo().getDeclaracao().equals("const")) {
                        this.tabelaSemantica.getLastNo().setValor("");
                    }
                    this.initializer2 = true;
                }
            } else {
                
                // ...
            }
        }
    }
      
    public void atribuicaoStruct(String nome, String linha) {
        
        if(!this.tabelaSemantica.verificarDeclaracaoStruct(nome)) {
            this.errosSemanticos += "Erro #20 - Erro na linha "+linha+", o identificador '"+nome+"' não foi inicializado.\n";
        } 
    }
    
    /**
     * Verifica se a ultima constante declarada foi inicializada.
     * @param linha 
     */
    public void verificarInicializacaoConst(String linha) {
        
        NoSemantico no = this.tabelaSemantica.getLastNo();
        if(no.getValor().isEmpty()) {
            
            this.errosSemanticos += "Erro #21 - Constante '"+no.getNome()+"' não inicializada na linha "+linha+".\n";
        }
    }
    
    public void addEscopo(String palavraReservada, String Identificador) {                          
        this.pilhaEscopo.addEscopo(palavraReservada, palavraReservada);      
    }
    
    public void addEscopo() {   
        
        NoSemantico no = this.tabelaSemantica.getLastNo();        
        this.pilhaEscopo.addEscopo(no.getDeclaracao(), no.getNome());      
    }
    
    public void removerEscopo() {        
        this.pilhaEscopo.removerLast();
    }
    
    public void addContexto(String contexto) {        
        this.pilhaContexto.addContexto(contexto);
    }
    
    public void removerContexto() {                
        this.pilhaContexto.removerLast();
    }
    
    /**
     * Foi decidido que a sobrecarga so sera aceita mediante a mudanca dos parametros, 
     * devendo manter o mesmo tipo de retorno e conteudo. 
     */
    public void verificarSobre_carga_scrita() {
            
        this.tabelaSemantica.getFuncoesPendentes().forEach(
            (no) -> {
                NoSemantico noAtual = this.tabelaSemantica.getNo(no.getIdSobrecarga());
                if(noAtual != null) {
                                    
                    if(this.compararParametros(noAtual.getParametros(), no.getParametros())) {
                        
                        this.errosSemanticos += "Erro #22 - Erro na linha "+no.getLinhaDeclaracao()+", já existe uma funcao declarada como '"+no.getNome()+"' e esta não é uma sobrecarga.\n";
                    } else if(!noAtual.getTipo().equals(no.getTipo())) {
                        
                        this.errosSemanticos += "Erro #23 - Erro na linha "+no.getLinhaDeclaracao()+", os tipo de retorno da função não corresponde para uma sobrecarga.\n";
                    } else if(!noAtual.getValor().equals(no.getValor())) {
                        
                        this.errosSemanticos += "Erro #24 - Erro na linha "+no.getLinhaDeclaracao()+", não é permitido a sobrescrita de funções.\n";
                    }
                }                
            }
        );

        this.tabelaSemantica.getProceduresPendentes().forEach(
            (no) -> {
                
                NoSemantico noAtual = this.tabelaSemantica.getNo(no.getIdSobrecarga());
                if(this.compararParametros(noAtual.getParametros(), no.getParametros())) {
                    
                    this.errosSemanticos += "Erro #25 - Erro na linha "+no.getLinhaDeclaracao()+", já existe uma procedure declarada como '"+no.getNome()+"' e esta não é uma sobrecarga.\n";
                } else if(!noAtual.getValor().equals(no.getValor())) {

                    this.errosSemanticos += "Erro #26 - Erro na linha "+no.getLinhaDeclaracao()+", não é permitido a sobrescrita de procedures.\n";
                }
            }
        ); 
    }    
    
    private boolean compararParametros(ArrayList<NoParametro> p1, ArrayList<NoParametro> p2) {
                
        if(p1.size() == p2.size()) {
         
            int igualdades = 0;
            for(NoParametro n1 : p1) {
                
                for(NoParametro n2 : p2) {
                    
                    if(n1.getTipo().equals(n2.getTipo()) && n1.getNome().equals(n2.getNome())) {
                        igualdades++;
                    } 
                }
            }
            
            if(igualdades == p1.size()) {
                return true;
            }
        }       
        return false;
    }
              
    public void addConteudoFuncao(String conteudo, boolean temReturn) {
        
        NoSemantico no = this.tabelaSemantica.getLastFunction();
        if(!temReturn) {
            this.errosSemanticos += "Erro #27 - Retorno não encontrado na função '"+no.getNome()+"' declarada na linha "+no.getLinhaDeclaracao()+".\n";
        }
        no.setValor(conteudo);
    }
    
    public void addConteudoProcedure(String conteudo, boolean temReturn, String linhaRetorno) {
        
        NoSemantico no = this.tabelaSemantica.getLastProcedure();
        if(temReturn) {
            this.errosSemanticos += "Erro #28 - Erro na linha "+linhaRetorno+", retorno inesperado na procedure '"+no.getNome()+"'.\n";
        }
        this.tabelaSemantica.getLastNo().setValor(conteudo);
    }
    
    public void addRetornoFuncao(ArrayList<NoRetorno> retorno, String linha) {
        
        NoSemantico noF = this.tabelaSemantica.getLastFunction();
        if(!retorno.isEmpty()) {
            
            if(retorno.size() == 1 && retorno.get(0).getClasse().contains("Identificador")) {
                NoSemantico no = this.tabelaSemantica.getNoIdentificador(retorno.get(0).getValor(), this.pilhaEscopo.getLastIdentificador());
                if(no != null) {                
                    switch(no.getDeclaracao()) {
                        case "parametro": {
                            if(!noF.getTipo().equals(no.getTipo())) {
                                this.errosSemanticos += "Erro #29 - Erro na linha "+linha+", tipo de retorno inválido.\n";
                            }
                            break;
                        }
                        case "var": {
                            if(!noF.getTipo().equals(no.getTipo())) {
                                this.errosSemanticos += "Erro #30 - Erro na linha "+linha+", tipo de retorno inválido.\n";
                            }
                            break;
                        }
                        case "const": {
                            if(!noF.getTipo().equals(no.getTipo())) {
                                this.errosSemanticos += "Erro #31 - Erro na linha "+linha+", tipo de retorno inválido.\n";
                            }
                            break;
                        }
                        case "typedef": {
                            if(!noF.getTipo().equals(no.getNome())) {
                                this.errosSemanticos += "Erro #32 - Erro na linha "+linha+", tipo de retorno inválido.\n";
                            }
                            break;
                        } 
                        default: {
                            this.errosSemanticos += "Erro #33 - Erro na linha "+linha+", tipo de retorno inválido.\n";
                        }
                    }
                } else {
                    this.errosSemanticos += "Erro #34 - Erro na linha "+linha+", tipo de retorno inválido.\n";
                }   
            } else {
                
                boolean erro = false;
                for(NoRetorno noR : retorno) {
                    
                    //noR.getClasse().contains("Numero")
                    if(noR.getClasse().contains("Identificador")) {
                        NoSemantico noZ = this.tabelaSemantica.getNoIdentificador(noR.getValor(), noF.getNome());
                        if(!noZ.getTipo().equals(noF.getTipo())){
                            erro = true;
                        }
                    }
                }
                if(erro) {
                    this.errosSemanticos += "Erro #35 - Erro na linha "+linha+", tipo de retorno inválido.\n";
                } else if(false) {
                    // IMPLEMENTAR O RESTNATE DAS VERIFICAÇÕES
                }
            }
        }
        // O Else eh erro sintatico.
    }
            
    public void setInitializer(boolean initializer) {
        this.initializer = initializer;
    }

    public boolean isInitializer() {
        return initializer;
    }

    public boolean isInitializer2() {
        return initializer2;
    }

    public void setInitializer2(boolean initializer2) {
        this.initializer2 = initializer2;
    }
    
    public String getErrosSemanticos() {        
        return this.errosSemanticos;
    }

    public void setIgnorarAtribuicao(boolean ignorarAtribuicao) {
        this.ignorarAtribuicao = ignorarAtribuicao;
    }
           
    public void printSemanticTable() {
        
        this.tabelaSemantica.printSemanticTable();
    }
    
}
