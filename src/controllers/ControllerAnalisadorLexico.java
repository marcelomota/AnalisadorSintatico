package controllers;

import models.TabelaSimbolo;
import models.Token;

public class ControllerAnalisadorLexico {
       
    /**
     * Percorre caractere por caractere da String, cataloga os tokens, lexemas e os respectivos erros.
     * @param texto String
     * @param tokens Token
     * @param simbolos TabelaSimbolo
     * @return String - Erros
     */
    public String analisar(String texto, Token tokens, TabelaSimbolo simbolos){
        
        // Contador de posicoes da String.
        int i = 0;
        // Contador de linhas.
        int linha = 1;
        // String de erros.
        String erros = "";
        // Guarda o token anterior.
        String anterior = "";
        
        // Repeticao em quanto houverem caracteres para serem lidos.
        do {
            // Obtem o caractere da posicao atual.
            char atual = texto.charAt(i);  
            
            // Verifica se o caractere eh um operador logico.
            if(operadoresLogicos(Character.toString(atual))){
                // Recebe o contador de posicoes +1.
                int j = i+1;
                // Obtem o proximo caractere.
                char atual2 = texto.charAt(j);                
                if(atual == '&') {
                    // Verifica se eh o Operador Logico & ou &&.
                    if(atual2 == '&') {    
                        // Adiciona o Token.
                        tokens.addToken("Op.Logico", "&&", linha);
                        // Adiciona ao Token anterior.
                        anterior = "&&";
                        // Incrementa a posicao atual referente ao segundo &.
                        i++;
                    } else {
                        // Adiciona o Token.
                        tokens.addToken("Op.Logico", Character.toString(atual), linha);
                        // Adiciona ao Token anterior.
                        anterior = Character.toString(atual);
                    }
                } else if(atual == '|') {    
                    // Verifica se eh o Operador Logico | ou ||.
                    if(atual2 == '|') {
                        // Adiciona o Token.
                        tokens.addToken("Op.Logico", "||", linha);
                        // Adiciona ao Token anterior.
                        anterior = "||";
                        i++;
                    } else {
                        // Adiciona o Token.
                        tokens.addToken("Op.Logico", Character.toString(atual), linha);
                        // Adiciona ao Token anterior.
                        anterior = Character.toString(atual);
                    }
                }
            // Verifica se o caractere eh um Operador Relacional.
            } else if(operadoresRelacionais(Character.toString(atual))){
                // Recebe o contador de posicoes +1.
                int j = i+1;
                // Obtem o proximo caractere.
                char atual2 = texto.charAt(j);                
                if(atual == '!') {
                    // Verifica se eh o Operador Logico ! ou o Operador Relacional !=.
                    if(atual2 == '=') {  
                        // Adiciona o Token.
                        tokens.addToken("Op.Relacional", "!=", linha);
                        // Adiciona ao Token anterior.
                        anterior = "!=";
                        // Incrementa o contador de posicoes referente ao =.
                        i++;
                    } else {
                        // Adiciona o Token.
                        tokens.addToken("Op.Logico", Character.toString(atual), linha);
                        // Adiciona ao Token anterior.
                        anterior = Character.toString(atual);
                    }
                } else if(atual == '=') {
                    // Verifica se eh o Operador Relacional = ou ==.
                    if(atual2 == '=') {
                        // Adiciona o Token.
                        tokens.addToken("Op.Relacional", "==", linha);
                        // Adiciona ao Token anterior.
                        anterior = "==";
                        // Incrementa o contador de posicoes referente ao segundo =.
                        i++;
                    } else {
                        // Adiciona o Token.
                        tokens.addToken("Op.Relacional", Character.toString(atual), linha);
                        // Adiciona ao Token anterior.
                        anterior = Character.toString(atual);
                    }
                } else if(atual == '<') {
                    // Verifica se eh o Operador Relacional < ou <=.
                    if(atual2 == '=') {
                        // Adiciona o Token.
                        tokens.addToken("Op.Relacional", "<=", linha);
                        // Adiciona ao Token anterior.
                        anterior = "<=";
                        // Incrementa o contador de posicoes referente ao =.
                        i++;
                    } else {
                        // Adiciona o Token.
                        tokens.addToken("Op.Relacional", Character.toString(atual), linha);
                        // Adiciona ao Token anterior.
                        anterior = Character.toString(atual);
                    }
                } else if(atual == '>') {
                    // Verifica se eh o Operador Relacional > ou >=.
                    if(atual2 == '=') {
                        // Adiciona o Token.
                        tokens.addToken("Op.Relacional", ">=", linha);
                        // Adiciona ao Token anterior.
                        anterior = ">=";
                        // Incrementa o contador de posicoes referente ao =.
                        i++;
                    } else {
                        // Adiciona o Token.
                        tokens.addToken("Op.Relacional", Character.toString(atual), linha);
                        // Adiciona ao Token anterior.
                        anterior = Character.toString(atual);
                    }
                }
            // Verifica se o caractere eh um Delimitador.    
            } else if(delimitadores(Character.toString(atual))){
                // Adiciona o Token.
                tokens.addToken("Delimitador", Character.toString(atual), linha);
                // Adiciona ao Token anterior.
                anterior = Character.toString(atual);
            // Verifica se o caractere eh um Operador Aritmetico.    
            } else if(operadoresAritmeticos(Character.toString(atual))) {
                // Recebe o contador de posicoes +1.
                int j = (i+1);
                // Obtem o proximo caractere.
                char atual2 = texto.charAt(j);                                
                if(atual == '/') {                    
                    // Verifica se eh um Comentario de Linha
                    if(atual2 == '/') {
                        // Avanca as posicoes em quanto nao chegar ao final da linha.
                        while(atual2 != '\n') { 
                            // Incrementa o contador de posicoes.
                            i++; 
                            // Verifica se chegou ao final da String
                            if(i >= texto.length()) {
                                break;
                            } 
                            // Recebe o caractere da posicao atual.
                            atual2 = texto.charAt(i);                        
                        }    
                        // Incrementa o contador de linhas.
                        linha++;  
                    // Verifica se eh um Comentario de Bloco    
                    } else if (atual2 == '*') {   
                        // Referencia o caractere anterior, mas inicia com um nao utilizado.
                        char anterior2 = '§';
                        // Controle da repeticao.
                        boolean chave = true;
                        while(chave) { 
                            // Verifica se chegou ao fecha comentario.
                            if(anterior2 == '*' && atual2 == '/'){
                                chave = false;                                
                            } else {
                                if(atual2 == '\n') {
                                    // Incrementa o contador de linhas.
                                    linha++;
                                }
                                // Incrementa o contador de posicoes.
                                i++; 
                                // Verifica se chegou ao final da String
                                if(i >= texto.length()) {
                                    // Decrementa o contador de linhas por causa do \n do final.
                                    linha--;
                                    // Adiciona o Erro encontrado.
                                    erros += "Erro na linha "+linha+" - Fecha comentario não encontrado!\n";
                                    break;
                                } 
                                // Recebe o caractere atual.
                                anterior2 = atual2;
                                // Recebe o proximo caractere.
                                atual2 = texto.charAt(i); 
                            }                                                   
                        }
                    } else {
                        // Adiciona o Token.
                        tokens.addToken("Op.Aritmetico", Character.toString(atual), linha); 
                        // Adiciona ao Token anterior.
                        anterior = Character.toString(atual);
                    }
                } else if(atual == '+') {
                    // Verifica se eh o Operador Aritmetico + ou ++.
                    if(atual2 == '+') {
                        // Adiciona o Token.
                        tokens.addToken("Op.Aritmetico", "++", linha); 
                        // Adiciona ao Token anterior.
                        anterior = "++";
                        // Incrementa o contador de posicoes referente ao segundo +.
                        i++;
                    } else {
                        // Adiciona o Token.
                        tokens.addToken("Op.Aritmetico", Character.toString(atual), linha);
                        // Adiciona ao Token anterior.
                        anterior = Character.toString(atual);
                    }                    
                } else if(atual == '-') {
                    // Verifica se eh o Operador Aritmetico - ou --.
                    if(atual2 == '-') {
                        // Adiciona o Token.
                        tokens.addToken("Op.Aritmetico", "--", linha); 
                        // Adiciona ao Token anterior.
                        anterior = "--";
                        // Incrementa o contador de posicoes referente ao segundo -.
                        i++;
                    } else {
                        // Obtem o numero da linha atual.
                        int linhaAux = linha;
                        // Avanca as posicoes em quanto os proximos caracteres forem espacos.
                        while(espaco(Character.toString(atual2))){
                            if(atual2 == '\n') {
                                // Incrementa o contador de linhas auxiliar.
                                linhaAux++;
                            }
                            // Incrementa o contador de posicoes auxiliar.
                            j++;
                            // Verifica se chegou ao final da String.
                            if(j >= texto.length()) {                                
                                break;
                            } 
                            // Recebe o caractere atual.
                            atual2 = texto.charAt(j);
                        } 
                        // Verifica se o caractere eh um digito.
                        if(digito(Character.toString(atual2))) {
                            // O contador de linhas recebe o contador de linhas auxliaiar.
                            linha = linhaAux;
                            // Armazena o numero lido.
                            String numero = "";
                            /* Verifica o Token anterior, para saber se o numero eh negativo ou 
                            * o - eh um Operador Aritmetico.
                            */
                            if(verificaAnterior(anterior)) {
                                // Adiciona o - ao numero. 
                                numero += "-";
                            } else {
                                // Adiciona o Token.
                                tokens.addToken("Op.Aritmetico", Character.toString(atual), linha);                            
                            } 
                            // Em quantos os proximos caracteres forem digitos, adicona ao numero.
                            while(digito(Character.toString(atual2))){                                
                                // Adiciona o digito atual.
                                numero += Character.toString(atual2);
                                // Incrementa o contador de posicoes auxiliar.
                                j++;
                                // Verifica se chegou ao final da String.
                                if(j >= texto.length()) {                                
                                    break;
                                } 
                                // Recebe o caractere atual.
                                atual2 = texto.charAt(j);
                            }
                            // Verifica se o caractere atual eh um ponto.
                            if(atual2 == '.') {                                    
                                // Incrementa o contador de posicoes auxiliar.
                                j++;
                                // Verifica se chegou ao final da String.
                                if(j < texto.length()) { 
                                    // Recebe o caractere atual.
                                    atual2 = texto.charAt(j);
                                    // Verifica se apos o . eh um digito.
                                    if(digito(Character.toString(atual2))) {
                                        // Adiciona o . ao numero.
                                        numero += ".";
                                        // Em quantos os proximos caracteres forem digitos, adicona ao numero.
                                        while(digito(Character.toString(atual2))){                                
                                            // Adiciona o digito atual.
                                            numero += Character.toString(atual2);
                                            // Incrementa o contador de linhas auxiliar.
                                            j++;
                                            // Verifica se chegou ao fim da String.
                                            if(j >= texto.length()) {                                
                                                break;
                                            } 
                                            // Recebe o caractere atual.
                                            atual2 = texto.charAt(j);
                                        }
                                        // Adiciona o Token.
                                        tokens.addToken("Numero", numero, linha);  
                                        // Adiciona ao Token anterior.
                                        anterior = numero;
                                        // O contador de caracteres recebe a posicao do ultimo digito.
                                        i = (j-1);
                                    } else {
                                        // Adiciona o Token.
                                        tokens.addToken("Numero", numero, linha); 
                                        // Adiciona ao Token anterior.
                                        anterior = numero;
                                        // O contador de caracteres recebe a posicao do ultimo digito.
                                        i = (j-2);
                                    } 
                                } else {
                                    // Adiciona o Token.
                                    tokens.addToken("Numero", numero, linha); 
                                    // Adiciona ao Token anterior.
                                    anterior = numero;
                                    // O contador de caracteres recebe a posicao do ultimo digito.
                                    i = (j-2);
                                }                                                                    
                            } else {
                                // Adiciona o Token.
                                tokens.addToken("Numero", numero, linha);  
                                // Adiciona ao Token anterior.
                                anterior = numero;
                                // O contador de caracteres recebe a posicao do ultimo digito.
                                i = (j-1);
                            }                              
                        } else {
                            // Adiciona o Token. 
                            tokens.addToken("Op.Aritmetico", Character.toString(atual), linha);
                            // Adiciona ao Token anterior.
                            anterior = Character.toString(atual);
                        }                          
                    }
                // Operador Aritmetico *.
                } else if(atual == '*') {
                    // Adiciona o Token.
                    tokens.addToken("Op.Aritmetico", Character.toString(atual), linha);  
                    // Adiciona ao Token anterior.
                    anterior = Character.toString(atual);
                }
            // Verifica se o caractere eh uma letra.    
            } else if(letra(Character.toString(atual))) {  
                // Armazena a sequencia de letras ou simbolos permitidos.
                String palavra = Character.toString(atual);
                // Recebe o contador de posicoes +1.
                int j = (i+1);
                // Obtem o proximo caractere.
                char atual2 = texto.charAt(j);    
                // Em quanto for uma letra ou um simbolo valido para os Identificadores, armazena na String palavra.
                while(condicaoFinal(Character.toString(atual2))) { 
                    // Adiciona o caractere atual.
                    palavra += Character.toString(atual2);
                    // Incrementa o contador de posicoes auxiliar.
                    j++;                    
                    // Verifica se chegou ao final da String.
                    if(j >= texto.length()) {
                        break;
                    } else {
                        // Recebe o procimo caractere.
                        atual2 = texto.charAt(j);
                    }                                             
                }
                // Verifica se a palavra armazena eh uma Palavra Reservada.
                if(palavrasReservadas(palavra)) {
                    // Adiciona o Token.
                    tokens.addToken("Palavra_Reservada", palavra, linha);  
                    // Adiciona ao Token anterior.
                    anterior = palavra;
                } else {     
                    // Eh um Identificador.
                    // Armazena o nome do Identificador.
                    String nome;
                    // Verfica se o Identificador ja foi catalogado.
                    if(simbolos.contem(palavra)) {
                        // Recebe o nome do Identificador.
                        nome = simbolos.getNomeSimbolo(palavra);
                    } else {
                        // Adiciona um novo Identificador.
                        nome = simbolos.addSimbolo(palavra);
                    }
                    // Adiciona o Token.
                    tokens.addToken(nome, palavra, linha);  
                    // Adiciona ao Token anterior.
                    anterior = nome;
                }
                // O contador de posicoes recebe a posicao do ultimo caractere utilizado.
                i = (j-1);
            // Verifica se chegou ao inicio de uma Cadeia de Caracteres (Iniciada pela ").    
            } else if(atual == '"') {   
                // Incrementa o contador de posicoes referente ao ".
                i++;
                // Obtem o proximo caractere.
                char atual2 = texto.charAt(i); 
                // Recebe o numero de linhas atual.
                int linhaInicial = linha;
                // Armazena a cadeia de caracteres lida.
                String cadeia = "";
                // Para verificar se a cadeia de caracteres esta completa.
                boolean controleErro = true;
                // Repete em quanto o caractere atual nao for uma ".
                while(atual2 != '"') {                     
                    if(atual2 == '\n'){
                        // Incrementa o contador de linhas.
                        linha++;
                    }                     
                    // Verifica o caractere atual eh uma \.
                    if(atual2 == '\\'){
                        // Recebe o proximo caractere.
                        char verifica = texto.charAt(i+1);
                        // Verifica se o proximo caractere eh uma ".
                        if(verifica == '"') {
                            // Adiciona aspas dentro da cadeia, \".
                            cadeia += '\"';
                            // Incrementa o contador de posicoes referente ao \".
                            i = (i+2);
                        } else {
                            // Adiciona o caractere atual a cadeia.
                            cadeia += Character.toString(atual2);
                            // Incrementa o contador de posicoes.
                            i++; 
                        } 
                    } else {
                        // Adiciona o caractere atual a cadeia.
                        cadeia += Character.toString(atual2);
                        // Incrementa o contador de posicoes.
                        i++;   
                    } 
                    // Verifica se chegou ao final da String
                    if(i >= texto.length()) {
                        // Decrementa o contador de linhas (\n no final da linha).
                        linha--;
                        // Adiciona o Erro encontrado.
                        erros += "Erro na linha "+linha+" - Fecha aspas não encontrado!\n";                          
                        controleErro = false;
                        break;
                    } else {
                        // Recebe o proximo caractere.
                        atual2 = texto.charAt(i);
                    } 
                }
                // Verifica se houve erro.
                if(controleErro) {
                   // Verifica a validade da cadeia de caracteres.
                    if(cadeiaDeCaracteres(cadeia)){      
                        // Adiciona o Token.
                        tokens.addToken("Cadeia_de_Caracteres", cadeia, linha); 
                        // Adiciona ao Token anterior.
                        anterior = cadeia;
                    } else {
                        // Adiciona o Erro encontrado.
                        erros += "Erro na linha "+linha+" - Cadeia de caracteres inválida!\n";
                    }   
                }        
            // Verifica se o caractere eh um digito.    
            } else if(digito(Character.toString(atual))) {
                // Armazena a sequencia de digitos.
                String numero = Character.toString(atual);
                // Recebe o contador de posicoes +1.
                int j = (i+1);
                // Obtem o proximo caractere.
                char atual2 = texto.charAt(j); 
                // Em quanto os proximos caracteres forem digitos, adiciona ao numero.
                while(digito(Character.toString(atual2))) {   
                    // Adiciona o caractere atual.
                    numero += Character.toString(atual2);
                    // Incrementa o contador de posicoes auxiliar.
                    j++;
                    // Verifica se chegou ao final da String.
                    if(j >= texto.length()) {                                
                        break;
                    } 
                    // Recebe o proximo caractere.
                    atual2 = texto.charAt(j);
                }
                // Verifica se o caractere atual eh um ponto.
                if(atual2 == '.') {                    
                    // Incrementa o contador de posicoes auxiliar.
                    j++;
                    // Verifica se chegou ao final da String.
                    if(j < texto.length()) { 
                        // Recebe o caractere atual.
                        atual2 = texto.charAt(j);   
                        // Verifica se o caractere eh um digito.
                        if(digito(Character.toString(atual2))) {
                            // Adiciona o . ao numero.
                            numero += ".";            
                            // Continua na repeticao em quanto o proximo caractere for um digito.
                            while(digito(Character.toString(atual2))){                           
                                // Adiciona o digito atual ao numero.
                                numero += Character.toString(atual2);
                                // Incrementa o contador de posicoes auxiliar.
                                j++;
                                // Verifica se chegou ao final da String.
                                if(j >= texto.length()) {                                
                                    break;
                                } 
                                // Recebe o proximo caractere.
                                atual2 = texto.charAt(j);
                            }
                            // Adiciona o Token.
                            tokens.addToken("Numero", numero, linha);  
                            // Adiciona ao Token anterior.
                            anterior = numero;
                            // O contador de posicoes recebe a posicao do ultimo caractere utilizado.
                            i = (j-1);
                        } else {
                            // Adiciona o Token.
                            tokens.addToken("Numero", numero, linha); 
                            // Adiciona ao Token anterior.
                            anterior = numero;
                            // O contador de posicoes recebe a posicao do ultimo caractere utilizado.
                            i = (j-2);
                        } 
                    } else {
                        // Adiciona o Token.
                        tokens.addToken("Numero", numero, linha); 
                        // Adiciona ao Token anterior.
                        anterior = numero;
                        // O contador de posicoes recebe a posicao do ultimo caractere utilizado.
                        i = (j-2);
                    }                                                                    
                } else {
                    // Adiciona o Token.
                    tokens.addToken("Numero", numero, linha);  
                    // Adiciona ao Token anterior.
                    anterior = numero;
                    // O contador de posicoes recebe a posicao do ultimo caractere utilizado.
                    i = (j-1);
                }
            // Verifica se o caractere atual eh quebra de linha.    
            } else if(atual == '\n'){
                // Incrementa ocontador de linhas. 
                linha++;
            // Se nao for nenhuma das opcoes acima e nao for espaco em branco: Erro de Caractere Inválido.    
            } else if(atual != ' ' && atual != '\t' && atual != '\r') {
                // Adiciona o Erro encontrado.
                erros += "Erro na linha "+linha+" - Caractere inválido\n";
            }
            i++;        
        } while(i < texto.length());     
        return erros;
    }
    
