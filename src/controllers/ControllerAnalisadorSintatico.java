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
        
        // Verifica se o token atual eh o primeiro de 
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
        // Verifica se eh token autual eh 'function'
        if(atual[1].trim().equals("function")) {
            
            this.idTokenAtual++;
            this.procedureType();
            this.procedureDeclarator();
            String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
            // Verifica se eh token autual eh '('
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
        // Verifica se eh token autual eh o primeiro de <ParameterList>
        if(atual[1].trim().equals("bool") || atual[1].trim().equals("float") ||
                atual[1].trim().equals("int") || atual[1].trim().equals("string") ||
                atual[0].contains("Identificador_")) {
            
            this.procedureParameterList();
            // Verifica se eh token autual eh ')'
            if(atual[1].trim().equals(")")) {
                
                this.idTokenAtual++;
                String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                // Verifica se eh token autual eh '{'
                if(atual2[1].trim().equals("{")) {
                    
                    this.idTokenAtual++;
                    this.procedureStmtOrDeclarationList();
                    String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                    // Verifica se eh token autual eh '}'
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
            
        // Verifica se eh token autual eh ')'
        } else if(atual[1].trim().equals(")")) {
            
            this.idTokenAtual++;
            String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            // Verifica se eh token autual eh '{'
            if(atual2[1].trim().equals("{")) {

                this.idTokenAtual++;
                this.procedureStmtOrDeclarationList();
                String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                // Verifica se eh token autual eh '}'
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
    
        
    }
    
    private void procedureProcedureDeflf() {
        
    }
    
    private void procedureTypedefDef() {}
    private void procedureTypedefDeflf() {}
    
    /**
     * <VarDef> ::= 'var' '{' <DeclarationList> '}'
     */
    private void procedureVarDef() {
       
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");        
        // Verifica se eh token autual eh 'var'
        if(atual[1].trim().equals("var")) {
        
            this.idTokenAtual++;            
            String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            // Verifica se eh token autual eh '{'
            if(atual2[1].trim().equals("{")) {
        
                this.idTokenAtual++;
                this.procedureDeclarationList();
                String[] atual3 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
                // Verifica se eh token autual eh '}'
                if(atual3[1].trim().equals("}")) {
        
                    // Produziu corretamente
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
    
    private void procedureConstDef() {}
    private void procedureStructDef() {}
    private void procedureStructDeflf() {}
    private void procedureParameterList() {}
    private void procedureParameterList1() {}
    private void procedureParameterDeclaration() {}
    
    /**
     * <DeclarationList> ::= <Declaration> <DeclarationList1>  
     */
    private void procedureDeclarationList() {
    
        this.procedureDeclaration();
        this.procedureDeclarationList1();
    
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
            
            // Construiu corretamente            
        } else {
            
            // Erro
        }
    }
    
    /**
     * <DeclarationList1> ::= <Declaration> <DeclarationList1> | 
     */
    private void procedureDeclarationList1() {
    
        String[] atual = this.tokens.getUnicToken(this.idTokenAtual).split(",");
        // Verifica se o token atual eh o primeiro de <Declaration>
        if(atual[1].trim().equals("bool") || atual[1].trim().equals("float") || 
                atual[1].trim().equals("int") || atual[1].trim().equals("string") ||
                atual[0].contains("Identificador_")) {
        
            this.procedureDeclaration();
            this.procedureDeclarationList1();
        }
        
        // Vazio
    }
    
    /**
     * <InitDeclaratorList> ::= <InitDeclarator> <InitDeclaratorList1>
     */
    private void procedureInitDeclaratorList() {
            
        this.procedureInitDeclarator();
        this.procedureInitDeclaratorList1();
    } 
    
    private void procedureInitDeclaratorList1() {}
    
    /**
     * <InitDeclarator> ::= <Declarator> <InitDeclaratorlf>
     */
    private void procedureInitDeclarator() {
    
        this.procedureDeclarator();
        this.procedureInitDeclaratorlf();
    }
    
    /**
     * 
     */
    private void procedureInitDeclaratorlf() {
    
        
    }
    
    private void procedureInitializer() {}
    private void procedureInitializerlf() {}          
    private void procedureInitializerList() {}               
    private void procedureInitializerList1() {}
    
    /**
     * <Declarator> ::= 'Identifier' <Declarator1> 
     */
    private void procedureDeclarator() {
    
        
    } 
    
    private void procedureDeclarator1() {}
    private void procedureDeclarator1lf() {}
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
                            
                            // Produziu corretamente.
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
        } else {
         
            // Erro
            String[] atual2 = this.tokens.getUnicToken(this.idTokenAtual).split(",");
            String linha = atual2[2].replaceAll(">", " ");
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
    private void procedureEqualOp() {}
    private void procedureRelationalOp() {}      
    private void procedureAdditiveOp() {}   
    private void procedureMultOp() {}        
    private void procedureUnaryOp() {}
    private void procedurePostfixOp() {}            
    private void procedurePostfixOplf() {}
    private void procedureArgumentList() {}
    private void procedureArgumentList1() {}
    private void procedureType() {}

    

    
      
}
