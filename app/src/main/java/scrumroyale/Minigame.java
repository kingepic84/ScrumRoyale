package scrumroyale;

import java.util.List;

import scrumroyale.vegas.Casino;
import scrumroyale.vegas.Vegas.Effect;

/**
 * Minigame Interface. Base class for all the minigames in Las Vegas Royale.
 */
public abstract class Minigame {

    /** The name of the minigame. */
    protected String name;

    /** The description of the minigame. */
    protected String description;

    /** Enum containing minigame instructions. */
    public enum Instruction {
        /** Update payouts. */
        PAYOUT,
        /** Update the minigame's state. */
        UPDATE
    };

    /** Minigame types. */
    public enum Type {
        /** Jackpot Minigame. */
        JACKPOT,
        /** Pay Day Minigame. */
        PAYDAY,
        /** Fifty Fifty Minigame. */
        FIFTYFIFTY,
        /** Lucky Punch Minigame. */
        LUCKYPUNCH,
        /** Black Box Minigame. */
        BLACKBOX,
        /** High Five Minigame. */
        HIGHFIVE,
        /** Block it! Minigame. */
        BLOCKIT,
        /** Bad Luck :( Minigame. */
        BADLUCK,
    }

    /** Activation Condition. */
    public final Effect activationCondtion;

    /**
     * Minigame Constructor. Requires name and description.
     * 
     * @param name        The name of the minigame.
     * @param description The description of the minigame.
     * @param cond        This Minigame's effect.
     */
    public Minigame(String name, String description, Effect cond) {
        this.name = name;
        this.description = description;
        this.activationCondtion = cond;
    }

    /**
     * Gets the minigame description.
     * 
     * @return A string of the description.
     */
    public String getDescription() {
        return name + " - " + description;
    }

    /**
     * Activates the effect of a minigame.
     *
     * @param casinos List of casinos.
     * @param players List of players.
     * @param player  Player on this minigame.
     * @param console UI Instance.
     * @param casino  The casino this minigame is associated with.
     */
    public abstract void execute(List<Casino> casinos, List<Player> players, int player, UI console, int casino);

}
