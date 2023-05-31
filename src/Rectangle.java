import java.awt.Graphics;
import java.awt.Color;
import java.awt.Point;

public class Rectangle {
    private Card card;
    private int width = 100;
    private int fullHeight = 140;

    private int smallerHeight = 40;

    private Point startingPoint;

    private boolean halfHidden;

    public Rectangle(Card card, Point startingPoint, boolean halfHidden){
        this.card = card;
        this.startingPoint = startingPoint;
        this.halfHidden = halfHidden;
    }

    public int getWidth(){
        return width;
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

        if (halfHidden){
            g.setColor(color);
            g.fillRect(startingPoint.x, startingPoint.y, width, smallerHeight);
            if (card.isVisible()){
                g.drawString(card.getValue(), startingPoint.x + width/2 - 5, startingPoint.y + smallerHeight /2);
            }
        } else {
            g.fillRect(startingPoint.x, startingPoint.y, width, fullHeight);
            if (card.isVisible()){
                g.drawString(card.getValue(), startingPoint.x + width/2 - 5, startingPoint.y + fullHeight /2);
            }
        }
    }

    public void draw(Graphics g, String cardVal) {
        if (cardVal.equals("empty")){
            g.setColor(Color.GRAY);
            g.fillRect(startingPoint.x, startingPoint.y, width, fullHeight);

        } else {
            // set the color
            g.setColor(Color.WHITE);

            // draw the rectangle given the top left point and width and height
            //g.drawRect(startingPoint.x, startingPoint.y, width, height); // the getter methods here are INHERITED FROM SHAPE!
            g.fillRect(startingPoint.x, startingPoint.y, width, fullHeight);

            if (cardVal.equals("d") || cardVal.equals("h")){
                g.setColor(Color.RED);
            } else if (cardVal.equals("C") || cardVal.equals("S")) {
                g.setColor(Color.BLACK);
            }

                //g.drawImage()
            g.drawString(cardVal, startingPoint.x + width/2 - 5, startingPoint.y + fullHeight /2);
        }





    }
}
