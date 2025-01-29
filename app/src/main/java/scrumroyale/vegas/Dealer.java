package scrumroyale.vegas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import scrumroyale.RNG;

/**
 * Dealer class.
 */
public class Dealer {

    // chose linked list to improve pulling from deck speed vs arraylist

    // The {@code LinkedList} deck that holds the cards.
    private LinkedList<Integer> deck = new LinkedList<Integer>();

    // The Random Number generator instance.
    private RNG random = RNG.getInstance();

    /**
     * Creates a deck of 90 cards with payout values from 3-10.
     *
     */
    public Dealer() {
        int numOfCards = 0;
        for (int cardValue = 3; cardValue <= 11; cardValue++) {
            for (int cardNum = 0; cardNum < numOfCards; cardNum++) {
                deck.add(cardValue - 1);
            }

            switch (cardValue) {
                case 3:
                    numOfCards = 11;
                    break;
                case 4:
                    numOfCards = 11;
                    break;
                case 5:
                    numOfCards = 13;
                    break;
                case 6:
                    numOfCards = 15;
                    break;
                case 7:
                    numOfCards = 13;
                    break;
                case 8:
                    numOfCards = 11;
                    break;
                case 9:
                    numOfCards = 9;
                    break;
                case 10:
                    numOfCards = 7;
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Gets the current size of the deck.
     * 
     * @return The current size of the Deck.
     */
    private int getDeckSize() {
        return deck.size();
    }

    /**
     * Pulls a set of cards from the deck and sorts them in descending order
     * also attaches the sets total value at the end of the list after being sorted.
     * 
     * @param set the size of the cards pulled.
     * @return A {@code List} of the cards pulled.
     */
    public ArrayList<Integer> pullCards(int set) {
        int setTotal = 0;
        ArrayList<Integer> setBuff = new ArrayList<Integer>();
        int randomCard = 0;
        int listIndex = 0;
        // pulls cards from deck adds them to the setbuff, adds card value to set total.
        for (int setIndex = 0; setIndex < set; setIndex++) {
            listIndex = random.nextInt(getDeckSize());
            randomCard = deck.get(listIndex);
            deck.remove(listIndex);
            setBuff.add(randomCard);
            setTotal += randomCard;
        }
        // no need to sort buff in descending order if theres just one.
        if (setBuff.size() > 1) {
            setBuff.sort(Comparator.reverseOrder());
        }
        // add total to the end.
        setBuff.add(setTotal);
        return setBuff;
    }

    /**
     * Sorts the card buffer in order.
     * 
     * @param cardBuff The card buffer to sort.
     * @return The sorted card buffer.
     */
    private List<Integer> mergeSort(Map<Integer, List<Integer>> cardBuff) {
        List<Integer> doneBuff = new ArrayList<Integer>();
        List<Integer> setValues = new ArrayList<Integer>(cardBuff.keySet().stream().toList());
        setValues.sort(Comparator.reverseOrder());
        for (int i = 0; i < setValues.size(); i++) {
            doneBuff.addAll(cardBuff.get(setValues.get(i)));
        }
        return doneBuff;
    }

    /**
     * Deals out a set of cards based on parameters passed in.
     *
     * @param numOfCards Takes an integer that specifies the number of cards being
     *                   dealt.
     * @param set        Takes an integer that specifies how many cards should be in
     *                   each group.
     * @return returns a List of integers in descending order with the lowest
     *         index holding the highest payout group. Ties are broken between
     *         groups by highest individual payout card among the groups.
     */
    public Map<Integer, List<Integer>> dealToVegas(int numOfCards, int set) {
        // uncomment if needed to help debug code
        /*
         * if (numOfCards <= 0 || set <= 0 || numOfCards % set != 0) {
         * throw new IllegalArgumentException(
         * "Both integers must be greater than 0 and the first parameter must be evenly divisible by the second."
         * );
         * }
         */
        // map to organize the cards being sent base on highest combination (float)to
        // get rid of spot bug error load capacity should be set to highest possible set
        // combos which is numOfCards,set
        Map<Integer, List<Integer>> cardBuff = new HashMap<>(numOfCards / set, numOfCards / (float) set);
        ArrayList<Integer> setBuff;
        // total of set buff to be added map
        int setTotal;
        // int(numOfCards/set) times
        for (int indexNumofCards = 0; indexNumofCards < numOfCards; indexNumofCards = set + indexNumofCards) {
            // sets the local setBuff to the array returned from pullcards
            setBuff = new ArrayList<Integer>(pullCards(set));
            // gets settotal from the end of the list
            setTotal = setBuff.get(setBuff.size() - 1);
            // removes setotal from setbuff array
            setBuff.remove(setBuff.size() - 1);
            // adds set total the key and the card combo making the set total the values
            if (cardBuff.containsKey(setTotal) == false) {
                cardBuff.put(setTotal, setBuff);
            } else {
                // find highest card between sets, the highest gets placed at a lower index in
                // the end
                if (Collections.max(cardBuff.get(setTotal)) >= Collections.max(setBuff)) {
                    cardBuff.get(setTotal).addAll(setBuff);
                } else {
                    setBuff.addAll(cardBuff.get(setTotal));
                    cardBuff.put(setTotal, setBuff);
                }
            }
        }
        // puts the returned list from merge sort into a 2d array for Vegas to use
        List<Integer> merged = new ArrayList<>(mergeSort(cardBuff));
        Map<Integer, List<Integer>> cardsMap = new HashMap<>(6);
        for (int i = 0; i < numOfCards / set; i++) {
            cardsMap.put(i, new ArrayList<>(2));
            for (int j = 0; j < set; j++) {
                cardsMap.get(i).add(merged.get((2 * i) + j));
            }
        }
        return cardsMap;
    }

}
