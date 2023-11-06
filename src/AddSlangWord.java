import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddSlangWord extends JFrame implements ActionListener {
    SlangWordDic slangWord;
    JButton btnBack, btnAdd;
    JTextField textFieldMeaning, textFieldSlang;

    public AddSlangWord() {
        slangWord = SlangWordDic.getInstance();
        Container container = this.getContentPane();

        // Create and configure title label
        JLabel titleLabel = new JLabel("Add Slang Words");
        titleLabel.setForeground(Color.BLUE);
        titleLabel.setFont(new Font("Gill Sans MT", Font.BOLD, 40)); // Increase font size and make it bold
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setPreferredSize(new Dimension(400, 80)); // Increase label size

        // Create form panel
        JPanel form = new JPanel();
        form.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Increase insets for more spacing
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel labelForSlang = new JLabel("Slang word: ");
        labelForSlang.setFont(new Font("Arial", Font.BOLD, 20)); // Increase font size and make it bold
        labelForSlang.setPreferredSize(new Dimension(150, 30)); // Increase label size
        form.add(labelForSlang, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        textFieldSlang = new JTextField(25); // Increase text field size
        form.add(textFieldSlang, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel labelForMeaning = new JLabel("Meaning: ");
        labelForMeaning.setFont(new Font("Arial", Font.BOLD, 20)); // Increase font size and make it bold
        labelForMeaning.setPreferredSize(new Dimension(150, 30)); // Increase label size
        form.add(labelForMeaning, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        textFieldMeaning = new JTextField(25); // Increase text field size
        form.add(textFieldMeaning, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel();
        btnBack = new JButton("Back");
        btnAdd = new JButton("Add");
        btnBack.setFont(new Font("Arial", Font.BOLD, 20)); // Increase button font size and make it bold
        btnAdd.setFont(new Font("Arial", Font.BOLD, 20)); // Increase button font size and make it bold

        btnBack.addActionListener(this);
        btnAdd.addActionListener(this);

        buttonPanel.add(btnBack);
        buttonPanel.add(btnAdd);

        // Set content
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.add(Box.createRigidArea(new Dimension(0, 30))); // Increase top padding
        container.add(titleLabel);
        container.add(Box.createRigidArea(new Dimension(0, 30))); // Increase space after title
        container.add(form);
        container.add(Box.createRigidArea(new Dimension(0, 30))); // Increase space before buttons
        container.add(buttonPanel);

        // Set Frame properties
        setTitle("Add Slang Word");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        pack();
        setLocationRelativeTo(null); // Center the frame on the screen
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnBack) {
            dispose();
            new MenuController();
        } else if (e.getSource() == btnAdd) {
            String slang = textFieldSlang.getText().trim();
            String meaning = textFieldMeaning.getText().trim();

            if (slang.isEmpty() || meaning.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Slang and Meaning cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (slangWord.checkExists(slang)) {
                Object[] options = { "Overwrite", "Duplicate" };
                int n = JOptionPane.showOptionDialog(this,
                        "Slang `" + slang + "` already exists in the Slang Word List", "Duplicate Slang",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                if (n == 0) {
                    slangWord.addSlangWord(slang, meaning, true);
                    JOptionPane.showMessageDialog(this, "Slang Word Overwritten Successfully.");
                } else {
                    slangWord.addSlangWord(slang, meaning, false);
                    JOptionPane.showMessageDialog(this, "Slang Word Duplicated Successfully.");
                }
            } else {
                slangWord.addSlangWord(slang, meaning, false);
                JOptionPane.showMessageDialog(this, "Slang Word Added Successfully.");
            }

            textFieldSlang.setText("");
            textFieldMeaning.setText("");
        }
    }

}
