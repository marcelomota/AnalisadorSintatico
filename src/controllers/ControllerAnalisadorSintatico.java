package controllers;

import models.Token;

public class ControllerAnalisadorSintatico {
          
    private String errosSintaticos;
    private Token tokens;
    private int idTokenAtual;
     
    private ControllerAnalisadorSemantico analisadorSemantico;
    
    public ControllerAnalisadorSintatico() {
        
    }
    
    /**
     * Analisa sintaticamente e semanticamente a sequencia de tokens de forma preditiva e recursiva.
     * @param tokens
     * @param analisadorSemantico
     * @return 
     */
    public String analisar(Token tokens, ControllerAnalisadorSemantico analisadorSemantico) {
        
        this.tokens = tokens;        
        this.idTokenAtual = 0;
        this.errosSintaticos = "";
        this.analisadorSemantico = analisadorSemantico;
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            this.procedureS();
            this.analisadorSemantico.verificarSobre_carga_scrita();
        }        
        
        return this.errosSintaticos;
    }
    
    /**
     * S ::= GlobalDeclaration S1 
     */
    private void procedureS() {
        
        this.procedureGlobalDeclaration();
        this.procedureS1();        
    }

    /**
     * S1 ::= GlobalDeclaration S1 | 
     */
    private void procedureS1() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            // Verifica se o token atual eh o primeiro de <GlobalDeclaration>
            if(atual[1].trim().equals("struct") || atual[1].trim().equals("procedure") ||
                    atual[1].trim().equals("typedef") || atual[1].trim().equals("const") ||
                    atual[1].trim().equals("function") || atual[1].trim().equals("var") ||
                    atual[1].trim().equals("start")) {

                this.procedureGlobalDeclaration();
                this.procedureS1();
            }

            // Vazio
        }   
        
        // Vazio
    }
    
    /**
     * GlobalDeclaration ::= StartDef | VarDef | ConstDef | StructDef | FunctionDef | ProcedureDef | TypedefDef
     */
    private void procedureGlobalDeclaration() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            // Verifica se o token atual eh o primeiro de <StartDef>
            if(atual[1].trim().equals("start")) {

                this.procedureStartDef();  

            // Verifica se o token atual eh o primeiro de <VarDef>    
            } else if(atual[1].trim().equals("var")) {

                this.procedureVarDef(); 

            // Verifica se o token atual eh o primeiro de <ConstDef>      
            } else if(atual[1].trim().equals("const")) {

                this.procedureConstDef(); 

            // Verifica se o token atual eh o primeiro de <StructDef>
            } else if(atual[1].trim().equals("struct")) {

                this.procedureStructDef();  

            // Verifica se o token atual eh o primeiro de <FunctionDef>
            } else if(atual[1].trim().equals("function")) {

                this.procedureFunctionDef(); 

            // Verifica se o token atual eh o primeiro de <ProcedureDef>
            } else if(atual[1].trim().equals("procedure")) {

                this.procedureProcedureDef();

            // Verifica se o token atual eh o primeiro de <TypedefDef>
            } else if(atual[1].trim().equals("typedef")) {

                this.procedureTypedefDef();
            } else {

                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #1 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                this.errosSintaticos += "Erro 1 - Palavra Reservada não encontrada na linha "+linha.trim()+".\n";                
                this.modalidadeDesespero("start, struct, procedure, typedef, const, function, var");
            }
        } else {
            
            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureGlobalDeclaration()");
            this.errosSintaticos += "Erro @1 - Palavra Reservada não encontrada na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";  
        }        
    }
    
    /**
     * FunctionDef ::= 'function' Type Declarator '(' FunctionDeflf
     */
    private void procedureFunctionDef() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
            // Verifica se o token atual eh 'function'
            if(atual[1].trim().equals("function")) {

                this.analisadorSemantico.declararFuncao(atual[2].replaceAll(">", " ").trim());
                this.idTokenAtual++;
                this.analisadorSemantico.addContexto("function");
                this.procedureType();                               
                this.procedureDeclarator();                 
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                    // Verifica se o token atual eh '('
                    if(atual2[1].trim().equals("(")) {

                        this.idTokenAtual++;  
                        this.analisadorSemantico.addEscopo();
                        this.procedureFunctionDeflf();
                        this.analisadorSemantico.removerEscopo();
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        //this.errosSintaticos += "Erro #2 - '"+atual2[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                        this.errosSintaticos += "Erro 2 - Delimitador '(' não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                    }
                } else {

                    //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureFunctionDef()");
                    this.errosSintaticos += "Erro @2 - Delimitador '(' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                }                        
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #3 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                this.errosSintaticos += "Erro 3 - Palavra Reservada 'function' não encontrada na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
            } 
        } else {
            
            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureFunctionDef()");
            this.errosSintaticos += "Erro @3 - Palavra Reservada 'function' não encontrada na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
        }               
    }
    
    /**
     * FunctionDeflf ::= ParameterList ')' '{' StmtOrDeclarationList '}' | ')' '{' StmtOrDeclarationList '}' 
     */
    private void procedureFunctionDeflf() {
        
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            int idTokenInicio = 0;
            int idTokenFim = 0;
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
            // Verifica se o token atual eh o primeiro de <ParameterList>
            if(atual[1].trim().equals("bool") || atual[1].trim().equals("float") ||
                    atual[1].trim().equals("int") || atual[1].trim().equals("string") ||
                    atual[0].contains("Identificador_")) {
                
                this.procedureParameterList();
                this.analisadorSemantico.removerContexto();
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(","); 
                    // Verifica se o token atual eh ')'
                    if(atual2[1].trim().equals(")")) {

                        this.analisadorSemantico.removerContexto();
                        this.idTokenAtual++;
                        if(this.idTokenAtual < this.tokens.getSize()) {

                            String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                            // Verifica se o token atual eh '{'
                            if(atual3[1].trim().equals("{")) {
                                
                                this.idTokenAtual++;    
                                idTokenInicio = this.idTokenAtual;
                                this.procedureStmtOrDeclarationList();
                                if(this.idTokenAtual < this.tokens.getSize()) {

                                    String[] atual4 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                                    // Verifica se o token atual eh '}'
                                    if(atual4[1].trim().equals("}")) {
                                        
                                        idTokenFim = this.idTokenAtual;
                                        this.idTokenAtual++;
                                    } else {

                                        // Erro
                                        String linha = atual4[2].replaceAll(">", " ");
                                        //this.errosSintaticos += "Erro #4 - '"+atual4[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                                        this.errosSintaticos += "Erro 4 - Delimitador '}' não encontrado na linha "+this.getLinhaErro(this.idTokenAtual-1)+".\n";
                                        this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                                    }
                                } else {

                                    //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureFunctionDeflf()");
                                    this.errosSintaticos += "Erro @4 - Delimitador '}' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";                                    
                                }                    
                            } else {

                                // Erro
                                String linha = atual3[2].replaceAll(">", " ");
                                //this.errosSintaticos += "Erro #5 - '"+atual3[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                                this.errosSintaticos += "Erro 5 - Delimitador '{' não encontrado na linha "+this.getLinhaErro(this.idTokenAtual-1)+".\n";
                                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                            }
                        } else {

                            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureFunctionDeflf()");
                            this.errosSintaticos += "Erro @5 - Delimitador '{' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                        }                
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        //this.errosSintaticos += "Erro #6 - '"+atual2[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                        this.errosSintaticos += "Erro 6 - Delimitador ')' não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                    }
                } else {
                    
                    //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureFunctionDeflf()");
                    this.errosSintaticos += "Erro @6 - Delimitador ')' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                }            

            // Verifica se o token atual eh ')'
            } else if(atual[1].trim().equals(")")) {

                this.idTokenAtual++;
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                    // Verifica se o token atual eh '{'
                    if(atual2[1].trim().equals("{")) {
                        
                        this.idTokenAtual++;  
                        idTokenInicio = this.idTokenAtual;
                        this.procedureStmtOrDeclarationList();
                        if(this.idTokenAtual < this.tokens.getSize()) {

                            String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                            // Verifica se o token atual eh '}'
                            if(atual3[1].trim().equals("}")) {

                                idTokenFim = this.idTokenAtual;
                                this.idTokenAtual++;
                            } else {

                                // Erro
                                String linha = atual3[2].replaceAll(">", " ");
                                //this.errosSintaticos += "Erro #7 - '"+atual3[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                                this.errosSintaticos += "Erro 7 - Delimitador '}' não encontrado na linha "+this.getLinhaErro(this.idTokenAtual-1)+".\n";
                                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                            }
                        } else {
                            
                            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureFunctionDeflf()");
                            this.errosSintaticos += "Erro @7 - Delimitador '}' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                        }                
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        //this.errosSintaticos += "Erro #8 - '"+atual2[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                        this.errosSintaticos += "Erro 8 - Delimitador '{' não encontrado na linha "+this.getLinhaErro(this.idTokenAtual-1)+".\n";
                        this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                    }
                } else {
                        
                    //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureFunctionDeflf()");
                    this.errosSintaticos += "Erro @8 - Delimitador '{' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                }            
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #9 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                this.errosSintaticos += "Erro 9* - Declaração inválida na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
            }
            
            if(idTokenInicio != 0 && idTokenFim != 0) {
                this.getConteudoFuncao(idTokenInicio, idTokenFim);
            }   
        } else {
                
            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureFunctionDeflf()");
            this.errosSintaticos += "Erro @9 - Declaração inválida na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
        }        
    }
    
    /**
     * ProcedureDef ::= 'procedure' 'Identifier' '(' ProcedureDefdlf
     */
    private void procedureProcedureDef() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
            // Verifica se o token atual eh 'procedure'
            if(atual[1].trim().equals("procedure")) {

                this.analisadorSemantico.declararProcedure(atual[2].replaceAll(">", " ").trim());                
                this.idTokenAtual++;
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                    // Verifica se o token atual eh 'Identifier'
                    if(atual2[0].contains("Identificador_")) {

                        this.analisadorSemantico.addNomeProcedure(atual2[1].trim(), atual2[2].replaceAll(">", " ").trim());
                        this.idTokenAtual++;
                        if(this.idTokenAtual < this.tokens.getSize()) {

                            String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                            // Verifica se o token atual eh '('
                            if(atual3[1].trim().equals("(")) {

                                this.idTokenAtual++;
                                
                                this.analisadorSemantico.addEscopo();
                                this.procedureProcedureDeflf();
                                this.analisadorSemantico.removerEscopo();
                            } else {

                                // Erro
                                String linha = atual3[2].replaceAll(">", " ");
                                //this.errosSintaticos += "Erro #10 - '"+atual3[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                                this.errosSintaticos += "Erro 10 - Delimitador '(' não encontrado na linha "+linha.trim()+".\n";
                                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                            } 
                        } else {
                                
                            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureProcedureDef()");
                            this.errosSintaticos += "Erro @10 - Delimitador '(' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                        }                               
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        //this.errosSintaticos += "Erro #11 - '"+atual2[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                        this.errosSintaticos += "Erro 11 - Identificador não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                    }
                } else {
                        
                    //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureProcedureDef()");
                    this.errosSintaticos += "Erro @11 - Identificador não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                }            
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #12 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                this.errosSintaticos += "Erro 12 - Palavra Reservada 'procedure' não encontrada na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
            }  
        } else {
            
            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureProcedureDef()");
            this.errosSintaticos += "Erro @12 - Palavra Reservada 'procedure' não encontrada na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
        }              
    }
    
    /**
     * ProcedureDeflf ::= ParameterList ')' '{' StmtOrDeclarationList '}' |  ')' '{' StmtOrDeclarationList '}' 
     */
    private void procedureProcedureDeflf() {
        
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            int idTokenInicio = 0;
            int idTokenFim = 0;
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
            // Verifica se o token atual eh primeiro de <ParameterList>
            if(atual[1].trim().equals("bool") || atual[1].trim().equals("float") ||
                    atual[1].trim().equals("int") || atual[1].trim().equals("string") ||
                    atual[0].contains("Identificador_")) {
                                                
                this.analisadorSemantico.addContexto("procedure");
                this.procedureParameterList();
                this.analisadorSemantico.removerContexto();
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                    // Verifica se o token atual eh ')'
                    if(atual2[1].trim().equals(")")) {

                        this.idTokenAtual++;
                        if(this.idTokenAtual < this.tokens.getSize()) {

                            String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                            // Verifica se o token atual eh '{'
                            if(atual3[1].trim().equals("{")) {
                                                                
                                this.idTokenAtual++;   
                                idTokenInicio = this.idTokenAtual;
                                this.procedureStmtOrDeclarationList();
                                
                                if(this.idTokenAtual < this.tokens.getSize()) {

                                    String[] atual4 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                                    // Verifica se o token atual eh '}'
                                    if(atual4[1].trim().equals("}")) {
                                                 
                                        idTokenFim = this.idTokenAtual;
                                        this.idTokenAtual++; 
                                    } else {

                                        // Erro
                                        String linha = atual4[2].replaceAll(">", " ");
                                        //this.errosSintaticos += "Erro #13 - '"+atual4[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                                        this.errosSintaticos += "Erro 13 - Delimitador '}' não encontrado na linha "+this.getLinhaErro(this.idTokenAtual-1)+".\n";
                                        this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                                    }
                                } else {
                                        
                                    //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureProcedureDeflf()");
                                    this.errosSintaticos += "Erro @13 - Delimitador '}' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                                }                    
                            } else {

                                // Erro
                                String linha = atual3[2].replaceAll(">", " ");
                                //this.errosSintaticos += "Erro #14 - '"+atual3[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                                this.errosSintaticos += "Erro 14 - Delimitador '{' não encontrado na linha "+this.getLinhaErro(this.idTokenAtual-1)+".\n";
                                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                            }
                        } else {
                            
                            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureProcedureDeflf()");
                            this.errosSintaticos += "Erro @14 - Delimitador '{' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                        }                
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        //this.errosSintaticos += "Erro #15 - '"+atual2[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                        this.errosSintaticos += "Erro 15 - Delimitador ')' não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                    }
                } else {
                    
                    //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureProcedureDeflf()");
                    this.errosSintaticos += "Erro @15 - Delimitador ')' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                }            

            // Verifica se o token atual eh ')'    
            } else if(atual[1].trim().equals(")")) {

                this.idTokenAtual++;
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                    // Verifica se o token atual eh '{'
                    if(atual2[1].trim().equals("{")) {
                        
                        this.idTokenAtual++;    
                        idTokenInicio = this.idTokenAtual;
                        this.procedureStmtOrDeclarationList();
                        if(this.idTokenAtual < this.tokens.getSize()) {

                            String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                            // Verifica se o token atual eh '}'
                            if(atual3[1].trim().equals("}")) {
                                
                                idTokenFim = this.idTokenAtual;
                                this.idTokenAtual++;    
                            } else {

                                // Erro
                                String linha = atual3[2].replaceAll(">", " ");
                                //this.errosSintaticos += "Erro #16 - '"+atual3[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                                this.errosSintaticos += "Erro 16 - Delimitador '}' não encontrado na linha "+this.getLinhaErro(this.idTokenAtual-1)+".\n";
                                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                            }
                        } else {
                            
                            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureProcedureDeflf()");
                            this.errosSintaticos += "Erro @16 - Delimitador '}' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                        }                
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        //this.errosSintaticos += "Erro #17 - '"+atual2[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                        this.errosSintaticos += "Erro 17 - Delimitador '{' não encontrado na linha "+this.getLinhaErro(this.idTokenAtual-1)+".\n";
                        this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                    }
                } else {
                    
                    //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureProcedureDeflf()");
                    this.errosSintaticos += "Erro @17 - Delimitador '{' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                }            
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #18 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                this.errosSintaticos += "Erro 18 - Delimitador ')' não encontrado na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
            }
            
            if(idTokenInicio != 0 && idTokenFim != 0) {
                this.getConteudoProcedure(idTokenInicio, idTokenFim);
            } 
        } else {
            
            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureProcedureDeflf()");
            this.errosSintaticos += "Erro @18 - Delimitador ')' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";    
        }        
    }
    
    /**
     * TypedefDef ::= 'typedef' TypedefDeflf 
     */
    private void procedureTypedefDef() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
            // Verifica se o token atual eh 'typedef'
            if(atual[1].trim().equals("typedef")) {

                this.analisadorSemantico.declararTypedef(atual[2].replaceAll(">", " ").trim());
                this.idTokenAtual++;                
                this.procedureTypedefDeflf();                
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #19 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                this.errosSintaticos += "Erro 19 - Palavra Reservada 'typedef' não encontrada na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
            } 
        } else {
            
            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureTypedefDef()");
            this.errosSintaticos += "Erro @19 - Palavra Reservada 'typedef' não encontrada na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
        }               
    }
    
    /**
     * TypedefDeflf ::= Type 'Identifier' ';' | StructDef 'Identifier' ';'
     */
    private void procedureTypedefDeflf() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
            // Verifica se o token atual eh primeiro de <Type>
            if(atual[1].trim().equals("bool") || atual[1].trim().equals("float") ||
                    atual[1].trim().equals("int") || atual[1].trim().equals("string") ||
                    atual[0].contains("Identificador_")) {
                
                this.analisadorSemantico.addContexto("typedef");
                this.procedureType();
                this.analisadorSemantico.removerContexto();
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                    // Verifica se o token atual eh 'Identifier'
                    if(atual2[0].contains("Identificador_")) {

                        this.analisadorSemantico.addNomeTypedef(atual2[1].trim(), atual2[2].replaceAll(">", " ").trim());
                        this.idTokenAtual++;
                        if(this.idTokenAtual < this.tokens.getSize()) {

                            String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                            // Verifica se o token atual eh ';'
                            if(atual3[1].trim().equals(";")) {

                                this.idTokenAtual++;
                            } else {

                                // Erro
                                String linha = atual3[2].replaceAll(">", " ");
                                //this.errosSintaticos += "Erro #20 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                                this.errosSintaticos += "Erro 20 - Delimitador ';' não encontrado na linha "+this.getLinhaErro(idTokenAtual-1)+".\n";
                                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                            }
                        } else {
                            
                            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureTypedefDeflf()");
                            this.errosSintaticos += "Erro @20 - Delimitador ';' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                        }                
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        //this.errosSintaticos += "Erro #21 - '"+atual2[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                        this.errosSintaticos += "Erro 21 - Identificador não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                    }
                } else {
                    
                    //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureTypedefDeflf()");
                    this.errosSintaticos += "Erro @21 - Identificador não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                }            

            // Verifica se o token atual eh primeiro de <StructDef>    
            } else if(atual[1].trim().equals("struct")) {

                this.procedureStructDef();
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                    // Verifica se eh token autual eh 'Identifier'
                    if(atual2[0].contains("Identificador_")) {

                        this.idTokenAtual++;
                        if(this.idTokenAtual < this.tokens.getSize()) {

                            String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                            // Verifica se o token atual eh ';'
                            if(atual3[1].trim().equals(";")) {

                                this.idTokenAtual++;
                            } else {

                                // Erro
                                String linha = atual3[2].replaceAll(">", " ");
                                //this.errosSintaticos += "Erro #22 - '"+atual3[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                                this.errosSintaticos += "Erro 22 - Delimitador ';' não encontrado na linha "+this.getLinhaErro(idTokenAtual-1)+".\n";
                                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                            }  
                        } else {
                            
                            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureTypedefDeflf()");
                            this.errosSintaticos += "Erro @22 - Delimitador ';' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                        }                              
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        //this.errosSintaticos += "Erro #23 - '"+atual2[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                        this.errosSintaticos += "Erro 23 - Identificador não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                    }
                } else {
                    
                    //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureTypedefDeflf()");
                    this.errosSintaticos += "Erro @23 - Identificador não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                }            
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #24 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                this.errosSintaticos += "Erro 24 - Tipo não encontrado na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
            }
        } else {
            
            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureTypedefDeflf()");
            this.errosSintaticos += "Erro @24 - Tipo não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
        }        
    }
    
    /**
     * VarDef ::= 'var' '{' DeclarationList '}'
     */
    private void procedureVarDef() {
       
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
            // Verifica se o token atual eh 'var'
            if(atual[1].trim().equals("var")) {

                this.analisadorSemantico.declararVar(atual[2].replaceAll(">", " ").trim());
                this.idTokenAtual++;      
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                    // Verifica se o token atual eh '{'
                    if(atual2[1].trim().equals("{")) {

                        this.idTokenAtual++;     
                        this.analisadorSemantico.addContexto("var");
                        this.procedureDeclarationList();
                        this.analisadorSemantico.removerContexto();
                        if(this.idTokenAtual < this.tokens.getSize()) {

                            String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                            // Verifica se o token atual eh '}'
                            if(atual3[1].trim().equals("}")) {

                                this.idTokenAtual++;
                            } else {

                                // Erro
                                String linha = atual3[2].replaceAll(">", " ");
                                //this.errosSintaticos += "Erro #25 - '"+atual3[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                                this.errosSintaticos += "Erro 25 - Delimitador '}' não encontrado na linha "+this.getLinhaErro(this.idTokenAtual-1)+".\n";
                                this.modalidadeDesespero("false, (, return, print, ++, --, Numero, }, if, while, ;, {, true, "
                                        + "procedure, struct, typedef, function, start, !, scan, Cadeia_de_Caracteres, const, var, Identificador_");
                            }
                        } else {
                            
                            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureVarDef()");
                            this.errosSintaticos += "Erro @25 - Delimitador '}' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                        }                
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        //this.errosSintaticos += "Erro #26 - '"+atual2[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                        this.errosSintaticos += "Erro 26 - Delimitador '{' não encontrado na linha "+this.getLinhaErro(this.idTokenAtual-1)+".\n";
                        this.modalidadeDesespero("false, (, return, print, ++, --, Numero, }, if, while, ;, {, true, "
                            + "procedure, struct, typedef, function, start, !, scan, Cadeia_de_Caracteres, const, var, Identificador_");
                    }
                } else {
                    
                    //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureVarDef()");
                    this.errosSintaticos += "Erro @26 - Delimitador '{' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                }            
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #27 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                this.errosSintaticos += "Erro 27 - Palavra Reservada 'var' não encontrada na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("false, (, return, print, ++, --, Numero, }, if, while, ;, {, true, "
                    + "procedure, struct, typedef, function, start, !, scan, Cadeia_de_Caracteres, const, var, Identificador_");
            }
        } else {
            
            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureVarDef()");
            this.errosSintaticos += "Erro @27 - Palavra Reservada 'var' não encontrada na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
        }        
    }
    
    /**
     * ConstDef ::= 'const' '{' DeclarationList '}'
     */
    private void procedureConstDef() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
            // Verifica se o token atual eh 'const'
            if(atual[1].trim().equals("const")) {

                this.analisadorSemantico.declararConst(atual[2].replaceAll(">", " ").trim());
                this.idTokenAtual++;
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                    // Verifica se o token atual eh '{'
                    if(atual2[1].trim().equals("{")) {

                        this.idTokenAtual++;
                        this.analisadorSemantico.addContexto("const");
                        this.procedureDeclarationList();
                        this.analisadorSemantico.removerContexto();
                        if(this.idTokenAtual < this.tokens.getSize()) {

                            String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                            // Verifica se o token atual eh '}'
                            if(atual3[1].trim().equals("}")) {

                                this.analisadorSemantico.verificarInicializacaoConst(atual[2].replaceAll(">", " ").trim());
                                this.idTokenAtual++;
                            } else {

                                // Erro
                                String linha = atual3[2].replaceAll(">", " ");
                                //this.errosSintaticos += "Erro #28 - '"+atual3[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                                this.errosSintaticos += "Erro 28 - Delimitador '}' não encontrado na linha "+this.getLinhaErro(this.idTokenAtual-1)+".\n";
                                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                            }
                        } else {
                            
                            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureConstDef()");
                            this.errosSintaticos += "Erro @28 - Delimitador '}' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                        }                
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        //this.errosSintaticos += "Erro #29 - '"+atual2[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                        this.errosSintaticos += "Erro 29 - Delimitador '{' não encontrado na linha "+this.getLinhaErro(this.idTokenAtual-1)+".\n";
                        this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                    }
                } else {
                    
                    //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureConstDef()");
                    this.errosSintaticos += "Erro @29 - Delimitador '{' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                }            
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #30 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                this.errosSintaticos += "Erro 30 - Palavra Reservada 'const' não encontrada na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
            }
        } else {
            
            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureConstDef()");
            this.errosSintaticos += "Erro @30 - Palavra Reservada 'const' não encontrada na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
        }        
    }
    
    /**
     * StructDef ::= 'struct' 'Identifier' StructDeflf 
     */
    private void procedureStructDef() {
        
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
            // Verifica se o token atual eh 'struct'
            if(atual[1].trim().equals("struct")) {

                this.analisadorSemantico.declararStruct(atual[2].replaceAll(">", " ").trim());
                this.idTokenAtual++;    
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                    // Verifica se o token atual eh 'Identifier'
                    if(atual2[0].contains("Identificador_")) {

                        this.analisadorSemantico.addNomeStruct(atual2[1].trim(), atual2[2].replaceAll(">", " ").trim());
                        
                        this.idTokenAtual++;
                        
                        this.analisadorSemantico.addEscopo();
                        this.procedureStructDeflf();
                        this.analisadorSemantico.removerEscopo();
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        //this.errosSintaticos += "Erro #31 - '"+atual2[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                        this.errosSintaticos += "Erro 31 - Identificador não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start, Identificador_");
                    }
                } else {
                    
                    //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureStructDef()");
                    this.errosSintaticos += "Erro @31 - Identificador não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                }            
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #32 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                this.errosSintaticos += "Erro 32 - Palavra Reservada 'struct' não encontrada na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start, Identificador_");
            }
        } else {
            
            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureStructDef()");
            this.errosSintaticos += "Erro @32 - Palavra Reservada 'struct' não encontrada na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
        }        
    }
    
    /**
     * StructDeflf ::= '{' DeclarationList '}' | 'extends' 'Identifier' '{' DeclarationList '}'
    */
    private void procedureStructDeflf() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
            // Verifica se o token atual eh '{'
            if(atual[1].trim().equals("{")) {
                
                this.idTokenAtual++;
                this.procedureDeclarationList();
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                    // Verifica se o token atual eh '}'
                    if(atual2[1].trim().equals("}")) {
                        
                        this.idTokenAtual++;
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        //this.errosSintaticos += "Erro #33 - '"+atual2[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                        this.errosSintaticos += "Erro 33 - Delimitador '}' não encontrado na linha "+this.getLinhaErro(this.idTokenAtual-1)+".\n";
                        this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start, Identificador_");
                    }
                } else {
                    
                    //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureStructDeflf()");
                    this.errosSintaticos += "Erro @33 - Delimitador '}' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                }

            // Verifica se o token atual eh 'extends'    
            } else if(atual[1].trim().equals("extends")) {

                this.idTokenAtual++;
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                    // Verifica se o token atual eh 'Identifier'
                    if(atual2[0].contains("Identificador_")) {

                        this.analisadorSemantico.addExtensao(atual2[1].trim(), atual2[2].replaceAll(">", " ").trim());
                        this.idTokenAtual++;
                        if(this.idTokenAtual < this.tokens.getSize()) {

                            String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                            // Verifica se o token atual eh '{'
                            if(atual3[1].trim().equals("{")) {
                                
                                this.idTokenAtual++;
                                this.procedureDeclarationList();
                                if(this.idTokenAtual < this.tokens.getSize()) {

                                    String[] atual4 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                                    // Verifica se o token atual eh '}'
                                    if(atual4[1].trim().equals("}")) {

                                        this.idTokenAtual++;
                                    } else {

                                        // Erro
                                        String linha = atual4[2].replaceAll(">", " ");
                                        //this.errosSintaticos += "Erro #34 - '"+atual4[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                                        this.errosSintaticos += "Erro 34 - Delimitador '}' não encontrado na linha "+linha.trim()+".\n";
                                        this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start, Identificador_");
                                    }
                                } else {
                                    
                                    //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureStructDeflf()");
                                    this.errosSintaticos += "Erro @34 - Delimitador '}' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                                }                    
                            } else {

                                // Erro
                                String linha = atual3[2].replaceAll(">", " ");
                                //this.errosSintaticos += "Erro #35 - '"+atual3[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                                this.errosSintaticos += "Erro 35 - Delimitador '{' não encontrado na linha "+linha.trim()+".\n";
                                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start, Identificador_");
                            }
                        } else {
                            
                            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureStructDeflf()");
                            this.errosSintaticos += "Erro @35 - Delimitador '{' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                        }                
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        //this.errosSintaticos += "Erro #36 - '"+atual2[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                        this.errosSintaticos += "Erro 36 - Identificador não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start, Identificador_");
                    }
                } else {
                    
                    System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureStructDeflf()");
                    this.errosSintaticos += "Erro @36 - Identificador não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                }            
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #37 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                this.errosSintaticos += "Erro 37* - Delimitador '{' não encontrado na linha "+this.getLinhaErro(this.idTokenAtual-1)+".\n";
                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start, Identificador_");
            }
        } else {
            
            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureStructDeflf()");
            this.errosSintaticos += "Erro @37 - Delimitador '{' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
        }        
    }
    
    /**
     * ParameterList ::= ParameterDeclaration ParameterList1   
     */
    private void procedureParameterList() {
    
        this.procedureParameterDeclaration();
        this.procedureParameterList1();        
    }
    
    /**
     * ParameterList1 ::= ',' ParameterDeclaration ParameterList1 | 
     */
    private void procedureParameterList1() {
        
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
            // Verifica se o token atual eh ','
            if(atual[0].contains("Delimitador") && atual.length == 4) { 

                this.idTokenAtual++;
                this.procedureParameterDeclaration();
                this.procedureParameterList1();
            }

            // Vazio            
        } 
        
        // Vazio
    }
    
    /**
     * ParameterDeclaration ::= Type Declarator
     */
    private void procedureParameterDeclaration() {
    
        this.procedureType();
        this.procedureDeclarator();
    }
    
    /**
     * DeclarationList ::= Declaration DeclarationList1  
     */
    private void procedureDeclarationList() {
    
        this.procedureDeclaration();
        this.procedureDeclarationList1();    
    }
    
    /**
     * DeclarationList1 ::= Declaration DeclarationList1 | 
     */
    private void procedureDeclarationList1() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            // Verifica se o token atual eh primeiro de <Declaration>
            if(atual[1].trim().equals("bool") || atual[1].trim().equals("float") || 
                    atual[1].trim().equals("int") || atual[1].trim().equals("string") ||
                    atual[0].contains("Identificador_")) {

                this.procedureDeclaration();
                this.procedureDeclarationList1();
            }

            // Vazio
        } 
        
        // Vazio
    }
    
    /**
     * Declaration ::= Type InitDeclaratorList ';'
     */
    private void procedureDeclaration() {
    
        this.procedureType();
        this.procedureInitDeclaratorList();
        
        if(this.idTokenAtual < this.tokens.getSize()) {
                        
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            // Verifica se o token atual eh ';'
            if(atual[1].trim().equals(";")) {

                this.idTokenAtual++;          
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #38 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                this.errosSintaticos += "Erro 38 - Delimitador ';' não encontrado na linha "+this.getLinhaErro(idTokenAtual-1)+".\n";
                this.modalidadeDesespero("}, bool, float, int, string, Identificador_");
            }
        } else {
            
            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureDeclaration()");
            this.errosSintaticos += "Erro @38 - Delimitador ';' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
        }          
    }
    
    /**
     * InitDeclaratorList ::= InitDeclarator InitDeclaratorList1
     */
    private void procedureInitDeclaratorList() {
            
        this.procedureInitDeclarator();
        this.procedureInitDeclaratorList1();
    } 
    
    /**
     * InitDeclaratorList1 ::= ',' InitDeclarator InitDeclaratorList1 | 
     */
    private void procedureInitDeclaratorList1() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
            // Verifica se o token atual eh ','
            if(atual[0].contains("Delimitador") && atual.length == 4) { 

                this.idTokenAtual++;
                this.procedureInitDeclarator();
                this.procedureInitDeclaratorList1();
            }

            // Vazio
        } 
        
        // Vazio
    }
    
    /**
     * InitDeclarator ::= Declarator InitDeclaratorlf
     */
    private void procedureInitDeclarator() {
    
        this.procedureDeclarator();
        this.procedureInitDeclaratorlf();
    }
    
    /**
     * InitDeclaratorlf ::= '=' Initializer | 
     */
    private void procedureInitDeclaratorlf() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            // Verifica se o token atual eh '='
            if(atual[1].trim().equals("=")) {
                
                this.idTokenAtual++;   
                this.analisadorSemantico.setInitializer(true);
                this.procedureInitializer();
                this.analisadorSemantico.setInitializer(false);
            }

            // Vazio
        } 
        
        // Vazio
    }
    
    /**
     * Initializer ::= AssignExpr | '{' InitializerList Initializerlf
     */
    private void procedureInitializer() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
            // Verifica se o token atual eh primeiro de <AssignExpr> 
            if(atual[0].contains("Numero") || atual[1].trim().equals("false") ||
                    atual[1].trim().equals("true") || atual[0].contains("Cadeia_de_Caracteres") ||
                    atual[1].trim().equals("(") || atual[0].contains("Identificador_") ||
                    atual[1].trim().equals("!") || atual[1].trim().equals("++") ||
                    atual[1].trim().equals("--")) {

                this.procedureAssignExpr();

            // Verifica se o token atual eh '{'
            } else if(atual[1].trim().equals("{")) {

                this.idTokenAtual++;
                this.procedureInitializerList();
                this.procedureInitializerlf();
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #39 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                this.errosSintaticos += "Erro 39 - Erro de inicialização na linha "+this.getLinhaErro(this.idTokenAtual-1)+".\n";
                this.modalidadeDesespero2(";, }");
            }
        } else {
            
            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureInitializer()");
            this.errosSintaticos += "Erro @39 - Erro de inicialização na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
        }        
    }
    
    /**
     * Initializerlf ::= '}' | ',' '}'    
     */
    private void procedureInitializerlf() {

        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            // Verifica se o token atual eh '}'
            if(atual[1].trim().equals("}")) {

                this.idTokenAtual++;   

            // Verifica se o token atual eh ','         
            } else if(atual[0].contains("Delimitador") && atual.length == 4) {

                this.idTokenAtual++; 
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                    // Verifica se o token atual eh '}'
                    if(atual2[1].trim().equals("}")) {

                        this.idTokenAtual++;    
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        //this.errosSintaticos += "Erro #40 - '"+atual2[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                        this.errosSintaticos += "Erro 40 - Delimitador '}' não encontrado na linha "+this.getLinhaErro(this.idTokenAtual-1)+".\n";
                        this.modalidadeDesespero2("}, ;");                        
                    }
                } else {
                    
                    //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureInitializerlf()");
                    this.errosSintaticos += "Erro @40 - Delimitador '}' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                }            
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #41 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                this.errosSintaticos += "Erro 41 - Delimitador '}' não encontrado na linha "+this.getLinhaErro(this.idTokenAtual-1)+".\n";
                this.modalidadeDesespero2("}, ;");
            }
        } else {
            
            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureInitializerlf()");
            this.errosSintaticos += "Erro @41 - Delimitador '}' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
        }        
    }
    
    /**
     * InitializerList ::= Initializer InitializerList1    
     */
    private void procedureInitializerList() {
    
        this.procedureInitializer();
        this.procedureInitializerList1();
    }
    
    /**
     * InitializerList1 ::= ',' Initializer InitializerList1 | 
     */
    private void procedureInitializerList1() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            // Verifica se o token atual eh ','
            if(atual[0].contains("Delimitador") && atual.length == 4) {

                this.idTokenAtual++;
                this.procedureInitializer();
                this.procedureInitializerList1();
            }

            // Vazio
        }
        
        // Vazio
    }
    
    /**
     * Declarator ::= 'Identifier' Declarator1 
     */
    private void procedureDeclarator() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            // Verifica se o token atual eh 'Identifier'
            if(atual[0].contains("Identificador_")) {
                
                this.analisadorSemantico.addNome(atual[1].trim(), atual[2].replaceAll(">", " ").trim());
                this.idTokenAtual++;                
                this.procedureDeclarator1();
            } else {

                // Erro    
                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #42 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                this.errosSintaticos += "Erro 42 - Identificador não encontrado na linha "+linha.trim()+".\n";
                this.modalidadeDesespero2("=, ;, ), (");
            }
        } else {
            
            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureDeclarator()");
            this.errosSintaticos += "Erro @42 - Identificador não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
        }        
    } 
    
    /**
     * Declarator1 ::= '[' Declarator1lf | 'vazio'
     */
    private void procedureDeclarator1() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            // Verifica se o token atual eh '['
            if(atual[1].trim().equals("[")) {

                this.idTokenAtual++;
                this.procedureDeclarator1lf();
            }

            // Vazio
        } 
        
        // Vazio
    }
    
    /**
     * Declarator1lf ::= CondExpr ']' Declarator1 | ']' Declarator1
     */
    private void procedureDeclarator1lf() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            // Verifica se o token atual eh primeiro de <CondExpr>
            if(atual[1].trim().equals("false") || atual[1].trim().equals("true") ||
                    atual[1].trim().equals("(") || atual[1].trim().equals("!") ||
                    atual[1].trim().equals("++") || atual[1].trim().equals("--") ||
                    atual[0].contains("Numero") || atual[0].contains("Cadeia_de_Caracteres") ||
                    atual[0].contains("Identificador_")) {

                    this.procedureCondExpr();
                    if(this.idTokenAtual < this.tokens.getSize()) {

                        String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                        // Verifica se o token atual eh ']'
                        if(atual2[1].trim().equals("]")) {

                            this.idTokenAtual++;
                            this.procedureDeclarator1();
                        } else {

                            // Erro
                            String linha = atual2[2].replaceAll(">", " ");
                            //this.errosSintaticos += "Erro #43 - '"+atual2[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                            this.errosSintaticos += "Erro 43 - Delimitador ']' não encontrado na linha "+linha.trim()+".\n";
                            this.modalidadeDesespero2("=, ;, ), (");
                        }
                    } else {
                        
                        //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureDeclarator1lf()");
                        this.errosSintaticos += "Erro @43 - Delimitador ']' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                    }                

            // Verifica se o token atual eh ']'
            } else if(atual[1].trim().equals("]")) {

                this.idTokenAtual++;
                this.procedureDeclarator1();            
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #44 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                this.errosSintaticos += "Erro 44 - Delimitador ']' não encontrado na linha "+linha.trim()+".\n";
                this.modalidadeDesespero2("=, ;, ), (");
            }
        } else {
            
            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureDeclarator1lf()");
            this.errosSintaticos += "Erro @44 - Delimitador ']' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
        }        
    }
    
    /**
     * Stmt ::= IterationStmt | ExprStmt | CompoundStmt  | PrintStmt | ScanStmt | IfStmt | ReturnStmt
     */
    private void procedureStmt() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            // Verifica se o token atual eh primeiro de <IterationStmt>
            if(atual[1].trim().equals("while")) {

                this.procedureIterationStmt();

            // Verifica se o token atual eh primeiro de <ExprStmt>    
            } else if(atual[1].trim().equals("false") || atual[1].trim().equals(";") ||
                    atual[1].trim().equals("true") || atual[1].trim().equals("(") ||
                    atual[1].trim().equals("!") || atual[1].trim().equals("++") ||
                    atual[1].trim().equals("--") || atual[0].contains("Numero") ||
                    atual[0].contains("Cadeia_de_Caracteres") || atual[0].contains("Identificador_")) {

                this.procedureExprStmt();

            // Verifica se o token atual eh primeiro de <CompoundStmt>      
            } else if(atual[1].trim().equals("{")) {

                this.procedureCompoundStmt();

            // Verifica se o token atual eh primeiro de <PrintStmt>      
            } else if(atual[1].trim().equals("print")) {

                this.procedurePrintStmt();

            // Verifica se o token atual eh primeiro de <ScanStmt>      
            } else if(atual[1].trim().equals("scan")) {

                this.procedureScanStmt();

            // Verifica se o token atual eh primeiro de <IfStmt>      
            } else if(atual[1].trim().equals("if")) {

                this.procedureIfStmt();

            // Verifica se o token atual eh primeiro de <ReturnStmt>      
            } else if(atual[1].trim().equals("return")) {

                this.procedureReturnStmt();
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #45 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                this.errosSintaticos += "Erro 45 - Declaração esperada não encontrada na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;, "
                        + "{, scan, true, Cadeia_de_Caracteres, var, Identificador_");
            }
        } else {
            
            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureStmt()");
            this.errosSintaticos += "Erro @45 - Declaração esperada não encontrada na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
        }        
    }
    
    /**
     * StmtOrDeclarationList ::= Stmt StmtOrDeclarationList1 | VarDef StmtOrDeclarationList1   
     */
    private void procedureStmtOrDeclarationList() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
            // Verifica se o token atual eh primeiro de <Stmt>
            if(atual[1].trim().equals("false") || atual[1].trim().equals("(")
                    || atual[1].trim().equals("return") || atual[1].trim().equals("print")
                    || atual[1].trim().equals("!") || atual[1].trim().equals("++")
                    || atual[1].trim().equals("--") || atual[0].contains("Numero")
                    || atual[1].trim().equals("if") || atual[1].trim().equals("while")
                    || atual[1].trim().equals(";") || atual[1].trim().equals("{")
                    || atual[1].trim().equals("scan") || atual[1].trim().equals("true")
                    || atual[0].contains("Cadeia_de_Caracteres") || atual[0].contains("Identificador_")) {

                this.procedureStmt();
                this.procedureStmtOrDeclarationList1();

            // Verifica se o token atual eh primeiro de <VarDef>
            } else if(atual[1].trim().equals("var")) {

                this.procedureVarDef(); 
                this.procedureStmtOrDeclarationList1(); 
            } else {

                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #46 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                this.errosSintaticos += "Erro 46 - Declaração inválida na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("}");
            }
        } else {
            
            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureStmtOrDeclarationList()");
            this.errosSintaticos += "Erro @46 - Declaração inválida na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
        }        
    } 
    
    /**
     * StmtOrDeclarationList1 ::= Stmt StmtOrDeclarationList1 | VarDef StmtOrDeclarationList1 |
     */
    private void procedureStmtOrDeclarationList1() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
            // Verifica se o token atual eh primeiro de <Stmt>
            if(atual[1].trim().equals("false") || atual[1].trim().equals("(")
                    || atual[1].trim().equals("return") || atual[1].trim().equals("print")
                    || atual[1].trim().equals("!") || atual[1].trim().equals("++")
                    || atual[1].trim().equals("--") || atual[0].contains("Numero")
                    || atual[1].trim().equals("if") || atual[1].trim().equals("while")
                    || atual[1].trim().equals(";") || atual[1].trim().equals("{")
                    || atual[1].trim().equals("scan") || atual[1].trim().equals("true")
                    || atual[0].contains("Cadeia_de_Caracteres") || atual[0].contains("Identificador_")) {

                this.procedureStmt();
                this.procedureStmtOrDeclarationList1();

            // Verifica se o token atual eh primeiro de <VarDef>
            } else if(atual[1].trim().equals("var")) {

                this.procedureVarDef(); 
                this.procedureStmtOrDeclarationList1(); 
            } 

            // Vazio
        } 
        
        // Vazio
    }
    
    /**
     * StartDef ::= 'start' '(' ')' '{' StmtOrDeclarationList '}'
     */
    private void procedureStartDef() {
            
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
            // Verifica se o token atual eh 'start'
            if(atual[1].trim().equals("start")) {
                                
                this.analisadorSemantico.declararStart(atual[2].replaceAll(">", " ").trim());
                
                this.idTokenAtual++;           
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
                    // Verifica se o token atual eh '('
                    if(atual2[1].trim().equals("(")) {

                        this.idTokenAtual++;
                        if(this.idTokenAtual < this.tokens.getSize()) {

                            String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
                            // Verifica se o token atual eh ')'
                            if(atual3[1].trim().equals(")")) {                    

                                this.idTokenAtual++;
                                if(this.idTokenAtual < this.tokens.getSize()) {

                                    String[] atual4 = this.tokens.getUnicToken(this.idTokenAtual).split(","); 
                                    // Verifica se o token atual eh '{'
                                    if(atual4[1].trim().equals("{")) {
                                        
                                        this.idTokenAtual++;
                                        
                                        this.analisadorSemantico.addEscopo("start", "start");
                                        this.procedureStmtOrDeclarationList();  
                                        this.analisadorSemantico.removerEscopo();
                                        
                                        if(this.idTokenAtual < this.tokens.getSize()) {

                                            String[] atual5 = this.tokens.getUnicToken(this.idTokenAtual).split(","); 
                                            // Verifica se o token atual eh '}'
                                            if(atual5[1].trim().equals("}")) {
                                                                                                
                                                this.idTokenAtual++;                                                                                                
                                            } else {

                                                // Erro                  
                                                String linha = atual5[2].replaceAll(">", " ");
                                                //this.errosSintaticos += "Erro #47 - '"+atual5[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                                                this.errosSintaticos += "Erro 47 - Delimitador '}' não encontrado na linha "+linha.trim()+".\n";
                                                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                                            }
                                        } else {
                                                
                                            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureStartDef()");
                                            this.errosSintaticos += "Erro @47 - Delimitador '}' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                                        }                        
                                    } else {

                                        // Erro
                                        String linha = atual4[2].replaceAll(">", " ");
                                        //this.errosSintaticos += "Erro #48 - '"+atual4[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                                        this.errosSintaticos += "Erro 48 - Delimitador '{' não encontrado na linha "+linha.trim()+".\n";
                                        this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                                    }
                                } else {
                                    
                                    //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureStartDef()");
                                    this.errosSintaticos += "Erro @48 - Delimitador '{' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                                }                    
                            } else {

                                // Erro
                                String linha = atual3[2].replaceAll(">", " ");
                                //this.errosSintaticos += "Erro #49 - '"+atual3[1].trim()+"' inesperado na linha "+linha.trim()+".\n"; 
                                this.errosSintaticos += "Erro 49 - Delimitador ')' não encontrado na linha "+linha.trim()+".\n";   
                                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                            }
                        } else {
                            
                            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureStartDef()");
                            this.errosSintaticos += "Erro @49 - Delimitador ')' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";   
                        }                
                    } else {

                        // Erro  
                        String linha = atual2[2].replaceAll(">", " ");
                        //this.errosSintaticos += "Erro #50 - '"+atual2[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                        this.errosSintaticos += "Erro 50 - Delimitador '(' não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                    }
                } else {
                    
                    //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureStartDef()");
                    this.errosSintaticos += "Erro @50 - Delimitador '(' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                }            
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #51 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                this.errosSintaticos += "Erro 51 - Palavra Reservada 'Start' esperada na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
            }
        } else {
            
            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureStartDef()");
            this.errosSintaticos += "Erro @51 - Palavra Reservada 'Start' esperada na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
        }                
    }
    
    /**
     * PrintStmt ::= 'print' '(' ArgumentList ')' ';'    
     */
    private void procedurePrintStmt() {
        
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
            // Verifica se o token atual eh 'print'
            if(atual[1].trim().equals("print")) {

                this.idTokenAtual++;
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
                    // Verifica se o token atual eh '('
                    if(atual2[1].trim().equals("(")) {

                        this.idTokenAtual++;
                        this.procedureArgumentList();
                        if(this.idTokenAtual < this.tokens.getSize()) {

                            String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
                            // Verifica se o token atual eh ')'
                            if(atual3[1].trim().equals(")")) {

                                this.idTokenAtual++;
                                if(this.idTokenAtual < this.tokens.getSize()) {

                                    String[] atual4 = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
                                    // Verifica se o token atual eh ';'
                                    if(atual4[1].trim().equals(";")) {

                                        this.idTokenAtual++;
                                    } else {

                                        // Erro
                                        String linha = atual4[2].replaceAll(">", " ");
                                        //this.errosSintaticos += "Erro #52 - '"+atual4[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                                        this.errosSintaticos += "Erro 52 - Delimitador ';' não encontrado na linha "+linha.trim()+".\n";
                                        this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;,"
                                                + " {, scan, true, Cadeia_de_Caracteres, var, Identificador_");
                                    }
                                } else {
                                    
                                    //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedurePrintStmt()");
                                    this.errosSintaticos += "Erro @52 - Delimitador ';' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                                }                    
                            } else {

                                // Erro
                                String linha = atual3[2].replaceAll(">", " ");
                                //this.errosSintaticos += "Erro #53 - '"+atual3[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                                this.errosSintaticos += "Erro 53 - Delimitador ')' não encontrado na linha "+linha.trim()+".\n";
                                this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;,"
                                                + " {, scan, true, Cadeia_de_Caracteres, var, Identificador_");
                            }
                        } else {
                            
                            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedurePrintStmt()");
                            this.errosSintaticos += "Erro @53 - Delimitador ')' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                        }                
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        //this.errosSintaticos += "Erro #54 - '"+atual2[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                        this.errosSintaticos += "Erro 54 - Delimitador '(' não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;,"
                                                + " {, scan, true, Cadeia_de_Caracteres, var, Identificador_");
                    }
                } else {
                    
                    //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedurePrintStmt()");
                    this.errosSintaticos += "Erro @54 - Delimitador '(' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                }            
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #55 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                this.errosSintaticos += "Erro 55 - Palavra Reservada 'print' não encontrada na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;,"
                                                + " {, scan, true, Cadeia_de_Caracteres, var, Identificador_");
            }
        } else {
            
            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedurePrintStmt()");
            this.errosSintaticos += "Erro @55 - Palavra Reservada 'print' não encontrada na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
        }        
    } 
    
    /**
     * ScanStmt ::= 'scan' '(' ArgumentList ')' ';'
     */
    private void procedureScanStmt() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
            // Verifica se o token atual eh 'scan'
            if(atual[1].trim().equals("scan")) {

                this.idTokenAtual++;
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
                    // Verifica se o token atual eh '('
                    if(atual2[1].trim().equals("(")) {

                        this.idTokenAtual++;
                        this.procedureArgumentList();
                        if(this.idTokenAtual < this.tokens.getSize()) {

                            String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
                            // Verifica se o token atual eh ')'
                            if(atual3[1].trim().equals(")")) {

                                this.idTokenAtual++;
                                if(this.idTokenAtual < this.tokens.getSize()) {

                                    String[] atual4 = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
                                    // Verifica se o token atual eh ';'
                                    if(atual4[1].trim().equals(";")) {

                                        this.idTokenAtual++;
                                    } else {

                                        // Erro
                                        String linha = atual4[2].replaceAll(">", " ");
                                        //this.errosSintaticos += "Erro #56 - '"+atual4[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                                        this.errosSintaticos += "Erro 56 - Delimitador ';' não encontrado na linha "+linha.trim()+".\n";
                                        this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;,"
                                                + " {, scan, true, Cadeia_de_Caracteres, var, Identificador_");
                                    }
                                } else {
                                    
                                    //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureScanStmt()");
                                    this.errosSintaticos += "Erro @56 - Delimitador ';' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                                }                    
                            } else {

                                // Erro
                                String linha = atual3[2].replaceAll(">", " ");
                                //this.errosSintaticos += "Erro #57 - '"+atual3[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                                this.errosSintaticos += "Erro 57 - Delimitador ')' não encontrado na linha "+linha.trim()+".\n";
                                this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;,"
                                                + " {, scan, true, Cadeia_de_Caracteres, var, Identificador_");
                            }
                        } else {
                            
                            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureScanStmt()");
                            this.errosSintaticos += "Erro @57 - Delimitador ')' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                        }                
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        //this.errosSintaticos += "Erro #58 - '"+atual2[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                        this.errosSintaticos += "Erro 58 - Delimitador '(' não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;,"
                                                + " {, scan, true, Cadeia_de_Caracteres, var, Identificador_");
                    }
                } else {
                    
                    //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureScanStmt()");
                    this.errosSintaticos += "Erro @58 - Delimitador '(' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                }            
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #59 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                this.errosSintaticos += "Erro 59 - Palavra Reservada 'scan' não encontrada na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;,"
                                                + " {, scan, true, Cadeia_de_Caracteres, var, Identificador_");
            }
        } else {
            
            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureScanStmt()");
            this.errosSintaticos += "Erro @59 - Palavra Reservada 'scan' não encontrada na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
        }        
    }
    
    /**
     * IterationStmt ::= 'while' '(' Expr ')' Stmt
     */
    private void procedureIterationStmt() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
            // Verifica se o token atual eh 'while'
            if(atual[1].trim().equals("while")) {

                this.idTokenAtual++;
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
                    // Verifica se o token atual eh '('
                    if(atual2[1].trim().equals("(")) {

                        this.idTokenAtual++;
                        this.procedureExpr();
                        if(this.idTokenAtual < this.tokens.getSize()) {

                            String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
                            // Verifica se o token atual eh ')'
                            if(atual3[1].trim().equals(")")) {

                                this.idTokenAtual++;
                                this.procedureStmt();
                            } else {

                                // Erro
                                String linha = atual3[2].replaceAll(">", " ");
                                //this.errosSintaticos += "Erro #60 - '"+atual3[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                                this.errosSintaticos += "Erro 60 - Delimitador ')' não encontrado na linha "+linha.trim()+".\n";
                                this.modalidadeDesespero("}, false, (, return, print, else, !, ++, --, Numero, if,"
                                        + " while, ;, {, scan, true, Cadeia_de_Caracteres, var, Identificador_");
                            }
                        } else {
                            
                            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureIterationStmt()");
                            this.errosSintaticos += "Erro @60 - Delimitador ')' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                        }                
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        //this.errosSintaticos += "Erro #61 - '"+atual2[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                        this.errosSintaticos += "Erro 61 - Delimitador '(' não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if,"
                                        + " while, ;, {, scan, true, Cadeia_de_Caracteres, var, Identificador_");
                    }
                } else {
                    
                    //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureIterationStmt()");
                    this.errosSintaticos += "Erro @61 - Delimitador '(' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                }            
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #62 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                this.errosSintaticos += "Erro 62 - Palavra Reservada 'while' não encontrada na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if,"
                                        + " while, ;, {, scan, true, Cadeia_de_Caracteres, var, Identificador_");
            }
        } else {
            
            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureIterationStmt()");
            this.errosSintaticos += "Erro @62 - Palavra Reservada 'while' não encontrada na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
        }        
    }
    
    /**
     * IfStmt ::= 'if' Expr 'then' Stmt IfStmtlf  
     */
    private void procedureIfStmt() {
        
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
            // Verifica se o token atual eh 'while'
            if(atual[1].trim().equals("if")) {

                this.idTokenAtual++;
                this.procedureExpr();
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
                    // Verifica se o token atual eh 'then'
                    if(atual2[1].trim().equals("then")) {

                        this.idTokenAtual++;
                        this.procedureStmt();
                        this.procedureIfStmtlf();
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        //this.errosSintaticos += "Erro #63 - '"+atual2[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                        this.errosSintaticos += "Erro 63 - Palavra Reservada 'then' não encontrada na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;, {,"
                                + " scan, true, Cadeia_de_Caracteres, var, Identificador_");
                    }
                } else {
                    
                    //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureIfStmt()");
                    this.errosSintaticos += "Erro @63 - Palavra Reservada 'then' não encontrada na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                }
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #64 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                this.errosSintaticos += "Erro 64 - Palavra Reservada 'if' não encontrado na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;, {,"
                                + " scan, true, Cadeia_de_Caracteres, var, Identificador_");
            }
        } else {
            
            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureIfStmt()");
            this.errosSintaticos += "Erro @64 - Palavra Reservada 'if' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
        }        
    }    
    
    /**
     * IfStmtlf ::= 'else' Stmt |
     */
    private void procedureIfStmtlf() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
            // Verifica se o token atual eh 'else'
            if(atual[1].trim().equals("else")) {

                this.idTokenAtual++;
                this.procedureStmt();
            }

            // Vazio
        } 

        // Vazio
    }
    
    /**
     * ReturnStmt ::= 'return' Expr ';'
     */
    private void procedureReturnStmt() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
            // Verifica se o token atual eh 'return'
            if(atual[1].trim().equals("return")) {

                this.idTokenAtual++;
                this.procedureExpr();
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
                    // Verifica se o token atual eh ';'
                    if(atual2[1].trim().equals(";")) {

                        this.idTokenAtual++;
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        //this.errosSintaticos += "Erro #65 - '"+atual2[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                        this.errosSintaticos += "Erro 65 - Delimitador ';' não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;,"
                                + " {, scan, true, Cadeia_de_Caracteres, var, Identificacor_");
                    }
                } else {
                    
                    //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureReturnStmt()");
                    this.errosSintaticos += "Erro @65 - Delimitador ';' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                }            
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #66 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                this.errosSintaticos += "Erro 66 - Palavra Reservada 'return' não encontrada na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;,"
                                + " {, scan, true, Cadeia_de_Caracteres, var, Identificacor_");
            }
        } else {
            
            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureReturnStmt()");
            this.errosSintaticos += "Erro @66 - Palavra Reservada 'return' não encontrada na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
        }        
    }
    
    /**
     * CompoundStmt ::= '{' CompoundStmtlf 
     */
    private void procedureCompoundStmt() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
            // Verifica se o token atual eh '{'
            if(atual[1].trim().equals("{")) {

                this.idTokenAtual++;
                this.procedureCompoundStmtlf();
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #67 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                this.errosSintaticos += "Erro 67 - Delimitador '{' não encontrado na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;,"
                        + " {, scan, true, Cadeia_de_Caracteres, var, Identificador_");
            }
        } else {
                
            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureCompoundStmt()");
            this.errosSintaticos += "Erro @67 - Delimitador '{' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
        }                
    } 
    
    /**
     * CompoundStmtlf ::= '}' | StmtOrDeclarationList '}'
     */
    private void procedureCompoundStmtlf() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
            // Verifica se o token atual eh '}'
            if(atual[1].trim().equals("}")) {

                this.idTokenAtual++;

            // Verifica se o token atual eh primeiro de <StmtOrDeclarationList>  
            } else if(atual[1].trim().equals("false") || atual[1].trim().equals("(") ||
                    atual[1].trim().equals("return") || atual[1].trim().equals("print") ||
                    atual[1].trim().equals("!") || atual[1].trim().equals("++") ||
                    atual[1].trim().equals("--") || atual[1].trim().equals("var") ||
                    atual[1].trim().equals("if") || atual[1].trim().equals("while") ||
                    atual[1].trim().equals(";") || atual[1].trim().equals("{") || 
                    atual[1].trim().equals("scan") || atual[1].trim().equals("true") ||
                    atual[0].contains("Cadeia_de_Caracteres") || atual[0].contains("Identificador_")) {

                this.procedureStmtOrDeclarationList();

                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
                    // Verifica se o token atual eh '}'
                    if(atual2[1].trim().equals("}")) {

                        this.idTokenAtual++;
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        //this.errosSintaticos += "Erro #68 - '"+atual2[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                        this.errosSintaticos += "Erro 68 - Delimitador '}' não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;,"
                                + " {, scan, true, Cadeia_de_Caracteres, var, Identificador_");
                    }
                } else {
                    
                    //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureCompoundStmtlf()");
                    this.errosSintaticos += "Erro @68 - Delimitador '}' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                }            
            } else {
                
                // Erro
                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #69 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                this.errosSintaticos += "Erro 69 - Declaração inválida na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;,"
                                + " {, scan, true, Cadeia_de_Caracteres, var, Identificador_");
            }
        } else {
            
            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureCompoundStmtlf()");
            this.errosSintaticos += "Erro @69 - Declaração inválida na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
        }       
    }
    
    /**
     * ExprStmt ::= ';' | Expr ';'
     */
    private void procedureExprStmt() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
            // Verifica se o token atual eh ';'
            if(atual[1].trim().equals(";")) {

                this.analisadorSemantico.setIgnorarAtribuicao(false);
                this.idTokenAtual++;

            // Verifica se o token atual eh primeiro de <Expr>    
            } else if(atual[1].trim().equals("false") || atual[1].trim().equals("true") ||
                    atual[1].trim().equals("(") || atual[1].trim().equals("!") ||
                    atual[1].trim().equals("++") || atual[1].trim().equals("--") ||
                    atual[0].contains("Numero") || atual[0].contains("Cadeia_de_Caracteres") ||
                    atual[0].contains("Identificador_")) {

                this.procedureExpr();
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
                    // Verifica se o token atual eh ';'
                    if(atual2[1].trim().equals(";")) {

                        this.analisadorSemantico.setIgnorarAtribuicao(false);
                        this.idTokenAtual++;
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        //this.errosSintaticos += "Erro #70 - '"+atual2[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                        this.errosSintaticos += "Erro 70 - Delimitador ';' não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;,"
                                + " {, scan, true, Cadeia_de_Caracteres, var, Identificador_");
                    }
                } else {
                    
                    //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureExprStmt()");
                    this.errosSintaticos += "Erro @70 - Delimitador ';' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                }            
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #71 - "+atual[1].trim()+ "inesperado na linha"+linha.trim()+".\n";
                this.errosSintaticos += "Erro 71 - Delimitador ';' não encontrado na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;,"
                                + " {, scan, true, Cadeia_de_Caracteres, var, Identificador_");
            }
        } else {
            
            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureExprStmt()");
            this.errosSintaticos += "Erro @71 - Delimitador ';' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
        }        
    }
    
    /**
     * Expr ::= AssignExpr Expr1     
     */
    private void procedureExpr() {
    
        this.procedureAssignExpr();
        this.procedureExpr1();
    }   
    
    /**
     * Expr1 ::= ',' AssignExpr Expr1 | 
     */
    private void procedureExpr1() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            String linhaS = atual[2].replaceAll(">", " ").trim();
            // Verifica se o token atual eh ','
            if(atual[0].contains("Delimitador") && atual.length == 4) {

                this.analisadorSemantico.addValor(",", linhaS, "Op");
                this.idTokenAtual++;
                this.procedureAssignExpr();
                this.procedureExpr1();
            }

            // Vazio
        } 
        
        // Vazio
    }
    
    /**
     * AssignExpr ::= CondExpr AssignExpr1  
     */
    private void procedureAssignExpr() {
    
        this.procedureCondExpr();
        this.procedureAssignExpr1();
    }    
    
    /**
     * AssignExpr1 ::= '=' CondExpr AssignExpr1 | 
     */
    private void procedureAssignExpr1() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
            // Verifica se o token atual eh '='
            if(atual[1].trim().equals("=")) {

                
                this.idTokenAtual++;
                if(this.analisadorSemantico.isInitializer()) {
                    
                    this.analisadorSemantico.addValor("=", atual[2].replaceAll(">", " ").trim(), "Op");
                    this.procedureCondExpr();
                    this.procedureAssignExpr1();
                } else {
                    
                    this.procedureCondExpr();
                    this.procedureAssignExpr1();
                    this.analisadorSemantico.setInitializer2(false);
                }                
            }

            // Vazio
        } 
        
        // Vazio
    }
    
    /**
     * CondExpr ::= LogicalOrExpr   
     */
    private void procedureCondExpr() {
    
        this.procedureLogicalOrExpr();
    }    
    
    /**
     * LogicalOrExpr ::= LogicalAndExpr LogicalOrExpr1  
     */
    private void procedureLogicalOrExpr() {
    
        this.procedureLogicalAndExpr();
        this.procedureLogicalOrExpr1();
    }   
    
    /**
     * LogicalOrExpr1 ::= '||' LogicalAndExpr LogicalOrExpr1 |
     */
    private void procedureLogicalOrExpr1() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
            String linhaS = atual[2].replaceAll(">", " ");
            // Verifica se o token atual eh '||'
            if(atual[1].trim().equals("||")) {

                this.analisadorSemantico.addValor("||", linhaS, "Op");
                this.idTokenAtual++;
                this.procedureLogicalAndExpr();
                this.procedureLogicalOrExpr1();
            }

            // Vazio
        } 
        
        // Vazio
    }
    
    /**
     * LogicalAndExpr ::= EqualExpr LogicalAndExpr1
     */
    private void procedureLogicalAndExpr() {
    
        this.procedureEqualExpr();
        this.procedureLogicalAndExpr1();
    }
    
    /**
     * LogicalAndExpr1 ::= '&&' EqualExpr LogicalAndExpr1 | 
     */
    private void procedureLogicalAndExpr1() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
            String linhaS = atual[2].replaceAll(">", " ").trim();
            // Verifica se o token atual eh '&&'
            if(atual[1].trim().equals("&&")) {

                this.analisadorSemantico.addValor("&&", linhaS, "Op");
                this.idTokenAtual++;
                this.procedureEqualExpr();
                this.procedureLogicalAndExpr1();
            }

            // Vazio
        } 
        
        // Vazio
    }
    
    /**
     * EqualExpr ::= RelationalExpr EqualExpr1
     */
    private void procedureEqualExpr() {
    
        this.procedureRelationalExpr();
        this.procedureEqualExpr1();
    }
    
    /**
     * EqualExpr1 ::= EqualOp RelationalExpr EqualExpr1 | 
     */
    private void procedureEqualExpr1() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
            // Verifica se o token atual eh primeiro de <EqualOp>
            if(atual[1].trim().equals("!=") || atual[1].trim().equals("==")) {

                this.procedureEqualOp();
                this.procedureRelationalExpr();
                this.procedureEqualExpr1();
            }

            // Vazio
        } 
        
        // Vazio
    }
    
    /**
     * RelationalExpr ::= AdditiveExpr RelationalExpr1  
     */
    private void procedureRelationalExpr() {
    
        this.procedureAdditiveExpr();
        this.procedureRelationalExpr1();
    } 
    
    /**
     * RelationalExpr1 ::= RelationalOp AdditiveExpr RelationalExpr1 | 
     */
    private void procedureRelationalExpr1() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
            // Verifica se o token atual eh primeiro de <RelationalOp>
            if(atual[1].trim().equals("<") || atual[1].trim().equals("<=") ||
                    atual[1].trim().equals(">") || atual[1].trim().equals(">=")) {

                this.procedureRelationalOp();
                this.procedureAdditiveExpr();
                this.procedureRelationalExpr1();
            }

            // Vazio
        } 
        
        // Vazio
    }
    
    /**
     * AdditiveExpr ::= MultExpr AdditiveExpr1    
     */
    private void procedureAdditiveExpr() {
    
        this.procedureMultExpr();
        this.procedureAdditiveExpr1();
    }  
    
    /**
     * AdditiveExpr1 ::= AdditiveOp MultExpr AdditiveExpr1 | 
     */
    private void procedureAdditiveExpr1() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
            // Verifica se o token atual eh primeiro de <AdditiveOp>
            if(atual[1].trim().equals("-") || atual[1].trim().equals("+")) {

                this.procedureAdditiveOp();
                this.procedureMultExpr();
                this.procedureAdditiveExpr1();
            }

            // Vazio
        } 
        
        // Vazio
    }
    
    /**
     * MultExpr ::= UnaryExpr MultExpr1  
     */
    private void procedureMultExpr() {
        
        this.procedureUnaryExpr();
        this.procedureMultExpr1();
    } 
    
    /**
     * MultExpr1 ::= MultOp UnaryExpr MultExpr1 | 
     */
    private void procedureMultExpr1() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
            // Verifica se o token atual eh primeiro de <MultOp>
            if(atual[1].trim().equals("*") || atual[1].trim().equals("/")) {

                this.procedureMultOp();
                this.procedureUnaryExpr();
                this.procedureMultExpr1();
            }

            // Vazio
        } 
        
        // Vazio
    }
    
    /**
     * UnaryExpr ::= UnaryOp UnaryExpr | PostfixExpr 
     */
    private void procedureUnaryExpr() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
            // Verifica se o token atual eh primeiro de <UnaryOp>
            if(atual[1].trim().equals("!") || atual[1].trim().equals("++") || atual[1].trim().equals("--")) {

                this.procedureUnaryOp();
                this.procedureUnaryExpr();

            // Verifica se o token atual eh primeiro de <PostfixExpr>    
            } else if(atual[1].trim().equals("false") || atual[1].trim().equals("true") ||
                    atual[1].trim().equals("(") || atual[0].contains("Numero") ||
                    atual[0].contains("Cadeia_de_Caracteres") || atual[0].contains("Identificador_")) {

                this.procedurePostfixExpr();
            } else {
                
                // Erro
                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #72 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                this.errosSintaticos += "Erro 72 - Expressão inválida na linha "+linha.trim()+".\n";
                this.modalidadeDesespero2("-, +, then, *, ), <=, ||, ==, &&, >, =, ], }, <, !=, >=, ;, /");
            }
        } else {
            
            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureUnaryExpr()");
            this.errosSintaticos += "Erro @72 - Expressão inválida na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
        }        
    }   
    
    /**
     * PostfixExpr ::= PrimaryExpr PostfixExpr1
     */
    private void procedurePostfixExpr() {
        
        this.procedurePrimaryExpr();
        this.procedurePostfixExpr1();             
    }           
    
    /**
     * PostfixExpr1 ::= PostfixOp PostfixExpr1 |
     */
    private void procedurePostfixExpr1() {
                
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            //verifica se está no primeiro de <PostfixOp>
            if(atual[1].trim().equals("[") || atual[1].trim().equals("(") ||
               atual[1].trim().equals("++" )|| atual[1].trim().equals("--") ||
               atual[1].trim().equals(".")){

                this.procedurePostfixOp();
                this.procedurePostfixExpr1();
            }

            // Vazio
        } 
        
        // Vazio
    }
    
    /**
     * PrimaryExpr ::= 'Identifier' | 'Number' | 'Literal' | 'true' | 'false' | '(' Expr ')' 
     */
    private void procedurePrimaryExpr() {
        
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            String linhaS = atual[2].replaceAll(">", " ").trim();
            // Verifica se o token atual eh 'Identifier'
            if(atual[0].contains("Identificador_")){

                this.analisadorSemantico.addValor(atual[1].trim(), linhaS, "Identificador");
                this.idTokenAtual++;

            // Verifica se o token atual eh 'Number'    
            } else if(atual[0].contains("Numero")) {

                this.analisadorSemantico.addValor(atual[1].trim(), linhaS, "Op");
                this.idTokenAtual++;

            // Verifica se o token atual eh 'Literal'    
            } else if(atual[0].contains("Cadeia_de_Caracteres")) {

                this.analisadorSemantico.addValor(atual[1].trim(), linhaS, "Op");
                this.idTokenAtual++;

            // Verifica se o token atual eh 'true'    
            } else if(atual[1].trim().equals("true")) {

                this.analisadorSemantico.addValor(atual[1].trim(), linhaS, "Op");
                this.idTokenAtual++;

            // Verifica se o token atual eh 'false'    
            } else if(atual[1].trim().equals("false")) {

                this.analisadorSemantico.addValor(atual[1].trim(), linhaS, "Op");
                this.idTokenAtual++;

            // Verifica se o token atual eh '('    
            } else if(atual[1].trim().equals("(")) {

                this.analisadorSemantico.addValor("(", linhaS, "Op");
                this.idTokenAtual++;
                this.procedureExpr();
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                    String linhaS2 = atual[2].replaceAll(">", " ");
                    // Verifica se o token atual eh ')'
                    if(atual2[1].trim().equals(")")) {

                        this.analisadorSemantico.addValor(")", linhaS, "Op");
                        this.idTokenAtual++;
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        //this.errosSintaticos += "Erro #73 - '"+atual2[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                        this.errosSintaticos += "Erro 73 - Delimitador ')' não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero2("*, (, <=, ++, --, ==, =, }, !=, ;, /, -,'+, then, ), ||, &&, >, ], <, [, >=, .");
                    }  
                } else {
                    
                    //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedurePrimaryExpr()");
                    this.errosSintaticos += "Erro @73 - Delimitador ')' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                }                      
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #74 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                this.errosSintaticos += "Erro 74 - Expressão mal formada na linha "+linha.trim()+".\n";
                this.modalidadeDesespero2("*, (, <=, ++, --, ==, =, }, !=, ;, /, -,'+, then, ), ||, &&, >, ], <, [, >=, .");
            }
        } else {
            
            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedurePrimaryExpr()");
            this.errosSintaticos += "Erro @74 - Expressão mal formada na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
        }        
    }
    
    /**
     * EqualOp ::= '==' | '!='
     */
    private void procedureEqualOp() {
            
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            String linhaS = atual[2].replaceAll(">", " ").trim();
            // Verifica se o token atual eh '=='
            if(atual[1].trim().equals("==")){

                this.analisadorSemantico.addValor("==", linhaS, "Op");
                this.idTokenAtual++;

            // Verifica se o token atual é '!='    
            } else if(atual[1].trim().equals("!=")){

                this.analisadorSemantico.addValor("!=", linhaS, "Op");
                this.idTokenAtual++;
            } else{

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #75 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                this.errosSintaticos += "Erro 75 - Operador ('==' ou '!=') não encontrado na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("Numero, false, true, Cadeia_de_Caracteres, (, Identificador_, !, ++, --");
            }
        } else {
            
            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureEqualOp()");
            this.errosSintaticos += "Erro @75 - Operador ('==' ou '!=') não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
        }        
    }
    
    /**
     * RelationalOp ::= '<' | '>' | '<=' | '>='       
     */
    private void procedureRelationalOp() {
        
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            String linhaS = atual[2].replaceAll(">", " ").trim();
            // Verifica se o token atual eh '<'
            if(atual[1].trim().equals("<")){

                this.analisadorSemantico.addValor("<", linhaS, "Op");
                this.idTokenAtual++;

            // Verifica se o token atual eh '>'    
            } else if(atual[1].trim().equals(">")){

                this.analisadorSemantico.addValor(">", linhaS, "Op");
                this.idTokenAtual++;

            // Verifica se o token atual eh '<='    
            } else if(atual[1].trim().equals("<=")){

                this.analisadorSemantico.addValor("<=", linhaS, "Op");
                this.idTokenAtual++;

            // Verifica se o token atual eh '>='    
            } else if(atual[1].trim().equals(">=")){

                this.analisadorSemantico.addValor(">=", linhaS, "Op");
                this.idTokenAtual++;
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #76 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                this.errosSintaticos += "Erro 76 - Operador ('<', '>', '<=', '>=' ou '>') não encontrado na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("Numero, false, true, Cadeia_de_Caracteres, (, Identificador_, !, ++, --");
            }
        } else {
            
            System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureRelationalOp()");
            this.errosSintaticos += "Erro @76 - Operador ('<', '>', '<=', '>=' ou '>') não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
        }        
    }      
    
    /**
     * AdditiveOp ::= '+' | '-'
     */
    private void procedureAdditiveOp() {
        
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            String linhaS = atual[2].replaceAll(">", " ").trim();
            // Verifica se o token atual eh '+'
            if(atual[1].trim().equals("+")){

                this.analisadorSemantico.addValor("+", linhaS, "Op");
                this.idTokenAtual++;

            // Verifica se o token atual eh '-'    
            } else if(atual[1].trim().equals("-")){

                this.analisadorSemantico.addValor("-", linhaS, "Op");
                this.idTokenAtual++;            
            } else{

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #77 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                this.errosSintaticos += "Erro 77 - Operador ('+' ou '-') não encontrado na linha "+linha.trim()+".\n";
                this.modalidadeDesespero2("then, ), <=, ||, ==, &&, >, =, ], }, <, !=, >=, ;");
            }
        } else {
            
            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureAdditiveOp()");
            this.errosSintaticos += "Erro @77 - Operador ('+' ou '-') não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
        }        
    }   
    
    /**
     * MultOp ::= '*' | '/'
     */
    private void procedureMultOp() {
        
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            String linhaS = atual[2].replaceAll(">", " ").trim();
            // Verifica se o token atual eh '*'
            if(atual[1].trim().equals("*")){

                this.analisadorSemantico.addValor("*", linhaS, "Op");
                this.idTokenAtual++;

            // Verifica se o token atual eh '/'
            } else if(atual[1].trim().equals("/")){

                this.analisadorSemantico.addValor("/", linhaS, "Op");
                this.idTokenAtual++;        
            } else{

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #78 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                this.errosSintaticos += "Erro 78 - Operador ('*' ou '/') não encontrado na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("Numero, false, true, Cadeia_de_Caracteres, (, Identificador_, !, ++, --");
                
            }
        } else {
            
            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureMultOp()");
            this.errosSintaticos += "Erro @78 - Operador ('*' ou '/') não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
        }        
    }   
    
    /**
     * UnaryOp ::= '++' | '--' | '!'
     */
    private void procedureUnaryOp() {
        
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            String linhaS = atual[2].replaceAll(">", " ").trim();
            // Verifica se o token atual eh '++'
            if(atual[1].trim().equals("++")){

                this.analisadorSemantico.addValor("++", linhaS, "Op");
                this.idTokenAtual++;

            // Verifica se o token atual eh '--'    
            } else if(atual[1].trim().equals("--")){

                this.analisadorSemantico.addValor("--", linhaS, "Op");
                this.idTokenAtual++;

            // Verifica se o token atual eh '!'    
            } else if(atual[1].trim().equals("!")){

                this.analisadorSemantico.addValor("!", linhaS, "Op");
                this.idTokenAtual++;
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #79 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                this.errosSintaticos += "Erro 79 - Operador ('++', '--' ou '!') não encontrado na linha "+linha.trim()+".\n"; 
                this.modalidadeDesespero2("-, +, then, *, ), <=, ||, ==, &&, >, =, ], }, <, !=, >=, ;, /");
            }
        } else {
            
            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureUnaryOp()");
            this.errosSintaticos += "Erro @79 - Operador ('++', '--' ou '!') não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n"; 
        }        
    }
    
    /**
     * PostfixOp ::= '++' | '--' | '[' Expr ']' | '(' PostfixOplf | '.' 'Identifier'              
     */
    private void procedurePostfixOp() {
                
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            String linhaS = atual[2].replaceAll(">", " ").trim();
            // Verifica se o token atual é '++'
            if(atual[1].trim().equals("++")){

                this.analisadorSemantico.addValor("++", linhaS, "Op");
                this.idTokenAtual++;

            // Verifica se o token atual é '--'    
            } else if(atual[1].trim().equals("--")){

                this.analisadorSemantico.addValor("--", linhaS, "Op");
                this.idTokenAtual++;

            // Verifica se o token atual é '['    
            } else if(atual[1].trim().equals("[")){

                this.analisadorSemantico.addValor("[", linhaS, "Op");
                this.idTokenAtual++;
                this.procedureExpr();
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                    // Verifica se o token atual é ']'
                    if(atual2[1].trim().equals("]")){

                        this.analisadorSemantico.addValor("]", atual2[2].replaceAll(">", " ").trim(), "Op");
                        this.idTokenAtual++;
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        //this.errosSintaticos += "Erro #80 - '"+atual2[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                        this.errosSintaticos += "Erro 80 - Delimitador ']' não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero2("*, (, <=, ++, --, ==, =, }, !=, ;, /, -, +, then, ), ||, &&, >, ], <, [, >=, .");
                    }
                } else {
                    
                    //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedurePostfixOp()");
                    this.errosSintaticos += "Erro @80 - Delimitador ']' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                }

            // Verifica se o token atual é '('      
            } else if(atual[1].trim().equals("(")){

                this.analisadorSemantico.addValor("(", linhaS, "Op");
                this.idTokenAtual++;
                this.procedurePostfixOplf();  

            // Verifica se o token atual é '.'      
            } else if(atual[1].trim().equals(".")){

                this.idTokenAtual++;
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                    // Verifica se o token atual é um 'Identifier'
                    if(atual2[0].contains("Identificador_")){

                        this.analisadorSemantico.atribuicaoStruct(atual2[1].trim(), linhaS);
                        this.idTokenAtual++;
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        //this.errosSintaticos += "Erro #81 - '"+atual2[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                        this.errosSintaticos += "Erro 81 - Identificador não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero2("*, (, <=, ++, --, ==, =, }, !=, ;, /, -, +, then, ), ||, &&, >, ], <, [, >=, .");
                    }
                } else {
                    
                    //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedurePostfixOp()");
                    this.errosSintaticos += "Erro @81 - Identificador não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                }
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #82 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";  
                this.errosSintaticos += "Erro 82 - Operador ou Delimitador não encontrado na linha "+linha.trim()+".\n";  
                this.modalidadeDesespero2("*, (, <=, ++, --, ==, =, }, !=, ;, /, -, +, then, ), ||, &&, >, ], <, [, >=, .");
            }
        } else {
            
            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedurePostfixOp()");
            this.errosSintaticos += "Erro @82 - Operador ou Delimitador não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";  
        }   
    } 
    
    /**
     * PostfixOplf ::= ')' | ArgumentList ')'
     */
    private void procedurePostfixOplf() {
        
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            // Verifica se o token atual é ')'
            if(atual[1].trim().equals(")")){

                this.analisadorSemantico.addValor(")", atual[2].replaceAll(">", " ").trim(), "Op");
                this.idTokenAtual++; 

            // Verifica se o token atual eh primeiro de <ArgumentList>
            } else if(atual[0].contains("Numero") || atual[1].trim().equals("false") ||
                    atual[1].trim().equals("true") || atual[0].contains("Cadeia_de_Caracteres") ||
                    atual[1].trim().equals("(") || atual[0].contains("Identificador_") ||
                    atual[1].trim().equals("!") || atual[1].trim().equals("++") ||
                    atual[1].trim().equals("--")) {

                this.procedureArgumentList();
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                    // Verifica se o token atual eh ')'
                    if(atual2[1].trim().equals(")")){

                        this.analisadorSemantico.addValor(")", atual2[2].replaceAll(">", " ").trim(), "Op");
                        this.idTokenAtual++;
                    } else{

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        //this.errosSintaticos += "Erro #83 '"+atual2[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                        this.errosSintaticos += "Erro 83 - Delimitador ')' não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero2("-, +, then, *, ), (, <=, ||, ++, --, ==, &&, >, =, ], }, <, !=, [, >=, ;, /, .");
                    } 
                } else {
                    
                    //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedurePostfixOplf()");
                    this.errosSintaticos += "Erro @83 - Delimitador ')' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
                }    
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                //this.errosSintaticos += "Erro #84 - '"+atual[1].trim()+"' inesperado na linha "+linha.trim()+".\n";
                this.errosSintaticos += "Erro 84 - Delimitador ')' não encontrado na linha "+linha.trim()+".\n";
                this.modalidadeDesespero2("-, +, then, *, ), (, <=, ||, ++, --, ==, &&, >, =, ], }, <, !=, [, >=, ;, /, .");
            } 
        } else {
            
            // System.err.println("Erro - idTokenAtual > tokens.getSize() em procedurePostfixOplf()");
            this.errosSintaticos += "Erro @84 - Delimitador ')' não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
        }  
    }
    
    /**
     * ArgumentList ::= AssignExpr ArgumentList1            
     */
    private void procedureArgumentList() {
        
        this.procedureAssignExpr();
        this.procedureArgumentList1();   
    }
    
    /**
     * ArgumentList1 ::= ',' AssignExpr ArgumentList1 |
     */
    private void procedureArgumentList1() {
        
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            // Verifica se o token atual eh ','
            if(atual[0].contains("Delimitador") && atual.length == 4) {

                this.analisadorSemantico.addValor(",", atual[2].replaceAll(">", " ").trim(), "op");
                this.idTokenAtual++;
                this.procedureAssignExpr();
                this.procedureArgumentList1();
            }

            // Vazio            
        } 
        
        // Vazio
    }
    
    /**
     * Type ::= 'int' | 'string' | 'float' | 'bool'  | 'Identifier'
     */
    private void procedureType() {
        
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            String linha = atual[2].replaceAll(">", " ").trim();
            // Verifica se o token atual eh 'int'
            if(atual[1].trim().equals("int")) {

                this.analisadorSemantico.addTipo("int", linha);
                this.idTokenAtual++;

            // Verifica se o token atual eh 'string'    
            } else if(atual[1].trim().equals("string")) {

                this.analisadorSemantico.addTipo("string", linha);  
                this.idTokenAtual++;

            // Verifica se o token atual eh 'float'        
            } else if(atual[1].trim().equals("float")) {

                this.analisadorSemantico.addTipo("float", linha);
                this.idTokenAtual++;

            // Verifica se o token atual eh 'bool'   
            } else if(atual[1].trim().equals("bool")) {

                this.analisadorSemantico.addTipo("bool", linha);
                this.idTokenAtual++;

            // Verifica se o token atual eh 'Identifier'       
            } else if(atual[0].contains("Identificador_")) {

                this.analisadorSemantico.addTipo(atual[1].trim(), linha);
                this.idTokenAtual++;            
            } else  {

                // Erro
                //this.errosSintaticos += "Erro #85 - '"+atual[1].trim()+"' inesperado na linha " +linha.trim()+".\n";
                this.errosSintaticos += "Erro 85 - Tipo não encontrado na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("Identificador_");
            }
        } else {
            
            //System.err.println("Erro - idTokenAtual > tokens.getSize() em procedureType()");
            this.errosSintaticos += "Erro @85 - Tipo não encontrado na linha "+this.getLinhaErro(this.tokens.getSize()-1)+".\n";
        }        
    }
    
    /**
     * Utiliza como token de sincronização, algum token do conjunto seguinte da produção atual.
     * @param follow 
     */
    private void modalidadeDesespero(String follow) {
        
        do {
            
            if(this.idTokenAtual < this.tokens.getSize()) {
                 
                String[] seguintes = follow.split(",");
                for (String seguinte : seguintes) {

                    if (this.tokens.getUnicToken(idTokenAtual).contains(seguinte.trim())) {

                        return;
                    }             
                }   
                this.idTokenAtual++;
            } else {

                break;
            } 
        } while(true);        
    }
    
    /**
     * Utilizado quando simbolos que fazem parte do token, fazem parte tambem do conjunto seguinte (',', '<', '>').
     * @param follow 
     */
    private void modalidadeDesespero2(String follow) {
        
        do {
            
            if(this.idTokenAtual < this.tokens.getSize()) {
            
                String[] seguintes = follow.split(",");
                for (String seguinte : seguintes) {

                    String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                    if (atual[1].trim().equals(seguinte.trim()) || atual[0].contains(seguinte.trim()) ||
                            (atual[0].contains("Delimitador") && atual.length == 4)) {

                        return;
                    }               
                }
                this.idTokenAtual++;
            } else {

               break; 
            }
        } while(true);        
    }
    
    private int getLinhaErro(int index) {
                
        String[] atual = this.tokens.getUnicToken(index).split(",");
        if(atual.length == 4) {
            
            String linha = atual[3].replaceAll(">", " ");
            return Integer.parseInt(linha.trim());
        } else {
            
            String linha = atual[2].replaceAll(">", " ");
            return Integer.parseInt(linha.trim());
        }        
    }
    
    private void getConteudoFuncao(int idTokenInicio, int idTokenFim) {
        
        String conteudo = "";
        boolean temReturn = false;
        int tokenAtual = idTokenInicio;
        
        while(tokenAtual < idTokenFim) {
            
            String[] atual = this.tokens.getUnicToken(tokenAtual).split(",");
            if(atual[1].trim().equals("return")) {
                temReturn = true;
            }             
            conteudo += atual[1].trim();
            tokenAtual++;
        }
        
        this.analisadorSemantico.addConteudoFuncao(conteudo, temReturn);
    }
    
    private void getConteudoProcedure(int idTokenInicio, int idTokenFim) {
        
        String conteudo = "";
        boolean temReturn = false;
        String linhaRetorno = "";
        int tokenAtual = idTokenInicio;
                
        while(tokenAtual < idTokenFim) {
            
            String[] atual = this.tokens.getUnicToken(tokenAtual).split(",");            
            if(atual[1].trim().equals("return")) {
                temReturn = true;
                linhaRetorno = atual[2].replaceAll(">", " ").trim();
            }
            conteudo += atual[1].trim();
            tokenAtual++;
        }
        
        this.analisadorSemantico.addConteudoProcedure(conteudo, temReturn, linhaRetorno);
    }
    
}
