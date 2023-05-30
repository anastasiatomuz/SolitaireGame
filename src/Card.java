public class Card {
    private String suit;
    private String value;
    private boolean visible;

    /**
     * Creates a Card object to hold the information for each of the 52 cards
     *
     * @param suit can be: ♦ (diamond), ♥ (heart), ♣ (club), ♠ (spades)
     * @param value can be numerical values 2 - 10, or A(ce), J(ack), Q(ueen), K(ing)
     * @param visible if the user can access the information of the card
     */
    public Card(String suit, String value, boolean visible){
        this.suit = suit;//
        this.value = value;
        this.visible = visible;
    }

    public String getSuit(){
        return suit;
    }

    public String getValue(){
        return value;
    }

    public void flip(){
        visible = !visible;
    }

    public boolean isVisible(){
        return visible;
    }

    public int getNumbericalValue(){
        if (value.equals("A")){
            return 1;
        }
        if (value.equals("J")){
            return 11;
        }
        if (value.equals("Q")){
            return 12;
        }
        if (value.equals("K")){
            return 13;
        }
        return Integer.parseInt(value);
    }

    public String cardInfo(){
        return getValue() + getSuit();
    }

    public String getColor(){
        if (suit.equals("d") || suit.equals("h")){
            return "red";
        }
        return "black";
    }
}
