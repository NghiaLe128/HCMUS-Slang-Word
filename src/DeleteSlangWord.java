import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class DeleteSlangWord extends JFrame implements ActionListener, ListSelectionListener {
    private JButton btnBack;
    private JTable slangWordTable;
    private SlangWordDic slangWord;
    private DefaultTableModel model;
    private List<String[]> data;
    private JTextField searchField;
    private JButton searchButton;
    private List<String[]> originalData;

    public DeleteSlangWord() throws Exception {
        slangWord = SlangWordDic.getInstance();
        originalData = Arrays.asList(slangWord.getData());
        data = new ArrayList<>(originalData);

        // Create and configure title label
        JLabel titleLabel = new JLabel("Choose a Slang Word you want to delete");
        titleLabel.setForeground(Color.BLUE);
        titleLabel.setFont(new Font("Gill Sans MT", Font.PLAIN, 35));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Create and configure result label
        JLabel resultLabel = new JLabel("We have " + data.size() + " slang words");
        resultLabel.setForeground(Color.BLACK);
        resultLabel.setFont(new Font("Gill Sans MT", Font.PLAIN, 18));
        resultLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Create panel for search bar
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(40);
        searchField.setPreferredSize(new Dimension(200, 30));
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.setAlignmentX(CENTER_ALIGNMENT);

        // Create panel for slang word table
        JPanel panelTable = new JPanel();
        panelTable.setBackground(Color.BLACK);
        String column[] = { "STT", "Slag Word", "Meaning" };
        model = new DefaultTableModel(column, 0);
        slangWordTable = new JTable(model);
        slangWordTable.setRowHeight(30);
        for (int i = 0; i < 3; i++) {
            slangWordTable.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (c instanceof JLabel) {
                        JLabel label = (JLabel) c;
                        label.setHorizontalAlignment(JLabel.CENTER);
                    }
                    return c;
                }
                
            });
        }
        JTableHeader header = slangWordTable.getTableHeader();
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
	
        slangWordTable.getSelectionModel().addListSelectionListener(this);
        JScrollPane sp = new JScrollPane(slangWordTable);
        panelTable.setLayout(new BoxLayout(panelTable, BoxLayout.X_AXIS));
        panelTable.add(sp);

        // Create panel for back button
        JPanel bottomPanel = new JPanel();
        btnBack = new JButton("Back");
        btnBack.addActionListener(this);
        btnBack.setFocusable(false);
        btnBack.setAlignmentX(CENTER_ALIGNMENT);
        bottomPanel.add(btnBack);

        // Set layout and add components to the content pane
        Container con = getContentPane();
        con.setLayout(new BoxLayout(con, BoxLayout.Y_AXIS));
        con.add(Box.createRigidArea(new Dimension(0, 10)));
        con.add(titleLabel);
        con.add(Box.createRigidArea(new Dimension(0, 20)));
        con.add(resultLabel);
        con.add(searchPanel);
        con.add(Box.createRigidArea(new Dimension(0, 20)));
        con.add(panelTable);
        con.add(Box.createRigidArea(new Dimension(0, 20)));
        con.add(bottomPanel);

        setTitle("List Slang Words");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 700);
        setLocationRelativeTo(null);
        addRowsToTable();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnBack) {
            dispose();
            new MenuController();
        } else if (e.getSource() == searchButton) {
            String keyword = searchField.getText().toLowerCase().trim();
            data.clear();

            for (String[] row : originalData) {
                if (row[1].toLowerCase().contains(keyword) || row[2].toLowerCase().contains(keyword)) {
                    data.add(row);
                }
            }

            model.setRowCount(0);
            addRowsToTable();
        }
    }

    private void addRowsToTable() {
        for (String[] row : data) {
            model.addRow(row);
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int row = slangWordTable.getSelectedRow();
        if (row == -1) {
            return;
        }
        String slang = (String) slangWordTable.getValueAt(row, 1);

        int option = JOptionPane.showConfirmDialog(this, "Would you like to delete this slang word?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            String meaning = (String) slangWordTable.getValueAt(row, 2);
            slangWord.delete(slang, meaning);
            model.removeRow(row);
            JOptionPane.showMessageDialog(this, "Deleted successfully");
        }
    }
}
