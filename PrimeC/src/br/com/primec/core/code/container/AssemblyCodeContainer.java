package br.com.primec.core.code.container;

import java.util.ArrayList;
import java.util.List;

public class AssemblyCodeContainer {

    private final List<String> data = new ArrayList<>();
    private final List<String> text = new ArrayList<>();
    
    public AssemblyCodeContainer() {
        this.data.add(".data");
        this.text.add(".text");
    }

    public String build() {
        String tempData = "";
        String tempText = "";
        tempData = data.stream().map((d) -> d).reduce(tempData, String::concat);
        tempText = text.stream().map((t) -> t).reduce(tempText, String::concat);
        
        return tempData + "\n" + tempText;
    }
    
    public void addData(String data) {
        this.data.add(data);
    }
    
    public void addText(String text) {
        this.text.add(text);
    }
    
    public List<String> getData() {
        return data;
    }

    public List<String> getText() {
        return text;
    }
    
}