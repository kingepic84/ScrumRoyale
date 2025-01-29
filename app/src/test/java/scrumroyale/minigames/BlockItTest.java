package scrumroyale.minigames;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
//use to many mock commands save time import all until completed
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import scrumroyale.Dice;
import scrumroyale.Minigame.Type;
import scrumroyale.Player;
import scrumroyale.ui.ConsoleUI;
import scrumroyale.vegas.Casino;
import scrumroyale.vegas.Vegas;

public class BlockItTest {

    private BlockIt blockit;
    private Vegas vegas;
    private List<Casino> casinos;
    private List<Player> players;
    private ConsoleUI console;
    private Dice dice1;
    private Dice dice2;
    private Dice dice3;
    private Player player1;
    private Player player2;
    private Player player3;

    @BeforeEach
    void setUp() {
        vegas = new Vegas(3, false, 1, false, List.of(1));
        casinos = List.of(vegas.getCasino(1), vegas.getCasino(2), vegas.getCasino(3), vegas.getCasino(4),
                vegas.getCasino(5), vegas.getCasino(6));
        // make mocked player objects
        player1 = mock(Player.class);
        player2 = mock(Player.class);
        player3 = mock(Player.class);
        // place the mocked players in the player list
        players = new ArrayList<>(Arrays.asList(player1, player2, player3));
        console = new ConsoleUI();
        blockit = new BlockIt();
        // make dice to put on casinos
        dice1 = new Dice(1, false, 1);
        dice2 = new Dice(3, false, 2);
        dice3 = new Dice(2, false, 3);
        when(player1.getDice()).thenReturn(dice1);    
        when(player2.getDice()).thenReturn(dice2);    
        when(player3.getDice()).thenReturn(dice3);    
    }

    @Test
    void testGetDescription() {
        System.out.println(blockit.getDescription());
        assertTrue(blockit.getDescription().equals("Block It - Place blank dice on a casino. [1,1,2,2,3]"),
                "the description is wrong");
    }

    // need to include vegas to make sure vegas calls right person to play
    // @Test
    // void winnerPlayTest(){
    // private Dice dice1=new dice(3,false,1);
    // private Dice dice2=new dice(4,false,1);
    // private Dice dice3=new dice(5,false,1);
    // casinos.get(0).addDice(dice1);
    // casinos.get(0).addDice(dice2);
    // casinos.get(0).addDice(dice3);
    //
    // }

    @Test
    void diceWorkTest() {
        // player 2 places 3 die on casino 6
        casinos.get(5).addDice(dice2);
        // player 3 places dice on casino 1 to play blockIT
        casinos.get(0).addDice(dice3);
        // player 1 places 1 die on casino 6
        casinos.get(5).addDice(dice1);
        // player 3 places 2 dice on casino 6
        casinos.get(5).addDice(dice1);

        // player 2 should be winning the casino
        System.out.println(casinos.get(5).getPlayerPayouts());
        assertEquals(2, casinos.get(5).getPlayerPayouts().get(0).get(0));

        // pick pile 3 then casino 6
        when(player3.getUserInput(eq(Type.BLOCKIT), contains("The dice pool options are "), any(ConsoleUI.class))).thenReturn(3);
        when(player3.getUserInput(eq(Type.BLOCKIT), contains("Which casino will you add "), any(ConsoleUI.class))).thenReturn(6);
        blockit.execute(casinos, players, 3, console, 1);

        // player 1 should now be winning the casino
        System.out.println(casinos.get(5).getPlayerPayouts());
        assertEquals(1, casinos.get(5).getPlayerPayouts().get(0).get(0), "Block it dice do not block");
    }

    @Test
    void playersListIncreaseTest() {
        // player 2 places 3 die on casino 6
        casinos.get(5).addDice(dice2);
        // player 3 places dice on casino 1 to play blockIT
        casinos.get(0).addDice(dice3);
        // player 1 places 1 die on casino 6
        casinos.get(5).addDice(dice1);
        // player 3 places 2 dice on casino 6
        casinos.get(5).addDice(dice1);

        // player 2 should be winning the casino
        System.out.println(casinos.get(5).getPlayerPayouts());
        assertEquals(2, casinos.get(5).getPlayerPayouts().get(0).get(0));

        // pick pile 3 then casino 6
        when(player3.getUserInput(eq(Type.BLOCKIT), contains("The dice pool options are "), any(ConsoleUI.class))).thenReturn(3);
        when(player3.getUserInput(eq(Type.BLOCKIT), contains("Which casino will you add "), any(ConsoleUI.class))).thenReturn(6);
        blockit.execute(casinos, players, 3, console, 1);

        //the player list should increase by 1
        assertEquals(4, players.size(), "List of players does not increase.");
    }
    
    @Test
    void updatePileAfterRemoveTest() {
        // player 2 places 3 die on casino 6
        casinos.get(5).addDice(dice2);
        // player 3 places dice on casino 1 to play blockIT
        casinos.get(0).addDice(dice3);
        // player 1 places 1 die on casino 6
        casinos.get(5).addDice(dice1);
        // player 3 places 2 dice on casino 6
        casinos.get(5).addDice(dice1);

        // player 2 should be winning the casino
        assertEquals(2, casinos.get(5).getPlayerPayouts().get(0).get(0));

        // pick pile 3 then casino 6
        when(player3.getUserInput(eq(Type.BLOCKIT), contains("The dice pool options are "), any(ConsoleUI.class))).thenReturn(3);
        when(player3.getUserInput(eq(Type.BLOCKIT), contains("Which casino will you add "), any(ConsoleUI.class))).thenReturn(6);
        blockit.execute(casinos, players, 3, console, 1);

        // player 1 should now be winning the casino
        assertEquals(1, casinos.get(5).getPlayerPayouts().get(0).get(0));
        assertTrue(blockit.getDescription().equals("Block It - Place blank dice on a casino. [1,1,2,2]"),
                "the description should change when piles are removed.");

    }

