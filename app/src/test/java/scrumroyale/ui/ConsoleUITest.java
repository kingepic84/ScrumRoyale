package scrumroyale.ui;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import scrumroyale.Dice;
import scrumroyale.Die;
import scrumroyale.Player;
import scrumroyale.vegas.Casino;
import scrumroyale.vegas.GameState;
import scrumroyale.vegas.Vegas;

public class ConsoleUITest {
    private ConsoleUI console;
    private ConsoleUI console2;
    private ConsoleUI console3;
    private ConsoleUI console4;
    private ConsoleUI console5;
    private Vegas vegas;
    private ConsoleUI console6;
    private List<String> instructions1;
    private List<String> instructions3;
    private ByteArrayInputStream inStream;
    private InputStream systemIn = System.in;
    private PrintStream systemOut = System.out;
    private ByteArrayOutputStream outBAOStream = new ByteArrayOutputStream();
    private PrintStream outStream;

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
    void testPrintToConsole() {
        vegas = new Vegas(12, true, 1, true, List.of());
        List<Casino> casinos = List.of(vegas.getCasino(1), vegas.getCasino(2), vegas.getCasino(3), vegas.getCasino(4), vegas.getCasino(5), vegas.getCasino(6));
        List<Player> players = List.of(vegas.getPlayer(1), vegas.getPlayer(2), vegas.getPlayer(3), vegas.getPlayer(4), vegas.getPlayer(5), vegas.getPlayer(6), vegas.getPlayer(7), vegas.getPlayer(8), vegas.getPlayer(9), vegas.getPlayer(10), vegas.getPlayer(11), vegas.getPlayer(12));
        vegas.nextTurn();
        instructions1 = List.of("intro", "round start", "status update", "dice roll results");
        vegas.getPlayer(1).roll();
        inStream = new ByteArrayInputStream((vegas.getPlayer(1).getDice().getMost() + "\n")
                .getBytes(StandardCharsets.UTF_8));
        System.setIn(inStream);
        console = new ConsoleUI();
        console.display(instructions1, GameState.takeSnapshot(casinos, players, List.of(vegas.getCurrentPlayer(), vegas.getCurrentRound(), vegas.getNumRounds(), vegas.getStartingNumDice(), vegas.getPlayers(), -1), List.of(true, true)));
        int input = console.getUserInput(vegas.getPlayerObj().getDice().getDiceCounts(),vegas.getPlayerObj().getChips());
        instructions1 = List.of("user input", "round end", "results");
        console.display(instructions1, GameState.takeSnapshot(casinos, players, List.of(vegas.getCurrentPlayer(), vegas.getCurrentRound(), vegas.getNumRounds(), vegas.getStartingNumDice(), vegas.getPlayers(), input), List.of(true, true), vegas.getPlayer(1).expendDice(input).toString()));
        assertTrue(outBAOStream.toString(StandardCharsets.UTF_8).length() > 0);
    }

    @Test
    void testPrintToConsole2() {
        vegas = new Vegas(1, false, 1, true, List.of());
        List<Casino> casinos = List.of(vegas.getCasino(1), vegas.getCasino(2), vegas.getCasino(3), vegas.getCasino(4), vegas.getCasino(5), vegas.getCasino(6));
        vegas.nextTurn();
        instructions1 = List.of("intro", "round start", "status update", "dice roll results");
        vegas.getPlayer(1).roll();
        inStream = new ByteArrayInputStream((vegas.getPlayer(vegas.getCurrentPlayer()).getDice().getMost() + "\n")
                .getBytes(StandardCharsets.UTF_8));
        System.setIn(inStream);
        console2 = new ConsoleUI();
        console2.display(instructions1, GameState.takeSnapshot(casinos, List.of(vegas.getPlayer(1)), List.of(vegas.getCurrentPlayer(), vegas.getCurrentRound(), vegas.getNumRounds(), vegas.getStartingNumDice(), vegas.getPlayers(), -1), List.of(true, true)));
        int input = console2.getUserInput(vegas.getPlayerObj().getDice().getDiceCounts(),
        vegas.getPlayerObj().getChips());
        instructions1 = List.of("user input", "round end", "results");
        console2.display(instructions1, GameState.takeSnapshot(casinos, List.of(vegas.getPlayer(1)), List.of(vegas.getCurrentPlayer(), vegas.getCurrentRound(), vegas.getNumRounds(), vegas.getStartingNumDice(), vegas.getPlayers(), input), List.of(true, true), vegas.getPlayer(1).expendDice(input).toString()));
        assertTrue(outBAOStream.toString(StandardCharsets.UTF_8).length() > 0);

    }

