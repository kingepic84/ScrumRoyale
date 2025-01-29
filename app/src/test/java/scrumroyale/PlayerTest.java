package scrumroyale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class PlayerTest {

    private Player player;
    private Dice dice;
    private int testPlayersID = 1;

    /**
     * Initalizes a new Player object with a mocked Dice object before each test.
     * This ensures that each test starts with a clean setup.
     */
    @BeforeEach
    void setUp(){
        dice = Mockito.spy(new Dice(8, false, testPlayersID)); // Mocks the Dice in order to control the behavior during tests
        player = new Player(dice, testPlayersID); // Initializes player with dice and default balance = 0
    }

    /**
     * Tests that the addToBalance method correctly adds a specified value to the player's balance.
     */
    @Test
    void testAddToBalance() {
        int initialBalance = player.getBalance(); // Gets player's initial balance, should be = 0
        player.addToBalance(100); // Adds 100 to the balance

        // Checks that the balance value inreased by 100
        assertEquals(initialBalance + 100, player.getBalance(), "Balance should be increased by 100.");
    }

    /**
     * Tests that addToBalance does not accept negative values, ensuring balance can't be reduced.
     */
    @Test
    void testAddToBalanceNegative() {
        int initialBalance = player.getBalance();
        assertThrows(IllegalArgumentException.class, () -> {
            player.addToBalance(-50); // Tries to subtract from the balance
        });

        assertEquals(initialBalance, player.getBalance(), "Balance should not change when adding a negative value.");
    } 

    /**
     * Tests the expendDice method that removes dice of a specified face value from the player's dice set.
     * This should test both dice removal and retrieval of the removed dice.
     */
    @Test
    void testExpendDice() {
        Dice removedDice = new Dice(); // Creates a dummy dice set that simulates removed dice
        when(dice.removeDice(1)).thenReturn(removedDice); // Mocks the dice removal behavior

        // Expends the dice with face value 1 and retrieves them
        Dice result = player.expendDice(1);

        // Verifies that the dice with face value 1 were removed correctly
        assertEquals(removedDice, result, "The removed dice should match the expected dice set.");
        // verify(dice, times(1)).removeDice(1); // Ensures that removeDice() was only called once
    }

    /**
     * Tests the behavior of expendDice when no dice of the specified value exist.
     */
    @Test
    void testExpendDiceNoneExist() {
        Dice emptyDice = new Dice();
        when(dice.removeDice(5)).thenReturn(emptyDice); // Simulates no dice being removed for face value 5

        Dice result = player.expendDice(5);
        assertEquals(emptyDice, result, "No dice should be removed if no dice with the specified face exist.");
    }

    /**
     * Tests that the getBalance method to ensure the balance is returned correctly.
     */
    @Test
    void testGetBalance() {
        // Checks initial balance, should = 0
        assertEquals(0, player.getBalance(), "Initial balance should be 0.");

        // Adds some balance and then verifies the balance
        player.addToBalance(50);
        assertEquals(50, player.getBalance(), "Balance should be updated to 50.");
    }

    /**
     * Tests that the getDice method returns a deep copy of the player's dice set.
     */
    @Test
    void testGetDice() {
        Dice expectedDice = new Dice(8, false, 1); // Creates a new dice object for comparison
        // when(dice.deepCopy()).thenReturn(expectedDice); // Mocks the deep copy behavior seen in Dice.java

        // Checks that the dice set retrieved matches the deep copy of the dice
        assertEquals(expectedDice, player.getDice(), "The dice should be a deep copy of the player's dice.");
        // verify(dice, times(1)).deepCopy(); // Ensures that deepCopy() was only called once
    }

    /**
     * Tests that the setDice method properly updates the player's dice set.
     */
    @Test
    void testSetDice() {
        Dice newDice = new Dice(8, false, testPlayersID); // Mocks a new Dice object
        player.setDice(newDice); // Sets the new dice set for the player

        // Verifies that the player's dice set has been updated
        // when(newDice.deepCopy()).thenReturn(newDice); // Simulates deepCopy() behavior in setDice
        assertEquals(newDice, player.getDice(), "The player's dice should be updated to the new dice set.");
    }

    /**
     * Tests that setDice does not accept a null value and retains the old dice set.
     */
    @Test
    void testSetDiceNull() {
        Dice originalDice = player.getDice(); // Retains the original dice set
        assertThrows(NullPointerException.class, () -> {
            player.setDice(null); // Tries to set the dice to null
        });

        assertEquals(originalDice, player.getDice(), "The player's dice should not be updated to null.");
    }

    /**
     * Tests the roll method to ensure that all dice in the player's hand are rolled,
     * and their values are updated correctly.
     */
    @Test
    void testRoll() {
        // When rolling dice, verifies that rollDice() is called
        player.roll();
        assertTrue(!dice.getValues().contains(" "));
        // Verifies that the player's dice are rolled & rollDice() is only called once
        // verify(dice, times(1)).rollDice();
    }

    /**
     * Tests that the getPlayer method correctly returns the player's ID.
     */
    @Test
    void testGetPlayer() {
        int expectedPlayerId = testPlayersID; // The expected player ID per the local player object's initialization 
        assertEquals(expectedPlayerId, player.getPlayer(), "The player ID should match the expected value.");
    }

    /**
     * Tests the default (zero balance, non-AI) Player constructor.
     * Asserts that there is no balance, that the Player's ID is correct, and that the player is not AI.
     */
    @Test
    void testPlayerConstructor() {
        Player player = new Player(dice, testPlayersID); // Initializes a player with balance = 0

        // Verifies that the player's default balance (= 0) was initialized correctly
        assertEquals(0, player.getBalance(), "The player's balance should be zero.");

        // Verifies that the player's ID was initialized correctly
        assertEquals(testPlayersID, player.getPlayer(), "The player's ID should be initialized to " + testPlayersID + ".");

        // Verifies that the default player is NOT an AI player
        assertFalse(player.isPlayerAI(), "The player should not be an AI player.");
    }

    /**
     * Tests the default Player constructor that includes a custom balance.
     * Asserts that the balance is set correctly and that the player is not AI.
     */
    @Test
    void testPlayerConstructorWithBalance() {
        Player playerWithBalance = new Player(dice, testPlayersID, 200); // Initializes a player with balance = 200

        // Verifies that the player's balance was initialized correctly
        assertEquals(200, playerWithBalance.getBalance(), "The player's balance should be initialized to 200.");

        // Verifies that the default player is NOT an AI player
        assertFalse(playerWithBalance.isPlayerAI(), "The player should not be an AI player.");
    }

    /**
     * Tests the default, zero balance Player constructor for an AI player.
     * Asserts that there is no balance, that the Player ID is correct, and that the player IS an AI player.
     */
    @Test
    void testAIPlayerConstructor() {
        Player player = new Player(dice, testPlayersID, true); // Initializes a player with balance = 0

        // Verifies that the player's default balance (= 0) was initialized correctly
        assertEquals(0, player.getBalance(), "The player's balance should be zero.");

        // Verifies that the player's ID was initialized correctly
        assertEquals(testPlayersID, player.getPlayer(), "The player's ID should be initialized to " + testPlayersID + ".");

        // Verifies that the player IS an AI player
        assertTrue(player.isPlayerAI(), "The player should be an AI player.");
    }

    /**
     * Tests the AI Player constructor that also includes a custom balance.
     * Asserts that balance is set correctly and that the player IS an AI player.
     */
    @Test
    void testAIPlayerConstructorWithBalance() {
        Player playerWithBalance = new Player(dice, testPlayersID, 400, true); // Initializes a player with balance = 400

        // Verifies that the player's balance was initialized correctly
        assertEquals(400, playerWithBalance.getBalance(), "The player's balance should be initialized to 400.");

        // Verifies that the player IS an AI player
        assertTrue(playerWithBalance.isPlayerAI(), "The player should be an AI player.");
    }

    /**
     * Tests the default Player constructor for a non-AI player.
     * Asserts that the player is NOT an AI player.
     */
    @Test
    void testAIPlayerConstructorNotAI() {
        Player player = new Player(dice, testPlayersID, false);

        // Verifies that the player is NOT an AI player
        assertFalse(player.isPlayerAI(), "The player should not be an AI player.");
    }

    /**
     * Tests that the isPlayerAI method returns the correct boolean value.
     */
    @Test
    void testIsPlayerAI() {
        Player playerIsAI = new Player(dice, testPlayersID, true); // Initializes an AI player
        Player playerIsNotAI = new Player(dice, testPlayersID, false); // Initializes a non-AI player
        Player playerDefaultNotAI = new Player(dice, testPlayersID, false); // Initializes a default, non-AI player

        // Verifies that the AI player is an AI player
        assertTrue(playerIsAI.isPlayerAI(), "The player should be an AI player.");
        // Verifies that the non-AI player is NOT an AI player
        assertFalse(playerIsNotAI.isPlayerAI(), "The player should not be an AI player.");
        // Verifies that the default, non-AI player is NOT an AI player
        assertFalse(playerDefaultNotAI.isPlayerAI(), "The player should not be an AI player.");
        playerIsNotAI.setAI();
        assertTrue(playerIsNotAI.isPlayerAI());
    }

    /**
     * Tests the addChips method to ensure it correctly adds chips to the player's inventory.
     */
    @Test
    void testAddChips() {
        player.addChips(5); // Adds 5 chips
        assertEquals(5, player.getChips(), "Player should have 5 chips.");
        
        player.addChips(2); // Adds 2 more chips
        assertEquals(7, player.getChips(), "Player should now have 7 chips.");
    }

    /**
     * Tests the getChips method to ensure it returns the correct number of chips.
     */
    @Test
    void testGetChips() {
        assertEquals(0, player.getChips(), "Initial chip count should be 0.");
        
        player.addChips(4); // Adds 4 chips
        assertEquals(4, player.getChips(), "Player should now have 4 chips.");
    }

    /**
     * Tests the expendChip method when the player has chips to expend.
     */
    @Test
    void testExpendChipPlayerChips() {
        player.addChips(3); // Adds 3 chips
        assertTrue(player.expendChip(), "Should expend a chip when available.");
        assertEquals(2, player.getChips(), "Chip count should decrease by 1.");
    }

    /**
     * Tests the expendChip method when the player does NOT have chips to expend.
     */
    @Test
    void testExpendChipPlayerNoChips() {
        assertFalse(player.expendChip(), "Should not be able to expend a chip when none are available.");
        assertEquals(0, player.getChips(), "Chip count should remain = 0.");
    }

    /**
     * Tests the hasBigDice method when the player has a big die.
     */
    @Test
    void testHasBigDiceWhenHasBigDie() {
        // Creates a new Dice object and add only a big die
        Dice playerDice = player.getDice();
        Die bigDie = new Die(true, testPlayersID);  // dieShape is boolen where true = big
        playerDice.addDice(bigDie);
        player.setDice(playerDice); // Sets the player's dice = to the new dice set

        // Verifies that the player has a big die
        assertTrue(player.hasBigDice(), "Player should have a big die.");
    }

    /**
     * Tests the hasBigDice method when the player has no big dice.
     */
    @Test
    void testHasBigDiceWhenNoBigDie() {
        // Verifies that the player has no big dice, since the player initializes without them
        assertFalse(player.hasBigDice(), "Player should not have any big dice.");
    }

}
