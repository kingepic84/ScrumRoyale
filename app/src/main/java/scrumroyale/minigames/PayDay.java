package scrumroyale.minigames;

import java.util.List;
import java.util.Map;

import scrumroyale.Minigame;
import scrumroyale.Player;
import scrumroyale.UI;
import scrumroyale.vegas.Casino;
import scrumroyale.vegas.GameState;
import scrumroyale.vegas.GameState.Minishot;
import scrumroyale.vegas.Vegas.Effect;

/** Pay Day class. */
public class PayDay extends Minigame {

    /** Info String. */
    private String info;

    /**
     * Payday constructor. Takes in a casino integer input and calls its superclass
     * constructor.
     */
    public PayDay() {
        super("Pay Day", "Gain $1 for each casino you have presence on.", Effect.ACTIVATED);
    }

    /**
     * Goes through the list of casinos in the game and uses the get dice function
     * to count the amount of current player presences on casinos
     * adds amount of presences to the players balance and uses minishot to print to
     * console.
     * 
     * @param casinos The List of Casinos.
     * @param players The List of Players in the game.
     * @param player  Integer representing the current player who is playing the
     *                minigame.
     * @param console ConsoleUI Instance.
     */
    @Override
    public void execute(List<Casino> casinos, List<Player> players, int player, UI console, int casino) {
        int presence_count = 0;
        for (Casino cas : casinos) {
            Map<Integer, Integer> dice = cas.getDice();
            if (dice != null && dice.get(player) != null && dice.get(player) > 0) {
                presence_count += 1;
            }
        }
        players.get(player - 1).addToBalance(presence_count);
        info = "P" + player + " has presence at " + presence_count + " casinos";
        String payoutMessage = " (has presence at " + presence_count + " casinos)";
        Minishot minishotUpdate = GameState.takeMinishot(casinos, casino, players, this, info, player, false,
                presence_count);
        Minishot minishotPayout = GameState.takeMinishot(casinos, casino, players, this, payoutMessage, player, false,
                presence_count);
        console.displayMinigameInfo("minigame payout", minishotPayout);
        console.displayMinigameInfo("minigame update", minishotUpdate);
    }
}
