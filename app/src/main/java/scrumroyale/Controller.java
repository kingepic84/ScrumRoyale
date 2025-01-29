package scrumroyale;

import scrumroyale.Minigame.Type;

/** Controller Interface for Players and AI. */
public interface Controller {

    /**
     * Input for main game dice rolls.
     * 
     * @param ui    The UI object.
     * @param dice  A deep copy of the dice the player rolled.
     * @param chips The number of chips the player has.
     * @return An {@code int} representing the player's choice.
     */
    public int getUserInput(UI ui, Dice dice, int chips);

    /**
     * Input for minigame dice rolls.
     * 
     * @param opCode A {@code String} representing what the controller needs to ask
     *               of the player.
     * @param ui     The UI object.
     * @param dice   A deepcopy of the dice the player rolled.
     * @param chips  The number of chips the player has.
     * @param game   Which minigame this is.
     * @return An {@code int} representing the player's choice.
     */
    public int getUserInput(Type game, String opCode, UI ui, Dice dice, int chips);
}
