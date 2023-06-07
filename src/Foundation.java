import java.util.ArrayList;

public class Foundation {
    private final String SUIT;
    private ArrayList<Card> foundationStack;

    public Foundation(String suit){
        this.SUIT = suit;
        foundationStack = new ArrayList<>();
    }

    public String getSuit(){
        return SUIT;
    }

    public ArrayList<Card> getFoundationStack(){
        return foundationStack;
    }

    /**
     * Checks if the foundation stack has filled up with all of its cards.
     *
     * @return if the foundation stack has 13 cards
     */
    public boolean foundationCompleted(){
        return foundationStack.size() == 13;
    }


    /**
     * Attempts to add a card to the foundation stack.
     *
     * If the card is of the same suit and of greater value than of the preceding card,
     * it can be added to the foundation and the method returns true.
     *
     * If the card does not meet the conditions, return false.
     *
     * @param newCard The card object that is to be added at the top of the foundation stack.
     * @return if the attempt to add the card to foundation was successful
     */
    public boolean addCard(Card newCard){
        //you can't add a card to a foundation stack if it isn't of the same suit
        if (!newCard.getSuit().equals(SUIT)){
            return false;
        }

        //when foundation is currently empty, can only add a card of value Ace
        if (foundationStack.size() == 0) {
            if (newCard.getValue().equals("A")) {
                foundationStack.add(newCard);
                return true;
            } else {
                return false;
            }
        }

        //card to be added must have a value of one greater than the value before it
        if (newCard.getNumbericalValue() == foundationStack.get(foundationStack.size() - 1).getNumbericalValue() + 1){
            foundationStack.add(newCard);
            return true;
        }
        return false;
    }


}
