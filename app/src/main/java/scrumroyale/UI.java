package scrumroyale;

import java.util.List;
import java.util.Map;

import scrumroyale.vegas.GameState.Minishot;
import scrumroyale.vegas.GameState.Snapshot;

/** UI Interface for ConsoleUI and GUI. */
public interface UI {

    /**
     * Prints the Game to the console.
     *
     * @param printInstructions A {@code List} of the current
     *                          instructions.
     * @param snapshot          The GameState Snapshot to use.
     */
    public void display(List<String> printInstructions, Snapshot snapshot);

    /**
     * Displays minigame information.
     * 
     * @param printInstructions The instructions the UI will read to know what to do
     *                          with the snapshot info.
     * @param snapshot          information about the minigame.
     */
    public void displayMinigameInfo(String printInstructions, Minishot snapshot);

    /**
     * Used to get user input.
     * 
     * @param diceAvailable The dice available to the player.
     * @param chips         The player's chip count.
     * @return The player's choice as an {@code int}
     */
    public int getUserInput(Map<Integer, String> diceAvailable, int chips);

    /**
     * Used to get user input for the minigames.
     * 
     * @param prompt The prompt for the minigame.
     * @param min    The minimum entry value for the minigame.
     * @param max    The maximum entry value for the minigame.
     * @return An {@code int} representing the user's input.
     */
    public int getMinigameInput(String prompt, int min, int max);
}
