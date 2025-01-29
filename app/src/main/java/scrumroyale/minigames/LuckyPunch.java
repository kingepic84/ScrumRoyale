package scrumroyale.minigames;

import java.util.List;

import scrumroyale.Minigame;
import scrumroyale.Player;
import scrumroyale.UI;
import scrumroyale.vegas.Casino;
import scrumroyale.vegas.GameState;
import scrumroyale.vegas.GameState.Minishot;
import scrumroyale.vegas.Vegas.Effect;

/**
 * The {@code LuckyPunch} class represents a mini-game in the ScrumRoyale
 * application.
 * Players can choose one of three options to win chips or money, while another
 * player
 * attempts to guess their choice.
 * 
 * <p>
 * The mini-game includes user prompts, payouts based on player choices, and
 * updates
 * the game state accordingly.
 * 
 * <p>
 * This class extends the {@link Minigame} superclass and overrides the
 * {@code execute}
 * method to implement the game's logic.
 * 
 * @see Minigame
 */
public class LuckyPunch extends Minigame {

    /**
     * info string
     */
    private String info;

    /**
     * Constructs a {@code LuckyPunch} mini-game.
     * Initializes the game's name, description, and activation effect.
     */
    public LuckyPunch() {
        super("Lucky Punch", "Choose 1, 2, or 3 to win 2 chips, $3, or $4.", Effect.ACTIVATED);
    }

    /**
     * Executes the Lucky Punch mini-game logic.
     * 
     * <p>
     * In this game:
     * <ul>
     * <li>The current player chooses between winning 2 chips, $3, or $4.
     * <li>The player to their left tries to guess the current player's choice.
     * <li>If the guess is incorrect, the current player receives the payout.
     * </ul>
     * 
     * @param casinos a list of {@link Casino} objects representing the game state.
     * @param players a list of {@link Player} objects participating in the game.
     * @param player  the index of the current player in the {@code players} list.
     * @param console a {@link UI} object for displaying prompts and results.
     * @param casino  the index of the casino involved in this mini-game.
     */
    @Override
    public void execute(List<Casino> casinos, List<Player> players, int player, UI console, int casino) {
        int currentPlayerIndex = player - 1;
        int leftPlayerIndex = (player + players.size() - 2) % players.size(); // modulus operation allows to wrap around
        Player playerToLeft = players.get(leftPlayerIndex);
        Player currPlayer = players.get(currentPlayerIndex);
        StringBuilder prompt = new StringBuilder();
        prompt.append("You can win 2 chips (1), $3 (2), or $4 (3).\nWhat is your choice? (1, 2, or 3):~1~3");
        StringBuilder prompt2 = new StringBuilder();
        prompt2.append("P" + currPlayer.getPlayer()
                + " can win 2 chips (1), $3 (2), or $4 (3).\nWhat is your choice? (1, 2, or 3):~1~3");
        int tokensGrabbed = currPlayer.getUserInput(Type.LUCKYPUNCH, prompt.toString(), console);
        int guessedAmount = playerToLeft.getUserInput(Type.LUCKYPUNCH, prompt2.toString(), console);
        String payoutMessage = "";
        String guessedMessage = (guessedAmount == 1) ? "2 chips" : "$" + (guessedAmount + 1);
        switch (tokensGrabbed) {
            case 1:
                payoutMessage = String.format(" (chose 2 chips and P" + playerToLeft.getPlayer() + " guessed %s)",
                        guessedMessage);
                if (tokensGrabbed != guessedAmount) {
                    currPlayer.addChips(2); // pay chip
                }
                break;
            case 2:
                if (tokensGrabbed != guessedAmount) {
                    currPlayer.addToBalance(3);// pay money
                }
                payoutMessage = String.format(" (chose $3 and P" + playerToLeft.getPlayer() + " guessed %s)",
                        guessedMessage);
                break;
            case 3:
                if (tokensGrabbed != guessedAmount) {
                    currPlayer.addToBalance(4); // pay money
                }
                payoutMessage = String.format(" (chose $4 and P" + playerToLeft.getPlayer() + " guessed %s)",
                        guessedMessage);
                break;
            default:
                break;
        }
        // Update game state info
        int actualGrabbed = tokensGrabbed;
        if ((tokensGrabbed == 2 || tokensGrabbed == 3)) {
            actualGrabbed++;
            if (guessedAmount == 1) {
                info = String.format("P%d chose $%d and P%d guessed 2 chips", player, actualGrabbed,
                        playerToLeft.getPlayer());
            } else {
                info = String.format("P%d chose $%d and P%d guessed $%d", player, actualGrabbed,
                        playerToLeft.getPlayer(), guessedAmount + 1);
            }
        } else if (tokensGrabbed == 1) {
            actualGrabbed++;
            if (guessedAmount == 1) {
                info = String.format("P%d chose 2 chips and P%d guessed 2 chips", player, playerToLeft.getPlayer());
            } else {
                info = String.format("P%d chose 2 chips and P%d guessed $%d", player, playerToLeft.getPlayer(),
                        guessedAmount + 1);
            }
        }
        Minishot minishotPayout = GameState.takeMinishot(casinos, casino, players, this, payoutMessage, player,
                (tokensGrabbed == 1) ? true : false, actualGrabbed);
        if (guessedAmount != tokensGrabbed) {
            console.displayMinigameInfo("minigame payout", minishotPayout);
        }
        Minishot minishotUpdate = GameState.takeMinishot(casinos, casino, players, this, info, player,
                (tokensGrabbed == 1) ? true : false, actualGrabbed);
        console.displayMinigameInfo("minigame update", minishotUpdate);

    }

}
