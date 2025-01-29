package scrumroyale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

/* Test class for Dice class */
public class DiceTest {
    private Dice dice;
    private Dice dice2;

    private Dice mockDice;

    @BeforeEach
    void setUp() {
        dice = new Dice(7, false, 1);
        dice2 = new Dice(7, true, 1);
        mockDice = spy(new Dice(8, true, 1));
    }

    /**
     * Tests the {@link Dice#getDice()} method.
     * <p>
     *
     * This test creates a {@link Dice} object with 7 sides. It then verifies that
     * the size of the list returned by {@link Dice#getDice()} is equal to 7,
     * ensuring that the
     * correct number of dice sides is initialized.
     */

    @Test
    void testGetDice() {
        assertEquals(7, dice.size());
    }

    /**
     * Tests the {@link Dice#getMost()} method.
     *
     * <p>
     * This test uses Mockito to simulate a dice roll and the subsequent
     * getting of the values, and verifies that the die value with the highest
     * occurrence is correctly identified by the {@code getMost()} method.
     *
     * <h3>Steps:
     * <ol>
     * <li>Use Mockito to mock the result of {@link Dice#getValues()}.
     * <li>Call {@link Dice#getMost()} to retrieve the die value with the highest
     * occurrence.
     * <li>Assert that the returned die value is among the expected die values with
     * the
     * highest count.
     * </ol>
     *
     *
     * <p>
     * This test ensures that the {@code getMost()} method correctly identifies the
     * most frequently rolled die value.
     */
    @Test
    void testGetMost() {
        List<String> vals = doReturn(new ArrayList<>(Arrays.asList("1", "3", "4", "5", "3", "3", "2", "6"))).when(mockDice).getValues();
        Map<Integer, String> diceCounts1 = doReturn(Map.of(1, "1", 3, "3", 4, "1", 5, "1", 6, "1")).when(mockDice).getDiceCounts();
        String most = mockDice.getMost();
        System.out.println(vals);
        System.out.println(diceCounts1);
        // Assert that the most frequent value is "3"
        assertEquals("3", most);
        // Test if it counts correctly for a large dice being present.
        List<String> vals2 = doReturn(new ArrayList<>(Arrays.asList("1", "3", "4", "6", "3", "3", "6", "6+6"))).when(mockDice).getValues();
        Map<Integer, String> diceCounts2 = doReturn(Map.of(1, "1",3, "3",6, "4")).when(mockDice).getDiceCounts();
        
        System.out.println(vals2);
        System.out.println(diceCounts2);
        String most2 = mockDice.getMost();
        // Assert that the most frequent value is "6"
        assertEquals("6", most2);
    }
    
    /**
     * Tests for when a {@link Die} is added to {@link Dice} that the die is
     * succesfully added to the ArrayList.
     * <h3>Steps:
     * <ol>
     * <li>Create an instance of {@link Dice} with 2 die in it
     * <li>Generate a random number
     * <li>Create and instance of {@link Die}
     * <li>Add it to dice using {@link Dice#addDice}
     * <li>Use assertEquals that the length of {@link Dice#size} equals 3 since we
     * added
     * 1 die
     * <ol>
     */
    
    @Test
    void testAddDice() {
        Die die = new Die(false, 1);
        dice.addDice(die);
        assertEquals(8, dice.size());
    }
    
    /**
     * Adds a set of {@link Dice} of size 3. asserts the the instance of Dice that
     * set was added to is now size 10.
     */
    @Test
    void testAddDice2() {
        Dice test_dice = new Dice(3, true, 1);
        dice2.addDice(test_dice);
        assertEquals(10, dice2.size());
    }
    
    /**
     * Creates a local instance of {@link Dice} of size 1. Gets what the value of
     * that dice is then removes that face. Asserts that the new calculated length
     * is the same as the current size after removal.
     */
    @Test
    void testRemoveDice() {
        
        Dice test_dice = new Dice(7, false, 1); // Create Dice with multiple dice (e.g., 3 dice)

        // Roll the dice and store the results
        test_dice.rollDice();
        String results = test_dice.toString();
        // Print the rolled results for debugging
        // System.out.println("Rolled dice values: " + results);

        // Remove the brackets and split the string into individual die values
        String trimmedResults = results.substring(1, results.length() - 1); // Remove [ and ]
        String[] dieValues = trimmedResults.split(",\\s*"); // Split by comma and optional whitespace
        
        // Convert the first value to an int to use for removal
        int dieValue = Integer.parseInt(dieValues[0]);
        // System.out.println("First die value: " + dieValue); // Debug print
        
        // Store the initial size of the dice before removal
        int initialSize = test_dice.size();
        
        // Remove dice with the specified value
        Dice removedDice = test_dice.removeDice(dieValue);
        
        // Check that the size of the dice decreased by the number of removed dice
        assertEquals(initialSize - removedDice.size(), test_dice.size());
    }
    
