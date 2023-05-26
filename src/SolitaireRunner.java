import javax.swing.JFrame;

public class SolitaireRunner {
    public static void main (String args[]) {
        // create a frame (main application window)
        JFrame frame = new JFrame();

        // create a Shape Panel
        SolitairePanel solitairePanel = new SolitairePanel();

        // add the shapePanel to the frame
        frame.add(solitairePanel);
        frame.pack();         // shrink to fit preferred size
        frame.setVisible(true); // show the frame

        // set the frame so that when the user closes the window
        // using the X button in the top right, the program stops
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
