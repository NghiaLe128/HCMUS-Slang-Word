import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class FindSlangWord extends JFrame implements ActionListener, TableModelListener {
	private JButton backButton, findButton;
	private JTextField searchField;
	private JTable resultTable;
	private JLabel titleLabel;
	private DefaultTableModel tableModel;
	private SlangWordDic slangWord;
	private String[][] searchResult;

	FindSlangWord() throws Exception {
		Container container = this.getContentPane();
		slangWord = SlangWordDic.getInstance();

		// Title Label
		titleLabel = new JLabel("Find Slang Words");
		titleLabel.setForeground(new Color(0, 102, 204));
		titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
		titleLabel.setAlignmentX(CENTER_ALIGNMENT);

		// Result Label
		JLabel resultLabel = new JLabel("Enter slang word to find out Meaning ");
		resultLabel.setForeground(new Color(51, 51, 51));
		resultLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		resultLabel.setAlignmentX(CENTER_ALIGNMENT);

		// Search Form
		JPanel searchPanel = new JPanel();
		JLabel searchLabel = new JLabel("Slang Word");
		searchLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		searchField = new JTextField();
		findButton = new JButton("Find");
		findButton.addActionListener(this);
		findButton.setMnemonic(KeyEvent.VK_ENTER);
		searchPanel.setLayout(new BorderLayout(10, 10));
		searchPanel.add(searchLabel, BorderLayout.LINE_START);
		searchPanel.add(searchField, BorderLayout.CENTER);
		searchPanel.add(findButton, BorderLayout.LINE_END);
		Dimension searchPanelSize = new Dimension(700, 40);
		searchPanel.setMaximumSize(searchPanelSize);
		searchPanel.setPreferredSize(searchPanelSize);
		searchPanel.setMinimumSize(searchPanelSize);

		// Search Result Table
		JPanel resultPanel = new JPanel();
		resultPanel.setBackground(new Color(240, 240, 240));

		String[] columnNames = { "STT", "Slang Word", "Meaning" };
		resultTable = new JTable(new DefaultTableModel(columnNames, 0));
		resultTable.setRowHeight(30);
		tableModel = (DefaultTableModel) resultTable.getModel();
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		resultTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		resultTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		resultTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		resultTable.getModel().addTableModelListener(this);
		JScrollPane scrollPane = new JScrollPane(resultTable);
		resultPanel.setLayout(new GridLayout(1, 1));
		resultPanel.add(scrollPane);

		// Back Button
		JPanel bottomPanel = new JPanel();
		backButton = new JButton("Back");
		backButton.setFocusable(false);
		bottomPanel.add(backButton);
		backButton.addActionListener(this);
		backButton.setAlignmentX(CENTER_ALIGNMENT);

		// Setting Content Layout
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		container.add(Box.createRigidArea(new Dimension(0, 10)));
		container.add(titleLabel);
		container.add(Box.createRigidArea(new Dimension(0, 10)));
		container.add(resultLabel);
		container.add(Box.createRigidArea(new Dimension(0, 10)));
		container.add(searchPanel);
		container.add(Box.createRigidArea(new Dimension(0, 10)));
		container.add(resultPanel);
		container.add(Box.createRigidArea(new Dimension(0, 10)));
		container.add(backButton);

		// Setting JFrame
		this.setTitle("Find Slang Words");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setSize(700, 700);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(screenSize.width / 2 - this.getSize().width / 2,
				screenSize.height / 2 - this.getSize().height / 2);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == findButton) {
			handleFindAction();
		} else if (e.getSource() == backButton) {
			handleBackAction();
		}
	}

	private void handleFindAction() {
		String key = searchField.getText();
		if (key.isEmpty()) {
			showErrorMessage("Please input slang word you want to find");
			return;
		}

		int searchMode = chooseSearchMode();
		if (searchMode == -1) {
			return; // User canceled the search mode selection
		}

		String[][] searchResult = performSearch(key, searchMode);
		if (searchResult != null) {
			updateSearchResult(searchResult);
		} else {
			showErrorMessage("Can't not find the slangWord");
		}
	}

	private void handleBackAction() {
		this.dispose();
		new MenuController();
	}

	private int chooseSearchMode() {
		Object[] options = { "Find Flow Slang Word", "Find Slang Flow definition" };
		int selectedOption = JOptionPane.showOptionDialog(
				this,
				"Choose mode you want to execute?",
				"Choose mode find",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				null);

		return selectedOption;
	}

	private String[][] performSearch(String key, int searchMode) {
		this.clearTable();
		long startTime = System.currentTimeMillis();
		String[][] searchResult = (searchMode == 0) ? slangWord.findByMean(key) : slangWord.findByDef(key);
		long endTime = System.currentTimeMillis();
		long timeElapsed = endTime - startTime;
		titleLabel.setText("Execution time in milliseconds(" + searchResult.length + " Results ): "
				+ String.valueOf(timeElapsed) + " ms");
		return searchResult;
	}

	private void updateSearchResult(String[][] searchResult) {
		for (String[] p : searchResult) {
			tableModel.addRow(p);
		}

		try {
			for (String[] item : searchResult) {
				slangWord.saveHistory(item[1], item[2]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showErrorMessage(String message) {
		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		int row = resultTable.getSelectedRow();
		int col = resultTable.getSelectedColumn();
		if (row == col && row == -1)
			return;
		// String Data = (String) resultTable.getValueAt(row, col);
		// System.out.println("Table element selected is: " + row + col + " : " + Data);
		if (col == 2) {
			// edit meaning
			slangWord.set((String) resultTable.getValueAt(row, 1), searchResult[row][2],
					(String) resultTable.getValueAt(row, 2));
			JOptionPane.showMessageDialog(this, "Updated a row.");
		}
		resultTable.setFocusable(false);
	}

	void clearTable() {
		int rowCount = resultTable.getRowCount();
		// Remove rows one by one from the end of the table
		for (int i = rowCount - 1; i >= 0; i--) {
			tableModel.removeRow(i);
		}
	}
}
