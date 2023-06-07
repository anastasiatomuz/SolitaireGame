import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

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

    private String label; //can be playingCard, stock, waste, or {diamond, heart, club, spades} for the foundations

    public MyRectangle(Card card, Point startingPoint, boolean halfHidden, String label){
        super(startingPoint, new Dimension(100, 140));
        this.card = card;
        this.startingPoint = startingPoint;
        this.halfHidden = halfHidden;
        this.currentDimension = FULL_DIMENSION;
        if (label.equals("d")){
            this.label = "diamonds";
        } else if (label.equals("h")){
            this.label = "hearts";
        } else if (label.equals("C")){
            this.label = "clubs";
        } else if (label.equals("S")){
            this.label = "spades";
        } else {
            this.label = label;
        }
    }

    public Dimension getSMALL_DIMENSION(){
        return SMALL_DIMENSION;
    }

    public boolean isFull(){
        if (currentDimension == FULL_DIMENSION){
            return true;
        }
        return false;
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

    public String getLabel(){
        return label;
    }

    public Point getStartingPoint(){
        return startingPoint;
    }

    public void setStartingPoint(Point newPoint){
        startingPoint = newPoint;
    }

    public void someInfo(){
        if (card != null){
            System.out.print(card.cardInfo() + " ");
        } else {
            System.out.println(label);
        }
        System.out.println(isFull());
    }

    public void setHalfHidden(boolean isSmaller){
        halfHidden = isSmaller;
        if (halfHidden){
            currentDimension = SMALL_DIMENSION;
        }
    }


    public void draw(Graphics g) throws IOException {
        if (label.equals("stock")){
            if (card == null){
                g.setColor(Color.gray);
                drawCardAndOutline(g,startingPoint.x, startingPoint.y, currentDimension.width, currentDimension.height);
            } else {
                drawCardAndOutline(g,startingPoint.x, startingPoint.y, currentDimension.width, currentDimension.height);
                generateImage(g, "back_of_card");
            }
            return;
        }

//        Color color;
//        if (card.isVisible()){
//            color = Color.white;
//        } else {
//            color = Color.pink;
//        }
//        g.setColor(color);

        if (halfHidden){
            changeCardType(false);//make card half-hidden dimensions
            drawCardAndOutline(g,startingPoint.x, startingPoint.y, currentDimension.width, currentDimension.height);
            if (card.isVisible()){
                generateCroppedImage(g, card.getImageName());
                g.setColor(Color.BLACK);
                //g.drawString(card.cardInfo(), startingPoint.x + width/2 - 5, startingPoint.y + smallerHeight /2);
            } else {
                generateCroppedImage(g, "back_of_card");
            }
        } else {
            changeCardType(true);//make card half-hidden dimensions
            drawCardAndOutline(g,startingPoint.x, startingPoint.y, currentDimension.width, currentDimension.height);

            if (card.isVisible()){
                g.setColor(Color.BLACK);
                //g.drawString(card.cardInfo(), startingPoint.x + width/2 - 5, startingPoint.y + height /2);
                generateImage(g, card.getImageName());
            } else {
                generateImage(g,"back_of_card");
            }
        }
    }

    public void draw(Graphics g, String cardVal) throws IOException {
        if (cardVal.equals("empty")) {
            g.setColor(Color.GRAY);
            drawCardAndOutline(g,startingPoint.x, startingPoint.y, currentDimension.width, currentDimension.height);



        } else if (cardVal.equals("full")){
            g.setColor(Color.PINK);
            drawCardAndOutline(g,startingPoint.x, startingPoint.y, currentDimension.width, currentDimension.height);
            generateImage(g, "back_of_card");
        } else {
            // set the color
            g.setColor(Color.WHITE);

            drawCardAndOutline(g,startingPoint.x, startingPoint.y, currentDimension.width, currentDimension.height);
//            if (cardVal.equals("diamonds") || cardVal.equals("hearts")){
//                g.setColor(Color.RED);
//            } else if (cardVal.equals("clubs") || cardVal.equals("spades")) {
//                g.setColor(Color.BLACK);
//            }
            if (card == null){
                generateImage(g, cardVal);
            } else {
                generateImage(g, card.getImageName());
            }


                //g.drawImage()
            //g.drawString(cardVal, startingPoint.x + width/2 - 5, startingPoint.y + height /2);
        }
    }

    private void drawCardAndOutline(Graphics g, int x, int y, int w, int h){
        g.fillRect(x, y, w, h);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, w, h);
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }

    public void generateImage(Graphics g, String cardImageName) throws IOException {
        //IMPORTANT for setting an image in a rectangle
        //https://stackoverflow.com/questions/9864267/loading-resources-like-images-while-running-project-distributed-as-jar-archive/9866659#9866659
        //https://www.baeldung.com/java-resize-image
        //
        String loc = "/resources/images/" + cardImageName + ".png";

        BufferedImage image = ImageIO.read(Objects.requireNonNull(getClass().getResource(loc)));

        g.drawImage(resizeImage(image, currentDimension.width, currentDimension.height), startingPoint.x, startingPoint.y, null);

    }

    public void generateCroppedImage(Graphics g, String cardImageName) throws IOException {
        String loc = "/resources/images/" + cardImageName + ".png";
        BufferedImage image = ImageIO.read(Objects.requireNonNull(getClass().getResource(loc)));
        BufferedImage SubImg = resizeImage(image, FULL_DIMENSION.width, FULL_DIMENSION.height).getSubimage(0,0, SMALL_DIMENSION.width, SMALL_DIMENSION.height);
        g.drawImage(SubImg,startingPoint.x, startingPoint.y, null );
    }
}
