package com.aaroncarsonart.tarotrl.deck;

public class TarotCard extends Keywordable {
    private int order;
    private int rank;
    private String suit;
    private String name;
    private String type;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDisplayName() {
        if (suit != null) {
            return getDisplayRank() + " of " + getSuit();
        } else {
            return getRank() + ". " + getName();
        }
    }

    public String getShorthandName() {
        if (suit != null) {
            String displayRank = getDisplayRank();
            if (displayRank.equals("10")) {
                displayRank = "X";
            } else if (displayRank.equals("Page")) {
                displayRank = "p";
            } else if (displayRank.equals("Knight")) {
                displayRank = "k";
            } else {
                displayRank = displayRank.substring(0,1);
            }
            String displaySuit = suit.substring(0,1);
            return displayRank + displaySuit;
        } else {
            return String.format("%2s", Integer.toString(rank));
        }
    }


    private String getDisplayRank() {
        String displayRank;
        switch (rank) {
            case 11: displayRank = "Page"; break;
            case 12: displayRank = "Knight"; break;
            case 13: displayRank = "Queen"; break;
            case 14: displayRank = "King"; break;
            default: displayRank = Integer.toString(getRank()); break;
        }
        return displayRank;
    }

    public TarotCardType getTarotCardType() {
        String shorthandName = getShorthandName();
        return TarotCardType.fromShorthandName(shorthandName);
    }

    @Override
    public String toString() {
        //return getShorthandName();
        return getDisplayName();
    }
}
