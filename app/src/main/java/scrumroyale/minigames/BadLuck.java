package scrumroyale.minigames;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import scrumroyale.Minigame;
import scrumroyale.Player;
import scrumroyale.UI;
import scrumroyale.vegas.Casino;
import scrumroyale.vegas.GameState;
import scrumroyale.vegas.Vegas.Effect;

/** Bad Luck class. */
public class BadLuck extends Minigame {

    /**
     * The Bad Luck constructor.
     */
    public BadLuck() {
        super("Bad Luck", "Each player with the least dice on this casino pays $5 to the bank at round end.",
                Effect.BEFORE_PAYOUT);
    }

    @Override
    public void execute(List<Casino> casinos, List<Player> players, int player, UI console, int casino) {
        Map<Integer, Integer> playerDice = casinos.get(casino - 1).getDice();
        List<Integer> playersInDebt = players.stream().map(((e) -> e.getPlayer())).collect(Collectors.toList());
        if (playerDice.size() < players.size()) {
            playersInDebt.removeAll(playerDice.keySet().stream().toList());
        } else {
            int min = Collections.min(playerDice.values());
            playersInDebt.removeAll(playerDice.values().stream().filter((entry) -> entry == min).toList());
        }
        for (Player p : players) {
            if (playersInDebt.contains(p.getPlayer())) {
                if (p.getBalance() >= 5) {
                    p.removeFromBalance(5);
                    console.displayMinigameInfo("minigame loss",
                            GameState.takeMinishot(casinos, casino, players, this, "", p.getPlayer(), false, 5));
                } else {
                    int balLeft = 5 - p.getBalance();
                    p.removeFromBalance(p.getBalance());
                    if (p.getBalance() != 0) {
                        console.displayMinigameInfo("minigame loss", GameState.takeMinishot(casinos, casino, players,
                                this, "", p.getPlayer(), false, p.getBalance()));
                    }
                    if (p.getChips() >= balLeft) {
                        for (int i = 0; i <= balLeft; i++) {
                            p.expendChip();
                        }
                        console.displayMinigameInfo("minigame loss", GameState.takeMinishot(casinos, casino, players,
                                this, "", p.getPlayer(), true, balLeft));
                    } else {
                        if (p.getChips() != 0) {
                            console.displayMinigameInfo("minigame loss", GameState.takeMinishot(casinos, casino,
                                    players, this, "", p.getPlayer(), true, p.getChips()));
                        }
                        for (int i = 0; i <= p.getChips(); i++) {
                            p.expendChip();
                        }
                    }
                }
            }
        }
    }
}
