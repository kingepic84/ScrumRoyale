package scrumroyale.minigames;

import java.util.List;

import scrumroyale.Dice;
import scrumroyale.Die;
import scrumroyale.Minigame;
import scrumroyale.Player;
import scrumroyale.UI;
import scrumroyale.vegas.Casino;
import scrumroyale.vegas.GameState;
import scrumroyale.vegas.GameState.Minishot;
import scrumroyale.vegas.Vegas.Effect;

/**
 * Jackpot class. Extends Minigame class to create the Jackpot minigame.
 */
public class Jackpot extends Minigame {

    /** Dice Instance. */
    private Dice dice;

    /** Minigame info string. */
    private String info;

    /** Initial payout. */
    private int initialJackpot = 3;

    /** Max jackpot payout. */
    private int maxJackpot = 8;

    /** Current payout. */
    private int payout = initialJackpot;

    /**
     * Jackpot constructor. Takes in a casino integer input and calls its superclass
     * constructor.
     */
    public Jackpot() {
        super("Jackpot", "Roll doubles or 7 to win $3.", Effect.ACTIVATED);
        payout = initialJackpot;
        dice = new Dice();
        // initializing to player 1 because player is neglegible
        dice.addDice(new Die(false, 1));
        dice.addDice(new Die(false, 1));
    }

    /**
     * Updates the description based on the current jackpot value.
     */
    private void updateDescription() {
        description = "Roll doubles or 7 to win $" + payout + ".";
    }

    /**
     * Method to advance the jackpot amount, up to the maximum limit.
     *
     */
    private void advanceJackpotAmount() {
        if (payout < maxJackpot) {
            payout++;
        }
    }

    /**
     * Executes the Jackpot minigame for the current player.
     * 
     * @param casinos       List of casinos.
     * @param players       List of players.
     * @param currentPlayer Current player.
     * @param console       Console UI instance.
     */
    @Override
    public void execute(List<Casino> casinos, List<Player> players, int currentPlayer, UI console, int casino) {
        Player minigamePlayer = players.get(currentPlayer - 1);
        dice.rollDice();
        List<String> diceValues = dice.getValues();
        int die1 = Integer.parseInt(diceValues.get(0));
        int die2 = Integer.parseInt(diceValues.get(1));
        int sum = die1 + die2;

        if (sum == 7 || die1 == die2) {
            info = dice.toString() + " was rolled, and payout was $" + payout + ". Potential payout is $3";
            minigamePlayer.addToBalance(payout);
            String payoutMessage = " (" + dice.toString() + " -> $" + payout + ")";
            Minishot minishotPayout = GameState.takeMinishot(casinos, casino, players, this, payoutMessage,
                    currentPlayer, false, payout);
            console.displayMinigameInfo("minigame payout", minishotPayout);
            payout = initialJackpot;
        } else {
            advanceJackpotAmount();
            info = dice.toString() + " was rolled. Potential payout is now $" + payout;
            Minishot minishotUpdate = GameState.takeMinishot(casinos, casino, players, this, info, currentPlayer, false,
                    payout);
            console.displayMinigameInfo("minigame update", minishotUpdate);
        }
        updateDescription();
    }
}
