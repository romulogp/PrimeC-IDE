package br.com.primec.core.table;

public enum Scope {

    GLOBAL("global");

    private final String description;

    private Scope(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
