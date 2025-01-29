package scrumroyale.minigames;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import scrumroyale.Minigame;
import scrumroyale.Player;
import scrumroyale.UI;
import scrumroyale.vegas.Casino;
import scrumroyale.vegas.GameState;
import scrumroyale.vegas.GameState.Minishot;
import scrumroyale.vegas.Vegas.Effect;

/**
 * Black Box class.
 */
public class BlackBox extends Minigame {

    /** Token List. */
    private List<Integer> tokens = Arrays.asList(0, 0, 0, 2, 2, 6);

    /** First Pile. */
    private List<Integer> pileOne;

    /** Second Pile. */
    private List<Integer> pileTwo;

    /**
     * The Black Box constructor.
     */
    public BlackBox() {
        super("Black Box", "During payout, winner plays split/choose for winnings.", Effect.BEFORE_PAYOUT);
    }

    /**
     * Strips a string to match the Black Box Specs.
     * 
     * @param input The input string to strip.
     * @return The Stripped string.
     */
    private String stripString(String input) {
        return input.replace(", ", "").replace("[", "").replace("]", "");
    }

    @Override
    public void execute(List<Casino> casinos, List<Player> players, int player, UI ui, int casino) {
        List<Player> tmpPlayers = (players.size() > 3) ? players.subList(0, 3) : players;
        pileOne = new ArrayList<Integer>();
        pileTwo = new ArrayList<Integer>();
        Player minigamePicker = tmpPlayers.get(player - 1);
        Player minigameSorter = tmpPlayers.get((player + tmpPlayers.size() - 2) % tmpPlayers.size());
        int payout = 0;
        List<Integer> choice;

        for (int token : tokens) {
            String prompt1 = "The current piles are " + pileOne.toString().replace(", ", "") + " and "
                    + pileTwo.toString().replace(", ", "") + ".\nIn which pile would you like to place " + token
                    + "? (1 or 2): ~1~2";
            int pile = minigameSorter.getUserInput(Type.BLACKBOX, prompt1, ui);
            if (pile == 1) {
                pileOne.add(token);
            }
            if (pile == 2) {
                pileTwo.add(token);
            }
        }

        String prompt2 = "The options [0, 0, 0, 2, 2, 6] have been split into 2 piles, numbering " + pileOne.size()
                + " and " + pileTwo.size() + ".\nWhich pile would you like? (1 or 2): ~1~2";
        int pick = minigamePicker.getUserInput(Type.BLACKBOX, prompt2, ui);
        choice = (pick == 1) ? pileOne : pileTwo;

        for (int i = 0; i < choice.size(); i++) {
            payout += choice.get(i);
        }

        // paying chips instead of moneys
        if (payout < 4 && payout > 0) {
            minigamePicker.addChips(payout);
            String payoutMessage = " (chose " + stripString(choice.toString()) + " out of "
                    + stripString(pileOne.toString()) + "|" + stripString(pileTwo.toString()) + ")";
            Minishot minishotPayout = GameState.takeMinishot(casinos, casino, players, this, payoutMessage, player,
                    true, payout);
            ui.displayMinigameInfo("minigame payout", minishotPayout);
            String payoutUpdate = "P" + minigameSorter.getPlayer() + " split as " + stripString(pileOne.toString())
                    + "|" + stripString(pileTwo.toString()) + " and " + "P" + minigamePicker.getPlayer() + " chose "
                    + stripString(choice.toString());
            Minishot minishotUpdate = GameState.takeMinishot(casinos, casino, players, this, payoutUpdate, player, true,
                    payout);
            ui.displayMinigameInfo("minigame update", minishotUpdate);
        }

        // paying moneys
        else {
            minigamePicker.addToBalance(payout);
            String payoutMessage = " (chose " + stripString(choice.toString()) + " out of "
                    + stripString(pileOne.toString()) + "|" + stripString(pileTwo.toString()) + ")";
            Minishot minishotPayout = GameState.takeMinishot(casinos, casino, players, this, payoutMessage, player,
                    false, payout);
            ui.displayMinigameInfo("minigame payout", minishotPayout);
            String payoutUpdate = "P" + minigameSorter.getPlayer() + " split as " + stripString(pileOne.toString())
                    + "|" + stripString(pileTwo.toString()) + " and " + "P" + minigamePicker.getPlayer() + " chose "
                    + stripString(choice.toString());
            Minishot minishotUpdate = GameState.takeMinishot(casinos, casino, players, this, payoutUpdate, player,
                    false, payout);
            ui.displayMinigameInfo("minigame update", minishotUpdate);
        }
    }
}
