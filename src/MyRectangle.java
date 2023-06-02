import java.awt.*;

public class MyRectangle extends Rectangle {
    private Card card;
    private int width = 100;
    private int height = 140;

    private int smallerHeight = 40;

    private Point startingPoint;

    private boolean halfHidden;

    private Dimension currentDimension;

    private final Dimension FULL_DIMENSION = new Dimension(100, 140);

    private final Dimension SMALL_DIMENSION = new Dimension(100, 40);

    public MyRectangle(Card card, Point startingPoint, boolean halfHidden){
        super(startingPoint, new Dimension(100, 140));
        this.card = card;
        this.startingPoint = startingPoint;
        this.halfHidden = halfHidden;
        this.currentDimension = FULL_DIMENSION;
    }

    public void changeCardType(boolean fullSize){
        if (fullSize){
            currentDimension = FULL_DIMENSION;
        } else {
            currentDimension = SMALL_DIMENSION;
        }
        setBounds(startingPoint.x, startingPoint.y, currentDimension.width, currentDimension.height);
    }
    public void updateCard(Card newCard){
        card = newCard;
    }

    public Card getCard(){
        return card;
    }

    public void draw(Graphics g) {
        Color color;
        if (card.isVisible()){
            color = Color.white;
        } else {
            color = Color.pink;
        }
        g.setColor(color);

        if (halfHidden){
            changeCardType(false);//make card half-hidden dimensions
            g.fillRect(startingPoint.x, startingPoint.y, currentDimension.width, currentDimension.height);
            if (card.isVisible()){
                g.setColor(Color.BLACK);
                g.drawString(card.cardInfo(), startingPoint.x + width/2 - 5, startingPoint.y + smallerHeight /2);
            }
        } else {
            changeCardType(true);//make card half-hidden dimensions
            g.fillRect(startingPoint.x, startingPoint.y, currentDimension.width, currentDimension.height);
            if (card.isVisible()){
                g.setColor(Color.BLACK);
                g.drawString(card.cardInfo(), startingPoint.x + width/2 - 5, startingPoint.y + height /2);
            }
        }
    }

    public void draw(Graphics g, String cardVal) {
        if (cardVal.equals("empty")) {
            g.setColor(Color.GRAY);
            g.fillRect(startingPoint.x, startingPoint.y, width, height);
        } else if (cardVal.equals("full")){
            g.setColor(Color.PINK);
            g.fillRect(startingPoint.x, startingPoint.y, width, height);
        } else {
            // set the color
            g.setColor(Color.WHITE);

            // draw the rectangle given the top left point and width and height
            //g.drawRect(startingPoint.x, startingPoint.y, width, height); // the getter methods here are INHERITED FROM SHAPE!
            g.fillRect(startingPoint.x, startingPoint.y, width, height);

            if (cardVal.equals("d") || cardVal.equals("h")){
                g.setColor(Color.RED);
            } else if (cardVal.equals("C") || cardVal.equals("S")) {
                g.setColor(Color.BLACK);
            }

                //g.drawImage()
            g.drawString(cardVal, startingPoint.x + width/2 - 5, startingPoint.y + height /2);
        }





    }
}
