package br.com.primec.core;

import br.com.primec.gui.PrimecIDE;

public class Semantico implements Constants {

    private Symbol currentSymbol;

    public void executeAction(int action, Token token) throws SemanticError {
        System.out.print("\nAção: #" + action + " - ");
        switch (action) {
            case 1:
                // Var Type Detection
                action1(token);
                break;
            case 2:
                // Var Declaration
                action2(token);
                break;
            case 3:
                // Function Declaration
                action3(token);
                break;
            case 4:
                // Vector Declaration
                action4(token);
                break;
            case 9:
                // Push Scope
                pushScope(token);
                break;
            case 10:
                // Pop Scope
                popScope(token);
                break;
            case 20:
                // Scope Change due to multiple ID's control
                action20(token);
                break;
            case 24:
                // Function Parameters Declaration
                action24(token);
                break;
            case 45:
                // Var Initialization
                action45(token);
                break;
            case 50:
                // Sum and Subtract
                action50(token);
                break;
            case 51:
                // Multiplication and Division
                action51(token);
                break;
            case 52:
                // Negative
                action52(token);
                break;
            case 60:
                action60(token);
                break;
            case 61:
                action61(token);
                break;
            case 100:
                // Var being Used
                action100(token);
                break;
        }
        showLog(token);
    }

    private void showLog(Token currentToken) {
        System.out.print(currentToken.getLexeme());
        System.out.println(PrimecIDE.symbolTable.toString());
    }
    
    private void action1(Token token) {
        currentSymbol = new Symbol();
        currentSymbol.setType(token.getLexeme());
    }

    private void action2(Token token) throws SemanticError {
        currentSymbol.setName(token.getLexeme());
        currentSymbol.setScope(PrimecIDE.stack.lastElement());
        try {
            addSymbol(currentSymbol);
        } catch (SemanticError se) {
            throw new SemanticError("A variável \"" + currentSymbol.getName() + "\" já foi declarada.", token.getPosition());
        }
    }

    private void action3(Token token) throws SemanticError {
        currentSymbol.setName(token.getLexeme());
        currentSymbol.setScope(PrimecIDE.stack.lastElement());
        currentSymbol.setFunction(true);
        try {
            addSymbol(currentSymbol);
        } catch (SemanticError se) {
            throw new SemanticError("A função \"" + currentSymbol.getName() + "\" já foi declarada.", token.getPosition());
        }
        pushScope(token);
    }

    private void action4(Token token) throws SemanticError {
        currentSymbol.setName(token.getLexeme());
        currentSymbol.setScope(PrimecIDE.stack.lastElement());
        currentSymbol.setVect(true);
        try {
            addSymbol(currentSymbol);
        } catch (SemanticError se) {
            throw new SemanticError("A variável \"" + currentSymbol.getName() + "\" já foi declarada.", token.getPosition());
        }
    }

    private void pushScope(Token token) {
        System.out.println("Escopo alterado de \"" + PrimecIDE.stack.lastElement() + "\" para \"" + token.getLexeme() + "\"");
        PrimecIDE.stack.push(token.getLexeme());
    }

    private void popScope(Token token) {
        String value = PrimecIDE.stack.pop();
        System.out.println("Escopo alterado de \"" + value + "\" para \"" + PrimecIDE.stack.lastElement() + "\"");
    }

    private void action20(Token token) throws SemanticError {
        modifyScope();
        action2(token);
    }

    private void action24(Token token) throws SemanticError {
        currentSymbol.setName(token.getLexeme());
        currentSymbol.setScope(PrimecIDE.stack.lastElement());
        currentSymbol.setParam(true);
        try {
            addSymbol(currentSymbol);
        } catch (SemanticError se) {
            throw new SemanticError("A variável " + currentSymbol.getName() + " já foi declarada.", token.getPosition());
        }
    }

    private void action45(Token token) {
        currentSymbol = new Symbol();
        currentSymbol.setName(token.getLexeme());
        PrimecIDE.symbolTable.findDeclaration(currentSymbol, PrimecIDE.stack).setInitialized(true);
    }

    public void action50(Token token) {
        
    }
    
    public void action51(Token token) {
        
    }
    
    public void action52(Token token) {
        
    }
    
    public void action60(Token token) {
        
    }
    
    public void action61(Token token) {
        
    }
    
    private void action100(Token token) throws SemanticError {
        currentSymbol = new Symbol();
        currentSymbol.setName(token.getLexeme());
        currentSymbol.setScope(PrimecIDE.stack.lastElement());
        Symbol symbolToSet = PrimecIDE.symbolTable.findDeclaration(currentSymbol, PrimecIDE.stack);
        if (symbolToSet != null) {
            symbolToSet.setUsed(true);
        } else {
            throw new SemanticError("A variável \"" + currentSymbol.getName() + "\" não foi declarada.");
        }
    }

    private void modifyScope() {
        PrimecIDE.stack.push(PrimecIDE.stack.pop() + PrimecIDE.getNextScopeSerial());
    }

    private void addSymbol(Symbol symbol) throws SemanticError {
        PrimecIDE.symbolTable.add(symbol);
        currentSymbol = null;
    }

}
