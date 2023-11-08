import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Quiz extends JFrame implements ActionListener {
    private JButton[] answerButtons;
    private String[] questions;
    JButton btnBack;
    JButton btnContinue;
    JButton btnRestart;

    private int currentQuestionIndex;

    private int type;

    public Quiz(int type) {
        this.type = type;
        questions = (SlangWordDic.getInstance()).quiz(type);

        setTitle("Question Quiz");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(700, 700);
        setLocationRelativeTo(null);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(5, 1));

        JLabel titleLabel = new JLabel("Find out the correct answer");
        titleLabel.setForeground(Color.BLUE);
        titleLabel.setFont(new Font("Gill Sans MT", Font.PLAIN, 35));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        JLabel questionLabel;
        if (type == 1) {
            questionLabel = new JLabel("Slang word `" + questions[0] + "` means?");
        } else {
            questionLabel = new JLabel("`" + questions[0] + "` has a slang word of?");
        }
        questionLabel.setFont(new Font("Gill Sans MT", Font.PLAIN, 18));
        questionLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel answerPanel = new JPanel();
        answerPanel.setLayout(new GridLayout(2, 2));

        answerButtons = new JButton[4];
        for (int i = 0; i < 4; i++) {
            answerButtons[i] = new JButton((char) ('A' + i) + ". " + questions[i + 1]);
            answerButtons[i].setFont(new Font("Lucida Handwriting", Font.PLAIN, 14));
            answerButtons[i].addActionListener(this);
            answerPanel.add(answerButtons[i]);
        }

        JPanel buttonPanel = new JPanel();
        btnBack = new JButton("Back");
        btnBack.addActionListener(this);
        buttonPanel.add(btnBack);

        btnContinue = new JButton("Continue");
        btnContinue.addActionListener(this);
        btnContinue.setEnabled(false);
        buttonPanel.add(btnContinue);

        btnRestart = new JButton("Restart");
        btnRestart.addActionListener(this);
        btnRestart.setEnabled(false);
        buttonPanel.add(btnRestart);

        contentPanel.add(titleLabel);
        contentPanel.add(questionLabel);
        contentPanel.add(answerPanel);
        contentPanel.add(buttonPanel);
        add(contentPanel);

        currentQuestionIndex = 0; // Initialize the current question index

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < 4; i++) {
            if (e.getSource() == answerButtons[i]) {
                answer(i);
                break;
            }
        }

        if (e.getSource() == btnBack) {
            this.dispose();
            new QuizFrame();
        } else if (e.getSource() == btnContinue) {
            continueQuiz();
        } else if (e.getSource() == btnRestart) {
            restartQuiz();
        }
    }

    public void answer(int ans) {
        for (int i = 0; i < 4; i++) {
            answerButtons[i].setEnabled(false);
            if (i == ans) {
                answerButtons[i].setBackground(Color.red);
            } else if (questions[i + 1].equals(questions[5])) {
                answerButtons[i].setBackground(Color.green);
            }
        }

        if (questions[ans + 1].equals(questions[5])) {
            JOptionPane.showMessageDialog(this, "Correct Answer.");
            btnContinue.setEnabled(true);
            btnRestart.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(this, "Wrong Answer", "Inane error", JOptionPane.ERROR_MESSAGE);
            btnContinue.setEnabled(false);
            btnRestart.setEnabled(true);
        }
    }

    public void continueQuiz() {
        this.dispose();
        new Quiz(type);
    }

    public void restartQuiz() {
        currentQuestionIndex = 0;
        resetQuizUI();
        btnRestart.setEnabled(false);
    }

    public void resetQuizUI() {
        for (int i = 0; i < 4; i++) {
            answerButtons[i].setEnabled(true);
            answerButtons[i].setBackground(null);
            answerButtons[i].setText((char) ('A' + i) + ". " + questions[i + currentQuestionIndex + 1]);
        }

        // Update the question label
        JLabel questionLabel;
        if (currentQuestionIndex % 2 == 0) {
            questionLabel = new JLabel("Slang word `" + questions[currentQuestionIndex] + "` means?");
        } else {
            questionLabel = new JLabel("`" + questions[currentQuestionIndex] + "` has a slang word of?");
        }
        questionLabel.setFont(new Font("Gill Sans MT", Font.PLAIN, 18));
        questionLabel.setHorizontalAlignment(JLabel.CENTER);
        
        JPanel contentPanel = (JPanel) getContentPane().getComponent(0);
        contentPanel.remove(1); // Remove the previous questionLabel
        contentPanel.add(questionLabel, 1); // Add the new questionLabel
        revalidate();
        repaint();

        btnContinue.setEnabled(false);
    }
}
