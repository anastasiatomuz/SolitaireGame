import javax.swing.*;
import java.awt.*;

public class TextPanel extends JPanel {


    private JLabel messageLabel;
    private JTextArea messageArea;

    public JTextArea getTA() {
        return messageArea;
    }

    public TextPanel (){

        messageLabel = new JLabel("Message");
        add(messageLabel);


        messageArea = new JTextArea("Welcome to solitaire\nPlease click a card to move it", 4, 60);
        messageArea.setEditable(true);
        add(messageArea);


        // initialize the panel using the init() private helper method
    }

    public JTextArea getTextArea(){
        return messageArea;
    }

    public void updateText(String toDisplay){
        messageArea.setText(toDisplay);
    }
}
