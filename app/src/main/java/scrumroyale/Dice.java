package scrumroyale;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dice Class for creating a set of dice.
 */
public class Dice {

    // The set of {@link Dice} using {@link List}.
    private List<Die> dice;

    /**
     * Creates an empty {@link List} of Dice to use with any of the Base game or any
     * of the minigames.
     */
    public Dice() {
        dice = new ArrayList<Die>();
    }

    /**
     * Creates a new {@link List} of Dice of size numDice to use with any of the
     * Base game or any of the minigames.
     * 
     * @param numDice  The number of dice to be created.
     * @param largeDie Whether this set of dice includes a large {@link Die} or not.
     * @param player   The player this set of dice belongs to.
     */
    public Dice(int numDice, boolean largeDie, int player) {
        dice = new ArrayList<Die>();
        numDice = largeDie ? numDice - 1 : numDice;
        for (int i = 0; i < numDice; i++)
            dice.add(new Die(false, player));
        if (largeDie)
            dice.add(new Die(true, player));
    }

    /**
     * Adds a die to the number of dice currently in the dice set
     * 
     * @param die The {@link Die} to add to the current {@link Dice} set
     */
    public void addDice(Die die) {
        dice.add(die);
    }

    /**
     * Adds a set of dice to the current dice set
     * 
     * @param diceToAdd the {@link Dice} to add to the current dice set
     */
    public void addDice(Dice diceToAdd) {
        dice.addAll(diceToAdd.getDice());
    }

    /**
     * Rolls all the dice in the current set of dice.
     */
    public void rollDice() {
        dice.forEach((die) -> {
            die.roll();
        });
    }

    /**
     * Removes all the dice matching the face passed in.
     * 
     * @param numToRemove the die face to remove the set of dice.
     * @return the {@link Dice} that were removed from the dice set.
     */
    public Dice removeDice(int numToRemove) {
        Dice diceRemoved = new Dice();
        dice.forEach((die) -> {
            if (die.value().substring(0, 1).equals(String.valueOf(numToRemove))) {
                diceRemoved.addDice(die);
            }
        });
        dice.removeIf(die -> (die.value().substring(0, 1).equals(String.valueOf(numToRemove))));
        return diceRemoved;
    }

    /**
     * Size Function.
     * 
     * @return The size of the dice set.
     */
    public int size() {
        return dice.size();
    }

    /**
     * Returns a string representation of the object.
     * 
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        List<String> results = new ArrayList<String>();
        dice.forEach((die) -> {
            results.add(die.value());
        });
        return results.toString();
    }

    /**
     * Gets the die face with the most occurences.
     * 
     * @return the die face that occurs the most within the dice set.
     */
    public String getMost() {
        return getDiceCounts().entrySet().stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(),
                        Integer.valueOf(entry.getValue().substring(0, 1))))
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(entry -> String.valueOf(entry.getKey())).toList().get(0);
    }

    /**
     * Provides a deep copy of the internal {@code Dice} list.
     * 
     * @return a new {@code Dice} object functionally the same as but different in
     *         origin to the internal {@code Dice} object.
     */
    public Dice deepCopy() {
        Dice d = new Dice();
        List<Die> tmpDice = getDice();
        for (int i = 0; i < tmpDice.size(); i++) {
            d.addDice(tmpDice.get(i));
        }
        return d;
    }

    /**
     * Gets and returns all the current values on the dice.
     * 
     * @return An {@code ArrayList} of strings containing the dies' current values.
     */
    public List<String> getValues() {
        List<String> values = new ArrayList<String>();
        for (Die die : dice) {
            values.add(die.value());
        }
        return values;
    }

    /**
     * Gets the {@code Die} at a specified index.
     * 
     * @param index The index of which {@code Die} to fetch.
     * @return The specified {@code Die}.
     */
    public Die get(int index) {
        Die d = new Die(dice.get(index).isBig(), dice.get(index).getPlayer());
        d.setValue(dice.get(index).value());
        return d;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dice.isEmpty()) ? 0 : dice.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Dice other = (Dice) obj;
        if (dice.isEmpty()) {
            if (!other.dice.isEmpty()) {
                return false;
            }
        } else if (!dice.equals(other.dice))
            return false;
        return true;
    }

    /**
     * Gets the counts of each dice face.
     * 
     * @return a {@code Map} of type &lt;{@code Integer}, {@code String}&gt;
     */
    public Map<Integer, String> getDiceCounts() {
        Map<Integer, String> diceMap = new HashMap<Integer, String>();
        try {
            getValues().stream().distinct().toList().forEach((elem) -> {
                diceMap.put(Integer.valueOf(elem.substring(0, 1)), countString(elem.substring(0, 1)));
            });
        } catch (NumberFormatException e) {
        }
        return diceMap;
    }

    /**
     * Private function to count the number of instances of each die in the set of
     * dice.
     * 
     * @param toCount The die face to count.
     * @return the amount of dice with that face in the current die set as a String.
     */

    private String countString(String toCount) {
        int counted = 0;
        String starred = "";
        for (String die : getValues()) {
            if (die.equals(toCount + "+" + toCount)) {
                counted += 2;
                starred = "*";
            } else if (die.equals(toCount)) {
                counted++;
            }
        }
        return String.valueOf(counted) + starred;
    }

    /**
     * Returns the current set of {@link Dice}.
     * 
     * @return The current dice set.
     */
    private List<Die> getDice() {
        List<Die> tmpDice = new ArrayList<Die>();
        dice.forEach((die) -> {
            tmpDice.add(die);
        });
        return tmpDice;
    }
}