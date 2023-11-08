import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class QuizFrame extends JFrame implements ActionListener {
    JButton btnFindDefinition, btnFindSlangWord, btnBack;

    public QuizFrame() {
        createUI();
    }

    private void createUI() {
        setTitle("Quiz Choose Mode");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        Container con = this.getContentPane();
        con.setLayout(new BoxLayout(con, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel("Quiz");
        lblTitle.setForeground(Color.BLUE);
        lblTitle.setFont(new Font("Gill Sans MT", Font.PLAIN, 35));
        lblTitle.setAlignmentX(CENTER_ALIGNMENT);
        lblTitle.setAlignmentY(-100);
        //lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        //mainPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new GridLayout(1, 2, 10, 10));
        panelButtons.setAlignmentX(CENTER_ALIGNMENT);

        Dimension size = new Dimension(500,200);
        panelButtons.setMaximumSize(size);
        panelButtons.setPreferredSize(size);
        panelButtons.setMinimumSize(size);

        btnFindDefinition = new JButton("1. Find Definition");
        btnFindDefinition.setFont(new Font("Gill Sans MT", Font.PLAIN, 20));
        btnFindDefinition.addActionListener(this);
        panelButtons.add(btnFindDefinition);

        btnFindSlangWord = new JButton("2. Find Slang Word");
        btnFindSlangWord.setFont(new Font("Gill Sans MT", Font.PLAIN, 20));
        btnFindSlangWord.addActionListener(this);
        panelButtons.add(btnFindSlangWord);

        btnBack = new JButton("Back");
        btnBack.setFont(new Font("Gill Sans MT", Font.PLAIN, 20));
        btnBack.addActionListener(this);

        JPanel panelBack = new JPanel();
        panelBack.add(btnBack);
        
        con.add(Box.createRigidArea(new Dimension(0, 100)));
		con.add(lblTitle);
		con.add(Box.createRigidArea(new Dimension(0, 100)));
		con.add(panelButtons);
		con.add(Box.createRigidArea(new Dimension(0, 100)));
		con.add(panelBack);

        

        centerWindow(this);
    }

    private void centerWindow(JFrame frame) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnBack) {
            dispose();
            new MenuController();
        } else if (e.getSource() == btnFindDefinition) {
            dispose();
            new Quiz(1);
        } else if (e.getSource() == btnFindSlangWord) {
            dispose();
            new Quiz(2);
        }
    }
}