    @Test
    void testPrintToConsole3() {
        vegas = new Vegas(2, true, 1, false, List.of());
        vegas.nextTurn();
        List<Casino> casinos = List.of(vegas.getCasino(1), vegas.getCasino(2), vegas.getCasino(3), vegas.getCasino(4), vegas.getCasino(5), vegas.getCasino(6));
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
        console3 = new ConsoleUI();
        instructions3 = List.of("intro", "round start", "status update", "dice roll results");
        console3.display(instructions3, GameState.takeSnapshot(casinos, List.of(p1, p2), List.of(vegas.getCurrentPlayer(), vegas.getCurrentRound(), vegas.getNumRounds(), vegas.getStartingNumDice(), vegas.getPlayers(), -1), List.of(false, true), ""));
        vegas.nextTurn();
        p2.addChips(2);
        int input2 = console3.getUserInput(vegas.getPlayerObj().getDice().getDiceCounts(),
        vegas.getPlayerObj().getChips());
        instructions3 = List.of("user input", "round end", "results");
        console3.display(instructions3, GameState.takeSnapshot(casinos, List.of(p1, p2), List.of(vegas.getCurrentPlayer(), vegas.getCurrentRound(), vegas.getNumRounds(), vegas.getStartingNumDice(), vegas.getPlayers(), input2), List.of(false, true), p2.expendDice(input2).toString()));
        assertTrue(outBAOStream.toString(StandardCharsets.UTF_8).length() > 0);

    }

    @Test
    void testPrintToConsole4() {
        vegas = new Vegas(2, false, 1, false, List.of());
        List<Casino> casinos = List.of(vegas.getCasino(1), vegas.getCasino(2), vegas.getCasino(3), vegas.getCasino(4), vegas.getCasino(5), vegas.getCasino(6));
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
        die13.setValue("3");
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
        p1.addChips(2);
        inStream = new ByteArrayInputStream(
                ("bob\nb\n9\n0\n" + p2.getDice().getMost() + "\n").getBytes(StandardCharsets.UTF_8));
        System.setIn(inStream);
        console4 = new ConsoleUI();
        instructions3 = List.of("intro", "round start", "status update", "dice roll results");
        console4.display(instructions3, GameState.takeSnapshot(casinos, List.of(p1, p2), List.of(vegas.getCurrentPlayer(), vegas.getCurrentRound(), vegas.getNumRounds(), vegas.getStartingNumDice(), vegas.getPlayers(), -1), List.of(false, false)));
        int input = console4.getUserInput(p1.getDice().getDiceCounts(), p1.getChips());
        if(input == 0){
            p1.expendChip();
        }
        Dice expended = p2.expendDice(input);
        instructions3 = List.of("user input");
        console4.display(instructions3, GameState.takeSnapshot(casinos, List.of(p1, p2), List.of(vegas.getCurrentPlayer(), vegas.getCurrentRound(), vegas.getNumRounds(), vegas.getStartingNumDice(), vegas.getPlayers(), input), List.of(false, false), expended.toString()));
        instructions3 = List.of("dice roll results");
        console4.display(instructions3, GameState.takeSnapshot(casinos, List.of(p1, p2), List.of(vegas.getCurrentPlayer(), vegas.getCurrentRound(), vegas.getNumRounds(), vegas.getStartingNumDice(), vegas.getPlayers(), -1), List.of(false, false)));
        int input1 = console4.getUserInput(p2.getDice().getDiceCounts(), p2.getChips());
        Dice expended2 = p2.expendDice(input1);
        casinos.get(4).addDice(expended2);
        instructions3 = List.of("user input");
        console4.display(instructions3, GameState.takeSnapshot(casinos, List.of(p1, p2), List.of(vegas.getCurrentPlayer(), vegas.getCurrentRound(), vegas.getNumRounds(), vegas.getStartingNumDice(), vegas.getPlayers(), input1), List.of(false, false), expended2.toString()));
        instructions3 = List.of("round end", "results");
        vegas.nextTurn();
        vegas.incrementRound();
        console4.display(instructions3, GameState.takeSnapshot(casinos, List.of(p1, p2), List.of(vegas.getCurrentPlayer(), vegas.getCurrentRound(), vegas.getNumRounds(), vegas.getStartingNumDice(), vegas.getPlayers(), -1), List.of(false, false)));
        assertTrue(outBAOStream.toString(StandardCharsets.UTF_8).length() > 0);
    }

