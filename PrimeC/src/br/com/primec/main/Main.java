package br.com.primec.main;

import br.com.primec.core.LexicalError;
import br.com.primec.core.Lexico;
import br.com.primec.core.SemanticError;
import br.com.primec.core.Semantico;
import br.com.primec.core.Sintatico;
import br.com.primec.core.SyntaticError;
import br.com.primec.gui.PrimecIDE;

public class Main {
    
    public static void main(String[] args) {
        PrimecIDE.loadWindow();
    }
    
    private static void alternativeMethod() {
        Lexico lexico = new Lexico();
        Sintatico sintatico = new Sintatico();
        Semantico semantico = new Semantico();

        lexico.setInput(""
                + "int teste(int um, str doi2) {\n"
                + " int batata = 10;\n"
                + "}"
                + "void main () {\n"
                + "int batata = 10;"
                + "}");

        try {
            sintatico.parse(lexico, semantico);
        } catch (LexicalError e) {
            e.printStackTrace();
        } catch (SyntaticError e) {
            e.printStackTrace();
        } catch (SemanticError e) {
            e.printStackTrace();
        }
    }
    
}
