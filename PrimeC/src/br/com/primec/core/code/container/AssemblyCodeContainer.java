package br.com.primec.core.code.container;

public class AssemblyCodeContainer {

    private String data;
    private String text;

    public AssemblyCodeContainer() {
        this.data = ""
                + ".data";
        this.text = ""
                + ".text";
    }

    public String build() {
        return this.data + "\n" + this.text;
    }
    
    public void addData(String data) {
        this.data = this.data + data;
    }
    
    public void addText(String text) {
        this.text = this.text + text;
    }
    
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
