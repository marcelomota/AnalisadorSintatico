//package models;
//
//import java.util.ArrayList;
//
//public class AnalisadorSemantico {
//    
//    private ArrayList<NoSemantico> tabelaSemantica;
//    private ArrayList<NoSemantico> funcoesPendentes;
//    private ArrayList<NoSemantico> proceduresPendentes;
//    private String erros; // 13
//    private PilhaEscopo escopo;      
//    private boolean temStart;
//    private boolean ativarAtribuicao;
//    private boolean ativarAtribuicao2;     
//         
//    private static int count;    
//    
//    public AnalisadorSemantico() {
//        
//        AnalisadorSemantico.count = 0;
//        this.tabelaSemantica = new ArrayList();
//        this.funcoesPendentes = new ArrayList();
//        this.proceduresPendentes = new ArrayList();
//        this.erros = "";
//        this.escopo = new PilhaEscopo();
//        this.escopo.addEscopo("global", "global");
//    }
//    
//    public void ativarAtribuicao(boolean ativar) {
//        this.ativarAtribuicao = ativar;
//    }
//    
//    public void ativarAtribuicao2(boolean ativar) {
//        this.ativarAtribuicao2 = ativar;
//    }
//    
//    public void atribuicaoStruct(String nome) {
//        
//        NoSemantico no = this.tabelaSemantica.get(this.getLastIndex());
//        if(no.getDeclaracao().equals("struct")) {
//            
//            for(NoSemantico s : this.tabelaSemantica) {
//                if(s.getNomeEscopo().equals(no.getDeclaracao()) && s.getValorEscopo().equals(no.getNome())) {
//
//                    if(s.getDeclaracao().equals("varStruct") && s.getNome().equals(nome)) {
//                        this.reEmpilhaNo(s.getId());
//                        return;
//                    }
//                }  
//            }
//        }
//    }
//    
//    public void addEscopo(String nome, String valor) {
//                      
//        this.escopo.addEscopo(nome, valor);
//    }
//    
//     public void addEscopo() {
//                      
//        NoSemantico no = this.tabelaSemantica.get(this.getLastIndex());
//        if(no.getDeclaracao().equals("function")) {
//            
//            this.escopo.addEscopo("function", no.getNome());
//        }        
//    }
//    
//    public void removerEscopo() {
//     
//        this.escopo.removeLast();
//    }
//        
//    public void declararStart(String linha) {
//        
//        if(this.temStart) {
//           this.erros += "Erro 01 - Mais de um metodo principal 'Start' foi declarado na linha "+linha+".\n";
//        } else {
//           this.temStart = true;
//        }
//    }
//    
//    public void declararVar(String linha) {
//        
//        NoSemantico no = new NoSemantico();
//        no.setDeclaracao("var");
//        no.setLinhaDeclaracao(linha);
//        no.setNomeEscopo(this.escopo.getLastNomeEscopo());
//        no.setValorEscopo(this.escopo.getLastValorEscopo());
//        this.tabelaSemantica.add(no);
//    }
//    
//    public void declararVar(String tipo, String linha) {
//        
//        NoSemantico no = new NoSemantico();
//        no.setDeclaracao("var");
//        no.setLinhaDeclaracao(linha);
//        no.setTipo(tipo);
//        no.setNomeEscopo(this.escopo.getLastNomeEscopo());
//        no.setValorEscopo(this.escopo.getLastValorEscopo());
//        this.tabelaSemantica.add(no);
//    }
//    
//    public void declararConst(String linha) {    
//
//        NoSemantico no = new NoSemantico();
//        no.setDeclaracao("const");
//        no.setLinhaDeclaracao(linha);
//        no.setNomeEscopo(this.escopo.getLastNomeEscopo());
//        no.setValorEscopo(this.escopo.getLastValorEscopo());
//        this.tabelaSemantica.add(no);
//    }
//    
//    public void declararConst(String tipo, String linha) {
//        
//        NoSemantico no = new NoSemantico();
//        no.setDeclaracao("const");
//        no.setLinhaDeclaracao(linha);
//        no.setTipo(tipo);
//        no.setNomeEscopo(this.escopo.getLastNomeEscopo());
//        no.setValorEscopo(this.escopo.getLastValorEscopo());
//        this.tabelaSemantica.add(no);
//    }
//    
//    public void declararStruct(String linha) {
//        
//        NoSemantico no = new NoSemantico();
//        no.setDeclaracao("struct");
//        no.setLinhaDeclaracao(linha);
//        no.setNomeEscopo(this.escopo.getLastNomeEscopo());
//        no.setValorEscopo(this.escopo.getLastValorEscopo());
//        this.tabelaSemantica.add(no);
//    }
//    
//    public void declararFuncao(String linha) {
//        
//        NoSemantico no = new NoSemantico();
//        no.setDeclaracao("function");
//        no.setLinhaDeclaracao(linha);
//        no.setNomeEscopo(this.escopo.getLastNomeEscopo());
//        no.setValorEscopo(this.escopo.getLastValorEscopo());
//        this.tabelaSemantica.add(no);
//    }
//    
//    public void declararProcedure(String linha) {
//        
//        NoSemantico no = new NoSemantico();
//        no.setDeclaracao("procedure");
//        no.setLinhaDeclaracao(linha);
//        no.setNomeEscopo(this.escopo.getLastNomeEscopo());
//        no.setValorEscopo(this.escopo.getLastValorEscopo());
//        this.tabelaSemantica.add(no);
//    }
//    
//    public void declararTypedef(String linha) {
//        
//        NoSemantico no = new NoSemantico();
//        no.setDeclaracao("typedef");
//        no.setLinhaDeclaracao(linha);
//        no.setNomeEscopo(this.escopo.getLastNomeEscopo());
//        no.setValorEscopo(this.escopo.getLastValorEscopo());
//        this.tabelaSemantica.add(no);
//    }
//    
//    public void declararVarStruct(String tipo, String linha) {
//        
//        NoSemantico no = new NoSemantico();
//        no.setDeclaracao("varStruct");
//        no.setLinhaDeclaracao(linha);
//        no.setTipo(tipo);
//        no.setNomeEscopo(this.escopo.getLastNomeEscopo());
//        no.setValorEscopo(this.escopo.getLastValorEscopo());
//        this.tabelaSemantica.add(no);
//    }
//        
//    public void addTipo(String tipo, String linha) {
//        
//        NoSemantico no = this.tabelaSemantica.get(this.getLastIndex());
//        String escopoAtual = this.escopo.getLastNomeEscopo();
//        if(no.getDeclaracao().equals("function")) {
//            
//            if(no.getTipo().isEmpty()) {
//                no.setTipo(tipo);
//            } else if(no.getValor().isEmpty()) {
//                no.setValor(tipo);
//            } else {
//                no.setValor2(tipo);
//            }
//        } else if(no.getDeclaracao().equals("procedure")) {
//            
//            if(no.getValor().isEmpty()) {
//                no.setValor(tipo);
//            } else {
//                no.setValor2(tipo);
//            }
//        } else if(escopoAtual.equals("struct")) {                      
//            
//            this.declararVarStruct(tipo, linha);
//        } else {
//            
//            if(this.tabelaSemantica.get(this.getLastIndex()).getTipo().isEmpty()) {
//                this.tabelaSemantica.get(this.getLastIndex()).setTipo(tipo);
//            } else {
//                this.erros += "ERRO AO ADICIONAR TIPO\n";
//            }   
//        }        
//    }
//    
//    public void addNome(String nome, String linha) {
//        
//        NoSemantico no = this.tabelaSemantica.get(this.getLastIndex());
//        if(no.getDeclaracao().equals("var")) {
//            
//            if(no.getNome().isEmpty()) {
//                
//                this.verificarNome(no, nome, linha);
//                this.tabelaSemantica.get(this.getLastIndex()).setNome(nome);                                                   
//            } else {
//                 
//                // Se a variavel atual da tabela já tem um nome, adiciona uma nova.
//                this.declararVar(no.getTipo(), linha);
//                this.verificarNome(no, nome, linha);             
//                this.tabelaSemantica.get(this.getLastIndex()).setNome(nome);
//            }            
//        } else if(no.getDeclaracao().equals("const")) {
//            
//            if(no.getNome().isEmpty()) {
//                
//                this.verificarNome(no, nome, linha);           
//                this.tabelaSemantica.get(this.getLastIndex()).setNome(nome);
//            } else {
//                 
//                // Se a constante atual da tabela já tem um nome, adiciona uma nova.
//                this.declararConst(this.tabelaSemantica.get(this.getLastIndex()).getTipo());
//                this.verificarNome(no, nome, linha);                
//                this.tabelaSemantica.get(this.getLastIndex()).setNome(nome);
//            }  
//        } else if(no.getDeclaracao().equals("varStruct")) {
//            
//            if(no.getNome().isEmpty()) {
//                
//                this.verificarNome(no, nome, linha);            
//                this.tabelaSemantica.get(this.getLastIndex()).setNome(nome);
//            } else {
//                 
//                // Se a variavel atual da struct já tem um nome, adiciona uma nova.                 
//                this.declararVarStruct(this.tabelaSemantica.get(this.getLastIndex()).getTipo(), linha);
//                this.verificarNome(no, nome, linha);               
//                this.tabelaSemantica.get(this.getLastIndex()).setNome(nome);
//            }  
//        } else if(no.getDeclaracao().equals("function")) {
//            
//            if(no.getNome().isEmpty()) {
//                
//                int id = this.verificarNomeFuncao(nome);
//                if(id != 0) {
//                    
//                    no.setIdSobrecarga(id);
//                }                
//                this.tabelaSemantica.get(this.getLastIndex()).setNome(nome);                
//            } else {
//                
//                // Se a funcao atual da tabela ja tem nome, adiciona os parametros.
//                if(no.getValor().isEmpty()) {
//                    
//                    no.setValor(nome);
//                } else {
//                    
//                    no.setValor2(nome);
//                }                
//            }
//        } else if(no.getDeclaracao().equals("procedure")) {
//            
//            if(no.getNome().isEmpty()) {
//                
//                int id = this.verificarNomeProcedure(nome);
//                if(id != 0) {
//                    
//                    no.setIdSobrecarga(id);
//                }                
//                this.tabelaSemantica.get(this.getLastIndex()).setNome(nome);                
//            } else {
//                
//                // Se a procedure atual da tabela ja tem nome, adiciona os parametros.
//                if(no.getValor().isEmpty()) {
//                    
//                    no.setValor(nome);
//                } else {
//                    
//                    no.setValor2(nome);
//                }                
//            }
//        } else {
//            
//            if(no.getNome().isEmpty()) {
//                
//                this.verificarNome(no, nome, linha);                
//                this.tabelaSemantica.get(this.getLastIndex()).setNome(nome);
//            } else {
//                this.erros += "ERRO AO ATUALIZAR NOME - Linha "+linha+"\n";
//            }
//        }        
//    }
//    
//    /**
//     * Adiciona parâmetros e argumentos.
//     * @param args 
//     * @param linha 
//     */
//    public void addArgs(String args, String linha) {
//        
//    }
//    
//    /**
//     * Adicoina atribuições.
//     * @param valor
//     * @param linha 
//     */
//    public void addValor(String valor, String linha) {
//        
//        if(this.ativarAtribuicao) {
//        
//            NoSemantico no = this.tabelaSemantica.get(this.getLastIndex());            
//            if(no.getValor().isEmpty()) {
//
//                no.setValor(valor);
//            } else {
//
//                no.setValor3(valor);
//            }
//        } else if(this.ativarAtribuicao2) {
//            
//            this.tabelaSemantica.get(this.getLastIndex()).setValor(valor);
//                        
//        } else {
//            
//            if(!this.verificarValor(valor, linha)) {
//                
//                this.erros += "Erro 13 - Erro na linha "+linha+", '"+valor+"' não foi declarado(a).\n";
//            }             
//        }        
//    }
//    
//    
//    
//         
//    public boolean verificarValor(String valor, String linha) {
//        
//        for(int i=0; i<this.tabelaSemantica.size(); i++) {
//            
//            NoSemantico no = this.tabelaSemantica.get(i);
//            if(no.getNome().equals(valor) && no.getValorEscopo().equals(this.escopo.getLastValorEscopo())) {
//                if(no.getDeclaracao().equals("const")) {
//                    
//                    this.erros += "Erro 12 - Valor da constante '"+valor+"' não pode ser alterado na linha "+linha+".\n";
//                } else {
//                    
//                    this.tabelaSemantica.add(this.tabelaSemantica.remove(i));
//                }                
//                return true;
//            }
//        }        
//         
//        return false;
//    }
//    
//    public void reEmpilhaNo(int id) {
//        
//        for(int i=0; i<this.tabelaSemantica.size(); i++) {
//            
//            NoSemantico no = this.tabelaSemantica.get(i);
//            if(no.getId() == id) {
//                                  
//                this.tabelaSemantica.add(this.tabelaSemantica.remove(i));
//                this.ativarAtribuicao2 = true;
//                                
//                return;
//            }
//        }
//    }
//    
//    
//}
