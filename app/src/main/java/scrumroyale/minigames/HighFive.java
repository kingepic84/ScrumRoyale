package scrumroyale.minigames;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import scrumroyale.Minigame;
import scrumroyale.Player;
import scrumroyale.UI;
import scrumroyale.vegas.Casino;
import scrumroyale.vegas.GameState;
import scrumroyale.vegas.Vegas.Effect;

/** High Five class. */
public class HighFive extends Minigame {

    private boolean claimed;

    /**
     * The High Five constructor.
     */
    public HighFive() {
        super("High Five", "The first player to place at least 5 dice here wins $10.", Effect.TRANSITORY);
        claimed = false;
    }

    @Override
    public void execute(List<Casino> casinos, List<Player> players, int player, UI console, int casino) {
        int most = Collections.max(casinos.get(casino - 1).getPlayerPayouts().entrySet().stream()
                .map(entry -> entry.getValue().get(2)).collect(Collectors.toList()));
        if (most >= 5 && !claimed) {
            players.get(player - 1).addToBalance(10);
            console.displayMinigameInfo("minigame payout",
                    GameState.takeMinishot(casinos, casino, players, this, "", player, false, 10));
            console.displayMinigameInfo("minigame update", GameState.takeMinishot(casinos, casino, players, this,
                    "P" + player + " has claimed $10", player, false, 10));
            claimed = true;
            this.description = "The payout has already been claimed.";
        }
    }

}