    /**
     * Verifica se a String recebida é igual a alguma das Strings reservadas.
     * @param palavra String
     * @return 
     */
    public boolean palavrasReservadas(String palavra) {
        
        if(palavra.equals("const")) {
            return true;
        } else if(palavra.equals("var")) {
            return true;
        } else if(palavra.equals("struct")) {
            return true;
        } else if(palavra.equals("typedef")) {
            return true;
        } else if(palavra.equals("procedure")) {
            return true;
        } else if(palavra.equals("function")) {
            return true;
        } else if(palavra.equals("return")) {
            return true;
        } else if(palavra.equals("start")) {
            return true;
        } else if(palavra.equals("if")) {
            return true;
        } else if(palavra.equals("then")) {
            return true;
        } else if(palavra.equals("else")) {
            return true;
        } else if(palavra.equals("while")) {
            return true;
        } else if(palavra.equals("scan")) {
            return true;
        } else if(palavra.equals("print")) {
            return true;
        } else if(palavra.equals("int")) {
            return true;
        } else if(palavra.equals("float")) {
            return true;
        } else if(palavra.equals("bool")) {
            return true;
        } else if(palavra.equals("string")) {
            return true;
        } else if(palavra.equals("true")) {
            return true;
        } else if(palavra.equals("false")) {
            return true;
        } else if(palavra.equals("extends")) {
            return true;
        }
        return false;
    }
    
