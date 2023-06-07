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

        // add the shapePanel to the frame
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

    public void play(){
        startUp();
        printGame();
        boolean gameWon = false;
        while (!gameWon){
            menu();
            printGame();
        }
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

    private void printGame(){
        System.out.println("\n=================================================================");
        System.out.print("Fountains:\n");
        System.out.print("Diamond: ");
        System.out.println(displayStack(foundations[0].getFoundationStack()));
        System.out.print("Heart: ");
        System.out.println(displayStack(foundations[1].getFoundationStack()));
        System.out.print("Club: ");
        System.out.println(displayStack(foundations[2].getFoundationStack()));
        System.out.print("Spade:  ");
        System.out.println(displayStack(foundations[3].getFoundationStack()));

        System.out.print("Stock: ");
        if (stock.size() == 0) {
            System.out.println("empty; must recycle waste to access more cards");
        } else {
            System.out.println("XX");
        }
        System.out.print("\nWaste: ");
        if (topWasteCard != null){
            System.out.println(topWasteCard.cardInfo());
        }

        System.out.println("\nTableau: ");
        for (int i = 0; i < tableau.length; i ++){
            System.out.println((i + 1) + ". " + displayStack(tableau[i].getStack()));
        }
        System.out.println("=================================================================\n");
    }

    public boolean hasWon(){
        for (Foundation foundation : foundations){
            if (foundation.getFoundationStack().size() != 13){
                return false;
            }
        }
        return true;
    }

    public String displayStack(ArrayList<Card> stackToDisplay){
        if (stackToDisplay.size() == 0){
            return "currently empty";
        }

        String toDisplay = "";
        for (Card card : stackToDisplay){
            if (card.isVisible()){
                toDisplay += card.cardInfo() + " ";
            } else {
                toDisplay += "XX  ";
            }
        }

        return toDisplay;
    }

    private void menu(){
        System.out.println("1. Select one card in tableau");
        System.out.println("2. Select multiple cards in tableau");
        System.out.println("3. Add card to waste");
        System.out.println("4. Move card from waste");
        System.out.println("5. Recycle waste");
        Scanner scan = new Scanner(System.in);
        System.out.print("What is your choice? ");
        int choice = scan.nextInt();
        if (choice == 1) {
            moveOneCard(1);
        } else if (choice == 2){
            moveMultipleCards();
        } else if (choice == 3) {
            if (stock.size() == 0) {
                System.out.println("Cannot bring up new card to waste. Must recycle waste first.");
            } else {
                waste.add(stock.remove(stock.size() - 1));
                topWasteCard = waste.get(waste.size() - 1);
            }
        } else if (choice == 4){
            moveCardFromWaste();
        } else if (choice == 5){
            if (stock.size() != 0){
                System.out.println("Cannot recycle waste since there are still card in the stock pile");
            } else {
                if (waste.size() == 0){
                    System.out.println("Cannot recycle waste because there are no more cards to recycle");
                } else {
                    for (int i = waste.size() - 1; i >= 0; i --){
                        stock.add(waste.remove(i));
                    }
                }
            }
            topWasteCard = null;
        }
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
            textPanel.updateText("" + toMove.cardInfo() + " was added to the stock");
            toMove.setVisible(true);
            waste.add(toMove);
            topWasteCard = waste.get(waste.size() - 1);
            wasteRect.updateCard(topWasteCard);
        }
    }

    public void moveOneCard(int tabNum){
        TableauStack stackFrom = tableau[tabNum];
        Card cardFrom = stackFrom.getTopCard();
        boolean moved = false;
        String actionStatement = cardFrom.cardInfo();
        for (int i = 0; i < foundations.length; i ++){
            Foundation foundation = foundations[i];
            if (foundation.addCard(cardFrom)){
                MyRectangle toMove = tableauRectangles.get(tabNum).remove(tableauRectangles.get(tabNum).size() - 1);
                if (tableauRectangles.get(tabNum).size() > 0){
                    MyRectangle revealed = tableauRectangles.get(tabNum).get(tableauRectangles.get(tabNum).size() - 1);
                    revealed.changeCardType(true);
                    revealed.getCard().setVisible(false);
                }
                toMove.setStartingPoint(FOUNDATION_POINTS[i]);
                toMove.updateCard(foundation.foundationStack.get(foundation.foundationStack.size() - 1));
                stackFrom.removeTopCard();
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
                        System.out.println(revealed.getCard().cardInfo());
                        revealed.changeCardType(true);
                        System.out.println("yes");
                        revealed.getCard().setVisible(false);
                    }

                    if (tableauRectangles.get(i).size() == 0){
                        tableauRectangles.get(i).add(rectMoved);
                        rectMoved.setStartingPoint(TABLEAU_POINTS[i]);
                    } else {
                        MyRectangle prev = tableauRectangles.get(i).get(tableauRectangles.get(i).size() - 1);
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


        String actionStatement = topWasteCard.cardInfo();
        for (Foundation foundation : foundations){
            if (foundation.addCard(topWasteCard)){
                waste.remove(waste.size() - 1);
                if (waste.size() == 0){
                    topWasteCard = null;
                } else {
                    topWasteCard = waste.get(waste.size() - 1);
                }
                moved = true;
                wasteRect.updateCard(topWasteCard);
                actionStatement += " moved to " + foundation.getSuit() + " foundation";
                break;
            }
        }

        if (!moved){
            for (int i = 0; i < tableau.length; i ++){
                TableauStack tableauStack = tableau[i];
                if (tableauStack.addOneCard(topWasteCard)){
                    waste.remove(waste.size() - 1);
                    if (waste.size() == 0){
                        topWasteCard = null;
                    } else {
                        topWasteCard = waste.get(waste.size() - 1);
                    }
                    wasteRect.updateCard(topWasteCard);
                    moved = true;
                    tableauStack.displayStack();
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

    public void moveMultipleCards(){
        String actionStatement = "";
        System.out.println("Tableau stacks are numbered 1 - 7 from top down");
        System.out.print("From which stack of the tableau do you want to choose? ");
        int userMove = scan.nextInt();
        TableauStack stackFrom = tableau[userMove - 1];

        System.out.println("Cards in a stack can be numbered with \"1\" being the top card.");
        System.out.println("The bottom card you choose must be no greater than the amount of cards in the stack " +
                "and must not be a hidden card");
        System.out.print("What is the number associated with the bottom card of the set of cards you wish to move? ");
        int userBottom = scan.nextInt();
        //user chose a number greater than the length of the tableau stack OR the card the user chose was hidden
        if (userBottom > stackFrom.getStack().size()){
            actionStatement = "You chose a number greater than the length of the stack";
        } else if (!stackFrom.getBottomCardOfSet(userBottom).isVisible()){
            actionStatement = "The card you have chosen cannot be moved because it is still hidden.";
        } else {
            Card cardFrom = stackFrom.getBottomCardOfSet(userBottom);
            ArrayList<Card> cardsToMove = stackFrom.getGroupToMove(userBottom);
            boolean moved = false;
            actionStatement = "The set of cards with the bottom card of " + cardFrom.cardInfo();


            for (int i = 0; i < tableau.length; i ++){
                TableauStack tableauStack = tableau[i];
                if (tableauStack.addMultipleCards(cardsToMove)){
                    stackFrom.removeMultipleCards(userBottom);
                    moved = true;
                    tableauStack.displayStack();
                    actionStatement += "was moved to tableau stack #" + (i + 1);
                    break;
                }
            }


            if (!moved){
                actionStatement += " was unable to be moved. Try another card";
            }
        }

        System.out.println(actionStatement);
    }

    public ArrayList<Card> getStock(){
        return stock;
    }

    public void updateRectsOutside(){
        gameComponent.updateRectangleObjects(stockRect, wasteRect, tableauRectangles, foundationRectangles);
    }

}
