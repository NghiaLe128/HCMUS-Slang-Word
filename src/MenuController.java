import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuController extends JFrame implements ActionListener {
    JButton cn1_2, cn3, cn4, cn5, cn6, cn7, cn8, cn9_10;

    MenuController() {

        // Label
        JLabel label = new JLabel("Slang Words");
        label.setForeground(Color.BLUE);
        label.setFont(new Font("Arial", Font.BOLD, 40));
        label.setAlignmentX(CENTER_ALIGNMENT);

        //Buttons
        cn1_2 = createButton("Find Slang Word");
        cn3 = createButton("History");
        cn4 = createButton("Add Slang Word");
        cn5 = createButton("Edit Slang Word");
        cn6 = createButton("Delete Slang Word");
        cn7 = createButton("Reset Slang Word");
        cn8 = createButton("Random Slang Words");
        cn9_10 = createButton("Quiz");

        // Panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4,2,10,10));
        panel.add(cn1_2);
        panel.add(cn3);
        panel.add(cn4);
        panel.add(cn5);
        panel.add(cn6);
        panel.add(cn7);
        panel.add(cn8);
        panel.add(cn9_10);

        // Container
        Container con = this.getContentPane();
        con.setLayout(new BoxLayout(con, BoxLayout.Y_AXIS));
        con.add(Box.createRigidArea(new Dimension(0, 10)));
        con.add(label);
        con.add(Box.createRigidArea(new Dimension(0, 30)));
        con.add(panel);

        // Setting Frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Menu Window");
        this.setSize(600, 500);
        this.setLocationRelativeTo(null); // Center the frame
        this.setVisible(true);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.addActionListener(this);
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        return button;
    }

    @Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cn1_2) {
			this.dispose();
			try {
				new FindSlangWord();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource() == cn3) {
			this.dispose();
			try {
				new History();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		} else if (e.getSource() == cn4) {
			// Add a slang word
			this.dispose();
			try {
				new AddSlangWord();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		} else if (e.getSource() == cn5) {
			this.dispose();
			try {
				new EditSlangWord();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		} else if (e.getSource() == cn6) {
			this.dispose();
			try {
				new DeleteSlangWord();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		} else if (e.getSource() == cn7) {
			this.dispose();
			try {
				new Reset();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
		} else if (e.getSource() == cn8) {
			this.dispose();
			try {
				new RandomSlangWord();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource() == cn9_10) {
			this.dispose();
            try {
				new QuizFrame();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
}
