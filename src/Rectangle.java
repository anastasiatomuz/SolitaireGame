import java.awt.Graphics;
import java.awt.Color;
import java.awt.Point;

public class Rectangle {
    private Card card;
    private int width = 100;
    private int height = 140;

    private Point startingPoint;

    public Rectangle(Card card, Point startingPoint){
        this.card = card;
        this.startingPoint = startingPoint;
    }

    public void updateCard(Card newCard){
        card = newCard;
    }

    public Card getCard(){
        return card;
    }

    public void draw(Graphics g) {
        // set the color
        g.setColor(Color.WHITE);

        // draw the rectangle given the top left point and width and height
        //g.drawRect(startingPoint.x, startingPoint.y, width, height); // the getter methods here are INHERITED FROM SHAPE!
        g.fillRect(startingPoint.x, startingPoint.y, width, height);
    }
}
