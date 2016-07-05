package br.com.primec.core;

import br.com.primec.core.table.Operation;
import br.com.primec.core.table.Symbol;
import br.com.primec.core.exception.SemanticError;
import br.com.primec.core.table.Scope;
import br.com.primec.gui.PrimecIDE;

public class Semantico implements Constants {

    private String funcCallName;
    private String stoId;
    private int vectorSize;
    private Symbol ldSymbol;
    private Symbol ldvSymbol;
    private Symbol forLdSymbol;
    private Token currentToken;
    private Symbol currentSymbol;
    private Operation ioOperation;
    private boolean firstExpression;
    private Operation vectorOperation;
    private Operation attribOperation;
    private Operation commandOperation;
    private Operation currentOperation;
    private Operation relacionalOperation;
    private Operation lineOperation;
    private String tempScopeName;
    private String jumpLabel;
    private boolean hasFunction = false;
    private boolean jumped = false;
    private boolean isInsideFunction = false;
    
    public Semantico() {
        init();
    }
    
    public final void fullReset() {
        this.funcCallName = "";
        this.stoId = null;
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
        this.relacionalOperation = null;
        this.forLdSymbol = null;
        this.lineOperation = null;
        this.tempScopeName = "";
        this.jumpLabel = "";
        this.hasFunction = false;
        this.jumped = false;
        this.isInsideFunction = false;
    }
    
