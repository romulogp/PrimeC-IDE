package br.com.primec.gui;

import br.com.primec.core.Symbol;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class SymbolTableView extends javax.swing.JDialog {

    private final PrimecIDE mainWindow;

    /**
     * Creates new form SymbleTable
     *
     * @param parent
     * @param modal
     */
    public SymbolTableView(PrimecIDE parent, boolean modal) {
        super(parent, modal);
        initComponents();
        mainWindow = parent;
        setVisible(true);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        updateTableContent(PrimecIDE.symbolTable.getSymbolTable());
    }

    public final void updateTableContent(List<Symbol> symbolTable) {
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
        jTable1.setModel(new DefaultTableModel(data, columnNames) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Tabela de Símbolos");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jTable1.setFont(new java.awt.Font("Courier New", 0, 13)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Nome", "Tipo", "Inicializada", "Usada", "Escopo", "Param", "Posição", "Vetor", "Matriz", "Função"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setFocusable(false);
        jTable1.setShowVerticalLines(false);
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 896, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        mainWindow.symbolTableView = null;
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}