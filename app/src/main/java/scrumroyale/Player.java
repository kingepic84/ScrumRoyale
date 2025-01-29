package scrumroyale;

import scrumroyale.Minigame.Type;
import scrumroyale.Controllers.AIController;
import scrumroyale.Controllers.PlayerController;

/**
 * Class to represent a player of ScrumRoyale.
 */
public class Player {

    // The dice to be used by the player.
    private Dice dice;

    // The player's current balance.
    private int balance;

    // The player's ID.
    private int player;

    // The player's chips.
    private int chips = 0;

    // The player's AI status.
    private boolean isAI = false;

    // The Player's Controller.
    private Controller playerController;

    /**
     * Player constructor with balance set to zero.
     * 
     * @param dice   the dice set to use with this player.
     * @param player an integer representing which player this instance represents.
     */
    public Player(Dice dice, int player) {
        this.dice = dice.deepCopy();
        this.player = player;
        balance = 0;
        playerController = new PlayerController();
    }

    /**
     * Player constructor.
     * 
     * @param dice    the dice set to use with this player.
     * @param player  an integer representing which player this instance represents.
     * @param balance the starting balance of the player.
     */
    public Player(Dice dice, int player, int balance) {
        this.dice = dice.deepCopy();
        this.balance = balance;
        this.player = player;
        playerController = new PlayerController();
    }

    // AI player constructor without balance input

    /**
     * Player constructor with balance set to zero.
     * 
     * @param dice   the dice set to use with this player.
     * @param player an integer representing which player this instance represents.
     * @param isAI   boolean representing the player's AI status.
     */
    public Player(Dice dice, int player, boolean isAI) {
        this.dice = dice.deepCopy();
        this.player = player;
        this.isAI = isAI;
        balance = 0;
        playerController = (isAI) ? new AIController() : new PlayerController();
    }

    // AI player constructor with balance input

    /**
     * Player constructor.
     * 
     * @param dice    the dice set to use with this player.
     * @param player  an integer representing which player this instance represents.
     * @param balance the starting balance of the player.
     * @param isAI    boolean representing the player's AI status.
     */
    public Player(Dice dice, int player, int balance, boolean isAI) {
        this.dice = dice.deepCopy();
        this.balance = balance;
        this.player = player;
        this.isAI = isAI;
        playerController = (isAI) ? new AIController() : new PlayerController();
    }

    /**
     * Gets the player's AI status.
     * 
     * @return A boolean representing the player's AI status.
     */
    public boolean isPlayerAI() {
        return isAI;
    }

    /**
     * Sets whether a player is AI or not.
     */
    public void setAI() {
        isAI = !isAI;
        playerController = (isAI) ? new AIController() : new PlayerController();
    }

    /**
     * Gets the player ID.
     * 
     * @return An integer representing this player's ID.
     */
    public int getPlayer() {
        return player;
    }

    /**
     * Get the dice of the current player.
     * 
     * @return the current player's dice.
     */
    public Dice getDice() {
        return dice.deepCopy();
    }

    /**
     * Set the dice.
     * 
     * @param dice the dice to set it to.
     */
    public void setDice(Dice dice) {
        this.dice = dice.deepCopy();
    }

    /**
     * Gets the balance of the current player.
     * 
     * @return the balance.
     */
    public int getBalance() {
        return balance;
    }

    /**
     * Adds to the current balance.
     * 
     * @param balanceToAdd The balance to add.
     */
    public void addToBalance(int balanceToAdd) {
        if (balanceToAdd < 0) {
            throw new IllegalArgumentException("Balance to add cannot be negative: " + balanceToAdd);
        }
        balance += balanceToAdd;
    }

    /**
     * Removes from the current balance.
     * 
     * @param balanceToRemove The balance to remove.
     */
    public void removeFromBalance(int balanceToRemove) {
        if (balanceToRemove < 0) {
            throw new IllegalArgumentException("Balance to remove cannot be negative: " + balanceToRemove);
        }
        balance -= balanceToRemove;
    }

    /**
     * Returns all the dice of a specified face.
     * 
     * @param face The face to remove and return.
     * @return All the dice matching that face.
     */
    public Dice expendDice(int face) {
        return dice.removeDice(face);
    }

    /**
     * Rolls all the dice in the player's hand.
     */
    public void roll() {
        dice.rollDice();
    }

    /**
     * Expend's a chip from the Player's inventory.
     * 
     * @return {@code true} if a chip was a chip expended, {@code false} otherwise.
     */
    public boolean expendChip() {
        if (chips > 0) {
            chips--;
            return true;
        }
        return false;
    }

    /**
     * Adds {@code numChips} chips to the player's inventory.
     * 
     * @param numChips The number of chips to add.
     */
    public void addChips(int numChips) {
        chips += numChips;
    }

    /**
     * Gets the current amount of chips the player has.
     * 
     * @return The amount of chips the player has.
     */
    public int getChips() {
        return chips;
    }

    /**
     * Returns a boolean value for whether this player has a big die or not.
     * 
     * @return {@code true} if player has a big die, {@code false} otherwise.
     */
    public boolean hasBigDice() {
        for (int i = 0; i < dice.size(); i++) {
            if (dice.get(i).isBig()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Used to get talk to the player's controller for general game.
     * 
     * @param ui The UI Instance.
     * @return Returns the player's choice as an {@code int}.
     */
    public int getUserInput(UI ui) {
        return playerController.getUserInput(ui, dice.deepCopy(), chips);
    }

    /**
     * Used to get talk to the player's controller for the minigames.
     * 
     * @param game   The minigame you're getting input for.
     * @param opCode The operation the controller is doing on the code.
     * @param ui     The UI Instance.
     * @return Returns the player's choice as an {@code int}.
     */
    public int getUserInput(Type game, String opCode, UI ui) {
        return playerController.getUserInput(game, opCode, ui, dice.deepCopy(), chips);
    }
}
