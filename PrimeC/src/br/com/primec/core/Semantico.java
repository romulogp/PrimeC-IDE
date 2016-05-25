package br.com.primec.core;

import br.com.primec.gui.PrimecIDE;

public class Semantico implements Constants {

    private Symbol currentSymbol;

    public void executeAction(int action, Token token) throws SemanticError {
        System.out.print("\nAção: #" + action + " - ");
        switch (action) {
            case 1:
                System.out.println("Tipo de variável, Token: ");
                action1(token);
                break;
            case 2:
                System.out.println("Declaração de variável: ");
                action2(token);
                break;
            case 3:
                System.out.print("Declaração de função: ");
                action3(token);
                break;
            case 6:
                System.out.println("Declaração de vetor: ");
                action6(token);
                break;
            case 9:
                System.out.print("Push Scope: ");
                pushScope(token);
                break;
            case 10:
                System.out.print("Pop Scope: ");
                popScope(token);
                break;
            case 20:
                action20(token);
                break;
            case 24:
                System.out.println("Declaração Parâmetros de Função: ");
                action24(token);
                break;
            case 45:
                System.out.println("Inicialização de Variável");
                action45(token);
                break;
            case 100:
                System.out.println("Variável sendo usada: ");
                action100(token);
                break;
        }
        System.out.print(token.getLexeme());
        System.out.println(PrimecIDE.symbolTable.toString());
//        System.out.println("Ação #"+action+", Token: "+token);
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

    private void action6(Token token) throws SemanticError {
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
        System.out.println("Escopo alterado de \"" + PrimecIDE.stack.pop() + "\" para \"" + PrimecIDE.stack.lastElement() + "\"");
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
