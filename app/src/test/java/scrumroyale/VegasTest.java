package scrumroyale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import scrumroyale.vegas.Casino;
import scrumroyale.vegas.GameState;
import scrumroyale.vegas.Vegas;
import scrumroyale.vegas.GameState.CasinoRecord;
import scrumroyale.vegas.GameState.PlayerRecord;
import scrumroyale.vegas.GameState.Snapshot;
import scrumroyale.vegas.GameState.VegasRecord;

public class VegasTest {
    private Vegas vegas;
    private Player player1;
    private Player player2;
    private Player player3;
    private Casino casino1;
    private Dice dice;
    private Die die1;
    private Die die2;
    private Die die3;
    private Die die4;
    private Die die5;
    private List<Player> players;
    private List<Casino> casinos;

    @BeforeEach
    void setUp() {
        vegas = new Vegas(3, false, 1, false, List.of());
        casino1 = vegas.getCasino(1);
        casino1.setPayouts(List.of(8,3));
        player1 = vegas.getPlayer(0);
        player2 = vegas.getPlayer(2);
        player3 = vegas.getPlayer(3);
        players = List.of(player1,player2,player3);
        casinos = List.of(casino1, vegas.getCasino(2));

    }

    @Test
    void testGeneralStatusUpdate() {
        List<CasinoRecord> casTuple = List.of(
                new CasinoRecord(new ArrayList<Integer>(Arrays.asList(8, 3)), new HashMap<>(Map.of(1, Arrays.asList(1, 8, 5), 2, Arrays.asList(2, 0, 3), 3,Arrays.asList(3, 0, 3))), ""),
                new CasinoRecord(new ArrayList<Integer>(Arrays.asList(8, 3)), new HashMap<>(Map.of(1, Arrays.asList(1, 8, 5), 2, Arrays.asList(2, 0, 3), 3,Arrays.asList(3, 0, 3))), ""),
                new CasinoRecord(new ArrayList<Integer>(Arrays.asList(8, 3)), new HashMap<>(Map.of(1, Arrays.asList(1, 8, 5), 2, Arrays.asList(2, 0, 3), 3,Arrays.asList(3, 0, 3))), ""),
                new CasinoRecord(new ArrayList<Integer>(Arrays.asList(8, 3)), new HashMap<>(Map.of(1, Arrays.asList(1, 8, 5), 2, Arrays.asList(2, 0, 3), 3,Arrays.asList(3, 0, 3))), ""),
                new CasinoRecord(new ArrayList<Integer>(Arrays.asList(8, 3)), new HashMap<>(Map.of(1, Arrays.asList(1, 8, 5), 2, Arrays.asList(2, 0, 3), 3,Arrays.asList(3, 0, 3))), ""),
                new CasinoRecord(new ArrayList<Integer>(Arrays.asList(8, 3)), new HashMap<>(Map.of(1, Arrays.asList(1, 8, 5), 2, Arrays.asList(2, 0, 3), 3,Arrays.asList(3, 0, 3))), ""));
        List<PlayerRecord> pTuple = List.of(
                new PlayerRecord(1, 3, 0, 2, true, "", player1.getDice().getDiceCounts()),
                new PlayerRecord(2, 6, 0, 2, true, "", player2.getDice().getDiceCounts()),
                new PlayerRecord(3, 6, 0, 2, true, "", player3.getDice().getDiceCounts()));
        dice = new Dice(3, false, 1);
        Dice dice2 = new Dice(6, false, 2);
        Dice dice3 = new Dice(6, false, 3);
        die1 = new Die(false, 1);
        die2 = new Die(false, 1);
        die3 = new Die(false, 1);
        die4 = new Die(false, 1);
        die5 = new Die(false, 1);
        Die die6 = new Die(true, 2);
        Die die7 = new Die(false, 2);
        Die die8 = new Die(true, 3);
        Die die9 = new Die(false, 3);
        die1.setValue("4");
        die2.setValue("4");
        die3.setValue("4");
        die4.setValue("4");
        die5.setValue("4");
        die6.setValue("4+4");
        die7.setValue("4");
        die8.setValue("4+4");
        die9.setValue("4");
        dice.addDice(die1);
        dice.addDice(die2);
        dice.addDice(die3);
        dice.addDice(die4);
        dice.addDice(die5);
        dice2.addDice(die6);
        dice2.addDice(die7);
        dice3.addDice(die8);
        dice3.addDice(die9);
        player1.setDice(dice);
        player2.setDice(dice2);
        player3.setDice(dice3);
        casino1.addDice(player1.expendDice(4));
        casino1.addDice(player2.expendDice(4));
        casino1.addDice(player3.expendDice(4));
        Snapshot vegasTuple = new Snapshot(casTuple, pTuple, new VegasRecord(true, false, 1, 1, 1, 7, 3, -1, ""));
        Snapshot actual = GameState.takeSnapshot(casinos, players, List.of(vegas.getCurrentPlayer(), vegas.getCurrentRound(), vegas.getNumRounds(), vegas.getStartingNumDice(), vegas.getPlayers(), -1), List.of(true, false));
        assertEquals(vegasTuple.casinos().get(0).payouts(), actual.casinos().get(0).payouts());
        assertEquals(vegasTuple.casinos().get(0).payouts(), actual.casinos().get(0).payouts());
        for (int i = 0; i < 3; i++) {
            assertEquals(vegasTuple.players().get(i).diceLeft(), actual.players().get(i).diceLeft());
            assertEquals(vegasTuple.players().get(i).balance(), actual.players().get(i).balance());
        }
        vegas.payPlayers(casino1.getPlayerPayouts());
    }

