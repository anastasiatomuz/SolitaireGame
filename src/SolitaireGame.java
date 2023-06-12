import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

public class SolitaireGame {
    private TableauStack[] tableau;
    private Foundation[] foundations;
    private ArrayList<Card> stock;
    private ArrayList<Card> waste;
    private Card topWasteCard;
    private Scanner scan;

    private MyRectangle stockRect;

    private MyRectangle wasteRect;

    private ArrayList<ArrayList<MyRectangle>> tableauRectangles;

    private ArrayList<MyRectangle> foundationRectangles;

    private GameComponent gameComponent;

    private TextPanel textPanel;

    private final Point STOCK_START_POINT = new Point(710, 23);

    private final Point WASTE_START_POINT = new Point(850, 170);

    private final Point[] FOUNDATION_POINTS = {new Point(50,23), new Point(170,23), new Point(290,23), new Point(410, 23)};

    private final Point[] TABLEAU_POINTS = {new Point(50,203), new Point(170,203), new Point(290,203),
            new Point(410,203), new Point(530,203), new Point(650,203), new Point(770,203)};

    public SolitaireGame(){
        // create a frame (main application window)
        JFrame frame = new JFrame();

        // create a Shape Panel
        SolitairePanel solitairePanel = new SolitairePanel(this);

        // add the solitairePanel to the frame
        frame.add(solitairePanel);
        frame.pack();         // shrink to fit preferred size
        frame.setVisible(true); // show the frame

        gameComponent = solitairePanel.getGameComponent();
        textPanel = solitairePanel.getTextPanel();
        setUpLayout();

        // set the frame so that when the user closes the window
        // using the X button in the top right, the program stops
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public MyRectangle getStockRect(){
        return stockRect;
    }

    public MyRectangle getWasteRect(){
        return wasteRect;
    }

    public ArrayList<MyRectangle> getFoundationRectangles(){
        return foundationRectangles;
    }

    public ArrayList<ArrayList<MyRectangle>> getTableauRectangles(){
        return tableauRectangles;
    }

    public Point[] getTABLEAU_POINTS(){
        return TABLEAU_POINTS;
    }


    public void setUpLayout(){
        startUp();
        int initialX = 30;
        int initialY = 23;
        MyRectangle rectToAdd;
        for (Foundation foundation : foundations){
            rectToAdd = new MyRectangle(null, new Point(initialX + 20 , initialY), false, foundation.getSuit());
            initialX += 20 + rectToAdd.getWidth();
            foundationRectangles.add(rectToAdd);
        }

        //creating rectangle object for stack and waste piles
        stockRect = new MyRectangle(stock.get(stock.size() - 1), new Point(initialX + 200 , initialY), false, "stock");
        wasteRect = new MyRectangle(null, new Point(initialX + 200 + 100 + 40, initialY), false, "waste");


        //creating rectangle object for the cards of the tableau
        initialX = 30 + 20 ;
        for (TableauStack tableauStack : tableau){
            initialY = 23 + 140 + 40;
            ArrayList<Card> cards = tableauStack.getStack();
            ArrayList<MyRectangle> rectsToAdd = new ArrayList<>();
            for (int i = 0; i < cards.size(); i ++){
                boolean halfHidden = false;
                if (i != cards.size() - 1){
                    halfHidden = true;
                }
                rectToAdd = new MyRectangle(cards.get(i), new Point(initialX , initialY), halfHidden, "playingCard");
                rectsToAdd.add(rectToAdd);
                if (halfHidden){
                    initialY += 40;
                } else {
                    initialY += 140;
                }
            }
            initialX += 100 + 20;//width of card and of space between tableau stacks
            tableauRectangles.add(rectsToAdd);
        }
        updateRectsOutside();
    }


    public Foundation[] getFoundations(){
        return foundations;
    }

    public TableauStack[] getTableau(){
        return tableau;
    }

    public void startUp(){
        scan = new Scanner(System.in);
        foundations = new Foundation[]{new Foundation("d"), new Foundation("h"),
                new Foundation("C"), new Foundation("S")};
        stock = new ArrayList<>();
        waste = new ArrayList<>();

        //intializing rectangle object collections
        tableauRectangles = new ArrayList<>(7);
        foundationRectangles = new ArrayList<>();


        //generate a full deck of cards
        ArrayList<Card> orderedDeck = new ArrayList<>();
        for (int i = 1; i <= 4; i ++){
            String suit;
            if (i == 1){
                suit = "d";
            } else if (i == 2){
                suit = "h";
            } else if (i == 3){
                suit = "C";
            } else {
                suit = "S";
            }
            String value;

            for (int j = 1; j <= 13; j ++){
                if (j == 1){
                    value = "A"; //Ace
                } else if (j == 11){
                    value = "J";
                } else if (j == 12){
                    value = "Q";
                } else if (j == 13){
                    value = "K";
                } else {
                    value = "" + j;
                }

                orderedDeck.add(new Card(suit, value, true));
            }
        }

        //shuffle deck
        while (orderedDeck.size() > 0){
            int randInd = (int) (Math.random() * orderedDeck.size());
            stock.add(orderedDeck.remove(randInd));
        }



        //set up the tableau stacks
        tableau = new TableauStack[7];
        for (int i = 1; i <= 7; i ++){
            ArrayList<Card> stackToAdd = new ArrayList<>();
            for (int j = 0; j < i; j ++){
                Card cardToMove = stock.remove(stock.size() - 1);
                if (j < i - 1){
                    //makes the cards that aren't the last one in tableau stack are not visible
                    cardToMove.flip();
                }
                stackToAdd.add(cardToMove);
            }
            tableau[i - 1] = new TableauStack(stackToAdd);
        }
    }

    public boolean hasWon(){
        for (Foundation foundation : foundations){
            if (foundation.getFoundationStack().size() != 13){
                return false;
            }
        }
        return true;
    }


    public void processStock(){
        if (stock.size() == 0){
            if (waste.size() == 0){
                textPanel.updateText("Cannot move cards to waste or recycle waste because " +
                        "you have used up all the cards from the waste and stock piles");
            } else {
                for (int i = waste.size() - 1; i >= 0; i --){
                    Card toRemove = waste.remove(i);
                    toRemove.setVisible(false);
                    stock.add(toRemove);
                }
                stockRect.updateCard(stock.get(stock.size() - 1));
                wasteRect.updateCard(null);
                textPanel.updateText("Waste recycled");
            }
        } else {
            Card toMove = stock.remove(stock.size() - 1);
            textPanel.updateText("" + toMove.getCardName() + " was added to the waste");
            toMove.setVisible(true);
            waste.add(toMove);
            topWasteCard = waste.get(waste.size() - 1);
            wasteRect.updateCard(topWasteCard);
            if (stock.size() == 0){
                stockRect.updateCard(null);
            }
        }
    }

    public void moveOneCard(int tabNum){
        TableauStack stackFrom = tableau[tabNum];
        Card cardFrom = stackFrom.getTopCard();
        boolean moved = false;
        String actionStatement = cardFrom.getCardName();
        for (int i = 0; i < foundations.length; i ++){ //got through 4 suits of foundations
            Foundation foundation = foundations[i];
            if (foundation.addCard(cardFrom)){
                MyRectangle toMove = tableauRectangles.get(tabNum).remove(tableauRectangles.get(tabNum).size() - 1);
                if (tableauRectangles.get(tabNum).size() > 0){
                    MyRectangle revealed = tableauRectangles.get(tabNum).get(tableauRectangles.get(tabNum).size() - 1);
                    revealed.setHalfHidden(false);
                    revealed.getCard().setVisible(true);
                }
                toMove.setStartingPoint(FOUNDATION_POINTS[i]);
                foundationRectangles.get(i).updateCard(foundation.getFoundationStack().get(foundation.getFoundationStack().size() - 1));
                //toMove.updateCard(foundation.getFoundationStack().get(foundation.getFoundationStack().size() - 1));
                stackFrom.removeTopCard(); //removes top card of logic place
                moved = true;
                actionStatement += " moved to " + foundation.getSuit() + " foundation";
                break;
            }
        }

        if (!moved){
            for (int i = 0; i < tableau.length; i ++){
                TableauStack tableauStack = tableau[i];
                if (tableauStack.addOneCard(cardFrom)){
                    //rectangle graphics object removed from old loc to new stack
                    MyRectangle rectMoved = tableauRectangles.get(tabNum).remove(tableauRectangles.get(tabNum).size() - 1);


                    if (tableauRectangles.get(tabNum).size() > 0){
                        MyRectangle revealed = tableauRectangles.get(tabNum).get(tableauRectangles.get(tabNum).size() - 1);
                        revealed.setHalfHidden(false);
                        revealed.getCard().setVisible(true);
                    }

                    if (tableauRectangles.get(i).size() == 0){
                        tableauRectangles.get(i).add(rectMoved);
                        rectMoved.setStartingPoint(TABLEAU_POINTS[i]);
                    } else {
                        MyRectangle prev = tableauRectangles.get(i).get(tableauRectangles.get(i).size() - 1);
                        prev.setHalfHidden(true);
                        prev.changeCardType(true);
                        rectMoved.setStartingPoint(new Point(prev.getStartingPoint().x,
                                prev.getStartingPoint().y + prev.getSMALL_DIMENSION().height));
                        tableauRectangles.get(i).add(rectMoved);
                    }


                    stackFrom.removeTopCard(); //card object moved in logic lists
                    moved = true;


//                    //updates the rect object that is newly revealed
//                    if (tableauRectangles.get(i).size() > 0){
//                        MyRectangle revealed = tableauRectangles.get(tabNum).get(tableauRectangles.get(tabNum).size() - 1);
//                        revealed.changeCardType(true);
//                        revealed.getCard().setVisible(true);
//                    }




                    actionStatement += " moved to tableau stack #" + (i+1);
                    break;
                }
            }
        }

        if (!moved){
            actionStatement += " was unable to be moved. Try another card";
        }
        textPanel.updateText(actionStatement);
        updateRectsOutside();

    }

    public void moveCardFromWaste(){
        boolean moved = false;
        if (waste.size() == 0){
            textPanel.updateText("Cannot move card anywhere because waste is empty.");
            return;
        }


        String actionStatement = topWasteCard.getCardName();
        for (int i = 0; i < foundations.length; i ++){
            Foundation foundation = foundations[i];
            if (foundation.addCard(topWasteCard)){
                waste.remove(waste.size() - 1);
                if (waste.size() == 0){
                    topWasteCard = null;
                } else {
                    topWasteCard = waste.get(waste.size() - 1);
                }
                foundationRectangles.get(i).updateCard(foundation.getFoundationStack().get(foundation.getFoundationStack().size() - 1));
                moved = true;
                wasteRect.updateCard(topWasteCard);
                actionStatement += " moved to ";
                switch (foundation.getSuit()) {
                    case "C" -> actionStatement += "clubs";
                    case "S" -> actionStatement += "spades";
                    case "d" -> actionStatement += "diamonds";
                    case "h" -> actionStatement += "hearts";
                }
                actionStatement += " foundation";
                break;
            }
        }

        if (!moved){
            for (int i = 0; i < tableau.length; i ++){
                TableauStack tableauStack = tableau[i];
                if (tableauStack.addOneCard(topWasteCard)){
                    Card removed = waste.remove(waste.size() - 1);
                    if (waste.size() == 0){
                        topWasteCard = null;
                    } else {
                        topWasteCard = waste.get(waste.size() - 1);
                    }
                    wasteRect.updateCard(topWasteCard);


                    //dealing with the card below where adding
                    if (tableauRectangles.get(i).size() != 0){
                        MyRectangle prev = tableauRectangles.get(i).get(tableauRectangles.get(i).size() - 1);
                        prev.setHalfHidden(true);
                        Point p = new Point(prev.getStartingPoint().x,
                                prev.getStartingPoint().y + prev.getSMALL_DIMENSION().height);
                        MyRectangle fromWaste = new MyRectangle(removed, p, false, removed.cardInfo());
                        tableauRectangles.get(i).add(fromWaste);
                    } else {
                        MyRectangle fromWaste = new MyRectangle(removed, TABLEAU_POINTS[i], false, removed.cardInfo());
                        tableauRectangles.get(i).add(fromWaste);
                    }
                    moved = true;
                    actionStatement += " moved to tableau stack #" + (i+1);
                    break;
                }
            }
        }

        if (!moved){
            actionStatement += " was unable to be moved. Try another card";
        }
        textPanel.updateText(actionStatement);
        updateRectsOutside();
    }

    public void moveMultipleRects(int tabLoc, int locFromBottom, int locTo){
        ArrayList<MyRectangle> multipleRectsToMove = new ArrayList<>();
        for (int i = locFromBottom; i < tableauRectangles.get(tabLoc).size(); i ++){
            MyRectangle rectToMove = tableauRectangles.get(tabLoc).remove(i);
            i--;
            multipleRectsToMove.add(rectToMove);
        }

        if (tableau[tabLoc].getStack().size() > 0){
            MyRectangle revealed = tableauRectangles.get(tabLoc).get(tableauRectangles.get(tabLoc).size() - 1);
            revealed.setHalfHidden(false);
            revealed.getCard().setVisible(true);
        }


        if (tableauRectangles.get(locTo).size() == 0){
            Point startingPoint = TABLEAU_POINTS[locTo];
            for (int i = 0; i < multipleRectsToMove.size(); i ++){
                MyRectangle toMove = multipleRectsToMove.get(i);
                toMove.setStartingPoint(new Point(startingPoint.x, startingPoint.y + (int)(toMove.getSMALL_DIMENSION().getHeight()) * (i)));
                tableauRectangles.get(locTo).add(toMove);
            }
        } else {
            MyRectangle prev = tableauRectangles.get(locTo).get(tableauRectangles.get(locTo).size() - 1);
            Point startingPoint = prev.getStartingPoint();
            prev.setHalfHidden(true);
            for (int i = 0; i < multipleRectsToMove.size(); i ++) {
                MyRectangle toMove = multipleRectsToMove.get(i);
                toMove.setStartingPoint(new Point(startingPoint.x, startingPoint.y + (int)(toMove.getSMALL_DIMENSION().getHeight()) * (i + 1)));
                tableauRectangles.get(locTo).add(toMove);
            }
        }

        updateRectsOutside();
    }

    public void moveMultipleCards(int tabLoc, int locFromBottom){
        String actionStatement = "";
        TableauStack stackFrom = tableau[tabLoc];
        Card cardFrom = stackFrom.getStack().get(locFromBottom);
        int locFromTop = stackFrom.getStack().size() - locFromBottom;

        ArrayList<Card> cardsToMove = stackFrom.getGroupToMove(locFromTop);
        boolean moved = false;
        actionStatement = "The set of cards with the bottom card of " + cardFrom.getCardName();

        int locFound = 0;
        for (int i = 0; i < tableau.length; i ++){
            TableauStack tableauStack = tableau[i]; //stackTo
            if (tableauStack.addMultipleCards(cardsToMove)){
                locFound = i;
                stackFrom.removeMultipleCards(locFromTop);
                moved = true;
                actionStatement += "was moved to tableau stack #" + (i + 1);
                break;
            }
        }

            if (!moved){
                actionStatement += " was unable to be moved. Try another card";
            } else {
                moveMultipleRects(tabLoc, locFromBottom, locFound);
            }


        textPanel.updateText(actionStatement);
        updateRectsOutside();
    }


    public ArrayList<Card> getStock(){
        return stock;
    }

    public void updateRectsOutside(){
        for (int i = 0; i < tableauRectangles.size(); i ++){
            ArrayList<MyRectangle> list = tableauRectangles.get(i);
            for (int j = 0; j < list.size(); j ++){
                MyRectangle toLookAt = list.get(j);
                if (j != 0 && j == list.size() - 1){
                    toLookAt.getCard().setVisible(true);
                }
            }

        }
        gameComponent.updateRectangleObjects(stockRect, wasteRect, tableauRectangles, foundationRectangles);
    }

}
