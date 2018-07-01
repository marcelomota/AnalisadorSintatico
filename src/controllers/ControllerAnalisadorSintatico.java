package controllers;

import models.Token;

public class ControllerAnalisadorSintatico {
          
    private String errosSintaticos;
    private Token tokens;
    private int idTokenAtual;
    
    public ControllerAnalisadorSintatico() {
   
        this.errosSintaticos = "";
    }
    
    /**
     * Analisa sintaticamente a sequencia de tokens de forma preditiva e recursiva.
     * @param tokens
     * @return 
     */
    public String analisar(Token tokens) {
        
        this.tokens = tokens;        
        this.idTokenAtual = 0;
        this.procedureS();
        /*
        if(this.idTokenAtual > this.tokens.getSize()) {
            
            // Sucesso
        } */
        
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
    
    /**
     * <GlobalDeclaration> ::= <StartDef> | <VarDef> | <ConstDef> | <StructDef> | <FunctionDef> | <ProcedureDef> | <TypedefDef>
     */
    private void procedureGlobalDeclaration() {
    
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
        }
    }
    
    /**
     * <FunctionDef> ::= 'function' <Type> <Declarator> '(' <FunctionDeflf>
     */
    private void procedureFunctionDef() {
    
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
        // Verifica se o token atual eh 'function'
        if(atual[1].trim().equals("function")) {
            
            this.idTokenAtual++;
            this.procedureType();
            this.procedureDeclarator();
            String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
            // Verifica se o token atual eh '('
            if(atual2[1].trim().equals("(")) {
             
                this.idTokenAtual++;
                this.procedureFunctionDeflf();
            } else {
              
                // Erro
                String linha = atual2[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Delimitador '(' não encontrado na linha "+linha.trim()+".\n";
            }            
        } else {
            
            // Erro
            String linha = atual[2].replaceAll(">", " ");
            this.errosSintaticos += "Erro - Palavra Reservada 'function' não encontrada na linha "+linha.trim()+".\n";
        }        
    }
    
    /**
     * <FunctionDeflf> ::= <ParameterList> ')' '{' <StmtOrDeclarationList> '}' | ')' '{' <StmtOrDeclarationList> '}' 
     */
    private void procedureFunctionDeflf() {
        
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
        // Verifica se o token atual eh o primeiro de <ParameterList>
        if(atual[1].trim().equals("bool") || atual[1].trim().equals("float") ||
                atual[1].trim().equals("int") || atual[1].trim().equals("string") ||
                atual[0].contains("Identificador_")) {
            
            this.procedureParameterList();
            String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(","); 
            // Verifica se o token atual eh ')'
            if(atual2[1].trim().equals(")")) {
                
                this.idTokenAtual++;
                String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                // Verifica se o token atual eh '{'
                if(atual3[1].trim().equals("{")) {
                    
                    this.idTokenAtual++;
                    this.procedureStmtOrDeclarationList();
                    String[] atual4 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                    // Verifica se o token atual eh '}'
                    if(atual4[1].trim().equals("}")) {

                        this.idTokenAtual++;
                    } else {
                        
                        // Erro
                        String linha = atual4[2].replaceAll(">", " ");
                        this.errosSintaticos += "Erro - Delimitador '}' não encontrado na linha "+linha.trim()+".\n";
                    }
                } else {
                    
                    // Erro
                    String linha = atual3[2].replaceAll(">", " ");
                    this.errosSintaticos += "Erro - Delimitador '{' não encontrado na linha "+linha.trim()+".\n";
                }
            } else {
                
                // Erro
                String linha = atual2[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Delimitador ')' não encontrado na linha "+linha.trim()+".\n";
            }
            
        // Verifica se o token atual eh ')'
        } else if(atual[1].trim().equals(")")) {
            
            this.idTokenAtual++;
            String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            // Verifica se o token atual eh '{'
            if(atual2[1].trim().equals("{")) {

                this.idTokenAtual++;
                this.procedureStmtOrDeclarationList();
                String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                // Verifica se o token atual eh '}'
                if(atual3[1].trim().equals("}")) {

                    this.idTokenAtual++;
                } else {

                    // Erro
                    String linha = atual3[2].replaceAll(">", " ");
                    this.errosSintaticos += "Erro - Delimitador '}' não encontrado na linha "+linha.trim()+".\n";
                }
            } else {

                // Erro
                String linha = atual2[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Delimitador '{' não encontrado na linha "+linha.trim()+".\n";
            }
        } else {
            
            // Erro
            String linha = atual[2].replaceAll(">", " ");
            this.errosSintaticos += "Erro - Delimitador ')' não encontrado na linha "+linha.trim()+".\n";
        }
    }
    
    /**
     * <ProcedureDef> ::= 'procedure' 'Identifier' '(' <ProcedureDefdlf>
     */
    private void procedureProcedureDef() {
    
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
        // Verifica se o token atual eh 'procedure'
        if(atual[1].trim().equals("procedure")) {
            
            this.idTokenAtual++;
            String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
            // Verifica se o token atual eh 'Identifier'
            if(atual2[0].contains("Identificador_")) {

                this.idTokenAtual++;
                String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                // Verifica se o token atual eh '('
                if(atual3[1].trim().equals("(")) {
                    
                    this.idTokenAtual++;
                    this.procedureProcedureDeflf();
                } else {
                    
                    // Erro
                    String linha = atual3[2].replaceAll(">", " ");
                    this.errosSintaticos += "Erro - Delimitador '(' não encontrado na linha "+linha.trim()+".\n";
                }                
            } else {
                
                // Erro
                String linha = atual2[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Identificador não encontrado na linha "+linha.trim()+".\n";
            }
        } else {
            
            // Erro
            String linha = atual[2].replaceAll(">", " ");
            this.errosSintaticos += "Erro - Palavra Reservada 'procedure' não encontrada na linha "+linha.trim()+".\n";
        }        
    }
    
    /**
     * <ProcedureDeflf> ::= <ParameterList> ')' '{' <StmtOrDeclarationList> '}' |  ')' '{' <StmtOrDeclarationList> '}' 
     */
    private void procedureProcedureDeflf() {
        
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
        // Verifica se o token atual eh primeiro de <ParameterList>
        if(atual[1].trim().equals("bool") || atual[1].trim().equals("float") ||
                atual[1].trim().equals("int") || atual[1].trim().equals("string") ||
                atual[0].contains("Identificador_")) {
            
            this.procedureParameterList();
            String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
            // Verifica se o token atual eh ')'
            if(atual2[1].trim().equals(")")) {
                
                this.idTokenAtual++;
                String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                // Verifica se o token atual eh '{'
                if(atual3[1].trim().equals("{")) {

                    this.idTokenAtual++;
                    this.procedureStmtOrDeclarationList();
                    String[] atual4 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                    // Verifica se o token atual eh '}'
                    if(atual4[1].trim().equals("}")) {

                        this.idTokenAtual++;
                    } else {

                        // Erro
                        String linha = atual4[2].replaceAll(">", " ");
                        this.errosSintaticos += "Erro - Delimitador '}' não encontrado na linha "+linha.trim()+".\n";
                    }
                } else {

                    // Erro
                    String linha = atual3[2].replaceAll(">", " ");
                    this.errosSintaticos += "Erro - Delimitador '{' não encontrado na linha "+linha.trim()+".\n";
                }
            } else {
                
                // Erro
                String linha = atual2[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Delimitador ')' não encontrado na linha "+linha.trim()+".\n";
            }
            
        // Verifica se o token atual eh ')'    
        } else if(atual[1].trim().equals(")")) {
            
            this.idTokenAtual++;
            String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
            // Verifica se o token atual eh '{'
            if(atual2[1].trim().equals("{")) {
                
                this.idTokenAtual++;
                this.procedureStmtOrDeclarationList();
                String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                // Verifica se o token atual eh '}'
                if(atual3[1].trim().equals("}")) {

                    this.idTokenAtual++;
                } else {

                    // Erro
                    String linha = atual3[2].replaceAll(">", " ");
                    this.errosSintaticos += "Erro - Delimitador '}' não encontrado na linha "+linha.trim()+".\n";
                }
            } else {
                
                // Erro
                String linha = atual2[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Delimitador '{' não encontrado na linha "+linha.trim()+".\n";
            }
        } else {
            
            // Erro
            String linha = atual[2].replaceAll(">", " ");
            this.errosSintaticos += "Erro - Delimitador ')' não encontrado na linha "+linha.trim()+".\n";
        }
    }
    
    /**
     * <TypedefDef> ::= 'typedef' <TypedefDeflf> 
     */
    private void procedureTypedefDef() {
    
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
        // Verifica se o token atual eh 'typedef'
        if(atual[1].trim().equals("typedef")) {

            this.idTokenAtual++;
            this.procedureTypedefDeflf();
        } else {

            // Erro
            String linha = atual[2].replaceAll(">", " ");
            this.errosSintaticos += "Erro - Palavra Reservada 'typedef' não encontrada na linha "+linha.trim()+".\n";
        }        
    }
    
    /**
     * <TypedefDeflf> ::= <Type> 'Identifier' ';' | <StructDef> 'Identifier' ';'
     */
    private void procedureTypedefDeflf() {
    
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
        // Verifica se o token atual eh primeiro de <Type>
        if(atual[1].trim().equals("bool") || atual[1].trim().equals("float") ||
                atual[1].trim().equals("int") || atual[1].trim().equals("string") ||
                atual[0].contains("Identificador_")) {
        
            this.procedureType();
            String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
            // Verifica se o token atual eh 'Identifier'
            if(atual2[0].contains("Identificador_")) {

                this.idTokenAtual++;
                String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                // Verifica se o token atual eh ';'
                if(atual3[1].trim().equals(";")) {

                    this.idTokenAtual++;
                } else {

                    // Erro
                    String linha = atual3[2].replaceAll(">", " ");
                    this.errosSintaticos += "Erro - Delimitador ';' não encontrado na linha "+linha.trim()+".\n";
                }
            } else {
                
                // Erro
                String linha = atual2[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Identificador não encontrado na linha "+linha.trim()+".\n";
            }
            
        // Verifica se o token atual eh primeiro de <StructDef>    
        } else if(atual[1].trim().equals("struct")) {
            
            this.procedureStructDef();
            String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
            // Verifica se eh token autual eh 'Identifier'
            if(atual2[0].contains("Identificador_")) {

                this.idTokenAtual++;
                String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                // Verifica se o token atual eh ';'
                if(atual3[1].trim().equals(";")) {

                    this.idTokenAtual++;
                } else {

                    // Erro
                    String linha = atual3[2].replaceAll(">", " ");
                    this.errosSintaticos += "Erro - Delimitador ';' não encontrado na linha "+linha.trim()+".\n";
                }                
            } else {
                
                // Erro
                String linha = atual2[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Identificador não encontrado na linha "+linha.trim()+".\n";
            }
        } else {
            
            // Erro
            String linha = atual[2].replaceAll(">", " ");
            this.errosSintaticos += "Erro - Tipo não encontrado na linha "+linha.trim()+".\n";
        }
    }
    
    /**
     * <VarDef> ::= 'var' '{' <DeclarationList> '}'
     */
    private void procedureVarDef() {
       
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
        // Verifica se o token atual eh 'var'
        if(atual[1].trim().equals("var")) {
        
            this.idTokenAtual++;            
            String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            // Verifica se o token atual eh '{'
            if(atual2[1].trim().equals("{")) {
        
                this.idTokenAtual++;
                this.procedureDeclarationList();
                String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                // Verifica se o token atual eh '}'
                if(atual3[1].trim().equals("}")) {
        
                    this.idTokenAtual++;
                } else {

                    // Erro
                    String linha = atual3[2].replaceAll(">", " ");
                    this.errosSintaticos += "Erro - Delimitador '}' não encontrado na linha "+linha.trim()+".\n";
                }
            } else {

                // Erro
                String linha = atual2[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Delimitador '{' não encontrado na linha "+linha.trim()+".\n";
            }
        } else {
            
            // Erro
            String linha = atual[2].replaceAll(">", " ");
            this.errosSintaticos += "Erro - Palavra Reservada 'var' não encontrada na linha "+linha.trim()+".\n";
        }
    }
    
    /**
     * <ConstDef> ::= 'const' '{' <DeclarationList> '}'
     */
    private void procedureConstDef() {
    
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
        // Verifica se o token atual eh 'const'
        if(atual[1].trim().equals("const")) {
        
            this.idTokenAtual++;
            String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
            // Verifica se o token atual eh '{'
            if(atual2[1].trim().equals("{")) {

                this.idTokenAtual++;
                this.procedureDeclarationList();
                String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                // Verifica se o token atual eh '}'
                if(atual3[1].trim().equals("}")) {

                    this.idTokenAtual++;
                } else {

                    // Erro
                    String linha = atual3[2].replaceAll(">", " ");
                    this.errosSintaticos += "Erro - Delimitador '}' não encontrado na linha "+linha.trim()+".\n";
                }
            } else {

                // Erro
                String linha = atual2[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Delimitador '{' não encontrado na linha "+linha.trim()+".\n";
            }
        } else {
            
            // Erro
            String linha = atual[2].replaceAll(">", " ");
            this.errosSintaticos += "Erro - Palavra Reservada 'const' não encontrada na linha "+linha.trim()+".\n";
        }
    }
    
    /**
     * <StructDef> ::= 'struct' 'Identifier' <StructDeflf> 
     */
    private void procedureStructDef() {
        
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
        // Verifica se o token atual eh 'struct'
        if(atual[1].trim().equals("struct")) {

            this.idTokenAtual++;            
            String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
            // Verifica se o token atual eh 'Identifier'
            if(atual2[0].contains("Identificador_")) {

                this.idTokenAtual++;
                this.procedureStructDeflf();
            } else {

                // Erro
                String linha = atual2[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Identificador não encontrado na linha "+linha.trim()+".\n";
            }
        } else {

            // Erro
            String linha = atual[2].replaceAll(">", " ");
            this.errosSintaticos += "Erro - Palavra Reservada 'struct' não encontrada na linha "+linha.trim()+".\n";
        }
    }
    
    /**
     * <StructDeflf> ::= '{' <DeclarationList> '}' | 'extends' 'Identifier' '{' <DeclarationList> '}'
     */
    private void procedureStructDeflf() {
    
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
        // Verifica se o token atual eh '{'
        if(atual[1].trim().equals("{")) {
            
            this.idTokenAtual++;
            this.procedureDeclarationList();
            String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
            // Verifica se o token atual eh '}'
            if(atual2[1].trim().equals("}")) {
                
                this.idTokenAtual++;
            } else {
                
                // Erro
                String linha = atual2[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Delimitador '}' não encontrado na linha "+linha.trim()+".\n";
            }        
        // Verifica se o token atual eh 'extends'    
        } else if(atual[1].trim().equals("extends")) {
            
            this.idTokenAtual++;
            String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
            // Verifica se o token atual eh '}'
            if(atual2[1].trim().equals("}")) {
                
                this.idTokenAtual++;
                String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                // Verifica se o token atual eh '{'
                if(atual3[0].contains("Identificador_")) {

                    this.idTokenAtual++;
                    this.procedureDeclarationList();
                    String[] atual4 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
                    // Verifica se o token atual eh '}'
                    if(atual4[1].trim().equals("}")) {

                        this.idTokenAtual++;
                    } else {

                        // Erro
                        String linha = atual4[2].replaceAll(">", " ");
                        this.errosSintaticos += "Erro - Delimitador '}' não encontrado na linha "+linha.trim()+".\n";
                    }
                } else {

                    // Erro
                    String linha = atual3[2].replaceAll(">", " ");
                    this.errosSintaticos += "Erro - Identificador não encontrado na linha "+linha.trim()+".\n";
                }
            } else {
                
                // Erro
                String linha = atual2[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Delimitador '}' não encontrado na linha "+linha.trim()+".\n";
            }
        } else {
            
            // Erro
            String linha = atual[2].replaceAll(">", " ");
            this.errosSintaticos += "Erro - Delimitador '{' não encontrado na linha "+linha.trim()+".\n";
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
        
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
        // Verifica se o token atual eh ','
        if(atual[0].contains("Delimitador") && atual.length == 4) { 

            this.idTokenAtual++;
            this.procedureDeclaration();
            this.procedureParameterList1();
        }
        
        // Vazio
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
    
    /**
     * <Declaration> ::= <Type> <InitDeclaratorList> ';'
     */
    private void procedureDeclaration() {
    
        this.procedureType();
        this.procedureInitDeclaratorList();
        
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
        // Verifica se o token atual eh ';'
        if(atual[1].trim().equals(";")) {
            
            this.idTokenAtual++;          
        } else {
            
            // Erro
            String linha = atual[2].replaceAll(">", " ");
            this.errosSintaticos += "Erro - Delimitador ';' não encontrado na linha "+linha.trim()+".\n";
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
    
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
        // Verifica se o token atual eh ','
        if(atual[0].contains("Delimitador") && atual.length == 4) { 

            this.idTokenAtual++;
            this.procedureInitDeclarator();
            this.procedureInitDeclaratorList1();
        }
        
        // Vazio
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
    
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
        // Verifica se o token atual eh '='
        if(atual[1].trim().equals("=")) {
            
            this.idTokenAtual++;    
            this.procedureInitializer();
        }
        
        // Vazio
    }
    
    /**
     * <Initializer> ::= <AssignExpr> | '{' <InitializerList> <Initializerlf>
     */
    private void procedureInitializer() {
    
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
        }
    }
    
    /**
     * <Initializerlf> ::= '}' | ',' '}'    
     */
    private void procedureInitializerlf() {

        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
        // Verifica se o token atual eh '}'
        if(atual[1].trim().equals("}")) {
            
            this.idTokenAtual++;   
            
        // Verifica se o token atual eh ','         
        } else if(atual[0].contains("Delimitador") && atual.length == 4) {

            this.idTokenAtual++; 
            String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            // Verifica se o token atual eh '}'
            if(atual2[1].trim().equals("}")) {

                this.idTokenAtual++;    
            } else {
                
                // Erro
                String linha = atual2[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Delimitador '}' não encontrado na linha "+linha.trim()+".\n";
            }
        } else {
            
            // Erro
            String linha = atual[2].replaceAll(">", " ");
            this.errosSintaticos += "Erro - Delimitador '}' não encontrado na linha "+linha.trim()+".\n";
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
    
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
        // Verifica se o token atual eh ','
        if(atual[0].contains("Delimitador") && atual.length == 4) {

            this.idTokenAtual++;
            this.procedureInitializer();
            this.procedureInitializerList1();
        }
        
        // Vazio
    }
    
    /**
     * <Declarator> ::= 'Identifier' <Declarator1> 
     */
    private void procedureDeclarator() {
    
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
        // Verifica se o token atual eh '}'
        if(atual[0].contains("Indetificador_")) {
            
            this.idTokenAtual++;
            this.procedureDeclarator1();
        } else {
            
            // Erro
            String linha = atual[2].replaceAll(">", " ");
            this.errosSintaticos += "Erro - Identificador não encontrado na linha "+linha.trim()+".\n";
        }
    } 
    
    /**
     * <Declarator1> ::= '[' <Declarator1lf> | 'vazio'
     */
    private void procedureDeclarator1() {
    
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
        // Verifica se o token atual eh '['
        if(atual[1].trim().equals("[")) {
            
            this.idTokenAtual++;
            this.procedureDeclarator1lf();
        }
        
        // Vazio
    }
    
    /**
     * <Declarator1lf> ::= <CondExpr> ']' <Declarator1> | ']' <Declarator1>
     */
    private void procedureDeclarator1lf() {
    
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
        // Verifica se o token atual eh primeiro de <CondExpr>
        if(atual[1].trim().equals("false") || atual[1].trim().equals("true") ||
                atual[1].trim().equals("(") || atual[1].trim().equals("!") ||
                atual[1].trim().equals("++") || atual[1].trim().equals("--") ||
                atual[0].contains("Numero") || atual[0].contains("Cadeia_de_Caracteres") ||
                atual[0].contains("Identificador_")) {
        
                this.procedureCondExpr();
                String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                // Verifica se o token atual eh ']'
                if(atual2[1].trim().equals("]")) {

                    this.idTokenAtual++;
                    this.procedureDeclarator1();
                } else {

                    // Erro
                    String linha = atual2[2].replaceAll(">", " ");
                    this.errosSintaticos += "Erro - Delimitador ']' não encontrado na linha "+linha.trim()+".\n";
                }
            
        // Verifica se o token atual eh ']'
        } else if(atual[1].trim().equals("]")) {
            
            this.idTokenAtual++;
            this.procedureDeclarator1();            
        } else {
            
            // Erro
            String linha = atual[2].replaceAll(">", " ");
            this.errosSintaticos += "Erro - Delimitador ']' não encontrado na linha "+linha.trim()+".\n";
        }
    }
    
    /**
     * <Stmt> ::= <IterationStmt> | <ExprStmt> | <CompoundStmt>  | <PrintStmt> | <ScanStmt> | <IfStmt> | <ReturnStmt>
     */
    private void procedureStmt() {
    
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
        }
    }
    
    /**
     * <StmtOrDeclarationList> ::= <Stmt> <StmtOrDeclarationList1> | <VarDef> <StmtOrDeclarationList1>   
     */
    private void procedureStmtOrDeclarationList() {
    
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
            this.errosSintaticos += "Erro - Declaração esperada não encontrada na linha "+linha.trim()+".\n";
        }
    } 
    
    /**
     * <StmtOrDeclarationList1> ::= <Stmt> <StmtOrDeclarationList1> | <VarDef> <StmtOrDeclarationList1> |
     */
    private void procedureStmtOrDeclarationList1() {
    
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
    
    /**
     * <StartDef> ::= 'start' '(' ')' '{' <StmtOrDeclarationList> '}'
     */
    private void procedureStartDef() {
            
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
        // Verifica se o token atual eh 'start'
        if(atual[1].trim().equals("start")) {
            
            this.idTokenAtual++;            
            String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
            // Verifica se o token atual eh '('
            if(atual2[1].trim().equals("(")) {
                
                this.idTokenAtual++;
                String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
                // Verifica se o token atual eh ')'
                if(atual3[1].trim().equals(")")) {                    
                    
                    this.idTokenAtual++;
                    String[] atual4 = this.tokens.getUnicToken(this.idTokenAtual).split(","); 
                    // Verifica se o token atual eh '{'
                    if(atual4[1].trim().equals("{")) {
                     
                        this.idTokenAtual++;
                        this.procedureStmtOrDeclarationList();                        
                        String[] atual5 = this.tokens.getUnicToken(this.idTokenAtual).split(","); 
                        // Verifica se o token atual eh '}'
                        if(atual5[1].trim().equals("}")) {
                            
                            this.idTokenAtual++;
                        } else {
                            
                            // Erro                            
                            String linha = atual5[2].replaceAll(">", " ");
                            this.errosSintaticos += "Erro - Delimitador '}' não encontrado na linha "+linha.trim()+".\n";
                        }
                    } else {
                        
                        // Erro
                        String linha = atual4[2].replaceAll(">", " ");
                        this.errosSintaticos += "Erro - Delimitador '{' não encontrado na linha "+linha.trim()+".\n";
                    }
                } else {
                    
                    // Erro
                    String linha = atual3[2].replaceAll(">", " ");
                    this.errosSintaticos += "Erro - Delimitador ')' não encontrado na linha "+linha.trim()+".\n";                    
                }
            } else {
                
                // Erro  
                String linha = atual2[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Delimitador '(' não encontrado na linha "+linha.trim()+".\n";
            }
        } else {
         
            // Erro
            String linha = atual[2].replaceAll(">", " ");
            this.errosSintaticos += "Erro - Palavra Reservada 'Start' esperada na linha "+linha.trim()+".\n";
        }        
    }
    
    /**
     * <PrintStmt> ::= 'print' '(' <ArgumentList> ')' ';'    
     */
    private void procedurePrintStmt() {
        
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
        // Verifica se o token atual eh 'print'
        if(atual[1].trim().equals("print")) {
            
            this.idTokenAtual++;
            String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
            // Verifica se o token atual eh '('
            if(atual2[1].trim().equals("(")) {

                this.idTokenAtual++;
                this.procedureArgumentList();
                String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
                // Verifica se o token atual eh ')'
                if(atual3[1].trim().equals(")")) {
                    
                    this.idTokenAtual++;
                    String[] atual4 = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
                    // Verifica se o token atual eh ';'
                    if(atual4[1].trim().equals(";")) {
                        
                        this.idTokenAtual++;
                    } else {

                        // Erro
                        String linha = atual4[2].replaceAll(">", " ");
                        this.errosSintaticos += "Erro - Delimitador ';' não encontrado na linha "+linha.trim()+".\n";
                    }
                } else {

                    // Erro
                    String linha = atual3[2].replaceAll(">", " ");
                    this.errosSintaticos += "Erro - Delimitador ')' não encontrado na linha "+linha.trim()+".\n";
                }
            } else {

                // Erro
                String linha = atual2[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Delimitador '(' não encontrado na linha "+linha.trim()+".\n";
            }
        } else {
            
            // Erro
            String linha = atual[2].replaceAll(">", " ");
            this.errosSintaticos += "Erro - Palavra Reservada 'print' não encontrada na linha "+linha.trim()+".\n";
        }
    } 
    
    /**
     * <ScanStmt> ::= 'scan' '(' <ArgumentList> ')' ';'
     */
    private void procedureScanStmt() {
    
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
        // Verifica se o token atual eh 'scan'
        if(atual[1].trim().equals("scan")) {
            
            this.idTokenAtual++;
            String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
            // Verifica se o token atual eh '('
            if(atual2[1].trim().equals("(")) {

                this.idTokenAtual++;
                this.procedureArgumentList();
                String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
                // Verifica se o token atual eh ')'
                if(atual3[1].trim().equals(")")) {
                    
                    this.idTokenAtual++;
                    String[] atual4 = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
                    // Verifica se o token atual eh ';'
                    if(atual4[1].trim().equals(";")) {
                        
                        this.idTokenAtual++;
                    } else {

                        // Erro
                        String linha = atual4[2].replaceAll(">", " ");
                        this.errosSintaticos += "Erro - Delimitador ';' não encontrado na linha "+linha.trim()+".\n";
                    }
                } else {

                    // Erro
                    String linha = atual3[2].replaceAll(">", " ");
                    this.errosSintaticos += "Erro - Delimitador ')' não encontrado na linha "+linha.trim()+".\n";
                }
            } else {

                // Erro
                String linha = atual2[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Delimitador '(' não encontrado na linha "+linha.trim()+".\n";
            }
        } else {
            
            // Erro
            String linha = atual[2].replaceAll(">", " ");
            this.errosSintaticos += "Erro - Palavra Reservada 'scan' não encontrada na linha "+linha.trim()+".\n";
        }
    }
    
    /**
     * <IterationStmt> ::= 'while' '(' <Expr> ')' <Stmt>
     */
    private void procedureIterationStmt() {
    
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
        // Verifica se o token atual eh 'while'
        if(atual[1].trim().equals("while")) {
            
            this.idTokenAtual++;
            String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
            // Verifica se o token atual eh '('
            if(atual2[1].trim().equals("(")) {

                this.idTokenAtual++;
                this.procedureExpr();
                String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
                // Verifica se o token atual eh ')'
                if(atual3[1].trim().equals(")")) {
                    
                    this.idTokenAtual++;
                    this.procedureStmt();
                } else {

                    // Erro
                    String linha = atual3[2].replaceAll(">", " ");
                    this.errosSintaticos += "Erro - Delimitador ')' não encontrado na linha "+linha.trim()+".\n";
                }
            } else {

                // Erro
                String linha = atual2[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Delimitador '(' não encontrado na linha "+linha.trim()+".\n";
            }
        } else {
            
            // Erro
            String linha = atual[2].replaceAll(">", " ");
            this.errosSintaticos += "Erro - Palavra Reservada 'while' não encontrada na linha "+linha.trim()+".\n";
        }
    }
    
    /**
     * <IfStmt> ::= 'if' <Expr> 'then' <Stmt> <IfStmtlf>  
     */
    private void procedureIfStmt() {
        
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
        // Verifica se o token atual eh 'while'
        if(atual[1].trim().equals("if")) {
            
            this.idTokenAtual++;
            this.procedureExpr();
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
            }
        } else {
            
            // Erro
            String linha = atual[2].replaceAll(">", " ");
            this.errosSintaticos += "Erro - Palavra Reservada 'if' não encontrado na linha "+linha.trim()+".\n";
        }
    }    
    
    /**
     * <IfStmtlf> ::= 'else' <Stmt> |
     */
    private void procedureIfStmtlf() {
    
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
        // Verifica se o token atual eh 'else'
        if(atual[1].trim().equals("else")) {
            
            this.idTokenAtual++;
            this.procedureStmt();
        }
        
        // Vazio
    }
    
    /**
     * <ReturnStmt> ::= 'return' <Expr> ';'
     */
    private void procedureReturnStmt() {
    
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
        // Verifica se o token atual eh 'return'
        if(atual[1].trim().equals("return")) {
            
            this.idTokenAtual++;
            this.procedureExpr();
            String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
            // Verifica se o token atual eh ';'
            if(atual2[1].trim().equals(";")) {

                this.idTokenAtual++;
            } else {
                
                // Erro
                String linha = atual2[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Delimitador ';' não encontrado na linha "+linha.trim()+".\n";
            }
        } else {
            
            // Erro
            String linha = atual[2].replaceAll(">", " ");
            this.errosSintaticos += "Erro - Palavra Reservada 'return' não encontrada na linha "+linha.trim()+".\n";
        }
    }
    
    /**
     * <CompoundStmt> ::= '{' <CompoundStmtlf> 
     */
    private void procedureCompoundStmt() {
    
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
        // Verifica se o token atual eh '{'
        if(atual[1].trim().equals("{")) {
            
            this.idTokenAtual++;
            this.procedureCompoundStmtlf();
        } else {
            
            // Erro
            String linha = atual[2].replaceAll(">", " ");
            this.errosSintaticos += "Erro - Delimitador '{' não encontrado na linha "+linha.trim()+".\n";
        }        
    } 
    
    /**
     * <CompoundStmtlf> ::= '}' | <StmtOrDeclarationList> '}'
     */
    private void procedureCompoundStmtlf() {
    
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
                    
            String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
            // Verifica se o token atual eh '}'
            if(atual2[1].trim().equals("}")) {

                this.idTokenAtual++;
            } else {
                
                // Erro
                String linha = atual2[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Delimitador '}' não encontrado na linha "+linha.trim()+".\n";
            }
        } else {
            
            // Erro
            String linha = atual[2].replaceAll(">", " ");
            this.errosSintaticos += "Erro - Delimitador '}' não encontrado na linha "+linha.trim()+".\n";
        }
    }
    
    /**
     * <ExprStmt> ::= ';' | <Expr> ';'
     */
    private void procedureExprStmt() {
    
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
            String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
            // Verifica se o token atual eh ';'
            if(atual2[1].trim().equals(";")) {

                this.idTokenAtual++;
            } else {

                // Erro
                String linha = atual2[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Delimitador ';' não encontrado na linha "+linha.trim()+".\n";
            }
        } else {
            
            // Erro
            String linha = atual[2].replaceAll(">", " ");
            this.errosSintaticos += "Erro - Delimitador ';' não encontrado na linha "+linha.trim()+".\n";
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
    
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
        // Verifica se o token atual eh ','
        if(atual[0].contains("Delimitador") && atual.length == 4) {

            this.idTokenAtual++;
            this.procedureAssignExpr();
            this.procedureExpr1();
        }
        
        // Vazio
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
    
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
        // Verifica se o token atual eh '='
        if(atual[1].trim().equals("=")) {
            
            this.idTokenAtual++;
            this.procedureCondExpr();
            this.procedureAssignExpr1();
        }
        
        // Vazio
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
    
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
        // Verifica se o token atual eh '||'
        if(atual[1].trim().equals("||")) {
            
            this.idTokenAtual++;
            this.procedureLogicalAndExpr();
            this.procedureLogicalOrExpr1();
        }
        
        // Vazio
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
    
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
        // Verifica se o token atual eh '&&'
        if(atual[1].trim().equals("&&")) {
            
            this.idTokenAtual++;
            this.procedureEqualExpr();
            this.procedureLogicalAndExpr1();
        }
        
        // Vazio
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
    
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
        // Verifica se o token atual eh primeiro de <EqualOp>
        if(atual[1].trim().equals("!=") || atual[1].trim().equals("==")) {
            
            this.procedureEqualOp();
            this.procedureRelationalExpr();
            this.procedureEqualExpr1();
        }
        
        // Vazio
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
    
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
        // Verifica se o token atual eh primeiro de <AdditiveOp>
        if(atual[1].trim().equals("-") || atual[1].trim().equals("+")) {
            
            this.procedureAdditiveOp();
            this.procedureMultExpr();
            this.procedureAdditiveExpr1();
        }
        
        // Vazio
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
    
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");  
        // Verifica se o token atual eh primeiro de <MultOp>
        if(atual[1].trim().equals("*") || atual[1].trim().equals("/")) {
            
            this.procedureMultOp();
            this.procedureUnaryExpr();
            this.procedureMultExpr1();
        }
        
        // Vazio
    }
    
    /**
     * <UnaryExpr> ::= <UnaryOp> <UnaryExpr> | <PostfixExpr> 
     */
    private void procedureUnaryExpr() {
    
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
    
    /**
     * <PrimaryExpr> ::= 'Identifier' | 'Number' | 'Literal' | 'true' | 'false' | '(' <Expr> ')' 
     */
    private void procedurePrimaryExpr() {
        
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
            String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            // Verifica se o token atual eh ')'
            if(atual2[1].trim().equals(")")) {
                
                this.idTokenAtual++;
            } else {
                
                // Erro
                String linha = atual2[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Delimitador ')' não encontrado na linha "+linha.trim()+".\n";
            }            
        } else {
            
            // Erro
            String linha = atual[2].replaceAll(">", " ");
            this.errosSintaticos += "Erro - Expressão mal formada na linha "+linha.trim()+".\n";
        }
    }
    
    /**
     * <EqualOp> ::= '==' | '!='
     */
    private void procedureEqualOp() {
            
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
        }
    }
    
    /**
     * <RelationalOp> ::= '<' | '>' | '<=' | '>='       
     */
    private void procedureRelationalOp() {
        
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
        }
    }      
    
    /**
     * <AdditiveOp> ::= '+' | '-'
     */
    private void procedureAdditiveOp() {
        
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
        }
    }   
    
    /**
     * <MultOp> ::= '*' | '/'
     */
    private void procedureMultOp() {
        
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
        }
    }        
    /**
     * <UnaryOp> ::= '++' | '--' | '!'
     */
    private void procedureUnaryOp() {
        
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
        }
    }
    
    /**
     * <PostfixOp> ::= '++' | '--' | '[' <Expr> ']' | '(' <PostfixOplf> | '.' 'Identifier'              
     */
    private void procedurePostfixOp() {
                
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
            // Verifica se o token atual é ']'
            String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            if(atual2[1].trim().equals("]")){
                
                this.idTokenAtual++;
            } else {
                
                // Erro
                String linha = atual2[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Delimitador ']' não encontrado na linha "+linha.trim()+".\n";
            }
        
        // Verifica se o token atual é '('      
        } else if(atual[1].trim().equals("(")){
            
            this.idTokenAtual++;
            this.procedurePostfixOplf();  
            
        // Verifica se o token atual é '.'      
        } else if(atual[1].trim().equals(".")){
            
            this.idTokenAtual++;
            // Verifica se o token atual é um 'Identifier'
            String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            if(atual2[0].contains("Identificador_")){
                
                this.idTokenAtual++;
            } else {
                
                // Erro
                String linha = atual2[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - Identificador não encontrado na linha "+linha.trim()+".\n";
            }
        } else {

            // Erro
            String linha = atual[2].replaceAll(">", " ");
            this.errosSintaticos += "Erro - Operador ou Delimitador não encontrado na linha "+linha.trim()+".\n";           
        }       
    } 
    
    /**
     * <PostfixOplf> ::= ')' | <ArgumentList> ')'
     */
    private void procedurePostfixOplf() {
        
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
                // Verifica se o token atual eh ')'
                String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                if(atual2[1].trim().equals(")")){
                    
                    this.idTokenAtual++;
                } else{
                
                    // Erro
                    String linha = atual2[2].replaceAll(">", " ");
                    this.errosSintaticos += "Erro - Delimitador ')' não encontrado na linha "+linha.trim()+".\n";
                }        
        } else {
            
            // Erro
            String linha = atual[2].replaceAll(">", " ");
            this.errosSintaticos += "Erro - Delimitador ')' não encontrado na linha "+linha.trim()+".\n";
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
        
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
        // Verifica se o token atual eh ','
        if(atual[0].contains("Delimitador") && atual.length == 4) {
            
            this.idTokenAtual++;
            this.procedureAssignExpr();
            this.procedureArgumentList1();
        }
        
        // Vazio
    }
    
    /**
     * <Type> ::= 'int' | 'string' | 'float' | 'bool'  | 'Identifier'
     */
    private void procedureType() {
        
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
        }
    }
    
}
