package controllers;

import models.Token;

public class ControllerAnalisadorSintatico {
          
    private String errosSintaticos;
    private Token tokens;
    private int idTokenAtual;
    
    public ControllerAnalisadorSintatico() {
        
    }
    
    /**
     * Analisa sintaticamente a sequencia de tokens de forma preditiva e recursiva.
     * @param tokens
     * @return 
     */
    public String analisar(Token tokens) {
        
        this.tokens = tokens;        
        this.idTokenAtual = 0;
        this.errosSintaticos = "";
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            this.procedureS();
        }        
        
        return this.errosSintaticos;
    }
    
    /**
     * <S> ::= <GlobalDeclaration> <S1>   
     */
    private void procedureS() {
        
        this.procedureGlobalDeclaration();
        this.procedureS1();        
    }

    /**
     * <S1> ::= <GlobalDeclaration> <S1> | 
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
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }
    
    /**
     * <GlobalDeclaration> ::= <StartDef> | <VarDef> | <ConstDef> | <StructDef> | <FunctionDef> | <ProcedureDef> | <TypedefDef>
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
                this.errosSintaticos += "Erro - Palavra Reservada não encontrada na linha "+linha.trim()+".\n";                
                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
            }
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }
    
    /**
     * <FunctionDef> ::= 'function' <Type> <Declarator> '(' <FunctionDeflf>
     */
    private void procedureFunctionDef() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
            // Verifica se o token atual eh 'function'
            if(atual[1].trim().equals("function")) {

                this.idTokenAtual++;
                this.procedureType();
                this.procedureDeclarator();
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                    // Verifica se o token atual eh '('
                    if(atual2[1].trim().equals("(")) {

                        this.idTokenAtual++;
                        this.procedureFunctionDeflf();
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        this.errosSintaticos += "Erro - Delimitador '(' não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                    }
                } else {

                    this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                }                        
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Palavra Reservada 'function' não encontrada na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
            } 
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }               
    }
    
    /**
     * <FunctionDeflf> ::= <ParameterList> ')' '{' <StmtOrDeclarationList> '}' | ')' '{' <StmtOrDeclarationList> '}' 
     */
    private void procedureFunctionDeflf() {
        
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
            // Verifica se o token atual eh o primeiro de <ParameterList>
            if(atual[1].trim().equals("bool") || atual[1].trim().equals("float") ||
                    atual[1].trim().equals("int") || atual[1].trim().equals("string") ||
                    atual[0].contains("Identificador_")) {

                this.procedureParameterList();
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
                                this.procedureStmtOrDeclarationList();
                                if(this.idTokenAtual < this.tokens.getSize()) {

                                    String[] atual4 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                                    // Verifica se o token atual eh '}'
                                    if(atual4[1].trim().equals("}")) {

                                        this.idTokenAtual++;
                                    } else {

                                        // Erro
                                        String linha = atual4[2].replaceAll(">", " ");
                                        this.errosSintaticos += "Erro - Delimitador '}' não encontrado na linha "+linha.trim()+".\n";
                                        this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                                    }
                                } else {

                                    this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                                }                    
                            } else {

                                // Erro
                                String linha = atual3[2].replaceAll(">", " ");
                                this.errosSintaticos += "Erro - Delimitador '{' não encontrado na linha "+linha.trim()+".\n";
                                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                            }
                        } else {

                            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                        }                
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        this.errosSintaticos += "Erro - Delimitador ')' não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                    }
                } else {

                    this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                }            

            // Verifica se o token atual eh ')'
            } else if(atual[1].trim().equals(")")) {

                this.idTokenAtual++;
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                    // Verifica se o token atual eh '{'
                    if(atual2[1].trim().equals("{")) {

                        this.idTokenAtual++;
                        this.procedureStmtOrDeclarationList();
                        if(this.idTokenAtual < this.tokens.getSize()) {

                            String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                            // Verifica se o token atual eh '}'
                            if(atual3[1].trim().equals("}")) {

                                this.idTokenAtual++;
                            } else {

                                // Erro
                                String linha = atual3[2].replaceAll(">", " ");
                                this.errosSintaticos += "Erro - Delimitador '}' não encontrado na linha "+linha.trim()+".\n";
                                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                            }
                        } else {

                            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                        }                
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        this.errosSintaticos += "Erro - Delimitador '{' não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                    }
                } else {

                    this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                }            
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Delimitador ')' não encontrado na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
            }
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }
    
    /**
     * <ProcedureDef> ::= 'procedure' 'Identifier' '(' <ProcedureDefdlf>
     */
    private void procedureProcedureDef() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
            // Verifica se o token atual eh 'procedure'
            if(atual[1].trim().equals("procedure")) {

                this.idTokenAtual++;
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                    // Verifica se o token atual eh 'Identifier'
                    if(atual2[0].contains("Identificador_")) {

                        this.idTokenAtual++;
                        if(this.idTokenAtual < this.tokens.getSize()) {

                            String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                            // Verifica se o token atual eh '('
                            if(atual3[1].trim().equals("(")) {

                                this.idTokenAtual++;
                                this.procedureProcedureDeflf();
                            } else {

                                // Erro
                                String linha = atual3[2].replaceAll(">", " ");
                                this.errosSintaticos += "Erro - Delimitador '(' não encontrado na linha "+linha.trim()+".\n";
                                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                            } 
                        } else {

                            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                        }                               
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        this.errosSintaticos += "Erro - Identificador não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                    }
                } else {

                    this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                }            
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Palavra Reservada 'procedure' não encontrada na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
            }  
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }              
    }
    
    /**
     * <ProcedureDeflf> ::= <ParameterList> ')' '{' <StmtOrDeclarationList> '}' |  ')' '{' <StmtOrDeclarationList> '}' 
     */
    private void procedureProcedureDeflf() {
        
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
            // Verifica se o token atual eh primeiro de <ParameterList>
            if(atual[1].trim().equals("bool") || atual[1].trim().equals("float") ||
                    atual[1].trim().equals("int") || atual[1].trim().equals("string") ||
                    atual[0].contains("Identificador_")) {

                this.procedureParameterList();
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
                                this.procedureStmtOrDeclarationList();
                                if(this.idTokenAtual < this.tokens.getSize()) {

                                    String[] atual4 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                                    // Verifica se o token atual eh '}'
                                    if(atual4[1].trim().equals("}")) {

                                        this.idTokenAtual++;
                                    } else {

                                        // Erro
                                        String linha = atual4[2].replaceAll(">", " ");
                                        this.errosSintaticos += "Erro - Delimitador '}' não encontrado na linha "+linha.trim()+".\n";
                                        this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                                    }
                                } else {

                                    this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                                }                    
                            } else {

                                // Erro
                                String linha = atual3[2].replaceAll(">", " ");
                                this.errosSintaticos += "Erro - Delimitador '{' não encontrado na linha "+linha.trim()+".\n";
                                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                            }
                        } else {

                            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                        }                
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        this.errosSintaticos += "Erro - Delimitador ')' não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                    }
                } else {

                    this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                }            

            // Verifica se o token atual eh ')'    
            } else if(atual[1].trim().equals(")")) {

                this.idTokenAtual++;
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                    // Verifica se o token atual eh '{'
                    if(atual2[1].trim().equals("{")) {

                        this.idTokenAtual++;
                        this.procedureStmtOrDeclarationList();
                        if(this.idTokenAtual < this.tokens.getSize()) {

                            String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                            // Verifica se o token atual eh '}'
                            if(atual3[1].trim().equals("}")) {

                                this.idTokenAtual++;
                            } else {

                                // Erro
                                String linha = atual3[2].replaceAll(">", " ");
                                this.errosSintaticos += "Erro - Delimitador '}' não encontrado na linha "+linha.trim()+".\n";
                                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                            }
                        } else {

                            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                        }                
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        this.errosSintaticos += "Erro - Delimitador '{' não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                    }
                } else {

                    this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                }            
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Delimitador ')' não encontrado na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
            }
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }
    
    /**
     * <TypedefDef> ::= 'typedef' <TypedefDeflf> 
     */
    private void procedureTypedefDef() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
            // Verifica se o token atual eh 'typedef'
            if(atual[1].trim().equals("typedef")) {

                this.idTokenAtual++;
                this.procedureTypedefDeflf();
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Palavra Reservada 'typedef' não encontrada na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
            } 
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }               
    }
    
    /**
     * <TypedefDeflf> ::= <Type> 'Identifier' ';' | <StructDef> 'Identifier' ';'
     */
    private void procedureTypedefDeflf() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
            // Verifica se o token atual eh primeiro de <Type>
            if(atual[1].trim().equals("bool") || atual[1].trim().equals("float") ||
                    atual[1].trim().equals("int") || atual[1].trim().equals("string") ||
                    atual[0].contains("Identificador_")) {

                this.procedureType();
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                    // Verifica se o token atual eh 'Identifier'
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
                                this.errosSintaticos += "Erro - Delimitador ';' não encontrado na linha "+linha.trim()+".\n";
                                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                            }
                        } else {

                            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                        }                
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        this.errosSintaticos += "Erro - Identificador não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                    }
                } else {

                    this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
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
                                this.errosSintaticos += "Erro - Delimitador ';' não encontrado na linha "+linha.trim()+".\n";
                                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                            }  
                        } else {

                            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                        }                              
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        this.errosSintaticos += "Erro - Identificador não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                    }
                } else {

                    this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                }            
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Tipo não encontrado na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
            }
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }
    
    /**
     * <VarDef> ::= 'var' '{' <DeclarationList> '}'
     */
    private void procedureVarDef() {
       
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
            // Verifica se o token atual eh 'var'
            if(atual[1].trim().equals("var")) {

                this.idTokenAtual++;      
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                    // Verifica se o token atual eh '{'
                    if(atual2[1].trim().equals("{")) {

                        this.idTokenAtual++;
                        this.procedureDeclarationList();
                        if(this.idTokenAtual < this.tokens.getSize()) {

                            String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                            // Verifica se o token atual eh '}'
                            if(atual3[1].trim().equals("}")) {

                                this.idTokenAtual++;
                            } else {

                                // Erro
                                String linha = atual3[2].replaceAll(">", " ");
                                this.errosSintaticos += "Erro - Delimitador '}' não encontrado na linha "+linha.trim()+".\n";
                                this.modalidadeDesespero("false, (, return, print, ++, --, Numero, }, if, while, ;, {, true, "
                                        + "procedure, struct, typedef, function, start, !, scan, Cadeia_de_Caracteres, const, var, Identificador_");
                            }
                        } else {

                            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                        }                
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        this.errosSintaticos += "Erro - Delimitador '{' não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("false, (, return, print, ++, --, Numero, }, if, while, ;, {, true, "
                            + "procedure, struct, typedef, function, start, !, scan, Cadeia_de_Caracteres, const, var, Identificador_");
                    }
                } else {

                    this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                }            
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Palavra Reservada 'var' não encontrada na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("false, (, return, print, ++, --, Numero, }, if, while, ;, {, true, "
                    + "procedure, struct, typedef, function, start, !, scan, Cadeia_de_Caracteres, const, var, Identificador_");
            }
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }
    
    /**
     * <ConstDef> ::= 'const' '{' <DeclarationList> '}'
     */
    private void procedureConstDef() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
            // Verifica se o token atual eh 'const'
            if(atual[1].trim().equals("const")) {

                this.idTokenAtual++;
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                    // Verifica se o token atual eh '{'
                    if(atual2[1].trim().equals("{")) {

                        this.idTokenAtual++;
                        this.procedureDeclarationList();
                        if(this.idTokenAtual < this.tokens.getSize()) {

                            String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                            // Verifica se o token atual eh '}'
                            if(atual3[1].trim().equals("}")) {

                                this.idTokenAtual++;
                            } else {

                                // Erro
                                String linha = atual3[2].replaceAll(">", " ");
                                this.errosSintaticos += "Erro - Delimitador '}' não encontrado na linha "+linha.trim()+".\n";
                                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                            }
                        } else {

                            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                        }                
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        this.errosSintaticos += "Erro - Delimitador '{' não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                    }
                } else {

                    this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                }            
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Palavra Reservada 'const' não encontrada na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
            }
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }
    
    /**
     * <StructDef> ::= 'struct' 'Identifier' <StructDeflf> 
     */
    private void procedureStructDef() {
        
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
            // Verifica se o token atual eh 'struct'
            if(atual[1].trim().equals("struct")) {

                this.idTokenAtual++;    
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                    // Verifica se o token atual eh 'Identifier'
                    if(atual2[0].contains("Identificador_")) {

                        this.idTokenAtual++;
                        this.procedureStructDeflf();
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        this.errosSintaticos += "Erro - Identificador não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start, Identificador_");
                    }
                } else {

                    this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                }            
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Palavra Reservada 'struct' não encontrada na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start, Identificador_");
            }
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }
    
    /**
     * <StructDeflf> ::= '{' <DeclarationList> '}' | 'extends' 'Identifier' '{' <DeclarationList> '}'
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
                        this.errosSintaticos += "Erro - Delimitador '}' não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start, Identificador_");
                    }
                } else {

                    this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                }

            // Verifica se o token atual eh 'extends'    
            } else if(atual[1].trim().equals("extends")) {

                this.idTokenAtual++;
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                    // Verifica se o token atual eh '}'
                    if(atual2[1].trim().equals("}")) {

                        this.idTokenAtual++;
                        if(this.idTokenAtual < this.tokens.getSize()) {

                            String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                            // Verifica se o token atual eh '{'
                            if(atual3[0].contains("Identificador_")) {

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
                                        this.errosSintaticos += "Erro - Delimitador '}' não encontrado na linha "+linha.trim()+".\n";
                                        this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start, Identificador_");
                                    }
                                } else {

                                    this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                                }                    
                            } else {

                                // Erro
                                String linha = atual3[2].replaceAll(">", " ");
                                this.errosSintaticos += "Erro - Identificador não encontrado na linha "+linha.trim()+".\n";
                                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start, Identificador_");
                            }
                        } else {

                            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                        }                
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        this.errosSintaticos += "Erro - Delimitador '}' não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start, Identificador_");
                    }
                } else {

                    this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                }            
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Delimitador '{' não encontrado na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start, Identificador_");
            }
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }
    
    /**
     * <ParameterList> ::= <ParameterDeclaration> <ParameterList1>   
     */
    private void procedureParameterList() {
    
        this.procedureParameterDeclaration();
        this.procedureParameterList1();        
    }
    
    /**
     * <ParameterList1> ::= ',' <ParameterDeclaration> <ParameterList1> | 
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
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        } 
    }
    
    /**
     * <ParameterDeclaration> ::= <Type> <Declarator>
     */
    private void procedureParameterDeclaration() {
    
        this.procedureType();
        this.procedureDeclarator();
    }
    
    /**
     * <DeclarationList> ::= <Declaration> <DeclarationList1>  
     */
    private void procedureDeclarationList() {
    
        this.procedureDeclaration();
        this.procedureDeclarationList1();    
    }
    
    /**
     * <DeclarationList1> ::= <Declaration> <DeclarationList1> | 
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
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }
    
    /**
     * <Declaration> ::= <Type> <InitDeclaratorList> ';'
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
                this.errosSintaticos += "Erro - Delimitador ';' não encontrado na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("}, bool, float, int, string, Identificador_");
            }
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }          
    }
    
    /**
     * <InitDeclaratorList> ::= <InitDeclarator> <InitDeclaratorList1>
     */
    private void procedureInitDeclaratorList() {
            
        this.procedureInitDeclarator();
        this.procedureInitDeclaratorList1();
    } 
    
    /**
     * <InitDeclaratorList1> ::= ',' <InitDeclarator> <InitDeclaratorList1> | 
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
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }
    
    /**
     * <InitDeclarator> ::= <Declarator> <InitDeclaratorlf>
     */
    private void procedureInitDeclarator() {
    
        this.procedureDeclarator();
        this.procedureInitDeclaratorlf();
    }
    
    /**
     * <InitDeclaratorlf> ::= '=' <Initializer> | 
     */
    private void procedureInitDeclaratorlf() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            // Verifica se o token atual eh '='
            if(atual[1].trim().equals("=")) {

                this.idTokenAtual++;    
                this.procedureInitializer();
            }

            // Vazio
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }
    
    /**
     * <Initializer> ::= <AssignExpr> | '{' <InitializerList> <Initializerlf>
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
                this.errosSintaticos += "Erro - Delimitador '{' não encontrado na linha "+linha.trim()+".\n";
                this.modalidadeDesespero2("}, ;");
            }
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }
    
    /**
     * <Initializerlf> ::= '}' | ',' '}'    
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
                        this.errosSintaticos += "Erro - Delimitador '}' não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero2("}, ;");
                        
                    }
                } else {

                    this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                }            
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Delimitador '}' não encontrado na linha "+linha.trim()+".\n";
                this.modalidadeDesespero2("}, ;");
            }
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }
    
    /**
     * <InitializerList> ::= <Initializer> <InitializerList1>    
     */
    private void procedureInitializerList() {
    
        this.procedureInitializer();
        this.procedureInitializerList1();
    }
    
    /**
     * <InitializerList1> ::= ',' <Initializer> <InitializerList1> | 'vazio'
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
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }
    
    /**
     * <Declarator> ::= 'Identifier' <Declarator1> 
     */
    private void procedureDeclarator() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            // Verifica se o token atual eh '}'
            if(atual[0].contains("Identificador_")) {

                this.idTokenAtual++;
                this.procedureDeclarator1();
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Identificador não encontrado na linha "+linha.trim()+".\n";
                this.modalidadeDesespero2("=, ;, ), (");
            }
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    } 
    
    /**
     * <Declarator1> ::= '[' <Declarator1lf> | 'vazio'
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
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }
    
    /**
     * <Declarator1lf> ::= <CondExpr> ']' <Declarator1> | ']' <Declarator1>
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
                            this.errosSintaticos += "Erro - Delimitador ']' não encontrado na linha "+linha.trim()+".\n";
                            this.modalidadeDesespero2("=, ;, ), (");
                        }
                    } else {

                        this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                    }                

            // Verifica se o token atual eh ']'
            } else if(atual[1].trim().equals("]")) {

                this.idTokenAtual++;
                this.procedureDeclarator1();            
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Delimitador ']' não encontrado na linha "+linha.trim()+".\n";
                this.modalidadeDesespero2("=, ;, ), (");
            }
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }
    
    /**
     * <Stmt> ::= <IterationStmt> | <ExprStmt> | <CompoundStmt>  | <PrintStmt> | <ScanStmt> | <IfStmt> | <ReturnStmt>
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
                this.errosSintaticos += "Erro - Declaração esperada não encontrada na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;, "
                        + "{, scan, true, Cadeia_de_Caracteres, var, Identificador_");
            }
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }
    
    /**
     * <StmtOrDeclarationList> ::= <Stmt> <StmtOrDeclarationList1> | <VarDef> <StmtOrDeclarationList1>   
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
                this.errosSintaticos += "Erro - Palavra reservada 'var' não encontrada na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("}");
            }
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    } 
    
    /**
     * <StmtOrDeclarationList1> ::= <Stmt> <StmtOrDeclarationList1> | <VarDef> <StmtOrDeclarationList1> |
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
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }            
    }
    
    /**
     * <StartDef> ::= 'start' '(' ')' '{' <StmtOrDeclarationList> '}'
     */
    private void procedureStartDef() {
            
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
            // Verifica se o token atual eh 'start'
            if(atual[1].trim().equals("start")) {

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
                                        this.procedureStmtOrDeclarationList();  
                                        if(this.idTokenAtual < this.tokens.getSize()) {

                                            String[] atual5 = this.tokens.getUnicToken(this.idTokenAtual).split(","); 
                                            // Verifica se o token atual eh '}'
                                            if(atual5[1].trim().equals("}")) {

                                                this.idTokenAtual++;
                                            } else {

                                                // Erro                            
                                                String linha = atual5[2].replaceAll(">", " ");
                                                this.errosSintaticos += "Erro - Delimitador '}' não encontrado na linha "+linha.trim()+".\n";
                                                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                                            }
                                        } else {

                                            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                                        }                        
                                    } else {

                                        // Erro
                                        String linha = atual4[2].replaceAll(">", " ");
                                        this.errosSintaticos += "Erro - Delimitador '{' não encontrado na linha "+linha.trim()+".\n";
                                        this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                                    }
                                } else {

                                    this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                                }                    
                            } else {

                                // Erro
                                String linha = atual3[2].replaceAll(">", " ");
                                this.errosSintaticos += "Erro - Delimitador ')' não encontrado na linha "+linha.trim()+".\n";   
                                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                            }
                        } else {

                            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                        }                
                    } else {

                        // Erro  
                        String linha = atual2[2].replaceAll(">", " ");
                        this.errosSintaticos += "Erro - Delimitador '(' não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
                    }
                } else {

                    this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                }            
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Palavra Reservada 'Start' esperada na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("struct, procedure, typedef, const, function, var, start");
            }
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }                
    }
    
    /**
     * <PrintStmt> ::= 'print' '(' <ArgumentList> ')' ';'    
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
                                        this.errosSintaticos += "Erro - Delimitador ';' não encontrado na linha "+linha.trim()+".\n";
                                        this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;,"
                                                + " {, scan, true, Cadeia_de_Caracteres, var, Identificador_");
                                    }
                                } else {

                                    this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                                }                    
                            } else {

                                // Erro
                                String linha = atual3[2].replaceAll(">", " ");
                                this.errosSintaticos += "Erro - Delimitador ')' não encontrado na linha "+linha.trim()+".\n";
                                this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;,"
                                                + " {, scan, true, Cadeia_de_Caracteres, var, Identificador_");
                            }
                        } else {

                            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                        }                
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        this.errosSintaticos += "Erro - Delimitador '(' não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;,"
                                                + " {, scan, true, Cadeia_de_Caracteres, var, Identificador_");
                    }
                } else {

                    this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                }            
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Palavra Reservada 'print' não encontrada na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;,"
                                                + " {, scan, true, Cadeia_de_Caracteres, var, Identificador_");
            }
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    } 
    
    /**
     * <ScanStmt> ::= 'scan' '(' <ArgumentList> ')' ';'
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
                                        this.errosSintaticos += "Erro - Delimitador ';' não encontrado na linha "+linha.trim()+".\n";
                                        this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;,"
                                                + " {, scan, true, Cadeia_de_Caracteres, var, Identificador_");
                                    }
                                } else {

                                    this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                                }                    
                            } else {

                                // Erro
                                String linha = atual3[2].replaceAll(">", " ");
                                this.errosSintaticos += "Erro - Delimitador ')' não encontrado na linha "+linha.trim()+".\n";
                                this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;,"
                                                + " {, scan, true, Cadeia_de_Caracteres, var, Identificador_");
                            }
                        } else {

                            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                        }                
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        this.errosSintaticos += "Erro - Delimitador '(' não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;,"
                                                + " {, scan, true, Cadeia_de_Caracteres, var, Identificador_");
                    }
                } else {

                    this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                }            
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Palavra Reservada 'scan' não encontrada na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;,"
                                                + " {, scan, true, Cadeia_de_Caracteres, var, Identificador_");
            }
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }
    
    /**
     * <IterationStmt> ::= 'while' '(' <Expr> ')' <Stmt>
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
                                this.errosSintaticos += "Erro - Delimitador ')' não encontrado na linha "+linha.trim()+".\n";
                                this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if,"
                                        + " while, ;, {, scan, true, Cadeia_de_Caracteres, var, Identificador_");
                            }
                        } else {

                            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                        }                
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        this.errosSintaticos += "Erro - Delimitador '(' não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if,"
                                        + " while, ;, {, scan, true, Cadeia_de_Caracteres, var, Identificador_");
                    }
                } else {

                    this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                }            
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Palavra Reservada 'while' não encontrada na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if,"
                                        + " while, ;, {, scan, true, Cadeia_de_Caracteres, var, Identificador_");
            }
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }
    
    /**
     * <IfStmt> ::= 'if' <Expr> 'then' <Stmt> <IfStmtlf>  
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
                        this.errosSintaticos += "Erro - Palavra Reservada 'then' não encontrada na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;, {,"
                                + " scan, true, Cadeia_de_Caracteres, var, Identificador_");
                    }
                } else {

                    this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                }
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Palavra Reservada 'if' não encontrado na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;, {,"
                                + " scan, true, Cadeia_de_Caracteres, var, Identificador_");
            }
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }    
    
    /**
     * <IfStmtlf> ::= 'else' <Stmt> |
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
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }
    
    /**
     * <ReturnStmt> ::= 'return' <Expr> ';'
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
                        this.errosSintaticos += "Erro - Delimitador ';' não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;,"
                                + " {, scan, true, Cadeia_de_Caracteres, var, Identificacor_");
                    }
                } else {
                    
                    this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                }            
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Palavra Reservada 'return' não encontrada na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;,"
                                + " {, scan, true, Cadeia_de_Caracteres, var, Identificacor_");
            }
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }
    
    /**
     * <CompoundStmt> ::= '{' <CompoundStmtlf> 
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
                this.errosSintaticos += "Erro - Delimitador '{' não encontrado na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;,"
                        + " {, scan, true, Cadeia_de_Caracteres, var, Identificador_");
            }
        } else {
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }                
    } 
    
    /**
     * <CompoundStmtlf> ::= '}' | <StmtOrDeclarationList> '}'
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
                        this.errosSintaticos += "Erro - Delimitador '}' não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;,"
                                + " {, scan, true, Cadeia_de_Caracteres, var, Identificador_");
                    }
                } else {

                    this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                }            
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Delimitador '}' não encontrado na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;,"
                                + " {, scan, true, Cadeia_de_Caracteres, var, Identificador_");
            }
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }       
    }
    
    /**
     * <ExprStmt> ::= ';' | <Expr> ';'
     */
    private void procedureExprStmt() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
            // Verifica se o token atual eh ';'
            if(atual[1].trim().equals(";")) {

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

                        this.idTokenAtual++;
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        this.errosSintaticos += "Erro - Delimitador ';' não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;,"
                                + " {, scan, true, Cadeia_de_Caracteres, var, Identificador_");
                    }
                } else {

                    this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                }            
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Delimitador ';' não encontrado na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("false, (, return, print, else, !, ++, --, Numero, }, if, while, ;,"
                                + " {, scan, true, Cadeia_de_Caracteres, var, Identificador_");
            }
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }
    
    /**
     * <Expr> ::= <AssignExpr> <Expr1>     
     */
    private void procedureExpr() {
    
        this.procedureAssignExpr();
        this.procedureExpr1();
    }   
    
    /**
     * <Expr1> ::= ',' <AssignExpr> <Expr1> | 
     */
    private void procedureExpr1() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
            // Verifica se o token atual eh ','
            if(atual[0].contains("Delimitador") && atual.length == 4) {

                this.idTokenAtual++;
                this.procedureAssignExpr();
                this.procedureExpr1();
            }

            // Vazio
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }
    }
    
    /**
     * <AssignExpr> ::= <CondExpr> <AssignExpr1>  
     */
    private void procedureAssignExpr() {
    
        this.procedureCondExpr();
        this.procedureAssignExpr1();
    }    
    
    /**
     * <AssignExpr1> ::= '=' <CondExpr> <AssignExpr1> | 
     */
    private void procedureAssignExpr1() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
            // Verifica se o token atual eh '='
            if(atual[1].trim().equals("=")) {

                this.idTokenAtual++;
                this.procedureCondExpr();
                this.procedureAssignExpr1();
            }

            // Vazio
        } else {
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        } 
    }
    
    /**
     * <CondExpr> ::= <LogicalOrExpr>   
     */
    private void procedureCondExpr() {
    
        this.procedureLogicalOrExpr();
    }    
    
    /**
     * <LogicalOrExpr> ::= <LogicalAndExpr> <LogicalOrExpr1>  
     */
    private void procedureLogicalOrExpr() {
    
        this.procedureLogicalAndExpr();
        this.procedureLogicalOrExpr1();
    }   
    
    /**
     * <LogicalOrExpr1> ::= '||' <LogicalAndExpr> <LogicalOrExpr1> |
     */
    private void procedureLogicalOrExpr1() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
            // Verifica se o token atual eh '||'
            if(atual[1].trim().equals("||")) {

                this.idTokenAtual++;
                this.procedureLogicalAndExpr();
                this.procedureLogicalOrExpr1();
            }

            // Vazio
        } else {
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }
    
    /**
     * <LogicalAndExpr> ::= <EqualExpr> <LogicalAndExpr1>
     */
    private void procedureLogicalAndExpr() {
    
        this.procedureEqualExpr();
        this.procedureLogicalAndExpr1();
    }
    
    /**
     * <LogicalAndExpr1> ::= '&&' <EqualExpr> <LogicalAndExpr1> | 
     */
    private void procedureLogicalAndExpr1() {
    
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
            // Verifica se o token atual eh '&&'
            if(atual[1].trim().equals("&&")) {

                this.idTokenAtual++;
                this.procedureEqualExpr();
                this.procedureLogicalAndExpr1();
            }

            // Vazio
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }
    
    /**
     * <EqualExpr> ::= <RelationalExpr> <EqualExpr1>
     */
    private void procedureEqualExpr() {
    
        this.procedureRelationalExpr();
        this.procedureEqualExpr1();
    }
    
    /**
     * <EqualExpr1> ::= <EqualOp> <RelationalExpr> <EqualExpr1> | 
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
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }
    
    /**
     * <RelationalExpr> ::= <AdditiveExpr> <RelationalExpr1>  
     */
    private void procedureRelationalExpr() {
    
        this.procedureAdditiveExpr();
        this.procedureRelationalExpr1();
    } 
    
    /**
     * <RelationalExpr1> ::= <RelationalOp> <AdditiveExpr> <RelationalExpr1> | 
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
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }
    
    /**
     * <AdditiveExpr> ::= <MultExpr> <AdditiveExpr1>    
     */
    private void procedureAdditiveExpr() {
    
        this.procedureMultExpr();
        this.procedureAdditiveExpr1();
    }  
    
    /**
     * <AdditiveExpr1> ::= <AdditiveOp> <MultExpr> <AdditiveExpr1> | 
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
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }
    
    /**
     * <MultExpr> ::= <UnaryExpr> <MultExpr1>  
     */
    private void procedureMultExpr() {
        
        this.procedureUnaryExpr();
        this.procedureMultExpr1();
    } 
    
    /**
     * <MultExpr1> ::= <MultOp> <UnaryExpr> <MultExpr1> | 
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
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }
    
    /**
     * <UnaryExpr> ::= <UnaryOp> <UnaryExpr> | <PostfixExpr> 
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
                this.errosSintaticos += "Erro - Expressão mal formada na linha "+linha.trim()+".\n";
                this.modalidadeDesespero2("-, +, then, *, ), <=, ||, ==, &&, >, =, ], }, <, !=, >=, ;, /");
            }
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }   
    
    /**
     * <PostfixExpr> ::= <PrimaryExpr> <PostfixExpr1>
     */
    private void procedurePostfixExpr() {
        
        this.procedurePrimaryExpr();
        this.procedurePostfixExpr1();             
    }           
    
    /**
     * <PostfixExpr1> ::= <PostfixOp> <PostfixExpr1> |
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
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }
    
    /**
     * <PrimaryExpr> ::= 'Identifier' | 'Number' | 'Literal' | 'true' | 'false' | '(' <Expr> ')' 
     */
    private void procedurePrimaryExpr() {
        
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            // Verifica se o token atual eh 'Identifier'
            if(atual[0].contains("Identificador_")){

                this.idTokenAtual++;

            // Verifica se o token atual eh 'Number'    
            } else if(atual[0].contains("Numero")) {

                this.idTokenAtual++;

            // Verifica se o token atual eh 'Literal'    
            } else if(atual[0].contains("Cadeia_de_Caracteres")) {

                this.idTokenAtual++;

            // Verifica se o token atual eh 'true'    
            } else if(atual[1].trim().equals("true")) {

                this.idTokenAtual++;

            // Verifica se o token atual eh 'false'    
            } else if(atual[1].trim().equals("false")) {

                this.idTokenAtual++;

            // Verifica se o token atual eh '('    
            } else if(atual[1].trim().equals("(")) {

                this.idTokenAtual++;
                this.procedureExpr();
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                    // Verifica se o token atual eh ')'
                    if(atual2[1].trim().equals(")")) {

                        this.idTokenAtual++;
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        this.errosSintaticos += "Erro - Delimitador ')' não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero2("*, (, <=, ++, --, ==, =, }, !=, ;, /, -,'+, then, ), ||, &&, >, ], <, [, >=, .");
                    }  
                } else {

                    this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                }                      
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Expressão mal formada na linha "+linha.trim()+".\n";
                this.modalidadeDesespero2("*, (, <=, ++, --, ==, =, }, !=, ;, /, -,'+, then, ), ||, &&, >, ], <, [, >=, .");
            }
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }
    
    /**
     * <EqualOp> ::= '==' | '!='
     */
    private void procedureEqualOp() {
            
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            // Verifica se o token atual eh '=='
            if(atual[1].trim().equals("==")){

                this.idTokenAtual++;

            // Verifica se o token atual é '!='    
            } else if(atual[1].trim().equals("!=")){

                this.idTokenAtual++;
            } else{

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Operador ('==' ou '!=') não encontrado na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("Numero, false, true, Cadeia_de_Caracteres, (, Identificador_, !, ++, --");
            }
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }
    
    /**
     * <RelationalOp> ::= '<' | '>' | '<=' | '>='       
     */
    private void procedureRelationalOp() {
        
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            // Verifica se o token atual eh '<'
            if(atual[1].trim().equals("<")){

                this.idTokenAtual++;

            // Verifica se o token atual eh '>'    
            } else if(atual[1].trim().equals(">")){

                this.idTokenAtual++;

            // Verifica se o token atual eh '<='    
            } else if(atual[1].trim().equals("<=")){

                this.idTokenAtual++;

            // Verifica se o token atual eh '>='    
            } else if(atual[1].trim().equals(">=")){

                this.idTokenAtual++;
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Operador ('<', '>', '<=', '>=' ou '>') não encontrado na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("Numero, false, true, Cadeia_de_Caracteres, (, Identificador_, !, ++, --");
            }
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }      
    
    /**
     * <AdditiveOp> ::= '+' | '-'
     */
    private void procedureAdditiveOp() {
        
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            // Verifica se o token atual eh '+'
            if(atual[1].trim().equals("+")){

                this.idTokenAtual++;

            // Verifica se o token atual eh '-'    
            } else if(atual[1].trim().equals("-")){

                this.idTokenAtual++;            
            } else{

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Operador ('+' ou '-') não encontrado na linha "+linha.trim()+".\n";
                this.modalidadeDesespero2("then, ), <=, ||, ==, &&, >, =, ], }, <, !=, >=, ;");
            }
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }
        
    }   
    
    /**
     * <MultOp> ::= '*' | '/'
     */
    private void procedureMultOp() {
        
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            // Verifica se o token atual eh '*'
            if(atual[1].trim().equals("*")){

                this.idTokenAtual++;

            // Verifica se o token atual eh '/'
            } else if(atual[1].trim().equals("/")){

                this.idTokenAtual++;        
            } else{

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Operador ('*' ou '/') não encontrado na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("Numero, false, true, Cadeia_de_Caracteres, (, Identificador_, !, ++, --");
                
            }
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }   
    
    /**
     * <UnaryOp> ::= '++' | '--' | '!'
     */
    private void procedureUnaryOp() {
        
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            // Verifica se o token atual eh '++'
            if(atual[1].trim().equals("++")){

                this.idTokenAtual++;

            // Verifica se o token atual eh '--'    
            } else if(atual[1].trim().equals("--")){

                this.idTokenAtual++;

            // Verifica se o token atual eh '!'    
            } else if(atual[1].trim().equals("!")){

                this.idTokenAtual++;
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Operador ('++', '--' ou '!') não encontrado na linha "+linha.trim()+".\n"; 
                this.modalidadeDesespero2("-, +, then, *, ), <=, ||, ==, &&, >, =, ], }, <, !=, >=, ;, /");
            }
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }
    
    /**
     * <PostfixOp> ::= '++' | '--' | '[' <Expr> ']' | '(' <PostfixOplf> | '.' 'Identifier'              
     */
    private void procedurePostfixOp() {
                
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            // Verifica se o token atual é '++'
            if(atual[1].trim().equals("++")){

                this.idTokenAtual++;

            // Verifica se o token atual é '--'    
            } else if(atual[1].trim().equals("--")){

                this.idTokenAtual++;

            // Verifica se o token atual é '['    
            } else if(atual[1].trim().equals("[")){

                this.idTokenAtual++;
                this.procedureExpr();
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                    // Verifica se o token atual é ']'
                    if(atual2[1].trim().equals("]")){

                        this.idTokenAtual++;
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        this.errosSintaticos += "Erro - Delimitador ']' não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero2("*, (, <=, ++, --, ==, =, }, !=, ;, /, -, +, then, ), ||, &&, >, ], <, [, >=, .");
                    }
                } else {
                    this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                }

            // Verifica se o token atual é '('      
            } else if(atual[1].trim().equals("(")){

                this.idTokenAtual++;
                this.procedurePostfixOplf();  

            // Verifica se o token atual é '.'      
            } else if(atual[1].trim().equals(".")){

                this.idTokenAtual++;
                if(this.idTokenAtual < this.tokens.getSize()) {

                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                    // Verifica se o token atual é um 'Identifier'
                    if(atual2[0].contains("Identificador_")){

                        this.idTokenAtual++;
                    } else {

                        // Erro
                        String linha = atual2[2].replaceAll(">", " ");
                        this.errosSintaticos += "Erro - Identificador não encontrado na linha "+linha.trim()+".\n";
                        this.modalidadeDesespero2("*, (, <=, ++, --, ==, =, }, !=, ;, /, -, +, then, ), ||, &&, >, ], <, [, >=, .");
                    }
                } else {
                    this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                }
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Operador ou Delimitador não encontrado na linha "+linha.trim()+".\n";  
                this.modalidadeDesespero2("*, (, <=, ++, --, ==, =, }, !=, ;, /, -, +, then, ), ||, &&, >, ], <, [, >=, .");
            }
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }   
    } 
    
    /**
     * <PostfixOplf> ::= ')' | <ArgumentList> ')'
     */
    private void procedurePostfixOplf() {
        
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            // Verifica se o token atual é ')'
            if(atual[1].trim().equals(")")){

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

                            this.idTokenAtual++;
                        } else{

                            // Erro
                            String linha = atual2[2].replaceAll(">", " ");
                            this.errosSintaticos += "Erro - Delimitador ')' não encontrado na linha "+linha.trim()+".\n";
                            this.modalidadeDesespero2("-, +, then, *, ), (, <=, ||, ++, --, ==, &&, >, =, ], }, <, !=, [, >=, ;, /, .");
                        } 
                    } else {
                        
                        this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
                    }    
            } else {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Delimitador ')' não encontrado na linha "+linha.trim()+".\n";
                this.modalidadeDesespero2("-, +, then, *, ), (, <=, ||, ++, --, ==, &&, >, =, ], }, <, !=, [, >=, ;, /, .");
            } 
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }  
    }
    
    /**
     * <ArgumentList> ::= <AssignExpr> <ArgumentList1>            
     */
    private void procedureArgumentList() {
        
        this.procedureAssignExpr();
        this.procedureArgumentList1();   
    }
    
    /**
     * <ArgumentList1> ::= ',' <AssignExpr> <ArgumentList1> | 'vazio'
     */
    private void procedureArgumentList1() {
        
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            // Verifica se o token atual eh ','
            if(atual[0].contains("Delimitador") && atual.length == 4) {

                this.idTokenAtual++;
                this.procedureAssignExpr();
                this.procedureArgumentList1();
            }

            // Vazio            
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }
    }
    
    /**
     * <Type> ::= 'int' | 'string' | 'float' | 'bool'  | 'Identifier'
     */
    private void procedureType() {
        
        if(this.idTokenAtual < this.tokens.getSize()) {
            
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            // Verifica se o token atual eh 'int'
            if(atual[1].trim().equals("int")) {

                this.idTokenAtual++;

            // Verifica se o token atual eh 'string'    
            } else if(atual[1].trim().equals("string")) {

                this.idTokenAtual++;

            // Verifica se o token atual eh 'float'        
            } else if(atual[1].trim().equals("float")) {

                this.idTokenAtual++;

            // Verifica se o token atual eh 'bool'   
            } else if(atual[1].trim().equals("bool")) {

                this.idTokenAtual++;

            // Verifica se o token atual eh 'Identifier'       
            } else if(atual[0].contains("Identificador_")) {

                this.idTokenAtual++;            
            } else  {

                // Erro
                String linha = atual[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Tipo não encontrado na linha "+linha.trim()+".\n";
                this.modalidadeDesespero("Identificador_");
            }
        } else {
            
            this.errosSintaticos += "Erro - Limite da lista de tokens.\n";
        }        
    }
    
    /**
     * Utiliza como token de sincronização, algum token do conjunto seguinte da produção atual.
     * @param follow 
     */
    private void modalidadeDesespero(String follow) {
        
        boolean trava = true;
        this.idTokenAtual++;
        do {
            
            String[] seguintes = follow.split(",");
            for (String seguinte : seguintes) {
                
                if (this.tokens.getUnicToken(this.idTokenAtual).contains(seguinte.trim())) {
                    
                    trava = false;
                    break;
                } else {
                    
                    this.idTokenAtual++;
                    if(this.idTokenAtual > this.tokens.getSize()) {
                        
                        trava = false;
                        break;
                    }
                }
            }
        } while(trava || this.idTokenAtual < this.tokens.getSize());        
    }
    
    /**
     * Utilizado quando simbolos que fazem parte do token, fazem parte tambem do conjunto seguinte (',', '<', '>').
     * @param follow 
     */
    private void modalidadeDesespero2(String follow) {
        
        boolean trava = true;
        this.idTokenAtual++;
        do {
            
            String[] seguintes = follow.split(",");
            for (String seguinte : seguintes) {
                
                String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                if (atual[1].trim().equals(seguinte.trim()) || atual[0].contains(seguinte.trim()) ||
                        (atual[0].contains("Delimitador") && atual.length == 4)) {
                    
                    trava = false;
                    break;
                } else {
                    
                    this.idTokenAtual++;
                    if(this.idTokenAtual > this.tokens.getSize()) {
                        
                        trava = false;
                        break;
                    }
                }
            }
        } while(trava || this.idTokenAtual < this.tokens.getSize());        
    }
    
}