    public final void init() {
        this.funcCallName = "";
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
        this.relacionalOperation = null;
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
            case 22: finalizeFunctionCall(); break;
            case 23: finishExpression(); break;
            case 24: functionParameteresDeclaration(); break;
            case 28: setReturnOperation(); break;
            case 29: openCloseInputOperation(); break;
            case 30: openCloseOutputOperation(); break;
            case 31: generateInput(); break;
            case 32: generateOutputID(); break;
            case 33: attribution(); break;
            case 34: endAttribution(); break;
            case 35: endVectorDetected(); break;
            case 41: endForCommand(); break;
            case 42: endWhileCommand(); break;
            case 43: endIfCommand(); break;
            case 44: startElseCommand(); break;
            case 45: setVariableInitialized(); break;
            case 46: setRelacionalOperation(); break;
            case 47: endCommandOperation(); break;
            case 48: setFunctCallOperation(); break;
            case 49: setCurrentOperation(); break;
            case 70: integerValue(); break;
            case 71: doubleValue(); break;
            case 99: init(); break;
            case 100: setCurrentSymbolBeingUsed(); break;
            case 101: endProgram(); break;
        }
    }

    // #1
    private void detectVarType() {
        currentSymbol = new Symbol();
        currentSymbol.setType(currentToken.getLexeme());
    }
    // #2
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
    // #3
    private void functionDeclaration() throws SemanticError {
        hasFunction = true;
        currentSymbol.setName(currentToken.getLexeme());
        currentSymbol.setScope(PrimecIDE.scopeStack.lastElement());
        currentSymbol.setFunction(true);
        commandOperation = Operation.FUNCTION;
        isInsideFunction = true;
        try {
            addSymbol(currentSymbol);
        } catch (SemanticError se) {
            throw new SemanticError("A função \"" + currentSymbol.getName() + "\" já foi declarada.", currentToken.getPosition());
        }
        pushScope();
    }
    // #4
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
    // #9
    private void pushScope() {
        if (PrimecIDE.scopeStack.lastElement().equals(Scope.GLOBAL.getDescription())) {
            if (hasFunction && !jumped) {
                PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.JMP("main0"));
                jumped = true;
            } else {
                PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.LBL("main0"));
            }
        }
        PrimecIDE.scopeStack.push(currentToken.getLexeme() + PrimecIDE.getNextScopeSerial());
        
        tempScopeName = PrimecIDE.scopeStack.lastElement();
        if (commandOperation != Operation.FUNCTION) {
            setCommandOperation();
        }
        if (commandOperation == Operation.WHILE) {
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.LBL(tempScopeName));
        } else if (commandOperation == Operation.FUNCTION) {
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.LBL(currentSymbol.getName()));
        }
        
    }
    //# 10
    private void popScope() {
        if (isInsideFunction) {
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.RET("0"));
            isInsideFunction = false;
        }
        PrimecIDE.scopeStack.pop();
    }
    // #20
    private void forScopeChange() throws SemanticError {
//        modifyScope();
        varDeclaration();
        forLdSymbol = ldSymbol;
    }
    private void finalizeFunctionCall() {
        
        PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.STO(funcCallName + ));
    }
    // #23
    private void finishExpression() {
        firstExpression = true;
    }
    // #24
    private void functionParameteresDeclaration() throws SemanticError {
        currentSymbol.setName(currentToken.getLexeme());
        currentSymbol.setScope(PrimecIDE.scopeStack.lastElement());
        currentSymbol.setParam(true);
        try {
            addSymbol(currentSymbol);
            PrimecIDE.asmCodeCon.addData(PrimecIDE.asmCodeGen.VAR(currentSymbol.getName()));
        } catch (SemanticError se) {
            throw new SemanticError("A variável " + currentSymbol.getName() + " já foi declarada.", currentToken.getPosition());
        }
    }
    // #28
    private void setReturnOperation() {
        firstExpression = true;
        currentOperation = Operation.SUM;
    }
    // #29
    private void openCloseInputOperation() {
        if (ioOperation == null) {
            this.ioOperation = Operation.INPUT;
        } else {
            this.ioOperation = null;
        }
    }
    // #30
    private void openCloseOutputOperation() {
        if (ioOperation == null) {
            this.ioOperation = Operation.OUTPUT;
        }
    }
    // #31
    private void generateInput() {
        if (ioOperation == Operation.INPUT) {
            PrimecIDE.asmCodeCon.addText(
                    PrimecIDE.asmCodeGen.INPUT(currentToken.getLexeme()));
        }
    }
    // #32
    private void generateOutputID() {
        if ((lineOperation == Operation.IF && commandOperation == Operation.IF)
                || (lineOperation == Operation.WHILE && commandOperation == Operation.WHILE)){
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.LD(currentToken.getLexeme()));
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.STO("1000"));
        }
        if (ioOperation == Operation.OUTPUT && lineOperation == Operation.F_CALL) {
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.LD(currentToken.getLexeme()));
        } else if (ioOperation == Operation.OUTPUT) {
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
    // #33
    private void attribution() {
        firstExpression = true;
        
        currentOperation = Operation.SUM; // Uma operação sempre inicia como positiva
        attribOperation = Operation.ATTRIB;
        lineOperation = attribOperation;
    }
    // #34
    private void endAttribution() {
        PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.STO(stoId));
        attribOperation = null;
    }
    // #35
    private void endVectorDetected() {
        if (attribOperation.equals(Operation.ATTRIB)) {
            ldvSymbol = currentSymbol;
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.STO("$indr"));
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.LDV(ldvSymbol.getName()));
        }
    }
    // #41
    private void endForCommand() {
        if (!tempScopeName.isEmpty()) {
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.LD(forLdSymbol.getName()));
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.ADD("1001"));
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.STO(forLdSymbol.getName()));
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.JMP(tempScopeName));
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.LBL("fim_" + tempScopeName));
            tempScopeName = "";
        }
    }
    // #42
    private void endWhileCommand() {
        if (!tempScopeName.isEmpty()) {
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.JMP(tempScopeName));
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.LBL("fim_" + tempScopeName));
            tempScopeName = "";
        }
    }
    // #43
    private void endIfCommand() {
        if (!tempScopeName.isEmpty()) {
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.LBL("fim_" + tempScopeName));
            tempScopeName = "";
        }
    }
    // #44
    private void startElseCommand() {
        commandOperation = Operation.ELSE;
        jumpLabel = tempScopeName;
        tempScopeName = PrimecIDE.scopeStack.lastElement();
        if (relacionalOperation == Operation.OP_EQ) {
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.BNE("fim_" + tempScopeName));
        }
    }
    // #45
    private void setVariableInitialized() {
        currentSymbol = new Symbol();
        currentSymbol.setName(currentToken.getLexeme());
        PrimecIDE.symbolTable.findDeclaration(currentSymbol, PrimecIDE.scopeStack).setInitialized(true);
    }
    // #46
    private void setRelacionalOperation() {
        this.relacionalOperation = currentOperation;
    }
    // #47
    public void endCommandOperation() {
        if (lineOperation == Operation.FOR && commandOperation == Operation.FOR) {
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.LD(forLdSymbol.getName()));
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.LBL(tempScopeName));
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.SUB("1000"));
            generateRerationalCode();
        }
        if ((lineOperation == Operation.IF && commandOperation == Operation.IF)
                || (lineOperation == Operation.WHILE && commandOperation == Operation.WHILE)) {
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.LD("1000"));
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.SUB("1001"));
            generateRerationalCode();
        }
        tempScopeName = PrimecIDE.scopeStack.lastElement(); 
        
        commandOperation = null;
    }
    // #48
    public void setFunctCallOperation() {
        funcCallName = currentToken.getLexeme();
        currentOperation = Operation.SUM;
        lineOperation = Operation.F_CALL;
    }
    
    
    // #49
    private void setCurrentOperation() {
        currentOperation = searchOperation(currentToken.getLexeme());
    }
    // #70
    public void integerValue() {
        if ((lineOperation == Operation.IF && commandOperation == Operation.IF)
                || (lineOperation == Operation.WHILE && commandOperation == Operation.WHILE)){
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.LDI(currentToken.getLexeme()));
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.STO("1001"));
        } else if (commandOperation == Operation.FOR && relacionalOperation != null) {
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.LDI(currentToken.getLexeme()));
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.STO("1000"));
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.LDI("1"));
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.STO("1001"));
        } else if (commandOperation == Operation.FOR) {
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.LDI(currentToken.getLexeme()));
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.STO(forLdSymbol.getName()));
        } else if (vectorOperation == Operation.VECTOR) {
            vectorSize = Integer.parseInt(currentToken.getLexeme());
            PrimecIDE.asmCodeCon.addData(PrimecIDE.asmCodeGen.VECT(currentSymbol.getName(), vectorSize));
            vectorOperation = null;
        } else if (lineOperation == Operation.F_CALL) {
            
            
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
    // #71
    public void doubleValue() {
        integerValue();
    }    
    // #100
    private void setCurrentSymbolBeingUsed() throws SemanticError {
        Symbol symbolToSet = buildSymbol(currentToken.getLexeme(), PrimecIDE.scopeStack.lastElement());
        if (symbolToSet != null) {
            symbolToSet.setUsed(true);
        } else {
            throw new SemanticError("A variável \"" + currentSymbol.getName() + "\" não foi declarada.");
        }
    }
    // #101
    private void endProgram() {
        PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.HLT());
    }
    
    // LDI
    private void loadImmediate(String value) {
        PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.LDI(value));
    }

    // STO $out_port
    private void generateOutputValue() {
        if (ioOperation == Operation.OUTPUT && lineOperation != Operation.F_CALL) {
            PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.INT_OUTPUT(currentToken.getLexeme()));
        }
    }
    
    // When we have an Attribution operation
    private void storeID() {
        this.stoId = currentSymbol.getName();
    }

    private Operation searchOperation(String search) {
        for (Operation operation : Operation.values()) {
            if (operation.getDescription().equals(search)) {
                 return operation;
            }
        }
        return null;
    }
    
    // Retrieve a symbol from SymbolTable
    public Symbol buildSymbol(String name, String scope) {
        currentSymbol = new Symbol();
        currentSymbol.setName(name);
        currentSymbol.setScope(scope);
        return PrimecIDE.symbolTable.findDeclaration(currentSymbol, PrimecIDE.scopeStack);
    }
    
    // When 'For' is found
    private void modifyScope() {
        PrimecIDE.scopeStack.push(PrimecIDE.scopeStack.pop() + PrimecIDE.getNextScopeSerial());
    }
    
    // Variable Yet Declared THROWS ERROR
    private void addSymbol(Symbol symbol) throws SemanticError {
        PrimecIDE.symbolTable.add(symbol);
    }
    
    public void setCommandOperation() {
        this.commandOperation = searchOperation(currentToken.getLexeme());
        this.lineOperation = commandOperation;
    }
    
    private void generateRerationalCode() {
         if (relacionalOperation != null) switch (relacionalOperation) {
            case OP_EQ:
                PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.BNE("fim_" + tempScopeName));
                break;
            case OP_NEQ:
                PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.BEQ("fim_" + tempScopeName));
                break;
            case OP_LT:
                PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.BGE("fim_" + tempScopeName));
                break;
            case OP_GT:
                PrimecIDE.asmCodeCon.addText(PrimecIDE.asmCodeGen.LTE("fim_" + tempScopeName));
                break;
            default:
                break;
        }
    }
    
}
