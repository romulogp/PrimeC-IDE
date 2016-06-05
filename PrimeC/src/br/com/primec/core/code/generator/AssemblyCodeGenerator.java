package br.com.primec.core.code.generator;

/**
 *
 * @author Rômulo Göelzer Portolann
 */
public class AssemblyCodeGenerator {
    
    public final String input(String id) {
        return "\n"
                + "\tLD $in_port"
                + "\n"
                + "\tSTO " + id;
    }
    
    public final String outputId(String id) {
        return "\n"
                + "\tLD " + id
                + "\n"
                + "\tSTO $out_port";
    }
    
    public final String outputValue(String value) {
        return "\n"
                + "\tLDI " + value
                + "\n"
                + "\tSTO $out_port";          
    } 
}
