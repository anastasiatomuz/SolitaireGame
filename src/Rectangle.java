import java.awt.Graphics;
import java.awt.Color;
import java.awt.Point;

public class Rectangle {
    private Card card;
    private int width;
    private int height;

    public Rectangle(Card card){
        this.card = card;
    }

    public void updateCard(Card newCard){
        card = newCard;
    }

    public Card getCard(){
        return card;
    }

    public void draw(Graphics g) {
        // set the color
        g.setColor(getColor());

        // draw the rectangle given the top left point and width and height
        g.drawRect(getMinX(), getMinY(), width, height); // the getter methods here are INHERITED FROM SHAPE!
    }
}
