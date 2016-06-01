package br.com.primec.core.table;

public enum Operation {
    
    OP_MAIS("+"),
    OP_MENOS("-"),
    OP_VEZES("*"),
    OP_DIVISAO("/"),
    OP_GT(">"),
    OP_LT("<"),
    OP_GTE(">="),
    OP_LTE("<="),
    OP_NEQ("!="),
    OP_EQ("=="),
    OP_LSHIFT("<<"),
    OP_RSHIFT(">>"),
    OP_BWS_AND("&"),
    OP_BWS_OR("|"),
    OP_BWS_XOR("^"),
    OP_BWS_NOT("~"),
    OP_OR("||"),
    OP_AND("&&"),
    OP_NOT("!");

    private final String description;

    private Operation(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return this.description;
    }

}