    @Test
    void testGetCurrentPlayer() {
        assertEquals(0, vegas.getCurrentPlayer());
    }

    @Test
    void testGetPlayers() {
        assertEquals(3, vegas.getPlayers());
    }

    @Test
    void testNextTurn() {
        assertEquals(0, vegas.getCurrentPlayer());
        vegas.nextTurn();
        assertEquals(1, vegas.getCurrentPlayer());
        vegas.nextTurn();
        assertEquals(2, vegas.getCurrentPlayer());
        vegas.nextTurn();
        assertEquals(3, vegas.getCurrentPlayer());
        vegas.nextTurn();
        assertEquals(1, vegas.getCurrentPlayer());
        vegas.roll();
        vegas.getPlayer(2).setDice(new Dice(0, false, 2));
        vegas.nextTurn();
        assertEquals(3, vegas.getCurrentPlayer());
    }

    // simulates 3 players use all dice on turn. turn machine
    @Test
    void testNextTurnRoundOver() {
        assertEquals(0, vegas.getCurrentPlayer());
        int player = vegas.getCurrentPlayer();
        Dice empty = new Dice(1, false, vegas.getCurrentPlayer());
        vegas.getPlayer(player+1).setDice(empty);
        vegas.nextTurn();
        assertEquals(1, vegas.getCurrentPlayer());
        empty = new Dice(1, false, vegas.getCurrentPlayer());
        player = vegas.getCurrentPlayer();
        vegas.getPlayer(player+1).setDice(empty);
        vegas.nextTurn();
        assertEquals(2, vegas.getCurrentPlayer());
        empty = new Dice(0, false, vegas.getCurrentPlayer());
        player = vegas.getCurrentPlayer();
        vegas.getPlayer(1).setDice(empty);
        vegas.getPlayer(2).setDice(empty);
        vegas.getPlayer(player+1).setDice(empty);
        assertEquals(-1, vegas.nextTurn());
    }