    /**
     * uses mockitos verify function to ensure that the rollDice function is
     * actually being called and rolling dice.
     */
    @Test
    void testRollDice() {
        MockitoAnnotations.openMocks(this);
        // Act
        mockDice.rollDice();
        
        // Assert that the roll method is called on each Die object
        verify(mockDice).rollDice();
    }

    /**
     * asserts that the value returned by {@link Dice#size} of dice is equal to 7,
     * which it should be because of the value of dice it was given when constructed
     */
    @Test
    void testSize() {
        assertEquals(7, dice.size());
    }

    // adds die with set values to an instance of dice, expected list is created and
    // asserted to be equal with the returned list from getValues
    @Test
    void testGetValues() {
        Dice dice_testing = new Dice();
        Die die1 = new Die(false, 1);
        Die die2 = new Die(false, 1);
        Die die3 = new Die(true, 1);
        die1.setValue("1");
        die2.setValue("2");
        die3.setValue("3");
        dice_testing.addDice(die1);
        dice_testing.addDice(die2);
        dice_testing.addDice(die3);
        List<String> actualValue = dice_testing.getValues();
        List<String> expectedValue = Arrays.asList("1", "2", "3");
        assertEquals(expectedValue, actualValue);
    }
    
    @Test
    void testGet() {
        Dice dice_testing = new Dice();
        Die die1 = new Die(false, 1);
        die1.setValue("1");
        dice_testing.addDice(die1);
        System.out.println("GET DIE VALUE: " + dice_testing.get(0).value());
        String expectedString = "1";
        System.out.println("DIE VALUE IN TEST GET:" + die1.value());
        assertEquals(expectedString, dice_testing.get(0).value());
    }
    
    @Test
    void testDeepCopy() {
        Dice diceCopy = dice.deepCopy();
        assertEquals(dice.getValues(), diceCopy.getValues());
    }

    @Test
    void testGetDiceCounts() {
        List<String> vals = doReturn(new ArrayList<>(Arrays.asList("1", "3", "4", "5", "3", "3", "2", "6"))).when(mockDice).getValues();
        System.out.println(vals);
        Map<Integer, String> map1 = new HashMap<Integer, String>();
        map1.put(1, "1");
        map1.put(3, "3");
        map1.put(4, "1");
        map1.put(5, "1");
        map1.put(2, "1");
        map1.put(6, "1");
        Map<Integer, String> map2 = new HashMap<Integer, String>();
        map2.put(1, "1");
        map2.put(3, "3");
        map2.put(4, "1");
        map2.put(6, "4*");
        assertEquals(map1, mockDice.getDiceCounts());
        List<String> vals2 = doReturn(new ArrayList<>(Arrays.asList("1", "3", "4", "6", "3", "3", "6", "6+6"))).when(mockDice).getValues();
        System.out.println(vals2);
        assertEquals(map2, mockDice.getDiceCounts());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    void testEquals(){
        Dice die1 = new Dice(7,false, 1);
        Dice die2 = new Dice(7,false, 1);
        Dice die3 = new Dice(7, true, 2);
        Dice die6 = new Dice(7, false, 2);
        Dice dice = new Dice(0, false, 2);
        Dice dice2 = new Dice(0, false, 2);
        Dice die4 = null;
        Die die5 = new Die();
        assertTrue(die1.equals(die2));
        assertTrue(die1.equals(die1));
        assertFalse(die1.equals(die3));
        assertFalse(die1.equals(die4));
        assertFalse(die1.equals(die5));
        assertFalse(die1.equals(dice));
        assertFalse(dice.equals(die6));
        assertTrue(dice.equals(dice2));
    }

    @Test
    void testHashCode(){
        Dice die1 = new Dice(7,false, 1);
        Dice die2 = new Dice(0,false, 1);
        Dice die3 = new Dice(7, true, 2);
        assertTrue(die1.hashCode() >= 1);
        assertTrue(die2.hashCode() >= 1);
        assertTrue(die3.hashCode() >= 1);
    }
}