    @Test
    void testPrintToConsole5() {
        vegas = new Vegas(3, false, 2, false, List.of());
        List<Casino> casinos = List.of(vegas.getCasino(1), vegas.getCasino(2), vegas.getCasino(3), vegas.getCasino(4), vegas.getCasino(5), vegas.getCasino(6));
        vegas.nextTurn();
        Player p1 = vegas.getPlayer(1);
        Player p2 = vegas.getPlayer(2);
        Player p3 = vegas.getPlayer(3);
        Dice dice1 = new Dice(0, false, 1);
        Dice dice2 = new Dice(0, false, 2);
        Dice dice3 = new Dice(0, false, 3);
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
        Die die17 = new Die(true, 3);
        Die die18 = new Die(false, 3);
        Die die19 = new Die(false, 3);
        Die die20 = new Die(false, 3);
        Die die21 = new Die(false, 3);
        Die die22 = new Die(false, 3);
        Die die23 = new Die(false, 3);
        Die die24 = new Die(false, 3);
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
        die13.setValue("3");
        dice2.addDice(die13);
        die14.setValue("5");
        dice2.addDice(die14);
        die15.setValue("4");
        dice2.addDice(die15);
        die16.setValue("4");
        dice2.addDice(die16);
        die17.setValue("4+4");
        dice3.addDice(die17);
        die18.setValue("4");
        dice3.addDice(die18);
        die19.setValue("3");
        dice3.addDice(die19);
        die20.setValue("3");
        dice3.addDice(die20);
        die21.setValue("3");
        dice3.addDice(die21);
        die22.setValue("5");
        dice3.addDice(die22);
        die23.setValue("6");
        dice3.addDice(die23);
        die24.setValue("4");
        dice3.addDice(die24);
        p1.setDice(dice1);
        p2.setDice(dice2);
        p3.setDice(dice3);
        p1.addChips(2);
        inStream = new ByteArrayInputStream(
                (p1.getDice().getMost() + "\n" + p2.getDice().getMost() + "\n" + p3.getDice().getMost() + "\n")
                        .getBytes(StandardCharsets.UTF_8));
        System.setIn(inStream);
        console5 = new ConsoleUI();
        instructions3 = List.of("intro", "round start", "status update", "dice roll results");
        console5.display(instructions3, GameState.takeSnapshot(casinos, List.of(p1, p2, p3), List.of(vegas.getCurrentPlayer(), vegas.getCurrentRound(), vegas.getNumRounds(), vegas.getStartingNumDice(), vegas.getPlayers(), -1), List.of(false, false)));
        int input = console5.getUserInput(p1.getDice().getDiceCounts(), p1.getChips());
        Dice expended = p1.expendDice(input);
        casinos.get(4).addDice(expended);
        instructions3 = List.of("user input");
        console5.display(instructions3, GameState.takeSnapshot(casinos, List.of(p1, p2, p3), List.of(vegas.getCurrentPlayer(), vegas.getCurrentRound(), vegas.getNumRounds(), vegas.getStartingNumDice(), vegas.getPlayers(), input), List.of(false, false), expended.toString()));
        instructions3 = List.of("dice roll results");
        vegas.nextTurn();
        int input2 = console5.getUserInput(p2.getDice().getDiceCounts(), p2.getChips());
        Dice expended2 = p2.expendDice(input2);
        casinos.get(4).addDice(expended2);
        console5.display(instructions3, GameState.takeSnapshot(casinos, List.of(p1, p2, p3), List.of(vegas.getCurrentPlayer(), vegas.getCurrentRound(), vegas.getNumRounds(), vegas.getStartingNumDice(), vegas.getPlayers(), input2), List.of(false, false), expended2.toString()));
        instructions3 = List.of("user input");
        console5.display(instructions3, GameState.takeSnapshot(casinos, List.of(p1, p2, p3), List.of(vegas.getCurrentPlayer(), vegas.getCurrentRound(), vegas.getNumRounds(), vegas.getStartingNumDice(), vegas.getPlayers(), -1), List.of(false, false)));
        vegas.nextTurn();
        instructions3 = List.of("dice roll results");
        console5.display(instructions3, GameState.takeSnapshot(casinos, List.of(p1, p2, p3), List.of(vegas.getCurrentPlayer(), vegas.getCurrentRound(), vegas.getNumRounds(), vegas.getStartingNumDice(), vegas.getPlayers(), -1), List.of(false, false)));
        int input3 = console5.getUserInput(p3.getDice().getDiceCounts(), p3.getChips());
        Dice expended3 = p3.expendDice(input3);
        casinos.get(4).addDice(expended3);
        p3.setDice(new Dice(0, false, 3));
        console5.display(instructions3, GameState.takeSnapshot(casinos, List.of(p1, p2, p3), List.of(vegas.getCurrentPlayer(), vegas.getCurrentRound(), vegas.getNumRounds(), vegas.getStartingNumDice(), vegas.getPlayers(), input3), List.of(false, false), expended3.toString()));
        instructions3 = List.of("user input", "round end", "results");
        console5.display(instructions3, GameState.takeSnapshot(casinos, List.of(p1, p2, p3), List.of(vegas.getCurrentPlayer(), vegas.getCurrentRound(), vegas.getNumRounds(), vegas.getStartingNumDice(), vegas.getPlayers(), -1), List.of(false, false)));
        vegas.incrementRound();
        instructions3 = List.of("round start");
        console5.display(instructions3, GameState.takeSnapshot(casinos, List.of(p1, p2, p3), List.of(vegas.getCurrentPlayer(), vegas.getCurrentRound(), vegas.getNumRounds(), vegas.getStartingNumDice(), vegas.getPlayers(), -1), List.of(false, false)));
        assertTrue(outBAOStream.toString(StandardCharsets.UTF_8).length() > 0);
    }

