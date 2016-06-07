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
    
    public final String var(String id) {
        return "\n"
                + "\t" + id + " : 0";
    }
    
    public final String vector(String id, int size) {
        String data = "\n"
                + "\t" + id + " : ";
        for (int i = 0; i < size-1; i++) {
            data += "0, ";
        }
        data += "0";
        return data;
    }
    
    public final String ADD(String id) {
        return "\n"
                + "\tADD " + id;
    }
    
    public final String ADDI(String value) {
        return "\n"
                + "\tADDI " + value;
    }
    
    public final String LD(String id) {
        return "\n"
                + "\tLD " + id;
    }
    
    public final String LDI(String value) {
        return "\n"
                + "\tLDI " + value;
    }
    
    public final String STO(String id) {
        return "\n"
                + "\tSTO " + id;
    }
    
    public final String HLT() {
        return "\n"
                + "\tHLT 0";
    }
    
}
