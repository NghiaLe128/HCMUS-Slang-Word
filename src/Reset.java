import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class Reset extends JFrame implements ActionListener, TableModelListener {
    private JButton btnBack;
    private JButton btnReset;
    private JTable slangWordTable;
    private SlangWordDic slangWord;
    private DefaultTableModel model;
    private String[][] dataCopy;

    public Reset() throws Exception {
        slangWord = SlangWordDic.getInstance();
        dataCopy = slangWord.getData();

        // Create and configure title label
        JLabel titleLabel = new JLabel("List Slang Words");
        titleLabel.setForeground(Color.GREEN);
        titleLabel.setFont(new Font("Gill Sans MT", Font.PLAIN, 35));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Create and configure result label
        JLabel resultLabel = new JLabel();
        resultLabel.setForeground(Color.BLACK);
        resultLabel.setFont(new Font("Gill Sans MT", Font.PLAIN, 18));
        resultLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Create panel for slang word table
        JPanel panelTable = new JPanel();
        panelTable.setBackground(Color.BLACK);
        String column[] = { "STT", "Slang Word", "Meaning" };
        model = new DefaultTableModel(column, 0);
        slangWordTable = new JTable(model);
        slangWordTable.setRowHeight(30);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 3; i++) {
            slangWordTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JTableHeader header = slangWordTable.getTableHeader();
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);

        slangWordTable.getModel().addTableModelListener(this);
        JScrollPane sp = new JScrollPane(slangWordTable);
        panelTable.setLayout(new BoxLayout(panelTable, BoxLayout.X_AXIS));
        panelTable.add(sp);

        // Create panel for buttons
        JPanel buttonPanel = new JPanel();
        btnBack = new JButton("Back");
        btnBack.addActionListener(this);
        btnBack.setFocusable(false);
        btnBack.setAlignmentX(CENTER_ALIGNMENT);

        btnReset = new JButton("Reset");
        btnReset.addActionListener(this);
        btnReset.setFocusable(false);
        btnReset.setAlignmentX(CENTER_ALIGNMENT);

        buttonPanel.add(btnBack);
        buttonPanel.add(btnReset);

        // Set the data in the table
        addRowsToTable();

        // Set layout and add components to the content pane
        Container con = getContentPane();
        con.setLayout(new BoxLayout(con, BoxLayout.Y_AXIS));
        con.add(Box.createRigidArea(new Dimension(0, 10)));
        con.add(titleLabel);
        con.add(Box.createRigidArea(new Dimension(0, 20)));
        con.add(resultLabel);
        con.add(Box.createRigidArea(new Dimension(0, 20)));
        con.add(panelTable);
        con.add(Box.createRigidArea(new Dimension(0, 20)));
        con.add(buttonPanel);

        setTitle("List Slang Words");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnBack) {
            dispose();
            new MenuController();
        } else if (e.getSource() == btnReset) {
            handleResetAction();
        }
    }

    private void handleResetAction() {
        int option = JOptionPane.showConfirmDialog(this, "Do you really want to reset Slang Word?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try {
                slangWord.reset();
                setTitle("List Slang Words After Reset");
                model.setRowCount(0);
                addRowsToTable();
                JOptionPane.showMessageDialog(this, "Slang words have been reset.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "An error occurred while resetting slang words.");
            }
        }
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        int row = slangWordTable.getSelectedRow();
        int col = slangWordTable.getSelectedColumn();
        if (row == -1 || col == -1) {
            return;
        }

        if (col == 2) {
            String slang = (String) slangWordTable.getValueAt(row, 1);
            String oldMeaning = dataCopy[row][2];
            String newMeaning = (String) slangWordTable.getValueAt(row, 2);
            slangWord.set(slang, oldMeaning, newMeaning);
            JOptionPane.showMessageDialog(this, "Updated meaning for slang word: " + slang);
        }
    }

    private void addRowsToTable() {
        String[][] data = slangWord.getData();
        for (String[] row : data) {
            model.addRow(row);
        }
    }
}
