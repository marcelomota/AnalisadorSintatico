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
        if(this.idTokenAtual > this.tokens.getSize()) {
            
            // Sucesso
        } else {
            
            // falha
            String msg = "Não foi possivel analisar sintaticamente essse arquivo";
            return msg;
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
            }            
        } else {
            
            // Erro
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
            // Verifica se o token atual eh ')'
            if(atual[1].trim().equals(")")) {
                
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
                    }
                } else {
                    
                    // Erro
                }
            } else {
                
                // Erro
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
                }
            } else {

                // Erro
            }
        } else {
            
            // Erro
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
                }                
            } else {
                
                // Erro
            }
        } else {
            
            // Erro
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
                    }
                } else {

                    // Erro
                }
            } else {
                
                // Erro
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
                }
            } else {
                
                // Erro
            }
        } else {
            
            // Erro
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
                }
            } else {
                
                // Erro
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
                }                
            } else {
                
                // Erro
            }
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
                }
            } else {

                // Erro
            }
        } else {
            
            // Erro
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
                }
            } else {

                // Erro
            }
        } else {
            
            // Erro
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
            }
        } else {

            // Erro
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
                    }
                } else {

                    // Erro
                }
            } else {
                
                // Erro
            }
        } else {
            
            // Erro
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
        if(atual[0].contains("Delimitador") && atual.length == 3) { 
//************************************************************************************** VERIFICAR **************************
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
        if(atual[0].contains("Delimitador") && atual.length == 3) { 
//************************************************************************************** VERIFICAR **************************
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
        }
    }
    
    /**
     * 
     */
    private void procedureInitializerlf() {}
    
    /**
     * 
     */
    private void procedureInitializerList() {}
    
    /**
     * 
     */
    private void procedureInitializerList1() {}
    
    /**
     * <Declarator> ::= 'Identifier' <Declarator1> 
     */
    private void procedureDeclarator() {
    
        
    } 
    
    /**
     * 
     */
    private void procedureDeclarator1() {}
    
    /**
     * 
     */
    private void procedureDeclarator1lf() {}
    
    /**
     * 
     */
    private void procedureStmt() {}
    
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
        }
    } 
    
    private void procedureStmtOrDeclarationList1() {}
    
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
                            String[] atualAux = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                            String linha = atualAux[2].replaceAll(">", " ");
                            this.errosSintaticos += "Erro - '}' esperado na linha "+linha.trim()+".";
                        }
                    } else {
                        
                        // Erro
                        String[] atualAux = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                        String linha = atualAux[2].replaceAll(">", " ");
                        this.errosSintaticos += "Erro - '{' esperado na linha "+linha.trim()+".";
                    }
                } else {
                    
                    // Erro
                    String[] atualAux = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                    String linha = atualAux[2].replaceAll(">", " ");
                    this.errosSintaticos += "Erro - ')' esperado na linha "+linha.trim()+".";                    
                }
            } else {
                
                // Erro  
                String[] atualAux = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                String linha = atualAux[2].replaceAll(">", " ");
                this.errosSintaticos += "Erro - '(' esperado na linha "+linha.trim()+".";
            }
        } else {
         
            // Erro
            String[] atualAux = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            String linha = atualAux[2].replaceAll(">", " ");
            this.errosSintaticos += "Erro - 'Start' esperado na linha "+linha.trim()+".";
        }        
    }
    
    private void procedurePrintStmt() {}             
    private void procedureScanStmt() {}
    private void procedureIterationStmt() {}
    private void procedureIfStmt() {}    
    private void procedureIfStmtlf() {}
    private void procedureReturnStmt() {}
    private void procedureCompoundStmt() {}           
    private void procedureCompoundStmtlf() {}
    private void procedureExprStmt() {}
    private void procedureExpr() {}   
    private void procedureExpr1() {}
    private void procedureAssignExpr() {}                 
    private void procedureAssignExpr1() {}
    private void procedureCondExpr() {}          
    private void procedureLogicalOrExpr() {}     
    private void procedureLogicalOrExpr1() {}
    private void procedureLogicalAndExpr() {}
    private void procedureLogicalAndExpr1() {}
    private void procedureEqualExpr() {}
    private void procedureEqualExpr1() {}
    private void procedureRelationalExpr() {}          
    private void procedureRelationalExpr1() {}
    private void procedureAdditiveExpr() {}         
    private void procedureAdditiveExpr1() {}
    private void procedureMultExpr() {}          
    private void procedureMultExpr1() {}
    private void procedureUnaryExpr() {}             
    private void procedurePostfixExpr() {}           
    private void procedurePostfixExpr1() {}
    private void procedurePrimaryExpr() {}
    /**
     * <EqualOp> ::= '==' | '!='
     */
    private void procedureEqualOp() {
            String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                //verifica se o token atual é '=='
                if(atual[1].trim().equals("==")){
                this.idTokenAtual++;
                } else{
                
                }//verifica se o atual é !=
                if(atual[1].trim().equals("!=")){
                this.idTokenAtual++;
                } else{
                
                }
    }
    /**
     * <RelationalOp> ::= '<' | '>' | '<=' | '>='       
     */
    private void procedureRelationalOp() {
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                //verifica se o token atual é '<'
                if(atual[1].trim().equals("<")){
                this.idTokenAtual++;
                } else{
                
                }//verifica se o atual é '>'
                if(atual[1].trim().equals(">")){
                this.idTokenAtual++;
                } else{
                
                }//verifica se o atual é '<='
                if(atual[1].trim().equals("<=")){
                this.idTokenAtual++;
                }else{
                
                }//verifica se o atual é '>='
                if(atual[1].trim().equals(">=")){
                this.idTokenAtual++;
                }
    }      
    /**
     * <AdditiveOp> ::= '+' | '-'
     */
    private void procedureAdditiveOp() {
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                //verifica se o token atual é '+'
                if(atual[1].trim().equals("+")){
                this.idTokenAtual++;
                } else{
                
                }//verifica se o atual é -
                if(atual[1].trim().equals("-")){
                this.idTokenAtual++;
                } else{
                
                }
    }   
    /**
     * <MultOp> ::= '*' | '/'
     */
    private void procedureMultOp() {
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                //verifica se o token atual é '*'
                if(atual[1].trim().equals("*")){
                this.idTokenAtual++;
                } else{
                
                }//verifica se o atual é '/'
                if(atual[1].trim().equals("/")){
                this.idTokenAtual++;
                } else{
                
                }
    }        
    /**
     * <UnaryOp> ::= '++' | '--' | '!'
     */
    private void procedureUnaryOp() {
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                //verifica se o token atual é '++'
                if(atual[1].trim().equals("++")){
                this.idTokenAtual++;
                } else{
                
                }//verifica se o atual é --
                if(atual[1].trim().equals("--")){
                this.idTokenAtual++;
                } else{
                
                }//verifica se o atual é '!'
                if(atual[1].trim().equals("!")){
                this.idTokenAtual++;
                }
    }
    /**
     * <PostfixOp> ::= '++' | '--' | '[' <Expr> ']' | '(' <PostfixOplf> | '.' 'Identifier'              
     */
    private void procedurePostfixOp() {
                String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                //verifica se o token atual é '++'
                if(atual[1].trim().equals("++")){
                this.idTokenAtual++;
                } else{
                
                }//verifica se o atual é --
                if(atual[1].trim().equals("--")){
                this.idTokenAtual++;
                } else{
                
                }//verifica se o atual é '['
                if(atual[1].trim().equals("[")){
                    this.idTokenAtual++;
                    this.procedureExpr();
                    //verifica se o token atual é ']'
                    String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                    if(atual2[1].trim().equals("]")){
                    this.idTokenAtual++;
                    }
                }//verifica se o token atual é '('
                if(atual[1].trim().equals("(")){
                    this.idTokenAtual++;
                    this.procedurePostfixOplf();
                } else{
                
                } //verifica se o token atual é '.'
                if(atual[1].trim().equals(".")){
                this.idTokenAtual++;
                //verifica se o token atual é um Identificador
                String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                if(atual2[1].trim().equals("Identificador_")){
                    this.idTokenAtual++;
                }
                }else {
                
                }
    } 
    /**
     * <PostfixOplf> ::= ')' | <ArgumentList> ')'
     */
    private void procedurePostfixOplf() {
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
        //verifica se o token atual é ')'
        if(atual[1].trim().equals(")")){
           this.idTokenAtual++; 
        }//verifica se o token atual é primeiro de <ArgumentList>
           if(atual[0].contains("Numero") || atual[1].trim().equals("false") ||
                atual[1].trim().equals("true") || atual[0].contains("Cadeia_de_Caracteres") ||
                atual[1].trim().equals("(") || atual[0].contains("Identificador_") ||
                atual[1].trim().equals("!") || atual[1].trim().equals("++") ||
                atual[1].trim().equals("--")) {
                
                this.procedureArgumentList();
                //verifica se o token atual é igual a ')'
                String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                if(atual2[1].trim().equals(")")){
                this.idTokenAtual++;
                } else{
                
                }
        
        }
        
    
    }
    
    /**
     * <ArgumentList> ::= <AssignExpr> <ArgumentList1>            
     */
    private void procedureArgumentList() {
        
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
        //verifica se o token atual é o primeiro de <AssignExpr>
        if(atual[0].contains("Numero") || atual[1].trim().equals("false") ||
                atual[1].trim().equals("true") || atual[0].contains("Cadeia_de_Caracteres") ||
                atual[1].trim().equals("(") || atual[0].contains("Identificador_") ||
                atual[1].trim().equals("!") || atual[1].trim().equals("++") ||
                atual[1].trim().equals("--")) {
            
                    this.procedureAssignExpr();
                    this.procedureArgumentList1();
        }
    
    
    }
    /**
     * <ArgumentList1> ::= ',' <AssignExpr> <ArgumentList1> | 'vazio'
     */
    private void procedureArgumentList1() {
        
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
        //verifica se o token atual é o primeiro de <AssignExpr>
        if(atual[0].contains("Numero") || atual[1].trim().equals("false") ||
                atual[1].trim().equals("true") || atual[0].contains("Cadeia_de_Caracteres") ||
                atual[1].trim().equals("(") || atual[0].contains("Identificador_") ||
                atual[1].trim().equals("!") || atual[1].trim().equals("++") ||
                atual[1].trim().equals("--")) {
        this.procedureAssignExpr();
        this.procedureArgumentList1();
        }//Concatenando com o conjuto Follow de <ArgumentList1> e verificando se faz parte do mesmo.
            if(atual[1].trim().equals(")")){
                this.idTokenAtual++;
                
                 }
    
    
    }
    
    /**
     * <Type> ::= 'int' | 'string' | 'float' | 'bool'  | 'Identifier'
     */
    private void procedureType() {
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
        //verifica se o token é primeiro de <Type>
        //verifica se o token atual é 'int'
                 if(atual[1].trim().equals("int")) {
                 this.idTokenAtual++;
           
                 } else{
                 
                 }
                 //String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                          //verifica se o token atual é 'string'
                          if(atual[1].trim().equals("string")) {
                          this.idTokenAtual++;
                          } else{
                          
                          }
                          //String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                                   //verifica se o token atual é 'float'
                                   if(atual[1].trim().equals("float")) {
                                   this.idTokenAtual++;
                                   } else{
                                   
                                   }
                                   //String[] atual4 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                                            //verifica se o token atual é 'bool'
                                            if(atual[1].trim().equals("bool")) {
                                            this.idTokenAtual++;
                                            } else{
                                            
                                            }
                                            //String[] atual5 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                                                     //verifica se o token atual é identifier
                                                     if(atual[1].trim().equals("Identificador_")) {
                                                     this.idTokenAtual++;
                                                     } else  {
                                                            
                                                     }
                                       
    
    }

    

    

    
      
}
