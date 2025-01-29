package scrumroyale.minigames;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import scrumroyale.Dice;
import scrumroyale.Minigame.Type;
import scrumroyale.Player;
import scrumroyale.ui.ConsoleUI;
import scrumroyale.vegas.Casino;
import scrumroyale.vegas.Vegas;

public class LuckyPunchTest {

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

    private LuckyPunch luckyPunch;
    private Vegas vegas;
    private List<Casino> casinos;
    private List<Player> players;
    private mockConsole mockConsole;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private Dice dice;

    @BeforeEach
    void setUp() {
        dice = new Dice(3, false, 1);
        vegas = new Vegas(4, false, 1, false, List.of(1));
        casinos = List.of(vegas.getCasino(1), vegas.getCasino(2), vegas.getCasino(3), vegas.getCasino(4),
                vegas.getCasino(5), vegas.getCasino(6));

        player1 = new Player(dice, 1);
        // make mocked player objects
        player2 = mock(Player.class);
        player3 = mock(Player.class);
        player4 = mock(Player.class);
        // place the mocked players in the player list
        players = new ArrayList<>(Arrays.asList(player1, player2, player3, player4));
        luckyPunch = new LuckyPunch();
        mockConsole = new mockConsole();
        when(player2.getUserInput(eq(Type.LUCKYPUNCH), anyString(), any(ConsoleUI.class))).thenReturn(0);
        when(player3.getUserInput(eq(Type.LUCKYPUNCH), anyString(), any(ConsoleUI.class))).thenReturn(0);
        when(player4.getUserInput(eq(Type.LUCKYPUNCH), anyString(), any(ConsoleUI.class))).thenReturn(0); // Invalid or
                                                                                                          // losing
                                                                                                          // guess
    }

    // Base Description Check
    @Test
    void testGetDescription() {
        assertTrue(luckyPunch.getDescription().equals("Lucky Punch - Choose 1, 2, or 3 to win 2 chips, $3, or $4."),
                "description is correct");
    }

    // Execute minigame
    @Test
    void testMinigame() {
        int currentPlayer = 1; // Testing with the first player
        Player player = players.get(currentPlayer - 1); // Get the player object
        for (int coinInput = 1; coinInput <= 3; coinInput++) {
            mockConsole.forceInput(coinInput); // Simulate the player's choice
            int initialChips = player.getChips();
            int initialBalance = player.getBalance();

            // Execute the Lucky Punch minigame
            luckyPunch.execute(casinos, players, currentPlayer, mockConsole, 0);

            // Assert the expected changes
            switch (coinInput) {
                case 1:
                    assertEquals(initialChips + 2, player.getChips(), "Player should win 2 chips.");
                    assertEquals(initialBalance, player.getBalance(), "Balance should not change with input 1.");
                    break;
                case 2:
                    assertEquals(initialBalance + 3, player.getBalance(), "Player should win $3.");
                    assertEquals(initialChips, player.getChips(), "Chips should not change with input 2.");
                    break;
                case 3:
                    assertEquals(initialBalance + 4, player.getBalance(), "Player should win $4.");
                    assertEquals(initialChips, player.getChips(), "Chips should not change with input 3.");
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected input: " + coinInput);
            }
        }
    }
}
