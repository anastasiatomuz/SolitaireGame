import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;

public class GameComponent extends JComponent {

    private int width; //width of the window
    private int height; //height of window
    private Color backgroundColor; //background color (green)
    private SolitaireGame solitaireGame; //logic class

    private MyRectangle stockRect;//rectangle object that displays the stock card

    private MyRectangle wasteRect;//rectangle object that displays the top waste card

    private TextPanel textPanel;//instance of TextPanel used to update messages

    private ArrayList<ArrayList<MyRectangle>> tableauRectangles;//list of rectangle objects that displays tableau cards

    private ArrayList<MyRectangle> foundationRectangles;//list of rectangle objects that holds fountain cards

    private boolean messageOn;//if the winning message is currently displayed


    public GameComponent(int width, int height, TextPanel textPanel, SolitaireGame solitaireGame) {
        super();
        this.width = width;
        this.height = height;
        this.solitaireGame = solitaireGame;
        stockRect = null;//new MyRectangle(null, new Point(0,0), false, "initial");
        wasteRect = null;//new MyRectangle(null, new Point(0,0), false, "initial");
        tableauRectangles = new ArrayList<>();
        foundationRectangles = new ArrayList<>();
        backgroundColor = new Color(45, 166, 16);
        this.textPanel = textPanel;
        messageOn = false;
        init();  // call helper method to do rest of setup
    }

    /* private helper method to initialize the game component size and set the mouse listeners */
    private void init() {

        // set the size of the component to the current width and height
        setSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width, height));


        // add the mouse listener
        addMouseListener(new MyMouseAdapter());

    }


    /**
     * Method to paint the shape canvas and all objects in it
     * @param g   the graphic context on which to paint
     */
    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        height = getHeight();
        width = getWidth();

        // clear using the background color
        g.setColor(backgroundColor);
        g.fillRect(0, 0, width, height);

        if (messageOn){
            g.setColor(Color.WHITE);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
            g.drawString("Congratulations!", 400, 400);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            g.drawString("You have won!", 450, 450);
        }


        //initialize rectangle object collections
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

        if (stockRect != null){
            if (stockRect.getCard() != null) {
                if (!stockRect.getCard().isVisible()) {
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
        }


        //drawing waste
        if (wasteRect != null){
            if (wasteRect.getCard() != null) {
                if (!wasteRect.getCard().isVisible()) {
                    try {
                        wasteRect.draw(g, "full");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        wasteRect.draw(g);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                try {
                    wasteRect.draw(g, "empty");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }



        for (int i = 0; i < tableauRectangles.size(); i++) {
            ArrayList<MyRectangle> tabStack = tableauRectangles.get(i);
            if (tabStack.size() == 0) {
                Point[] tabPoints = solitaireGame.getTABLEAU_POINTS();
                MyRectangle blank = new MyRectangle(null, tabPoints[i], false, "empty");
                try {
                    blank.draw(g, "empty");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                for (MyRectangle rectToDraw : tabStack) {
                    try {
                        rectToDraw.draw(g);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }


    /**
     * updates changed rectangle information from the solitaireGame class as you play and move the cards
     *
     * @param newStock MyRectangle object that updates stock
     * @param newWaste MyRectangle object that updates waste
     * @param newTableauRectangles 2D array of MyRectangle objects that updates tableau
     * @param newFoundationRectangles 2D array of MyRectangle objects that updates fountains
     */
    public void updateRectangleObjects(MyRectangle newStock, MyRectangle newWaste,
                                       ArrayList<ArrayList<MyRectangle>> newTableauRectangles,
                                       ArrayList<MyRectangle> newFoundationRectangles){
        stockRect = newStock;
        wasteRect = newWaste;
        tableauRectangles = newTableauRectangles;
        foundationRectangles = newFoundationRectangles;
    }


    class MyMouseAdapter extends MouseAdapter {
        // Method to handle when the user presses down the button
        public void mousePressed(MouseEvent e) {
            int currX = e.getX();
            int currY = e.getY();
            Point clickedPoint = e.getPoint();



            //user clicked stock
            if (stockRect.contains(e.getPoint())){
                solitaireGame.processStock();
            }

            //user clicked waste
            if (wasteRect.contains(e.getPoint())){
                solitaireGame.moveCardFromWaste();
            }

            //user clicked foundations
            for (MyRectangle foundation : foundationRectangles){
                if (foundation.contains(clickedPoint)){
                    textPanel.updateText("you clicked the " + foundation.getLabel() + " foundation!\n" +
                            "You can't move this card!");
                }
            }

            //user clicked tableau
            for (int i = 0; i < tableauRectangles.size(); i ++){
                for (int j = 0; j < tableauRectangles.get(i).size(); j ++){
                    MyRectangle rect = tableauRectangles.get(i).get(j);
                    if (rect.contains(e.getPoint())){
                        if (rect.getCard().isVisible()){
                            if (j == tableauRectangles.get(i).size() - 1){
                                solitaireGame.moveOneCard(i);
                            } else {
                                solitaireGame.moveMultipleCards(i,j);
                                break;
                            }
                        } else {
                            textPanel.updateText("You clicked a card that is hidden");
                        }
                    }
                }
            }


            //check if the user has won
            if (solitaireGame.hasWon()){
                textPanel.updateText("You have won!");
                messageOn = true;
            }


            // repaint all
            repaint();
        }
    }

}