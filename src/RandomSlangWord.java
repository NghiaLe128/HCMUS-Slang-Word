import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RandomSlangWord extends JFrame implements ActionListener {
    private JButton randomButton;
    private JButton backButton;
    private JLabel slangLabel;
    private JLabel meaningLabel;
    private SlangWordDic slangWord;

    RandomSlangWord() {
        slangWord = SlangWordDic.getInstance();

        setTitle("Random Slang Word");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Title
        JLabel titleLabel = new JLabel("Random Slang Word");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.BLUE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Slang Word
        slangLabel = new JLabel("Slang: ");
        slangLabel.setFont(new Font("Arial", Font.BOLD, 36));
        slangLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        slangLabel.setPreferredSize(new Dimension(600, 50));

        // Meaning
        meaningLabel = new JLabel("Meaning: ");
        meaningLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        meaningLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        meaningLabel.setPreferredSize(new Dimension(600, 50));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        randomButton = new JButton("Random Slang Word");
        randomButton.setFont(new Font("Arial", Font.BOLD, 24));
        randomButton.addActionListener(this);

        // Back button
        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 24));
        backButton.addActionListener(this);

        buttonPanel.add(randomButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(backButton);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(slangLabel);
        mainPanel.add(meaningLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(buttonPanel);

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == randomButton) {
            String[] randomSlang = slangWord.random();
            if (randomSlang != null && randomSlang.length == 2) {
                slangLabel.setText("Slang: " + randomSlang[0]);
                meaningLabel.setText("Meaning: " + randomSlang[1]);
            } else {
                slangLabel.setText("Slang: No data");
                meaningLabel.setText("Meaning: No data");
            }
        } else if (e.getSource() == backButton) {
            dispose();
            new MenuController();
        }
    }
}
