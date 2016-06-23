package br.com.primec.core;

import br.com.primec.core.table.Operation;
import br.com.primec.core.table.Symbol;
import br.com.primec.core.exception.SemanticError;
import br.com.primec.core.table.Scope;
import br.com.primec.gui.PrimecIDE;

public class Semantico implements Constants {

    private String stoId;
    private int vectorSize;
    private Symbol ldSymbol;
    private Symbol ldvSymbol;
    private Token currentToken;
    private Symbol currentSymbol;
    private Operation ioOperation;
    private boolean firstExpression;
    private Operation vectorOperation;
    private Operation attribOperation;
    private Operation commandOperation;
    private Operation currentOperation;
    
    public Semantico() {
        init();
    }
    
    public final void init() {
        this.vectorSize = 0;
        this.ldSymbol = null;
        this.ldvSymbol = null;
        this.ioOperation = null;
        this.currentToken = null;
        this.currentSymbol = null;
        this.vectorOperation = null;
        this.attribOperation = null;
        this.firstExpression = false;
        this.commandOperation = null;
        this.currentOperation = null;
    }
    
    public void executeAction(int action, Token currentToken) throws SemanticError {
        this.currentToken = currentToken;
        switch (action) {
            case 1: detectVarType(); break;
            case 2: varDeclaration(); break;
            case 3: functionDeclaration(); break;
            case 4: vectorDeclaration(); break;
            case 9: pushScope(); break;
            case 10: popScope(); break;
            case 20: forScopeChange(); break;
            case 24: functionParameteresDeclaration(); break;
            case 29: openCloseInputOperation(); break;
            case 30: openCloseOutputOperation(); break;
            case 31: generateInput(); break;
            case 32: generateOutputID(); break;
            case 33: attribution(); break;
            case 34: endAttribution(); break;
            case 35: endVectorDetected(); break;
            case 45: initializeVar(); break;
            case 49: setCurrentOperation(); break;
            case 50: leftShift(); break;
            case 51: rightShift(); break;
            case 60: /* sumOrSubtract(); */ break;
            case 62: negativeNumber(); break;
            case 70: integerValue(); break;
            case 71: doubleValue(); break;
            case 99: init(); break;
            case 100: setCurrentSymbolBeingUsed(); break;
        }
    }

    private void modifyScope() {
        PrimecIDE.scopeStack.push(PrimecIDE.scopeStack.pop() + PrimecIDE.getNextScopeSerial());
    }

    private void addSymbol(Symbol symbol) throws SemanticError {
        PrimecIDE.symbolTable.add(symbol);
    }
    
    private void pushScope() {
        PrimecIDE.scopeStack.push(currentToken.getLexeme());
        // gravar o comando IF
        setCurrentOperation();
        commandOperation = currentOperation;
    }

