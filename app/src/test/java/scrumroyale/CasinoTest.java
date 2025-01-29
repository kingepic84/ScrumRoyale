package scrumroyale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import scrumroyale.Dice;
import scrumroyale.Die;
import scrumroyale.vegas.Casino;

public class CasinoTest {
    private Casino casino;
    private Dice dice;
    private Die die1;
    private Die die2;
    private Die die3;
    private Die die4;

    // makes player 1 have 4 dice including a large one
    @BeforeEach
    void setUp() {
        casino = new Casino(new ArrayList<Integer>(Arrays.asList(8, 6)));
        dice = new Dice(0, false, 1);
        die1 = new Die(false, 1);
        die2 = new Die(false, 1);
        die3 = new Die(false, 1);
        die4 = new Die(true, 1);
        die1.setValue("4");
        die2.setValue("4");
        die3.setValue("4");
        die4.setValue("4+4");
        dice.addDice(die1);
        dice.addDice(die2);
        dice.addDice(die3);
        dice.addDice(die4);
    }

    @Test
    void testAddDice() {
        casino.addDice(dice);
        // large die +1
        assertEquals(dice.size() + 1, casino.getPlayerPayouts().get(0).get(2));
    }

    @Test
    void testAddDice2() {
        casino.addDice(die1);
        // player 1 should have one die using getPlayerPayouts
        assertEquals(1, casino.getPlayerPayouts().get(0).get(2));
    }

    @Test
    void testGetPayouts() {
        List<Integer> payouts = Arrays.asList(8, 6);
        assertTrue(payouts.equals(casino.getPayouts()));
    }

    @Test
    void testGetPlayerPayouts1p() {
        casino.addDice(dice);
        List<Integer> p1state = Arrays.asList(1, 8, 5);
        List<Integer> p1payouts = casino.getPlayerPayouts().get(0);
        assertTrue(p1state.equals(p1payouts));
    }

    // simulating 2 players playing
    @Test
    void testGetPlayerPayouts2p() {
        casino.addDice(dice);
        List<Integer> p1state = Arrays.asList(1, 8, 5);
        List<Integer> p1payouts = casino.getPlayerPayouts().get(0);
        die3 = new Die(false, 2);
        die4 = new Die(true, 2);
        die3.setValue("4");
        die4.setValue("4+4");
        casino.addDice(die3);
        casino.addDice(die4);
        List<Integer> p2state = Arrays.asList(2, 6, 3);
        List<Integer> p2payouts = casino.getPlayerPayouts().get(1);
        assertTrue(p2state.equals(p2payouts));
        assertTrue(p1state.equals(p1payouts));
    }

    // simulating 4 player playing where player 2 and 3 have same amount of die so
    // cancel out
    @Test
    void testGetPlayerPayouts4p() {
        casino.addDice(dice);
        List<Integer> p1state = Arrays.asList(1, 8, 5);
        die3 = new Die(false, 2);
        die4 = new Die(true, 2);
        die3.setValue("4");
        die4.setValue("4+4");
        casino.addDice(die3);
        casino.addDice(die4);
        List<Integer> p2state = Arrays.asList(2, 0, 3);
        Die die5 = new Die(false, 3);
        Die die6 = new Die(true, 3);
        die5.setValue("4");
        die6.setValue("4+4");
        casino.addDice(die5);
        casino.addDice(die6);
        List<Integer> p3state = Arrays.asList(3, 0, 3);
        Die die9 = new Die(false, 4);
        Die die10 = new Die(false, 4);
        die9.setValue("4");
        die10.setValue("4");
        casino.addDice(die9);
        casino.addDice(die10);
        List<Integer> p4state = Arrays.asList(4, 6, 2);
        Map<Integer, List<Integer>> payouts = casino.getPlayerPayouts();
        List<Integer> p1payouts = payouts.get(0);
        List<Integer> p2payouts = payouts.get(2);
        List<Integer> p3payouts = payouts.get(3);
        List<Integer> p4payouts = payouts.get(1);
        assertTrue(p2state.equals(p2payouts));
        assertTrue(p1state.equals(p1payouts));
        assertTrue(p3state.equals(p3payouts));
        assertTrue(p4state.equals(p4payouts));
    }

    @Test
    void testResetDice() {
        casino.resetDice();
        assertEquals(0, casino.getDice().size());
    }

    @Test
    void testSetPayouts() {
        casino.setPayouts(new ArrayList<Integer>(Arrays.asList(4, 3)));
        assertTrue(new ArrayList<Integer>(Arrays.asList(4, 3)).equals(casino.getPayouts()));
    }

    @Test
    void getDiceTest() {
        casino.addDice(dice);
        assertEquals(5, casino.getDice().get(1));
    }
}
