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

    public void setVisible(boolean newVisible){
        visible = newVisible;
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

    public String getCardName(){
        String name = "";
        if (value.equals("A")){
            name += "ace of ";
        } else if (value.equals("J")){
            name += "jack of ";
        } else if (value.equals("Q")){
            name += "queen of ";
        } else if (value.equals("K")){
            name += "king of ";
        } else {
            name += "" + value + " of ";
        }

        if (suit.equals("d")){
            name += "diamonds";
        } else if (suit.equals("h")){
            name += "hearts";
        } else if (suit.equals("S")){
            name += "spades";
        } else {
            name += "clubs";
        }
        return name;
    }

    public String getColor(){
        if (suit.equals("d") || suit.equals("h")){
            return "red";
        }
        return "black";
    }

    public String getImageName(){
        String val = "";
        if (value.equals("A")){
            val = "ace";
        } else if (value.equals("J")){
            val = "jack";
        } else if (value.equals("Q")){
            val = "queen";
        } else if (value.equals("K")){
            val = "king";
        } else {
            val = "" + value;
        }

        String suitOfCard = "";
        if (suit.equals("d")){
            suitOfCard = "diamonds";
        } else if (suit.equals("h")){
            suitOfCard = "hearts";
        } else if (suit.equals("C")){
            suitOfCard = "clubs";
        } else {
            suitOfCard = "spades";
        }

        return val + "_of_" + suitOfCard;
    }
}
