package com.aaroncarsonart.tarotrl.deck;

import com.aaroncarsonart.tarotrl.util.Globals;
import com.aaroncarsonart.tarotrl.util.JsonUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Stack;
import java.util.stream.IntStream;

// TODO possibly refactor to use TarotCardItems instead of TarotCards.
// TODO or create a new TarotCardItemDeck class to handle deck
// TODO functionalities for TarotCardItems.

public class TarotDeck {
    private String name;
    private List<String> types;
    private List<Suit> suits;

    /**
     * Note: the 0th origin is the top card.
     */
    private List<TarotCard> cards;
    private int[] ordering;

    public int cardCount() {
        return cards.size();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public List<Suit> getSuits() {
        return suits;
    }

    public void setSuits(List<Suit> suits) {
        this.suits = suits;
    }

    public List<TarotCard> getCards() {
        return cards;
    }

    public void setCards(List<TarotCard> cards) {
        this.cards = cards;
    }

    public int[] getOrdering() {
        return ordering;
    }

    public void setOrdering(int[] ordering) {
        this.ordering = ordering;
    }

    public void initOrdering() {
        ordering = IntStream.rangeClosed(0, cards.size()).toArray();
    }

    /**
     * Emulate a riffle shuffle algorithmically.
     */
    public void riffleShuffle() {
        Deque<TarotCard> left = new ArrayDeque<>(cards.subList(0, cards.size() / 2));
        Deque<TarotCard> right = new ArrayDeque<>(cards.subList(cards.size() / 2, cards.size()));
        Stack<TarotCard> finalShuffle = new Stack<>();

        int accuracy = 1 + Globals.RANDOM.nextInt(5);
        while (finalShuffle.size() != cards.size()) {
            Deque<TarotCard> nLeft = drawFromBottom(left, Globals.RANDOM.nextInt(accuracy));
            Deque<TarotCard> nRight = drawFromBottom(right, Globals.RANDOM.nextInt(accuracy));

            boolean leftFirst = Globals.RANDOM.nextBoolean();
            if (leftFirst) {

            }
        }
    }

    /**
     * Draw n cards from the top of the deck, preserving ordering.
     * (The top card of the deck will be the top card of the returned list.)
     *
     * @param deck The deck to draw from.  This list will be modified if n > 0.
     * @param n The number of cards to draw.
     * @return A list of cards drawn from the top of the deck.
     */
    private Deque<TarotCard> draw(Deque<TarotCard> deck, int n) {
        if (n < 1) {
            return deck;
        }
        Deque<TarotCard> draws = new ArrayDeque();
        for (int i = 0; i < n; ++i) {
            TarotCard card = deck.removeFirst();
            draws.addLast(card);
        }
        return draws;
    }

    /**
     * Draw n cards from the bottom of the deck, preserving ordering.
     * (The bottom card of the deck will be the bottom card of the returned list.)
     *
     * @param deck The deck to draw from.  This list will be modified if n > 0.
     * @param n The number of cards to draw.
     * @return A list of cards drawn from the bottom of the deck.
     */
    private Deque<TarotCard> drawFromBottom(Deque<TarotCard> deck, int n) {
        if (n < 1) {
            return deck;
        }
        Deque<TarotCard> draws = new ArrayDeque();
        for (int i = 0; i < n; ++i) {
            TarotCard card = deck.removeLast();
            draws.addFirst(card);
        }
        return draws;
    }

    @Override
    public String toString() {
        return JsonUtils.writeValueAsString(this);
    }

    public List<TarotCard> getSetOfCards() {
        return new ArrayList<>(cards);
    }

}
