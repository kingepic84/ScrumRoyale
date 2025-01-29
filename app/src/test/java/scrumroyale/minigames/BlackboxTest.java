package scrumroyale.minigames;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import scrumroyale.Dice;
import scrumroyale.Player;
import scrumroyale.Minigame.Type;
import scrumroyale.ui.ConsoleUI;
import scrumroyale.vegas.Casino;
import scrumroyale.vegas.Vegas;

public class BlackboxTest {

    private static class mockConsole extends ConsoleUI {
        private int forcedInput;

        @Override
        public int getMinigameInput(String prompt, int min, int max) {
            return forcedInput;
        }

        public void forceInput(int num) {
            this.forcedInput = num;
        }
    }

    private mockConsole mockConsole;
    private BlackBox blackbox;
    private Vegas vegas;
    private List<Casino> casinos;
    private List<Player> players;
    private Player picker;
    private Player sorter;
    private Dice dice;

    @BeforeEach
    void setUp(){
        dice = new Dice(3, false, 1);
        vegas = new Vegas(4, false, 1, false, List.of(1));
        casinos = List.of(vegas.getCasino(1), vegas.getCasino(2), vegas.getCasino(3), vegas.getCasino(4),
                vegas.getCasino(5), vegas.getCasino(6));
        picker = new Player(dice, 1);
        sorter = mock(Player.class);
        players = new ArrayList<>(Arrays.asList(picker,sorter));
        blackbox = new BlackBox();
        mockConsole = new mockConsole();
        //Force piles to be [0,0,2] and [0,2,6]
        when(sorter.getUserInput(eq(Type.BLACKBOX), contains("The current piles are "), any(ConsoleUI.class))).thenReturn(1).thenReturn(2).thenReturn(1).thenReturn(2).thenReturn(1).thenReturn(2);
    }

    //Test base description
    @Test
    void testGetDescription(){
        assertTrue(blackbox.getDescription().equals("Black Box - During payout, winner plays split/choose for winnings."));
    }

    //Test to make sure chips get paid out
    @Test
    void testMinigameChips() {
        int currentPlayer = 1;
        Player player = players.get(currentPlayer-1);
        mockConsole.forceInput(1); // Simulate the player's choice
        int initialChips = 0;
        int initialBalance = 0;

        blackbox.execute(casinos, players, currentPlayer, mockConsole, 0);

        assertEquals(initialChips + 2, player.getChips(), "Player should win 2 chips.");
        assertEquals(initialBalance, player.getBalance(), "Balance should not change with choice 1.");
    }

    //Test to make sure money get paid out
    @Test
    void testMinigamePayout() {
        int currentPlayer = 1;
        Player player = players.get(currentPlayer-1);
        mockConsole.forceInput(2); // Simulate the player's choice
        int initialChips = 0;
        int initialBalance = 0;

        blackbox.execute(casinos, players, currentPlayer, mockConsole, 0);

        assertEquals(initialBalance + 8, player.getBalance(), "Player should win $8.");
        assertEquals(initialChips, player.getChips(), "Chips should not change with choice 2.");
    }
}