    @Test
    void pileGoneTest() {
        // player 2 places 3 die on casino 6
        casinos.get(5).addDice(dice2);
        // player 3 places dice on casino 1 to play blockIT
        casinos.get(0).addDice(dice3);
        // player 1 places 1 die on casino 6
        casinos.get(5).addDice(dice1);
        // player 3 places 2 dice on casino 6
        casinos.get(5).addDice(dice1);
        // player 1 places 2 die on casino 5
        casinos.get(4).addDice(dice1);
        casinos.get(4).addDice(dice1);
        // player 3 places 1 dice on casino 5
        Dice dice32 = new Dice(1, false, 3);
        when(player3.getDice()).thenReturn(dice32);
        casinos.get(4).addDice(dice32);

        // player 2 should be winning the casino 6
        assertEquals(2, casinos.get(5).getPlayerPayouts().get(0).get(0));
        // player 1 should be winning the casino 5
        assertEquals(1, casinos.get(4).getPlayerPayouts().get(0).get(0));

        // pick pile 3 then casino 6
        when(player3.getUserInput(eq(Type.BLOCKIT), contains("The dice pool options are "), any(ConsoleUI.class))).thenReturn(3);
        when(player3.getUserInput(eq(Type.BLOCKIT), contains("Which casino will you add "), any(ConsoleUI.class))).thenReturn(6);
        blockit.execute(casinos, players, 3, console, 1);

        // player 1 should now be winning the casino
        assertEquals(1, casinos.get(5).getPlayerPayouts().get(0).get(0));
        assertTrue(blockit.getDescription().equals("Block It - Place blank dice on a casino. [1,1,2,2]"),
                "the description should change when piles are removed.");

        // pick pile 3 then casino 6
        when(player3.getUserInput(eq(Type.BLOCKIT), contains("The dice pool options are "), any(ConsoleUI.class))).thenReturn(3).thenReturn(2);
        when(player3.getUserInput(eq(Type.BLOCKIT), contains("Which casino will you add "), any(ConsoleUI.class))).thenReturn(5);
        blockit.execute(casinos, players, 3, console, 1);
        // player 1 should still be winning casino 6
        assertEquals(1, casinos.get(5).getPlayerPayouts().get(0).get(0));
        // player 3 should be winning casino 5 now
        assertEquals(3, casinos.get(4).getPlayerPayouts().get(0).get(0));
    }
    
    @Test
    void allPileGoneTest(){
        //player 2 places 3 die on casino 6
        casinos.get(5).addDice(dice2);
        //player 3 places dice on casino 1 to play blockIT
        casinos.get(0).addDice(dice3);
        //player 1 places 1 die on casino 6
        casinos.get(5).addDice(dice1);
        //player 3 places 2 dice on casino 6
        casinos.get(5).addDice(dice3);
        
        //player 2 should be winning the casino
        assertEquals(2,casinos.get(5).getPlayerPayouts().get(0).get(0));
        
        //pick all piles
        when(player3.getUserInput(eq(Type.BLOCKIT), contains("The dice pool options are "), any(ConsoleUI.class))).thenReturn(3).thenReturn(2).thenReturn(2).thenReturn(1).thenReturn(1);
        when(player3.getUserInput(eq(Type.BLOCKIT), contains("Which casino will you add"), any(ConsoleUI.class))).thenReturn(6).thenReturn(4).thenReturn(4).thenReturn(2).thenReturn(1);
        //off by one for player
        blockit.execute(casinos,players,3,console,1);
        blockit.execute(casinos,players,3,console,1);
        blockit.execute(casinos,players,3,console,1);
        blockit.execute(casinos,players,3,console,1);
        blockit.execute(casinos,players,3,console,1);
        
        assertTrue(blockit.getDescription().equals("Block It - Place blank dice on a casino. []"));
            Map<Integer, List<Integer>> oldCasino1 = casinos.get(0).getPlayerPayouts();
        Map<Integer, List<Integer>> oldCasino2 = casinos.get(1).getPlayerPayouts();
        Map<Integer, List<Integer>> oldCasino3 = casinos.get(2).getPlayerPayouts();
        Map<Integer, List<Integer>> oldCasino4 = casinos.get(3).getPlayerPayouts();
        Map<Integer, List<Integer>> oldCasino5 = casinos.get(4).getPlayerPayouts();
        Map<Integer, List<Integer>> oldCasino6 = casinos.get(5).getPlayerPayouts();
        blockit.execute(casinos,players,3,console,1);
        assertTrue(oldCasino1.equals(casinos.get(0).getPlayerPayouts()));
        assertTrue(oldCasino2.equals(casinos.get(1).getPlayerPayouts()));
        assertTrue(oldCasino3.equals(casinos.get(2).getPlayerPayouts()));
        assertTrue(oldCasino4.equals(casinos.get(3).getPlayerPayouts()));
        assertTrue(oldCasino5.equals(casinos.get(4).getPlayerPayouts()));
        assertTrue(oldCasino6.equals(casinos.get(5).getPlayerPayouts()));
        
        
        
    }

}
