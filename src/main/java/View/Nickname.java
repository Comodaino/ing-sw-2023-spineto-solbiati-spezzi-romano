package View;

import javax.swing.*;

public class Nickname extends JLabel {
    private JLabel label;
    private JTextField textField;
    private JButton button;
    private String nickname;

    public Nickname() {
        super();


        nickname = JOptionPane.showInputDialog("Enter your nickname:");
        while (nickname.length() > 10 || nickname.length()==0){
            nickname = JOptionPane.showInputDialog("You are wrong, please enter your nickname again :");
        }

        label = new JLabel(nickname);
        textField = new JTextField(20);
        button = new JButton("Ok");


        add(label);
        add(textField);
        add(button);

        setSize(300, 100);
    }
}