    @Test
    void testPrintToConsole6() {
        vegas = new Vegas(2, false, 1, true, List.of());
        List<Casino> casinos = List.of(vegas.getCasino(1), vegas.getCasino(2), vegas.getCasino(3), vegas.getCasino(4), vegas.getCasino(5), vegas.getCasino(6));
        vegas.nextTurn();
        Player p1 = vegas.getPlayer(1);
        Player p2 = vegas.getPlayer(2);
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
        die14.setValue("4");
        dice2.addDice(die14);
        die15.setValue("4");
        dice2.addDice(die15);
        die16.setValue("4");
        dice2.addDice(die16);
        p1.setDice(dice1);
        p2.setDice(dice2);
        p1.addChips(2);
        p1.addToBalance(10);
        inStream = new ByteArrayInputStream(
                (p1.getDice().getMost() + "\n" + p2.getDice().getMost() + "\n").getBytes(StandardCharsets.UTF_8));
        System.setIn(inStream);
        console6 = new ConsoleUI();
        instructions3 = List.of("intro", "round start", "status update", "dice roll results");
        console6.display(instructions3, GameState.takeSnapshot(casinos, List.of(p1, p2), List.of(vegas.getCurrentPlayer(), vegas.getCurrentRound(), vegas.getNumRounds(), vegas.getStartingNumDice(), vegas.getPlayers(), -1), List.of(true, false)));
        int input = console6.getUserInput(p1.getDice().getDiceCounts(), p1.getChips());
        Dice expended = p1.expendDice(input);
        casinos.get(4).addDice(expended);
        instructions3 = List.of("user input");
        console6.display(instructions3, GameState.takeSnapshot(casinos, List.of(p1, p2), List.of(vegas.getCurrentPlayer(), vegas.getCurrentRound(), vegas.getNumRounds(), vegas.getStartingNumDice(), vegas.getPlayers(), input), List.of(true, false), expended.toString()));
        vegas.nextTurn();
        instructions3 = List.of("dice roll results");
        console6.display(instructions3, GameState.takeSnapshot(casinos, List.of(p1, p2), List.of(vegas.getCurrentPlayer(), vegas.getCurrentRound(), vegas.getNumRounds(), vegas.getStartingNumDice(), vegas.getPlayers(), -1), List.of(true, false)));
        int input1 = console6.getUserInput(p2.getDice().getDiceCounts(), p2.getChips());
        Dice expended2 = p2.expendDice(input1);
        casinos.get(4).addDice(expended2);
        instructions3 = List.of("user input", "round end", "results");
        console6.display(instructions3, GameState.takeSnapshot(casinos, List.of(p1, p2), List.of(vegas.getCurrentPlayer(), vegas.getCurrentRound(), vegas.getNumRounds(), vegas.getStartingNumDice(), vegas.getPlayers(), input1), List.of(true, false), expended2.toString()));
        vegas.incrementRound();
        console6.display(List.of("round start"), GameState.takeSnapshot(casinos, List.of(p1, p2), List.of(vegas.getCurrentPlayer(), vegas.getCurrentRound(), vegas.getNumRounds(), vegas.getStartingNumDice(), vegas.getPlayers(), -1), List.of(true, false)));
        console6.display(List.of("nothing"), GameState.takeSnapshot(casinos, List.of(p1, p2), List.of(vegas.getCurrentPlayer(), vegas.getCurrentRound(), vegas.getNumRounds(), vegas.getStartingNumDice(), vegas.getPlayers(), -1), List.of(true, false)));
        assertTrue(outBAOStream.toString(StandardCharsets.UTF_8).length() > 0);
    }

