package scrumroyale;

/**
 * Die class for use with the Dice class.
 */
public class Die {

    /**
     * Current value of the Die face.
     */
    private String value = " ";

    // The shape of the die.
    private boolean dieShape;

    // The player that this die belongs to.
    private int player;

    // The RNG singleton instance.
    private RNG random = RNG.getInstance();

    /**
     * Creates a new {@code Die} given a shape and a {@code Player}.
     * 
     * @param dieShape whether the Die is a big die or not.
     * @param player   The player this die belongs to (usually inherited from Dice).
     */
    public Die(boolean dieShape, int player) {
        this.player = player;
        this.dieShape = dieShape;
    }

    /**
     * Die to be used without the need for a player or a dieShape. For use mostly
     * with minigames.
     */
    public Die() {
        dieShape = false;
        player = -1;
    };

    /**
     * Gets the player that this die belongs to.
     * 
     * @return The player.
     */
    public int getPlayer() {
        return player;
    }

    /**
     * Tells whether this current die is a big die or not.
     * 
     * @return true if big, false otherwise.
     */
    public boolean isBig() {
        return dieShape;
    }

    /**
     * Returns the current value on the die.
     * 
     * @return The current value.
     */
    public String value() {
        return value;
    }

    /**
     * Sets the value of the Die.
     * 
     * @param value The value to set it to.
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Rolls the die.
     */
    public void roll() {
        value = String.valueOf(random.nextInt(1, 7));
        if (dieShape) {
            value += "+" + value;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        result = prime * result + (dieShape ? 1231 : 1237);
        result = prime * result + player;
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Die other = (Die) obj;
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value) ||
                dieShape != other.dieShape ||
                player != other.player) {
            return false;
        }
        return true;
    }
}
