import java.util.ArrayList;

public class TableauStack {

    private ArrayList<Card> stack;

    public TableauStack(ArrayList<Card> stackToAdd){
        stack = stackToAdd;
    }

    public ArrayList<Card> getStack(){
        return stack;
    }


    public boolean addOneCard(Card newCard){
        //if stack is currently empty, can only add a card of value King
        if (stack.size() == 0){
            if (newCard.getValue().equals("K")){
                stack.add(newCard);
                return true;
            } else {
                return false;
            }
        }

        Card cardBefore = stack.get(stack.size() - 1);
        //can only add a card of 1 value less than preceding card
        if (newCard.getNumbericalValue() == cardBefore.getNumbericalValue() - 1){
            //can only add card with opposite color of the preceding card
            if (!(newCard.getColor().equals(cardBefore.getColor()))){
                stack.add(newCard);
                return true;
            }
        }

        return false;
    }

    public Card removeTopCard(){
        Card resultingCard = stack.remove(stack.size() - 1);
        if (stack.size() != 0 && !getTopCard().isVisible()){
            getTopCard().setVisible(true);
        }
        return resultingCard;
    }

    public void removeMultipleCards(int locFromTop){
        int startFrom = stack.size() - locFromTop;
        while (startFrom < stack.size()){
            stack.remove(startFrom);
        }
        if (stack.size() != 0 && !getTopCard().isVisible()){
            getTopCard().flip();
        }
    }

    public Card getTopCard(){
        return stack.get(stack.size() - 1);
    }

    /**
     * Checks and adds a set of multiple cards to a new stack of the tableau
     *
     * @param cardsToAdd an ArrayList of cards to be moved
     *
     * @return if you can move the set of cards to the new stack within the tableau
     */
    public boolean addMultipleCards(ArrayList<Card> cardsToAdd){
        //the card that will be used to determine if the multiple cards can be moved to a new Tableau stack
        Card mainCard = cardsToAdd.get(0);
        if (addOneCard(mainCard)){
            cardsToAdd.remove(0);
            stack.addAll(cardsToAdd);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Creates and returns a new ArrayList of a group of Cards from the top of the stack to be moved
     *
     * @param locFromTop the number from which you want to pick group of cards to move |
     *                   1 signifies top card, 2 signifies second card from top, etc |
     *                   precondition: 0 < locFromTop <= number of cards in that stack
     *
     * @return an ArrayList of Cards to move as a set starting with the card as locFromTop
     */
    public ArrayList<Card> getGroupToMove(int locFromTop){
        ArrayList<Card> toReturn = new ArrayList<>();
        for (int i = stack.size() - locFromTop; i < stack.size(); i ++){
            toReturn.add(stack.get(i));
        }
        return toReturn;
    }

    public String displayStack(){
        String toDisplay = "";
        for (Card card : stack){
            if (card.isVisible()){
                toDisplay += card.cardInfo() + " ";
            } else {
                toDisplay += "XX  ";
            }
        }
        return toDisplay;
    }

    public Card getBottomCardOfSet(int locFromTop){
        return stack.get(stack.size() - locFromTop);
    }
}
