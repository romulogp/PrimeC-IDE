package br.com.primec.gui.component.custom;

import br.com.primec.core.table.Symbol;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class DataTableBuilder extends JFrame {
    
    private final JTable table;

    public JTable getTable() {
        return table;
    }
    
    public DataTableBuilder(List<Symbol> symbolTable) {
        int COLUMNS = 10;
        Object[][] data = new Object[symbolTable.size()][COLUMNS];
        Object[] columnNames = {
                "Nome",
                "Tipo",
                "Inicializada",
                "Usada",
                "Escopo",
                "Parâmetro",
                "Pos",
                "Vetor",
                "Matriz",
                "Função"
        };
        
        int posController = 0;
        for (Symbol s : symbolTable) {
            Object[] row = new Object[COLUMNS];

            row[0] = s.getName();
            row[1] = s.getType();
            row[2] = s.isInitialized();
            row[3] = s.isUsed();
            row[4] = s.getScope();
            row[5] = s.isParam();
            row[6] = s.getPos();
            row[7] = s.isVect();
            row[8] = s.isMatrix();
            row[9] = s.isFunction();
            
            data[posController] = row;
            
            posController++;
        }
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        table = new JTable(model) {

            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                    case 1:
                    case 4:
                        return String.class;
                    case 6:
                        return Integer.class;
                    default :
                        return Boolean.class;
                }
            }
            
        };
    }
    
}