    @Test
    void testPrintToConsole7() {
        vegas = new Vegas(2, false, 1, false, List.of());
        List<Casino> casinos = List.of(vegas.getCasino(1), vegas.getCasino(2), vegas.getCasino(3), vegas.getCasino(4), vegas.getCasino(5), vegas.getCasino(6));
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
        die13.setValue("3");
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
        p1.addChips(3);
        inStream = new ByteArrayInputStream(
                ("bob\nb\n9\n0\n" + p2.getDice().getMost() + "\n").getBytes(StandardCharsets.UTF_8));
        System.setIn(inStream);
        console4 = new ConsoleUI();
        instructions3 = List.of("intro", "round start", "status update", "dice roll results");
        console4.display(instructions3, GameState.takeSnapshot(casinos, List.of(p1, p2), List.of(vegas.getCurrentPlayer(), vegas.getCurrentRound(), vegas.getNumRounds(), vegas.getStartingNumDice(), vegas.getPlayers(), -1), List.of(false, false)));
        int input = console4.getUserInput(p1.getDice().getDiceCounts(), p1.getChips());
        if(input == 0){
            p1.expendChip();
        }
        vegas.getCasino(input + 1).setPayouts(new ArrayList<>(Arrays.asList(10, 10)));
        instructions3 = List.of("user input");
        console4.display(instructions3, GameState.takeSnapshot(casinos, List.of(p1, p2), List.of(vegas.getCurrentPlayer(), vegas.getCurrentRound(), vegas.getNumRounds(), vegas.getStartingNumDice(), vegas.getPlayers(), input), List.of(false, false)));
        instructions3 = List.of("dice roll results");
        console4.display(instructions3, GameState.takeSnapshot(casinos, List.of(p1, p2), List.of(vegas.getCurrentPlayer(), vegas.getCurrentRound(), vegas.getNumRounds(), vegas.getStartingNumDice(), vegas.getPlayers(), -1), List.of(false, false)));
        int input1 = console4.getUserInput(p2.getDice().getDiceCounts(), p2.getChips());
        instructions3 = List.of("user input");
        console4.display(instructions3, GameState.takeSnapshot(casinos, List.of(p1, p2), List.of(vegas.getCurrentPlayer(), vegas.getCurrentRound(), vegas.getNumRounds(), vegas.getStartingNumDice(), vegas.getPlayers(), input1), List.of(false, false), p2.expendDice(input1).toString()));
        instructions3 = List.of("round end", "results");
        vegas.nextTurn();
        vegas.incrementRound();
        console4.display(instructions3, GameState.takeSnapshot(casinos, List.of(p1, p2), List.of(vegas.getCurrentPlayer(), vegas.getCurrentRound(), vegas.getNumRounds(), vegas.getStartingNumDice(), vegas.getPlayers(), -1), List.of(false, false)));
        assertTrue(outBAOStream.toString(StandardCharsets.UTF_8).length() > 0);
    }
}