    private void popScope() {
        PrimecIDE.scopeStack.pop();
        if (PrimecIDE.scopeStack.lastElement().equals(Scope.GLOBAL.getDescription())) {
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.HLT());
        }
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
            PrimecIDE.asmCodeCon.addData(
                    PrimecIDE.asmCodeGen.VAR(currentSymbol.getName()));
            ldSymbol = currentSymbol;
//            currentSymbol = null;
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
        vectorOperation = Operation.VECTOR;
        if (attribOperation == null) {
            currentSymbol.setName(currentToken.getLexeme());
            currentSymbol.setScope(PrimecIDE.scopeStack.lastElement());
            currentSymbol.setVect(true);
        }
        try {
            addSymbol(currentSymbol);
        } catch (SemanticError se) {
            if (attribOperation == null) {
                throw new SemanticError("A variável \"" + currentSymbol.getName() + "\" já foi declarada.", currentToken.getPosition());
            }
        }
    }

    private void attribution() {
        firstExpression = true;
        // Uma operação sempre inicia como positiva
        currentOperation = Operation.SUM;
        attribOperation = Operation.ATTRIB;
        storeID();
    }
    
    private void endAttribution() {
        PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.STO(stoId));
        attribOperation = null;
    }
    
    private void endVectorDetected() {
        if (attribOperation.equals(Operation.ATTRIB)) {
            ldvSymbol = currentSymbol;
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.STO("$indr"));
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.LDV(ldvSymbol.getName()));
        }
    }
    
    private void storeID() {
        this.stoId = currentSymbol.getName();
    }
    
    private void forScopeChange() throws SemanticError {
        modifyScope();
        varDeclaration();
    }

    private void functionParameteresDeclaration() throws SemanticError {
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
    
    public Symbol buildSymbol(String name, String scope) {
        currentSymbol = new Symbol();
        currentSymbol.setName(name);
        currentSymbol.setScope(scope);
        return PrimecIDE.symbolTable.findDeclaration(currentSymbol, PrimecIDE.scopeStack);
    }
    
    public void negativeNumber() {
        
    }
    
    public void integerValue() {
        if (vectorOperation == Operation.VECTOR) {
            vectorSize = Integer.parseInt(currentToken.getLexeme());
            PrimecIDE.asmCodeCon.addData(PrimecIDE.asmCodeGen.VECT(currentSymbol.getName(), vectorSize));
            vectorOperation = null;
        } else if (ioOperation == Operation.OUTPUT) {
            generateOutputValue();
        } else if (currentOperation == Operation.SUM) {
            if (firstExpression) {
                loadImmediate(currentToken.getLexeme());
                firstExpression = false;
            } else {
                PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.ADDI(currentToken.getLexeme()));
            }
        } else if (currentOperation == Operation.SUBTRACT) {
            if (firstExpression) {
                PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.LDI("0"));
                PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.SUBI(currentToken.getLexeme()));
                firstExpression = false;
            } else {
                PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.SUBI(currentToken.getLexeme()));
            }
        }
    }
    
    private void loadImmediate(String value) {
        PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.LDI(value));
    }
    
    public void doubleValue() {
        integerValue();
    }
        
    private void setCurrentSymbolBeingUsed() throws SemanticError {
        Symbol symbolToSet = buildSymbol(currentToken.getLexeme(), PrimecIDE.scopeStack.lastElement());
        if (symbolToSet != null) {
            symbolToSet.setUsed(true);
        } else {
            throw new SemanticError("A variável \"" + currentSymbol.getName() + "\" não foi declarada.");
        }
    }
    
    private void generateInput() {
        if (ioOperation == Operation.INPUT) {
            PrimecIDE.asmCodeCon.addText(
                    PrimecIDE.asmCodeGen.INPUT(currentToken.getLexeme()));
        }
    }
    
    private void generateOutputID() {
        if (ioOperation == Operation.OUTPUT) {
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.OUTPUT(currentToken.getLexeme()));
        } else if (currentOperation == Operation.SUM) {
            if (firstExpression) {
                PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.LD(currentToken.getLexeme()));
                firstExpression = false;
            } else {
                PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.ADD(currentToken.getLexeme()));
            }
        } else if (currentOperation == Operation.SUBTRACT) {
            if (firstExpression) {
                PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.LDI("0"));
                PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.SUB(currentToken.getLexeme()));
                firstExpression = false;
            } else {
                PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.SUB(currentToken.getLexeme()));
            }
        }
    }
    
    private void generateOutputValue() {
        if (ioOperation == Operation.OUTPUT) {
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.INT_OUTPUT(currentToken.getLexeme()));
        }
    }
    
    private void openCloseInputOperation() {
        if (ioOperation == null) {
            this.ioOperation = Operation.INPUT;
        } else {
            this.ioOperation = null;
        }
    }
    
    private void openCloseOutputOperation() {
        if (ioOperation == null) {
            this.ioOperation = Operation.OUTPUT;
        } else {
            this.ioOperation = null;
        }
    }
    
}
