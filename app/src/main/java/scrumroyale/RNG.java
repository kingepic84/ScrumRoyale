package scrumroyale;

import java.util.Random;

/**
 * RNG Singleton class for use all throughout app.
 */
public class RNG {

    // Singleton Instance
    private static RNG single = null;

    // Random module.
    private Random rand = null;

    // Random Seed.
    private static long seed = System.currentTimeMillis();

    /**
     * Create a new RNG object.
     */
    private RNG() {
        rand = new Random(seed);
    }

    /**
     * Get the singleton instance from the RNG class.
     * 
     * @return The singleton instance.
     */
    public static synchronized RNG getInstance() {
        if (single == null) {
            single = new RNG();
        }
        return single;
    }

    /**
     * Generate a random integer between {@code b1} and {@code b2}.
     * 
     * @param b1 The origin (Inclusive).
     * @param b2 The upper bound (Exclusive).
     * @return The randomly generated integer.
     */
    public int nextInt(int b1, int b2) {
        return rand.nextInt(b1, b2);
    }

    /**
     * Generate a random integer between {@code 0} and {@code b1}.
     * 
     * @param b1 The upper bound (Exclusive).
     * @return The randomly generated integer.
     */
    public int nextInt(int b1) {
        return rand.nextInt(b1);
    }

    /**
     * Picks a random number from a given list ({@code String[]}) of numbers.
     * 
     * @param numbers The numbers to choose from.
     * @return The randomly chosen number.
     */
    public int choice(String[] numbers) {
        return Integer.parseInt(numbers[rand.nextInt(numbers.length)]);
    }
}