    @Test
    void testNextTurnRoundOver2() {
        vegas = new Vegas(3, false, 2, false, List.of());
        assertEquals(1, vegas.getCurrentRound());
        Dice empty = new Dice(0, false, 1);
        Dice empty2 = new Dice(0, false, 2);
        Dice empty3 = new Dice(0, false, 3);
        vegas.getPlayer(1).setDice(empty);
        vegas.getPlayer(2).setDice(empty2);
        vegas.getPlayer(3).setDice(empty3);
        vegas.incrementRound();
        assertEquals(2, vegas.getCurrentRound());
    }

    @Test //one player
    void testNextTurnOne() {
        vegas = new Vegas(1, false, 2, false, List.of());
        Dice empty = new Dice(0, false, 1);
        vegas.getPlayer(1).setDice(empty);
        vegas.nextTurn();
        vegas.incrementRound();
        assertEquals(2, vegas.getCurrentRound());
    }

    @Test
    void testNextTurnGameOver() {
        assertEquals(1, vegas.getCurrentRound());
        int player = vegas.getCurrentPlayer();
        Dice empty = new Dice(1, false, vegas.getCurrentPlayer());
        vegas.getPlayer(player+1).setDice(empty);
        vegas.nextTurn();
        empty = new Dice(1, false, vegas.getCurrentPlayer());
        player = vegas.getCurrentPlayer();
        vegas.getPlayer(player+1).setDice(empty);
        vegas.nextTurn();
        empty = new Dice(0, false, vegas.getCurrentPlayer());
        player = vegas.getCurrentPlayer();
        vegas.getPlayer(1).setDice(empty);
        vegas.getPlayer(2).setDice(empty);
        vegas.getPlayer(player+1).setDice(empty);
        vegas.incrementRound();
        assertEquals(-1, vegas.getCurrentRound());
    }

    @Test
    void getNumRoundsTest() {
        assertEquals(1, vegas.getNumRounds());
    }

    @Test
    void getNumRoundsTest2() {
        vegas = new Vegas(3, false, 2, false, List.of());
        assertEquals(2, vegas.getNumRounds());
    }

    // startingDice
    @Test
    void startingDiceTest() {
        assertEquals(7, vegas.getStartingNumDice());
        vegas = new Vegas(3, true, 2, true, List.of());
        assertTrue(vegas.isChipsGame());
        assertEquals(8, vegas.getStartingNumDice());
    }
   
    @Test
    void testGetGameInfo(){
        vegas = new Vegas(3, true, 2, false, List.of());
        List<Integer> lst = vegas.getGameInfo();
        assertEquals(6, lst.size());
        vegas = new Vegas(3, false, 2, false, List.of());
        List<Integer> lst2 = vegas.getGameInfo();
        assertEquals(6, lst2.size());
        vegas = new Vegas(3, true, 2, false, List.of());
        List<Integer> lst3 = vegas.getGameInfo(-1);
        assertEquals(6, lst3.size());
        vegas = new Vegas(3, false, 2, false, List.of());
        List<Integer> lst4 = vegas.getGameInfo(-1);
        assertEquals(6, lst4.size());
        List<Boolean> lst5 = vegas.getBoolInfo();
        assertEquals(2, lst5.size());
    }
    
    @Test
    void testPlay(){
        vegas = new Vegas(3, true, 3, true, List.of(0, 1, 2));
        vegas.getPlayer(1).setAI();
        ByteArrayOutputStream outBAOStream = new ByteArrayOutputStream();
        PrintStream outStream = new PrintStream(outBAOStream, false, StandardCharsets.UTF_8);
        System.setOut(outStream);
        vegas.play();
        assertTrue(outBAOStream.toString(StandardCharsets.UTF_8).length() > 0);
        vegas = new Vegas(3, true, 3, true, List.of(3, 4, 5));
        vegas.getPlayer(1).setAI();
        ByteArrayOutputStream outBAOStream2 = new ByteArrayOutputStream();
        PrintStream outStream2 = new PrintStream(outBAOStream2, false, StandardCharsets.UTF_8);
        System.setOut(outStream2);
        vegas.play();
        assertTrue(outBAOStream2.toString(StandardCharsets.UTF_8).length() > 0);

    }
}
