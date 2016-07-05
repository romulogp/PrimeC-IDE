package br.com.primec.core.code.generator;

/**
 *
 * @author Rômulo Göelzer Portolann
 */
public class AssemblyCodeGenerator {
    
    public final String INPUT(String id) {
        return "\n"
                + "\tLD $in_port"
                + "\n"
                + "\tSTO " + id;
    }
    
    /**
     * 
     * @param id da variável a ser carregada
     * @return LD id  STO $out_port
     */
    public final String OUTPUT(String id) {
        return "\n"
                + "\tLD " + id
                + "\n"
                + "\tSTO $out_port";
    }
    
    /**
     * Utilizado quando se tem uma função, e, portanto é necessário separar do LD
     * @return STO $out_port
     */
    public final String OUTPUT() {
        return "\n"
                + "\tSTO $out_port";
    }
    
    public final String INT_OUTPUT(String value) {
        return "\n"
                + "\tLDI " + value
                + "\n"
                + "\tSTO $out_port";          
    }
    
    public final String VAR(String id) {
        return "\n"
                + "\t" + id + " : 0";
    }
    
    public final String VECT(String id, int size) {
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
    
    public final String SUB(String id) {
        return "\n"
                + "\tSUB " + id;
    }
    
    public final String SUBI(String value) {
        return "\n"
                + "\tSUBI " + value;
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
    
    public final String CALL(String functionName) {
        return "\n"
                + "\tCALL " + functionName;
    }
    
    public final String LDV(String id) {
        return "\n"
                + "\tLDV " + id;
    }
    
    public final String BNE(String id) {
        return "\n"
                + "\tBNE " + id;
    }
    
    public final String BEQ(String id) {
        return "\n"
                + "\tBEQ " + id;
    }
    
    public final String BGE(String id) {
        return "\n"
                + "\tBGE " + id;
    }
        
    public final String LTE(String id) {
        return "\n"
                + "\tLTE " + id;
    }
    
    public final String LBL(String label) {
        return "\n"
                + label + ":";
    }
    
    public final String JMP(String label) {
        return "\n"
                + "\tJMP " + label;
    }
    
    public final String RET(String value) {
        return "\n"
                + "\tRETURN " + value;
    }
    
    public final String HLT() {
        return "\n"
                + "\tHLT 0";
    }
    
}
