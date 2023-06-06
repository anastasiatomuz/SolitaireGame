import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;

public class GameComponent extends JComponent {

    private int width;
    private int height;
    private Color backgroundColor;
    private SolitaireGame solitaireGame;

    private Foundation[] foundations;


    private TableauStack[] tableau;

    private MyRectangle stockRect;

    private MyRectangle wasteRect;

    private TextPanel textPanel;

    private ArrayList<ArrayList<MyRectangle>> tableauRectangles;

    private ArrayList<MyRectangle> foundationRectangles;

    public GameComponent(int width, int height, TextPanel textPanel, SolitaireGame solitaireGame) {
        this.width = width;
        this.height = height;
        this.solitaireGame = solitaireGame;
        tableau = solitaireGame.getTableau();
        foundations = solitaireGame.getFoundations();
        backgroundColor = new Color(45, 166, 16);
        this.textPanel = textPanel;
//        tableauRectangles = new ArrayList<>();
//        foundationRectangles = new ArrayList<>(4);
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

    }

    public void updateRectangleObjects(MyRectangle newStock, MyRectangle newWaste,
                                       ArrayList<ArrayList<MyRectangle>> newTableauRectangles,
                                       ArrayList<MyRectangle> newFoundationRectangles){
        stockRect = newStock;
        wasteRect = newWaste;
        tableauRectangles = newTableauRectangles;
        foundationRectangles = newFoundationRectangles;
    }


    /**
     * Method to paint the shape canvas and all objects in it
     * @param g   the graphic context on which to paint
     */
    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        height = getHeight();
        int width = getWidth();

        // clear using the background color
        g.setColor(backgroundColor);
        g.fillRect(0, 0, width, height);

        for (MyRectangle foundRect : foundationRectangles) {
            if (foundRect.getCard() == null) {
                try {
                    foundRect.draw(g, foundRect.getLabel());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    foundRect.draw(g);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        //drawing stock
        if (stockRect.getCard() != null){
            if (!stockRect.getCard().isVisible()){
                try {
                    stockRect.draw(g, "full");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    stockRect.draw(g);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            try {
                stockRect.draw(g, "empty");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        for (ArrayList<MyRectangle> tabStack : tableauRectangles){
            for (MyRectangle rectToDraw : tabStack){
                try {
                    rectToDraw.draw(g);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }




        /*
        int initialX = 30;
        int initialY = 23;
        MyRectangle rectToAdd;
        for (Foundation foundation : foundations) {
            rectToAdd = new MyRectangle(null, new Point(initialX + 20, initialY), false, foundation.getSuit());
            initialX += 20 + rectToAdd.getWidth();
            try {
                rectToAdd.draw(g, foundation.getSuit());
                foundationRectangles.add(rectToAdd);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        for (MyRectangle foundRect : foundationRectangles)

            //stock
            stockRect = new MyRectangle(null, new Point(initialX + 200, initialY), false, "stock");
        try {
            stockRect.draw(g, "full");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //waste
        wasteRect = new MyRectangle(null, new Point(initialX + 200 + 100 + 40, initialY), false, "waste");
        try {
            wasteRect.draw(g, "empty");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        //tableau
        initialX = 30 + 20;
        for (TableauStack tableauStack : tableau) {
            initialY = 23 + 140 + 40;
            ArrayList<Card> cards = tableauStack.getStack();
            for (int i = 0; i < cards.size(); i++) {
                boolean halfHidden = false;
                if (i != cards.size() - 1) {
                    halfHidden = true;
                }
                rectToAdd = new MyRectangle(cards.get(i), new Point(initialX, initialY), halfHidden, "playingCard");
                try {
                    rectToAdd.draw(g);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                tableauRectangles.add(rectToAdd);
                if (halfHidden) {
                    initialY += 40;
                } else {
                    initialY += 140;
                }
            }
            initialX += 100 + 20;//width of card and of space between tableau stacks\

            */

    }





//        BufferedImage wPic = ImageIO.read(this.getClass().getResource("diamond.png"));
//        JLabel wIcon = new JLabel(new ImageIcon(wPic));

//        BufferedImage myPicture = null;
//        try {
//            myPicture = ImageIO.read(new File("C:/Users/tgs34/IdeaProjects/SolitaireGraphicsPlayground/src/"));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }



//        try {
//            BufferedImage bufferedImage = ImageIO.read(new File("src/SuitDiamonds.svg"));
//            Image image = bufferedImage.getScaledInstance(800, 500, Image.SCALE_DEFAULT);
//            ImageIcon icon = new ImageIcon(image);
//            JLabel jLabel = new JLabel();
//            jLabel.setIcon(icon);
//            add(jLabel);
//            setVisible(true);
//        } catch (IOException e) {
//            System.out.println(e);
//        }




    class MyMouseAdapter extends MouseAdapter {
        // Method to handle when the user presses down the button
        public void mousePressed(MouseEvent e) {
            int currX = e.getX();
            int currY = e.getY();
            Point clickedPoint = e.getPoint();

            System.out.println("" + currX + " " + currY);

            for (MyRectangle foundation : foundationRectangles){
                if (foundation.contains(clickedPoint)){
                    textPanel.updateText("you clicked the " + foundation.getLabel() + " foundation!\n" +
                            "You can't move this card!");
                }
            }

            if (stockRect.contains(e.getPoint())){
                textPanel.updateText("You clicked the stack!");
            }

            if (wasteRect.contains(e.getPoint())){
                textPanel.updateText("You clicked the waste!");
            }
            for (ArrayList<MyRectangle> tabStack : tableauRectangles){
                for (MyRectangle rect : tabStack){
                    if (rect.contains(clickedPoint)){
                        if (rect.getCard().isVisible()){
                            textPanel.updateText("You clicked the " + rect.getCard().cardInfo() + " card!");
                        } else {
                            textPanel.updateText("You clicked a card that is hidden");
                        }
                    }
                }
            }



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
