import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditSlangWord {
    private JFrame frame;
    private SlangWordDic slangWord;
    private JComboBox<String> editOption;
    private JTextField slangWordField;
    private JTextField oldValueField;
    private JTextField newValueField;
    private JButton editButton;
    private JButton returnButton;

    EditSlangWord() throws Exception {
        slangWord = SlangWordDic.getInstance();

        frame = new JFrame("Edit Slang Word");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // Title
        JLabel titleLabel = new JLabel("Edit Slang Word");
        titleLabel.setForeground(new Color(0, 102, 204));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.insets = new Insets(20, 20, 20, 20);
        c.gridx = 0;
        c.gridy = 0;
        frame.add(titleLabel, c);

        // Option
        JLabel label = new JLabel("Select an edit option:");
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        frame.add(label, c);

        String[] options = {"Edit Word", "Edit Definition"};
        editOption = new JComboBox<>(options);
        editOption.setFont(new Font("Arial", Font.PLAIN, 20));
        c.gridx = 1;
        c.gridy = 1;
        frame.add(editOption, c);

        // Slang Word
        JLabel slangWordLabel = new JLabel("Slang Word:");
        slangWordLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        c.gridx = 0;
        c.gridy = 2;
        frame.add(slangWordLabel, c);
        slangWordField = new JTextField();
        slangWordField.setFont(new Font("Arial", Font.PLAIN, 20));
        c.gridx = 1;
        c.gridy = 2;
        frame.add(slangWordField, c);

        // Old Value
        JLabel oldValueLabel = new JLabel("Old Value:");
        oldValueLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        oldValueLabel.setVisible(false);
        c.gridx = 0;
        c.gridy = 3;
        frame.add(oldValueLabel, c);
        oldValueField = new JTextField();
        oldValueField.setFont(new Font("Arial", Font.PLAIN, 20));
        oldValueField.setVisible(false);
        c.gridx = 1;
        c.gridy = 3;
        frame.add(oldValueField, c);

        // New Value
        JLabel newValueLabel = new JLabel("New Value:");
        newValueLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        c.gridx = 0;
        c.gridy = 4;
        frame.add(newValueLabel, c);
        newValueField = new JTextField();
        newValueField.setFont(new Font("Arial", Font.PLAIN, 20));
        c.gridx = 1;
        c.gridy = 4;
        frame.add(newValueField, c);

        // Button Edit
        editButton = new JButton("Edit");
        editButton.setFont(new Font("Arial", Font.PLAIN, 20));
        c.gridx = 0;
        c.gridy = 5;
        frame.add(editButton, c);
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String editOptionValue = (String) editOption.getSelectedItem();
                String word = slangWordField.getText();
                String oldValue = oldValueField.getText();
                String newValue = newValueField.getText();

                if (editOptionValue.equals("Edit Word")) 
                {
                    boolean[] editSuccess = new boolean[1];
                    slangWord.editSlangWord(word, newValue, editSuccess);
                     if (editSuccess[0]) {
                        JOptionPane.showMessageDialog(frame, "Edit successful!");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Error: Edit failed!");
                    }
                } else if (editOptionValue.equals("Edit Definition")) {
                    int editResult = slangWord.editDefinition(word, oldValue, newValue);

                    if (editResult == 0) {
                        JOptionPane.showMessageDialog(frame, "Edit successful!");
                    } else if (editResult == 1) {
                        JOptionPane.showMessageDialog(frame, "Error: Old meaning not found!");
                    } else if (editResult == 2) {
                        JOptionPane.showMessageDialog(frame, "Error: Slang word not found!");
}
                }
                
                
            }
        });
        

        // Button Return
        returnButton = new JButton("Return");
        returnButton.setFont(new Font("Arial", Font.PLAIN, 22));
        c.gridx = 1;
        c.gridy = 5;
        frame.add(returnButton, c);
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new MenuController();
            }
        });

        editOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editOption.getSelectedItem().equals("Edit Definition")) {
                    oldValueLabel.setVisible(true);
                    oldValueField.setVisible(true);
                } else {
                    oldValueLabel.setVisible(false);
                    oldValueField.setVisible(false);
                }
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
