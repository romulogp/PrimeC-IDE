package br.com.primec.core;

import br.com.primec.core.table.Operation;
import br.com.primec.core.table.Symbol;
import br.com.primec.core.exception.SemanticError;
import br.com.primec.gui.PrimecIDE;
import java.util.Stack;

public class Semantico implements Constants {

    private Symbol currentSymbol;
    private final Stack<String> values = new Stack<>();
    private Operation currentOperation;
    private Token currentToken;
    
    public void executeAction(int action, Token currentToken) throws SemanticError {
        this.currentToken = currentToken;
        switch (action) {
            case 1:
                // Var Type Detection
                detectVarType();
                break;
            case 2:
                // Var Declaration
                varDeclaration();
                break;
            case 3:
                // Function Declaration
                functionDeclaration();
                break;
            case 4:
                // Vector Declaration
                vectorDeclaration();
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
                changeScope();
                break;
            case 24:
                // Function Parameters Declaration
                action24();
                break;
            case 45:
                // Var Initialization
                initializeVar();
                break;
            case 49:
                // Current Operation
                setCurrentOperation();
                break;
            case 50:
                // Left Shift
                leftShift();
                break;
            case 51:
                // Right Shift
                rightShift();
                break;
            case 60:
                sumOrSubtract();
                break;
            case 62:
                // Negative
                negativeNumber();
                break;
            case 70:
                // INTEGER value found
                pushIntegerValue();
                break;
            case 71:
                // DOUBLE value found
                pushDoubleValue();
                break;
            case 100:
                // Var being Used
                setCurrentSymbolBeingUsed();
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
    
    private void detectVarType() {
        currentSymbol = new Symbol();
        currentSymbol.setType(currentToken.getLexeme());
    }

    private void varDeclaration() throws SemanticError {
        currentSymbol.setName(currentToken.getLexeme());
        currentSymbol.setScope(PrimecIDE.scopeStack.lastElement());
        try {
            addSymbol(currentSymbol);
        } catch (SemanticError se) {
            throw new SemanticError("A variável \"" + currentSymbol.getName() + "\" já foi declarada.", currentToken.getPosition());
        }
    }

    private void functionDeclaration() throws SemanticError {
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

    private void vectorDeclaration() throws SemanticError {
        currentSymbol.setName(currentToken.getLexeme());
        currentSymbol.setScope(PrimecIDE.scopeStack.lastElement());
        currentSymbol.setVect(true);
        try {
            addSymbol(currentSymbol);
        } catch (SemanticError se) {
            throw new SemanticError("A variável \"" + currentSymbol.getName() + "\" já foi declarada.", currentToken.getPosition());
        }
    }

    private void changeScope() throws SemanticError {
        modifyScope();
        varDeclaration();
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

    private void initializeVar() {
        currentSymbol = new Symbol();
        currentSymbol.setName(currentToken.getLexeme());
        PrimecIDE.symbolTable.findDeclaration(currentSymbol, PrimecIDE.scopeStack).setInitialized(true);
    }

    private void setCurrentOperation() {
        String OP = currentToken.getLexeme();
        
        for (Operation operation : Operation.values()) {
            if (OP.equals(operation.getDescription())) {
                currentOperation = operation;
            }
        }
    }
    
    public void leftShift() {
        // Left Shift
    }
    
    public void rightShift() {
        // Right Shift
    }
    
    public void sumOrSubtract() {
        double value1 = obtainValue(values.lastElement(), PrimecIDE.scopeStack.lastElement());
        double value2 = obtainValue(values.lastElement(), PrimecIDE.scopeStack.lastElement());
        
        if (currentOperation == Operation.SUM) {
            PrimecIDE.scopeStack.push(String.valueOf(value1 + value2));
        } else if (currentOperation == Operation.SUBTRACT) {
            PrimecIDE.scopeStack.push(String.valueOf(value1 - value2));
        }
    }
    
    public double obtainValue(String name, String scope) {
        Symbol tempSymbol;
        if ((tempSymbol = buildSymbol(name, scope)) != null) {
            values.pop();
            return tempSymbol.getValue();
        } else {
            return Double.parseDouble(values.pop());
        }
    }
    
    public Symbol buildSymbol(String name, String scope) {
        currentSymbol = new Symbol();
        currentSymbol.setName(name);
        currentSymbol.setScope(scope);
        return PrimecIDE.symbolTable.findDeclaration(currentSymbol, PrimecIDE.scopeStack);
    }
    
    public void negativeNumber() {
        
    }
    
    public void pushIntegerValue() {
        System.out.println("INTEGER value found: " + currentToken.getLexeme());
        values.push(currentToken.getLexeme());
    }
    
    public void pushDoubleValue() {
        System.out.println("DOUBLE value found: " + currentToken.getLexeme());
        values.push(currentToken.getLexeme());
    }
    
    private void setCurrentSymbolBeingUsed() throws SemanticError {
        Symbol symbolToSet = buildSymbol(currentToken.getLexeme(), PrimecIDE.scopeStack.lastElement());
        if (symbolToSet != null) {
            symbolToSet.setUsed(true);
        } else {
            throw new SemanticError("A variável \"" + currentSymbol.getName() + "\" não foi declarada.");
        }
    }

}
