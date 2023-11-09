import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class History extends JFrame implements ActionListener {
    private JButton btnBack, btnExit, btnClear, btnReset;
    private SlangWordDic slangWord = SlangWordDic.getInstance();
    private DefaultTableModel tableModel;
    private JTable resultTable;

    History() {
        // Frame setup
        setTitle("History Window");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // History label
        JLabel historyLabel = new JLabel("History Slangword Found");
        historyLabel.setForeground(Color.BLUE);
        historyLabel.setFont(new Font("Gill Sans MT", Font.PLAIN, 35));
        historyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(historyLabel, BorderLayout.NORTH);

        // Table
        String data[][] = slangWord.readHistory();
        String column[] = { "STT", "Slang Word", "Definition" };
        tableModel = new DefaultTableModel(data, column);
        resultTable = new JTable(tableModel);
        resultTable.setRowHeight(30);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);

		for (int i = 0; i < resultTable.getColumnCount(); i++) {
			resultTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
			
			// Đặt renderer cho tiêu đề của cột
			JTableHeader header = resultTable.getTableHeader();
			header.setDefaultRenderer(centerRenderer);
		}
        resultTable.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(resultTable);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        btnBack = new JButton("Back");
        btnExit = new JButton("Exit");
        btnClear = new JButton("Clear");
        btnReset = new JButton("Reset");
        btnBack.addActionListener(this);
        btnExit.addActionListener(this);
        btnClear.addActionListener(this);
        btnReset.addActionListener(this);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(btnBack);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnReset);
        buttonPanel.add(btnExit);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnExit) {
            System.exit(0);
        } else if (e.getSource() == btnBack) {
            dispose();
            new MenuController();
        } else if (e.getSource() == btnClear) {
            slangWord.clearHistory();
            JOptionPane.showMessageDialog(this, "History cleared.");
            refreshTable();
        } else if (e.getSource() == btnReset) {
            refreshTable();
        }
    }

    private void refreshTable() {
        // Refresh the table by re-reading the history and updating the model
        String data[][] = slangWord.readHistory();
        tableModel.setDataVector(data, new String[] { "STT", "Slang Word", "Definition" });
        tableModel.fireTableDataChanged();
    }
}
