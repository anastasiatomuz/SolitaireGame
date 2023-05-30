import org.w3c.dom.css.Rect;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GameComponent extends JComponent {

    private int width;
    private int height;
    private Color backgroundColor;
    private SolitaireGame solitaireGame;

    private Foundation[] foundations;

    public GameComponent(int width, int height) {
        this.width = width;
        this.height = height;
        solitaireGame = new SolitaireGame();
        solitaireGame.startUp();
        foundations = solitaireGame.getFoundations();
        backgroundColor = new Color(45, 166, 16);

        init();  // call helper method to do rest of setup
    }

    /* private helper method to initialize the shape component size and set the mouse listeners */
    private void init() {
        // !!! recall this class (ShapeIComponent) extends JComponent
        //     ALL CALLS BELOW ARE CALLS TO METHODS INHERITED FROM JComponent !!!

        // set the size of the component to the current width and height
        setSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width, height));


        // add the mouse listener and mouse motion listener
        addMouseListener(new MyMouseAdapter());
        /*
        addMouseMotionListener(new MyMouseMotionAdapter());
        */
    }


    /**
     * Method to paint the shape canvas and all objects in it
     * @param g   the graphic context on which to paint
     */
    @Override
    public void paintComponent (Graphics g) {
        height = getHeight();
        int width = getWidth();

        // clear using the background color
        g.setColor(backgroundColor);
        g.fillRect(0, 0, width, height);

        int initialX = 30;
        int initialY = 23;
        Rectangle rectToAdd;
        for (Foundation foundation : foundations){
            rectToAdd = new Rectangle(null, new Point(initialX + 20 , initialY));
            initialX += 20 + rectToAdd.getWidth();
            rectToAdd.draw(g, foundation.getSuit());
        }

        try {
            BufferedImage bufferedImage = ImageIO.read(new File("src/SuitDiamonds.svg"));
            Image image = bufferedImage.getScaledInstance(800, 500, Image.SCALE_DEFAULT);
            ImageIcon icon = new ImageIcon(image);
            JLabel jLabel = new JLabel();
            jLabel.setIcon(icon);
            add(jLabel);
            setVisible(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    class MyMouseAdapter extends MouseAdapter {
        // Method to handle when the user presses down the button
        public void mousePressed(MouseEvent e) {
            int currX = e.getX();
            int currY = e.getY();

            System.out.println("" + currX + " " + currY);

            // repaint all
            repaint();
        }

        // Method to handle when the user releases the mouse
        public void mouseReleased(MouseEvent e) {
            int currX = e.getX();
            int currY = e.getY();

        }
    }

//
//
//
//    /** An inner class for handling the mouse motion listener */
//    class MyMouseMotionAdapter extends MouseMotionAdapter {
//        /** Method to handle the drag of a mouse */
//        public void mouseDragged(MouseEvent e) {
//            int currX = e.getX();
//            int currY = e.getY();
//
//            // set the point 2 values
//            currentShape.setPoint2Values(currX, currY);
//
//            // repaint
//            repaint();
//        }
//    }


}
