package scrumroyale.vegas;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import scrumroyale.Dice;
import scrumroyale.Die;

/**
 * Casino Class.
 */
public class Casino {

    // The integer array of usually size 2 that holds the payouts for each casino.
    private List<Integer> payouts;

    // The dice on this casino.
    private Dice dice;

    // Record of who has what dice on the Casino.
    private Map<Integer, Dice> diceOnMe;

    /**
     * Casino constructor.
     * 
     * @param payouts The payouts that will be placed on this casino. Ordering is
     *                [High, Low].
     */
    public Casino(List<Integer> payouts) {
        this.payouts = payouts.stream().toList();
        dice = new Dice();
        diceOnMe = new HashMap<Integer, Dice>();
    }

    /**
     * Adds a set of dice onto this casino.
     * 
     * @param dice The dice to add.
     */
    public void addDice(Dice dice) {
        Dice diceToAdd = new Dice();
        dice.getValues().forEach((val) -> {
            if (val.contains("+")) {
                Die d = new Die(true, dice.get(0).getPlayer());
                d.setValue(val);
                diceToAdd.addDice(d);
            }
            Die die = new Die(true, dice.get(0).getPlayer());
            die.setValue(val);
            diceToAdd.addDice(die);
        });
        addToRecord(dice.get(0).getPlayer(), diceToAdd);
        this.dice.addDice(dice);
    }

    /**
     * Adds a single die to this casino.
     * 
     * @param die The die to add.
     */
    public void addDice(Die die) {
        Dice record = new Dice();
        if (die.isBig()) {
            record.addDice(die);
        }
        record.addDice(die);
        addToRecord(die.getPlayer(), record);
        dice.addDice(die);
    }

    /**
     * Gets the payouts on this casino.
     * 
     * @return the payouts.
     */
    public List<Integer> getPayouts() {
        return payouts.stream().toList();
    }

    /**
     * Sets the payouts on this casino.
     * 
     * @param payouts the payouts for this casino.
     */
    public void setPayouts(List<Integer> payouts) {
        this.payouts = payouts.stream().toList();
    }

    /**
     * Issue the payouts to the players at the end of a round.
     */
    public void resetDice() {
        dice = new Dice();
        diceOnMe.clear();
    }

    /**
     * Processes the {@code Dice} on this {@code Casino} at the end of the round
     * and determines which players get the payouts, this accounts for ties as well,
     * cancelling them out as necessary.
     * 
     * <p>
     * For example, a return value could be
     * {@code {0:[1,7,5], 1:[2,3,4], 2:[3,0,2], 3:[4,0,2]}}, where each element
     * in the returned array is a 1D array that holds the player, their payout,
     * and the number of dice they have on this casino, in that order.
     * 
     * 
     * @return A {@code Map}&lt;{@code Integer},
     *         {@code List}&lt;{@code Integer}&gt;&gt; containing the players who
     *         get payouts, along with their current dice on the casino in
     *         descending order of payouts.
     *
     */
    public Map<Integer, List<Integer>> getPlayerPayouts() {
        AtomicInteger idx = new AtomicInteger(0);
        return diceOnMe.entrySet().stream().collect(Collectors.groupingBy(entry -> entry.getValue().size(),
                Collectors.mapping(Map.Entry::getKey, Collectors.toList()))).entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .map(value -> Arrays.asList(
                                (entry.getValue().size() > 1) ? Integer.valueOf(entry.getKey() * -1) : entry.getKey(),
                                value)))
                .sorted(Comparator.<List<Integer>>comparingInt(list -> list.get(0)).reversed())
                .map(entry -> Arrays.asList(entry.get(1),
                        (idx.get() < 2 && entry.get(0) > 0) ? payouts.get(idx.getAndIncrement()) : 0,
                        Math.abs(entry.get(0))))
                .collect(Collectors.collectingAndThen(Collectors.toList(), (m) -> {
                    idx.set(0);
                    return m;
                }))
                .stream().collect(Collectors.toMap(key -> idx.getAndIncrement(), value -> value));
    }

    /**
     * Gets all the dice placed by all the players on this casino.
     * 
     * @return A {@code Map} where the {@code Player} is the key, and the value is
     *         how many dice they have on that casino.
     */
    public Map<Integer, Integer> getDice() {
        HashMap<Integer, Integer> diceMap = new HashMap<>();
        diceOnMe.forEach((key, value) -> {
            if (key != 0) {
                diceMap.put(key, value.size());
            }
        });
        return diceMap;
    }

    /**
     * Takes in a player and {@code Dice} object and adds it to the record of
     * players who currently have their dice on this casino.
     * 
     * @param player    The player (an an {@code int}) to use as the key.
     * @param diceToAdd The Dice object to add to them.
     */
    private void addToRecord(int player, Dice diceToAdd) {
        if (!diceOnMe.containsKey(player)) {
            diceOnMe.put(player, new Dice());
        }
        diceOnMe.get(player).addDice(diceToAdd);
    }
}
