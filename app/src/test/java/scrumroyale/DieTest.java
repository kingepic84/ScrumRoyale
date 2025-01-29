
package scrumroyale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DieTest {

    private Die regularDie;
    private Die bigDie;
    private Die gameDie;

    @BeforeEach
    public void setUp() {
        // Initialize a regular die and a big die before each test
        regularDie = new Die(false, 1);
        bigDie = new Die(true, 1);
        gameDie = new Die();
    }

    @Test
    public void testDieInitialValue() {
        // Test the initial value of the die is an empty space
        assertEquals(" ", regularDie.value());
        assertEquals(" ", bigDie.value());
    }

    @Test
    public void testRollRegularDie() {
        // Roll the regular die and check if the value is between 1 and 6
        regularDie.roll();
        String dieValue = regularDie.value();
        int numberRolled = Integer.parseInt(dieValue);

        assertTrue(numberRolled >= 1 && numberRolled <= 6,
                "Regular die should have a value between 1 and 6 after rolling");
    }

    @Test
    public void testRollBigDie() {
        // Roll the big die and check the format and values
        bigDie.roll();
        String dieValue = bigDie.value();

        // Split the value for big die to check if it's in the "X+X" format
        String[] splitValues = dieValue.split("\\+");
        assertEquals(2, splitValues.length, "Big die should have the format X+X");

        // Both parts should be integers between 1 and 6
        int firstValue = Integer.parseInt(splitValues[0]);
        int secondValue = Integer.parseInt(splitValues[1]);

        assertTrue(firstValue >= 1 && firstValue <= 6,
                "First value of big die should be between 1 and 6");
        assertTrue(secondValue >= 1 && secondValue <= 6,
                "Second value of big die should be between 1 and 6");

        // Both values should be the same in this implementation
        assertEquals(firstValue, secondValue,
                "Both values in a big die should be the same");
    }

    /**
     * tests that {@link Die#isBig()} returns false when a die is regular and true
     * when it is big
     */
    @Test
    void testIsBig() {
        assertEquals(false, regularDie.isBig());
        assertEquals(true, bigDie.isBig());
    }

    /**
     * tests that {@link Die#setValue(String)} is able to change the values of the
     * die
     */
    @Test
    void testSetValue() {
        regularDie.setValue("1");
        assertEquals("1", regularDie.value());
        regularDie.setValue("2");
        assertEquals("2", regularDie.value());
        regularDie.setValue("3");
        assertEquals("3", regularDie.value());
        regularDie.setValue("4");
        assertEquals("4", regularDie.value());
        regularDie.setValue("5");
        assertEquals("5", regularDie.value());
        regularDie.setValue("6");
        assertEquals("6", regularDie.value());
        bigDie.setValue("1");
        assertEquals("1", bigDie.value());
        bigDie.setValue("2");
        assertEquals("2", bigDie.value());
        bigDie.setValue("3");
        assertEquals("3", bigDie.value());
        bigDie.setValue("4");
        assertEquals("4", bigDie.value());
        bigDie.setValue("5");
        assertEquals("5", bigDie.value());
        bigDie.setValue("6");
        assertEquals("6", bigDie.value());
    }

    /**
     * asserts that the {@link Dice#getPlayer() returns a positive integer
     * representing a player}
     */
    @Test
    void testGetPlayer() {
        int player = gameDie.getPlayer();
        assertTrue(player == -1);
        int playerBigDie = bigDie.getPlayer();
        assertTrue(playerBigDie == 1);
        int playerRegDie = regularDie.getPlayer();
        assertTrue(playerRegDie == 1);

    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    void testEquals(){
        Die die1 = new Die(false, 1);
        Die die2 = new Die(false, 1);
        Die die5 = new Die(false, 2);
        Dice die4 = new Dice();
        assertTrue(die1.equals(die2));
        assertTrue(die1.equals(die1));
        assertFalse(die1.equals(null));
        die1.setValue("3");
        die2.setValue("3");
        assertTrue(die1.equals(die2));
        die2.setValue("4");
        assertFalse(die1.equals(die2));
        die1.setValue(null);
        assertFalse(die1.equals(die2));
        assertFalse(die2.equals(die1));
        die2.setValue(null);
        Die die3 = new Die(true, 2);
        die3.setValue("3");
        die5.setValue("3");
        assertTrue(die1.equals(die2));
        assertFalse(die1.equals(die3));
        assertFalse(die5.equals(die3));
        assertFalse(die1.equals(die4));
    }
    
    @Test
    void testHashCode(){
        Die die1 = new Die(false, 1);
        Die die3 = new Die(true, 2);
        assertTrue(die1.hashCode() > 1);
        die1.setValue("3");
        assertTrue(die1.hashCode() > 1);
        die1.setValue(null);
        assertTrue(die1.hashCode() > 1);
        assertTrue(die3.hashCode() > 1);
    }
    
}
