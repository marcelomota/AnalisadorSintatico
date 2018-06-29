package controllers;

import java.util.ArrayList;
import models.Gramatica;
import models.Producoes;
import models.Token;

public class ControllerAnalisadorSintatico {
          
    private ArrayList<Producoes> gramatica;
    private String errosSintaticos;
    private Token tokens;
    private int idTokenAtual;
    

    public ControllerAnalisadorSintatico(Gramatica gramatica) {
        this.gramatica = gramatica.getGramatica();
        this.errosSintaticos = "";
    }
    
    public String analisar(Token tokens) {
        
        this.tokens = tokens;        
        this.idTokenAtual = 0;
        //this.procedureS();
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
    public void procedureS() {
        
        this.procedureGlobalDeclaration();
        this.procedureS1();        
    }

    /**
     * <S1> ::= <GlobalDeclaration> <S1> | 
     */
    public void procedureS1() {
    
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
    public void procedureGlobalDeclaration() {
    
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
    
    public void procedureFunctionDef() {}
    public void procedureProcedureDef() {}
    public void procedureTypedefDef() {}
    public void procedureTypedefDeflf() {}
    
    /**
     * <VarDef> ::= 'var' '{' <DeclarationList> '}'
     */
    public void procedureVarDef() {
       
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
    
    public void procedureConstDef() {}
    public void procedureStructDef() {}
    public void procedureStructDeflf() {}
    public void procedureParameterList() {}
    public void procedureParameterList1() {}
    public void procedureParameterDeclaration() {}
    
    /**
     * <DeclarationList> ::= <Declaration> <DeclarationList1>  
     */
    public void procedureDeclarationList() {
    
        this.procedureDeclaration();
        this.procedureDeclarationList1();
    
    }
    
    /**
     * <Declaration> ::= <Type> <InitDeclaratorList> ';'
     */
    public void procedureDeclaration() {
    
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
    public void procedureDeclarationList1() {
    
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
    public void procedureInitDeclaratorList() {
            
        this.procedureInitDeclarator();
        this.procedureInitDeclaratorList1();
    } 
    
    public void procedureInitDeclaratorList1() {}
    
    /**
     * <InitDeclarator> ::= <Declarator> <InitDeclaratorlf>
     */
    public void procedureInitDeclarator() {
    
        this.procedureDeclarator();
        this.procedureInitDeclaratorlf();
    }
    
    /**
     * 
     */
    public void procedureInitDeclaratorlf() {
    
        
    }
    
    public void procedureInitializer() {}
    public void procedureInitializerlf() {}          
    public void procedureInitializerList() {}               
    public void procedureInitializerList1() {}
    
    /**
     * <Declarator> ::= 'Identifier' <Declarator1> 
     */
    public void procedureDeclarator() {
    
        
    } 
    
    public void procedureDeclarator1() {}
    public void procedureDeclarator1lf() {}
    public void procedureStmt() {}
    
    public void procedureStmtOrDeclarationList() {
    
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
    
    public void procedureStmtOrDeclarationList1() {}
    
    /**
     * <StartDef> ::= 'start' '(' ')' '{' <StmtOrDeclarationList> '}'
     */
    public void procedureStartDef() {
            
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
    
    public void procedurePrintStmt() {}             
    public void procedureScanStmt() {}
    public void procedureIterationStmt() {}
    public void procedureIfStmt() {}    
    public void procedureIfStmtlf() {}
    public void procedureReturnStmt() {}
    public void procedureCompoundStmt() {}           
    public void procedureCompoundStmtlf() {}
    public void procedureExprStmt() {}
    public void procedureExpr() {}   
    public void procedureExpr1() {}
    public void procedureAssignExpr() {}                 
    public void procedureAssignExpr1() {}
    public void procedureCondExpr() {}          
    public void procedureLogicalOrExpr() {}     
    public void procedureLogicalOrExpr1() {}
    public void procedureLogicalAndExpr() {}
    public void procedureLogicalAndExpr1() {}
    public void procedureEqualExpr() {}
    public void procedureEqualExpr1() {}
    public void procedureRelationalExpr() {}          
    public void procedureRelationalExpr1() {}
    public void procedureAdditiveExpr() {}         
    public void procedureAdditiveExpr1() {}
    public void procedureMultExpr() {}          
    public void procedureMultExpr1() {}
    public void procedureUnaryExpr() {}             
    public void procedurePostfixExpr() {}           
    public void procedurePostfixExpr1() {}
    public void procedurePrimaryExpr() {}
    public void procedureEqualOp() {}
    public void procedureRelationalOp() {}      
    public void procedureAdditiveOp() {}   
    public void procedureMultOp() {}        
    public void procedureUnaryOp() {}
    public void procedurePostfixOp() {}            
    public void procedurePostfixOplf() {}
    public void procedureArgumentList() {}
    public void procedureArgumentList1() {}
    public void procedureType() {}
      
}
