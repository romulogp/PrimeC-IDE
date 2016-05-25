package br.com.primec.core;

public class Symbol {

    private String name;
    private String type;
    private boolean initialized;
    private boolean used;
    private String scope;
    private boolean param;
    private int pos;
    private boolean vect;
    private boolean matrix;
    private boolean function;

    public Symbol() {
        this.initialized = false;
        this.used = false;
        this.param = false;
        this.vect = false;
        this.matrix = false;
        this.function = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public boolean isParam() {
        return param;
    }

    public void setParam(boolean param) {
        this.param = param;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public boolean isVect() {
        return vect;
    }

    public void setVect(boolean vect) {
        this.vect = vect;
    }

    public boolean isMatrix() {
        return matrix;
    }

    public void setMatrix(boolean matrix) {
        this.matrix = matrix;
    }

    public boolean isFunction() {
        return function;
    }

    public void setFunction(boolean function) {
        this.function = function;
    }

}
