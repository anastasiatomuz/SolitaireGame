import javax.swing.*;

public class ImagePanelRunner {
    public static void main(String[] args){
        JFrame frame = new JFrame();
        ImagePanel imagePanel = new ImagePanel();
        frame.add(imagePanel);
        frame.pack();         // shrink to fit preferred size
        frame.setVisible(true); // show the frame

        // set the frame so that when the user closes the window
        // using the X button in the top right, the program stops
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
