package br.com.primec.core;

import br.com.primec.core.table.Operation;
import br.com.primec.core.table.Symbol;
import br.com.primec.core.exception.SemanticError;
import br.com.primec.gui.PrimecIDE;
import java.util.Stack;

public class Semantico implements Constants {

    private Symbol currentSymbol;
    private final Stack<String> stackingValues = new Stack<>();
    private Operation currentOperation;
    private Token currentToken;
    
    public void executeAction(int action, Token currentToken) throws SemanticError {
        this.currentToken = currentToken;
        switch (action) {
            case 1:
                // Var Type Detection
                action1();
                break;
            case 2:
                // Var Declaration
                action2();
                break;
            case 3:
                // Function Declaration
                action3();
                break;
            case 4:
                // Vector Declaration
                action4();
                break;
            case 9:
                // Push Scope
                pushScope();
                break;
            case 10:
                // Pop Scope
                popScope();
                break;
            case 20:
                // Scope Change due to multiple ID's control
                action20();
                break;
            case 24:
                // Function Parameters Declaration
                action24();
                break;
            case 45:
                // Var Initialization
                action45();
                break;
            case 49:
                // Current Operation
                setCurrentOperation();
                break;
            case 50:
                // Left Shift
                action50();
                break;
            case 51:
                // Right Shift
                action51();
                break;
            case 60:
                action60();
                break;
            case 62:
                // Negative
                action62();
                break;
            case 70:
                // INTEGER value found
                action70();
                break;
            case 71:
                // DOUBLE value found
                action71();
                break;
            case 100:
                // Var being Used
                action100();
                break;
        }
        showLog();
    }

    private void showLog() {
        System.out.print(currentToken.getLexeme());
        System.out.println(PrimecIDE.symbolTable.toString());
    }
    
    private void modifyScope() {
        PrimecIDE.scopeStack.push(PrimecIDE.scopeStack.pop() + PrimecIDE.getNextScopeSerial());
    }

    private void addSymbol(Symbol symbol) throws SemanticError {
        PrimecIDE.symbolTable.add(symbol);
        currentSymbol = null;
    }
    
    private void pushScope() {
        System.out.println("Escopo alterado de \"" + PrimecIDE.scopeStack.lastElement() + "\" para \"" + currentToken.getLexeme() + "\"");
        PrimecIDE.scopeStack.push(currentToken.getLexeme());
    }

    private void popScope() {
        String value = PrimecIDE.scopeStack.pop();
        System.out.println("Escopo alterado de \"" + value + "\" para \"" + PrimecIDE.scopeStack.lastElement() + "\"");
    }
    
    private void action1() {
        currentSymbol = new Symbol();
        currentSymbol.setType(currentToken.getLexeme());
    }

    private void action2() throws SemanticError {
        currentSymbol.setName(currentToken.getLexeme());
        currentSymbol.setScope(PrimecIDE.scopeStack.lastElement());
        try {
            addSymbol(currentSymbol);
        } catch (SemanticError se) {
            throw new SemanticError("A variável \"" + currentSymbol.getName() + "\" já foi declarada.", currentToken.getPosition());
        }
    }

    private void action3() throws SemanticError {
        currentSymbol.setName(currentToken.getLexeme());
        currentSymbol.setScope(PrimecIDE.scopeStack.lastElement());
        currentSymbol.setFunction(true);
        try {
            addSymbol(currentSymbol);
        } catch (SemanticError se) {
            throw new SemanticError("A função \"" + currentSymbol.getName() + "\" já foi declarada.", currentToken.getPosition());
        }
        pushScope();
    }

    private void action4() throws SemanticError {
        currentSymbol.setName(currentToken.getLexeme());
        currentSymbol.setScope(PrimecIDE.scopeStack.lastElement());
        currentSymbol.setVect(true);
        try {
            addSymbol(currentSymbol);
        } catch (SemanticError se) {
            throw new SemanticError("A variável \"" + currentSymbol.getName() + "\" já foi declarada.", currentToken.getPosition());
        }
    }

    private void action20() throws SemanticError {
        modifyScope();
        action2();
    }

    private void action24() throws SemanticError {
        currentSymbol.setName(currentToken.getLexeme());
        currentSymbol.setScope(PrimecIDE.scopeStack.lastElement());
        currentSymbol.setParam(true);
        try {
            addSymbol(currentSymbol);
        } catch (SemanticError se) {
            throw new SemanticError("A variável " + currentSymbol.getName() + " já foi declarada.", currentToken.getPosition());
        }
    }

    private void action45() {
        currentSymbol = new Symbol();
        currentSymbol.setName(currentToken.getLexeme());
        PrimecIDE.symbolTable.findDeclaration(currentSymbol, PrimecIDE.scopeStack).setInitialized(true);
    }

    private void setCurrentOperation() {
        String OP = currentToken.getLexeme();
        
        Operation[] operations = Operation.values();
        for (Operation operation : operations) {
            if (OP.equals(operation.getDescription())) {
                currentOperation = operation;
            }
        }
    }
    
    public void action50() {
        
    }
    
    public void action51() {
        
    }
    
    public void action60() {
        
    }
    
    public void action62() {
        
    }
    
    public void action70() {
        System.out.println("INTEGER value found: " + currentToken.getLexeme());
        stackingValues.push(currentToken.getLexeme());
    }
    
    public void action71() {
        System.out.println("DOUBLE value found: " + currentToken.getLexeme());
        stackingValues.push(currentToken.getLexeme());
    }
    
    private void action100() throws SemanticError {
        currentSymbol = new Symbol();
        currentSymbol.setName(currentToken.getLexeme());
        currentSymbol.setScope(PrimecIDE.scopeStack.lastElement());
        Symbol symbolToSet = PrimecIDE.symbolTable.findDeclaration(currentSymbol, PrimecIDE.scopeStack);
        if (symbolToSet != null) {
            symbolToSet.setUsed(true);
        } else {
            throw new SemanticError("A variável \"" + currentSymbol.getName() + "\" não foi declarada.");
        }
    }

}
