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


        messageArea = new JTextArea("No message yet", 4, 60);
        messageArea.setEditable(true);
        add(messageArea);
        updateText("testing testing");


        // initialize the panel using the init() private helper method
    }

    public JTextArea getTextArea(){
        return messageArea;
    }

    public void updateText(String toDisplay){
        System.out.println("here: " + messageArea);
        messageArea.setText(toDisplay);
    }
}
