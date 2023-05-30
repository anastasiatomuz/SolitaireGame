import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import javax.swing.JComponent;

public class GameComponent extends JComponent {

    private int width;
    private int height;
    private Color backgroundColor;
    private SolitaireGame solitaireGame;

    private Card getTop
    public GameComponent(int width, int height) {
        this.width = width;
        this.height = height;
        solitaireGame = new SolitaireGame();
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

        /*
        // add the mouse listener and mouse motion listener
        addMouseListener(new MyMouseAdapter());
        addMouseMotionListener(new MyMouseMotionAdapter());
        */
    }

    public void setUpTableauAndStockAndWaste(){

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

    }


//    class MyMouseAdapter extends MouseAdapter {
//        // Method to handle when the user presses down the button
//        public void mousePressed(MouseEvent e) {
//            int currX = e.getX();
//            int currY = e.getY();
//
//            // create an object of the current shape type
//            if (currShapeType.equals(Shape.RECTANGLE)) {
//                currentShape = new Rectangle();
//            } else if (currShapeType.equals(Shape.OVAL)) {
//                currentShape = new Oval();
//            } else if (currShapeType.equals(Shape.ARC)) {
//                currentShape = new Arc();
//            } else if (currShapeType.equals(Shape.LINE)) {
//                currentShape = new Line();
//            }
//
//            // set point 1 in the new shape
//            currentShape.setPoint1Values(currX, currY);
//
//            // set point 2 temporarily to a nearby pixel to reduce flicker
//            // point 2 gets updated when mouse is dragged or released
//            currentShape.setPoint2Values(currX + 1, currY + 1);
//
//            // call helper method above
//            addShape(currentShape);
//
//            // repaint all
//            repaint();
//        }
//
//        // Method to handle when the user releases the mouse
//        public void mouseReleased(MouseEvent e) {
//            int currX = e.getX();
//            int currY = e.getY();
//
//            // update the point 2 values
//            currentShape.setPoint2Values(currX, currY);
//
//            // no current shape is being dragged
//            currentShape = null;
//
//            // repaint
//            repaint();
//        }
//    }
//
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
