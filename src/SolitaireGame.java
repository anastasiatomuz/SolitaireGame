import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class SolitaireGame {
    private TableauStack[] tableau;
    private Foundation[] foundations;
    private ArrayList<Card> stock;
    private ArrayList<Card> waste;
    private Card topWasteCard;
    private Scanner scan;



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
            moveOneCard();
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

    public void moveOneCard(){
        System.out.println("Tableau stacks are numbered 1 - 7 from top down");
        System.out.print("From which stack of the tableau do you want to choose? ");
        int userMove = scan.nextInt();
        TableauStack stackFrom = tableau[userMove - 1];
        Card cardFrom = stackFrom.getTopCard();
        boolean moved = false;
        String actionStatement = cardFrom.cardInfo();
        for (Foundation foundation : foundations){
            if (foundation.addCard(cardFrom)){
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
                    stackFrom.removeTopCard();
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
        System.out.println(actionStatement);
    }

    public void moveCardFromWaste(){
        boolean moved = false;
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
        System.out.println(actionStatement);
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


}
