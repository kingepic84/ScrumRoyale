package scrumroyale.minigames;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import scrumroyale.Dice;
import scrumroyale.Player;
import scrumroyale.ui.ConsoleUI;
import scrumroyale.vegas.Casino;
import scrumroyale.vegas.Vegas;

public class BadLuckTest {

    private BadLuck badluck;
    private Vegas vegas;
    private List<Casino> casinos;
    private List<Player> players;
    private ConsoleUI console;
    private Dice dice1;
    private Dice dice2;
    private Dice dice3;

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException {
        vegas = new Vegas(3, true, 1, true, List.of(7));
        badluck = new BadLuck();
        casinos = List.of(vegas.getCasino(1), vegas.getCasino(2), vegas.getCasino(3), vegas.getCasino(4),
                vegas.getCasino(5), vegas.getCasino(6));
        players = List.of(vegas.getPlayer(1), vegas.getPlayer(2), vegas.getPlayer(3));
        console = new ConsoleUI();
        // make dice to put on casinos
        dice1 = new Dice(1, false, 1);
        dice2 = new Dice(3, false, 2);
        dice3 = new Dice(2, false, 3);
        players.get(0).setDice(dice1);
        players.get(1).setDice(dice2);
        players.get(2).setDice(dice3);
    }

    @Test
    void testGetDescription() {
        System.out.println(badluck.getDescription());
        assertTrue(badluck.getDescription()
                .equals("Bad Luck - Each player with the least dice on this casino pays $5 to the bank at round end."));
    }

    @Test
    void leastDicePayTest() {
        // all but player 1 have dice on casino
        players.get(0).addToBalance(6);
        casinos.get(0).addDice(dice2);
        casinos.get(0).addDice(dice3);
        badluck.execute(casinos, players, 2, console, 1);
        assertEquals(1, players.get(0).getBalance());
    }

    @Test
    void leastDice2PlayerPayTest() {
        // only Player 2 has dice here
        players.get(0).addToBalance(6);
        players.get(2).addToBalance(7);
        casinos.get(0).addDice(dice2);
        badluck.execute(casinos, players, 2, console, 1);
        assertEquals(1, players.get(0).getBalance());
        assertEquals(2, players.get(2).getBalance());
    }
    
    @Test
    void notEnoughPayTest() {
        // all but player 1 have dice on casino
        players.get(0).addToBalance(3);
        players.get(0).addChips(2);
        casinos.get(0).addDice(dice2);
        casinos.get(0).addDice(dice3);
        assertEquals(2, players.get(0).getChips());
        badluck.execute(casinos, players, 2, console, 1);
        assertEquals(0, players.get(0).getBalance());
        // chips need to be taken if not enough money
        assertEquals(0, players.get(0).getChips());
    }

    @Test
    void noDebtTest() {
        // all but player 1 have dice on casino
        // $1, 2 chips not enough to pay 5 should have 0 all not negative
        players.get(0).addToBalance(1);
        players.get(0).addChips(2);
        casinos.get(0).addDice(dice2);
        casinos.get(0).addDice(dice3);
        assertEquals(2, players.get(0).getChips());
        badluck.execute(casinos, players, 2, console, 1);
        assertEquals(0, players.get(0).getBalance());
        // chips need to be taken if not enough money
        assertEquals(0, players.get(0).getChips());
    }

    @Test
    void otherPlayersUntouchecdTest() {
        // all but player 1 have dice on casino
        players.get(0).addToBalance(6);
        players.get(1).addToBalance(6);
        players.get(2).addToBalance(6);
        casinos.get(0).addDice(dice2);
        casinos.get(0).addDice(dice3);
        badluck.execute(casinos, players, 2, console, 1);
        assertEquals(1, players.get(0).getBalance());
        assertEquals(6, players.get(1).getBalance());
        assertEquals(6, players.get(2).getBalance());
    }
}
