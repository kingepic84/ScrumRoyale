package scrumroyale.Controllers;

import scrumroyale.Controller;
import scrumroyale.Dice;
import scrumroyale.UI;
import scrumroyale.Minigame.Type;

/** PlayerController class. */
public class PlayerController implements Controller {
    /**
     * PlayerController Constructor.
     */
    public PlayerController() {}
    
    /**
     * Input for main game dice rolls.
     * @param ui The UI object.
     * @param dice A deep copy of the dice the player rolled.
     * @param chips The number of chips the player has.
     * @return An {@code int} representing the player's choice.
     */
    @Override
    public int getUserInput(UI ui, Dice dice, int chips) {
        return ui.getUserInput(dice.getDiceCounts(), chips);
    }
    
    /**
     * Input for minigame dice rolls.
     * @param game Which minigame to get input for.
     * @param prompts A {@code String} representing what the controller needs to ask of the player.
     * @param ui The UI object.
     * @param dice A deepcopy of the dice the player rolled.
     * @param chips The number of chips the player has.
     * @return An {@code int} representing the player's choice.
     */
    @Override
    public int getUserInput(Type game, String prompts, UI ui, Dice dice, int chips) {
        String[] promptsAndThings = prompts.toString().split("~");
        int input = ui.getMinigameInput(promptsAndThings[0], Integer.parseInt(promptsAndThings[1]), Integer.parseInt(promptsAndThings[2]));
        return input;
    }

}