    /**
     * Verifica se a String Recebida eh valida pela expressao regular.
     * @param caractere String
     * @return 
     */
    public boolean operadoresAritmeticos(String caractere) {
        return caractere.matches("[\\+|\\-|\\*|/]");
    }
    
    /**
     * Verifica se a String Recebida eh valida pela expressao regular.
     * @param caractere String
     * @return 
     */
    public boolean operadoresRelacionais(String caractere) {
        return caractere.matches("[!|=|<|>]");
    }
    
    /**
     * Verifica se a String Recebida eh valida pela expressao regular.
     * @param caractere String
     * @return 
     */
    public boolean operadoresLogicos(String caractere) {
        if(caractere.equals("|")) {
            return true;
        } else {
            return caractere.matches("[&]");
        }
    }
    
    /**
     * Verifica se a String Recebida eh valida pela expressao regular.
     * @param caractere String
     * @return 
     */
    public boolean delimitadores(String caractere) {
        return caractere.matches("[;|,|\\(|\\)|\\[|\\]|\\{|\\}|\\.]");
    }
    
    /**
     * Verifica se a String Recebida eh valida pela expressao regular.
     * @param cadeia String
     * @return 
     */
    public boolean cadeiaDeCaracteres(String cadeia) {
        // Caracteres especiais - Os caracteres utilizados pela expressao regular.
        String especiais = "\\]|\\[|\\+|\\$|\\^|\\{|\\}|\\|\\?|\\.|\\(|\\)|\\*|\\-|\\\\|\"";
        // Outros - Letras e numeros.
        String outros = "a-z|A-Z|0-9|";
        // Asc - Os caracteres da tabela ascII que nao foram encluidos acima.
        String asc = " |#|!|%|´|`|@|/|~|_|<|>|=|:|;|,|'|&";
        return cadeia.matches("["+especiais+outros+asc+"]*");
    }
    
    /**
     * Verifica se a String Recebida eh valida pela expressao regular.
     * @param caractere String
     * @return 
     */
    public boolean condicaoFinal(String caractere) {
        return caractere.matches("[a-z|A-Z|0-9|_]*");
    }
    
    /**
     * Verifica se a String Recebida eh valida pela expressao regular.
     * @param caractere String
     * @return 
     */
    public boolean letra(String caractere){
        return caractere.matches("[a-z|A-Z]"); 
    }
    
    /**
     * Verifica se a String Recebida eh valida pela expressao regular.
     * @param caractere String
     * @return 
     */
    public boolean espaco(String caractere) {
        return caractere.matches("[\t|\n|\r| ]"); 
    }
    
    /**
     * Verifica se a String Recebida eh valida pela expressao regular.
     * @param caractere String
     * @return 
     */
    public boolean digito(String caractere) {
        return caractere.matches("[0-9]"); 
    }
    
    /**
     * Verifica se a String Recebida eh valida pela expressao regular.
     * @param anterior String
     * @return 
     */
    public boolean verificaAnterior(String anterior) {
        return anterior.matches("[!|=|<|>|\\+|\\-|\\*|/|\\(|,]");
    }
}
