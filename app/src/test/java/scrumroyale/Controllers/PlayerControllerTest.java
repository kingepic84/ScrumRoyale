package scrumroyale.Controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import scrumroyale.Dice;
import scrumroyale.Die;
import scrumroyale.Minigame.Type;
import scrumroyale.Player;
import scrumroyale.ui.ConsoleUI;
import scrumroyale.vegas.Vegas;

public class PlayerControllerTest {
    private ByteArrayInputStream inStream;
    private InputStream systemIn = System.in;
    private PrintStream systemOut = System.out;
    private ByteArrayOutputStream outBAOStream = new ByteArrayOutputStream();
    private PrintStream outStream;
    private Vegas vegas;
    private ConsoleUI console;

    @BeforeEach
    void setUp() {
        outStream = new PrintStream(outBAOStream, false, StandardCharsets.UTF_8);
        System.setOut(outStream);
    }

    @AfterEach
    void cleanUp() {
        System.setIn(systemIn);
        System.setOut(systemOut);
        outStream.flush();
    }

    @Test
    void testGetUserInput() {
        vegas = new Vegas(2, true, 1, false, List.of());
        vegas.nextTurn();
        Player p1 = vegas.getPlayer(1);
        Dice dice1 = new Dice(0, false, 1);
        Dice dice2 = new Dice(0, false, 2);
        Die die1 = new Die(true, 1);
        Die die2 = new Die(false, 1);
        Die die3 = new Die(false, 1);
        Die die4 = new Die(false, 1);
        Die die5 = new Die(false, 1);
        Die die6 = new Die(false, 1);
        Die die7 = new Die(false, 1);
        Die die8 = new Die(false, 1);
        Die die9 = new Die(true, 2);
        Die die10 = new Die(false, 2);
        Die die11 = new Die(false, 2);
        Die die12 = new Die(false, 2);
        Die die13 = new Die(false, 2);
        Die die14 = new Die(false, 2);
        Die die15 = new Die(false, 2);
        Die die16 = new Die(false, 2);
        die1.setValue("4+4");
        dice1.addDice(die1);
        die2.setValue("4");
        dice1.addDice(die2);
        die3.setValue("3");
        dice1.addDice(die3);
        die4.setValue("3");
        dice1.addDice(die4);
        die5.setValue("4");
        dice1.addDice(die5);
        die6.setValue("4");
        dice1.addDice(die6);
        die7.setValue("4");
        dice1.addDice(die7);
        die8.setValue("4");
        dice1.addDice(die8);
        die9.setValue("4+4");
        dice2.addDice(die9);
        die10.setValue("4");
        dice2.addDice(die10);
        die11.setValue("3");
        dice2.addDice(die11);
        die12.setValue("3");
        dice2.addDice(die12);
        die13.setValue("4");
        dice2.addDice(die13);
        die14.setValue("5");
        dice2.addDice(die14);
        die15.setValue("4");
        dice2.addDice(die15);
        die16.setValue("4");
        dice2.addDice(die16);
        Player p2 = vegas.getPlayer(2);
        p1.setDice(dice1);
        p2.setDice(dice2);
        inStream = new ByteArrayInputStream(
                (p1.getDice().getMost() + "\n" + p2.getDice().getMost() + "\n").getBytes(StandardCharsets.UTF_8));
        System.setIn(inStream);
        console = new ConsoleUI();
        int input = p1.getUserInput(console);
        assertEquals(Integer.parseInt(dice1.getMost()), input);
        int input2 = p2.getUserInput(console);
        assertEquals(Integer.parseInt(dice2.getMost()), input2);
    }

    @Test
    void testGetUserInput2() {
        vegas = new Vegas(1, true, 1, false, List.of());
        vegas.nextTurn();
        Player p1 = vegas.getPlayer(1);
        Dice dice1 = new Dice(0, false, 1);
        Die die1 = new Die(true, 1);
        Die die2 = new Die(false, 1);
        Die die3 = new Die(false, 1);
        Die die4 = new Die(false, 1);
        Die die5 = new Die(false, 1);
        Die die6 = new Die(false, 1);
        Die die7 = new Die(false, 1);
        Die die8 = new Die(false, 1);
        Die die9 = new Die(true, 2);
        die1.setValue("4+4");
        dice1.addDice(die1);
        die2.setValue("4");
        dice1.addDice(die2);
        die3.setValue("3");
        dice1.addDice(die3);
        die4.setValue("3");
        dice1.addDice(die4);
        die5.setValue("4");
        dice1.addDice(die5);
        die6.setValue("4");
        dice1.addDice(die6);
        die7.setValue("4");
        dice1.addDice(die7);
        die8.setValue("4");
        dice1.addDice(die8);
        die9.setValue("4+4");
        p1.setDice(dice1);
        inStream = new ByteArrayInputStream(("1\n0\n").getBytes(StandardCharsets.UTF_8));
        System.setIn(inStream);
        console = new ConsoleUI();
        int input = p1.getUserInput(Type.FIFTYFIFTY, "Would you like to take the payout (0), roll less (1), or roll more (2)? (0, 1, 2): ~0~2", console);
        System.out.println(input);
        assertTrue(outBAOStream.toString(StandardCharsets.UTF_8).length() > 0);
    }
}
