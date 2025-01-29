package scrumroyale.minigames;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import scrumroyale.Dice;
import scrumroyale.Player;
import scrumroyale.ui.ConsoleUI;
import scrumroyale.vegas.Casino;
import scrumroyale.vegas.Vegas;

public class JackpotTest {
    private Jackpot jackpot;
    private Vegas vegas;
    private List<Casino> casinos;
    private List<Player> players;
    private ConsoleUI console;
    private Dice dice;

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException {
        vegas = new Vegas(3, true, 1, true, List.of(0));
        jackpot = new Jackpot();
        casinos = List.of(vegas.getCasino(1), vegas.getCasino(2), vegas.getCasino(3), vegas.getCasino(4),
                vegas.getCasino(5), vegas.getCasino(6));
        players = List.of(vegas.getPlayer(1), vegas.getPlayer(2), vegas.getPlayer(3));
        console = new ConsoleUI();

        // mock private variables
        dice = Mockito.mock(Dice.class);
        Field field = Jackpot.class.getDeclaredField("dice");
        field.setAccessible(true);
        field.set(jackpot, dice);
    }

    // base getDescription check
    @Test
    void testGetDescription() {
        assertTrue(jackpot.getDescription().equals("Jackpot - Roll doubles or 7 to win $3."), "description is correct");
    }

    // make sure description updates properly
    @Test
    void testGetDescriptionUpdates() {
        when(dice.getValues()).thenReturn(List.of("4", "3"));
        // trying to garantee jackpot increases at least once
        String desc = "";
        int moneyValue = 3;
        boolean good = false;
        jackpot.execute(casinos, players, 1, console, 1);
        try {
            desc = jackpot.getDescription();
            moneyValue = Integer.parseInt(desc.substring(desc.indexOf("$") + 1, desc.indexOf(".")));
        } catch (NumberFormatException e) {
        }

        good = moneyValue == 3;

        System.out.println("d:" + desc);
        assertTrue(good, "description changed");
    }

    // make sure description updates properly
    @Test
    void testSumSame() {
        when(dice.getValues()).thenReturn(List.of("4", "3"));
        // trying to garantee jackpot increases at least once
        String desc = "";
        int moneyValue = 3;
        boolean good = false;
        jackpot.execute(casinos, players, 1, console, 1);
        try {
            desc = jackpot.getDescription();
            moneyValue = Integer.parseInt(desc.substring(desc.indexOf("$") + 1, desc.indexOf(".")));
        } catch (NumberFormatException e) {
        }

        good = moneyValue == 3;

        System.out.println("d:" + desc);
        assertTrue(good, "description changed when there are sums");

        // same
        when(dice.getValues()).thenReturn(List.of("4", "4"));
        // trying to garantee jackpot increases at least once
        desc = "";
        moneyValue = 3;
        good = false;
        jackpot.execute(casinos, players, 1, console, 1);
        try {
            desc = jackpot.getDescription();
            moneyValue = Integer.parseInt(desc.substring(desc.indexOf("$") + 1, desc.indexOf(".")));
        } catch (NumberFormatException e) {
        }

        good = moneyValue == 3;

        System.out.println("d:" + desc);
        assertTrue(good, "description changed when values are same");
    }

    // make sure max money value caps at 8
    @Test
    void testGetDescriptionMax() {
        when(dice.getValues()).thenReturn(List.of("4", "3"));
        String desc = "";
        int moneyValue = 3;
        boolean good = true;
        int currentPlayer = 0;
        // trying to guaranteed jackpot increases to max Jackpot once and no further
        for (int i = 0; i < 9; i++) {
            currentPlayer++;
            if (currentPlayer == 4) {
                currentPlayer = 1;
            }
            jackpot.execute(casinos, players, currentPlayer, console, 1);
            try {
                desc = jackpot.getDescription();
                moneyValue = Integer.parseInt(desc.substring(desc.indexOf("$") + 1, desc.indexOf(".")));
            } catch (NumberFormatException e) {
                break;
            }
            if (moneyValue > 8) {
                good = false;
            }
        }
        assertTrue(good, "description changed and caps");
    }

    // test pay player and ExecuteMiniGame
    @Test
    void testExecuteMiniGame() {
        when(dice.getValues()).thenReturn(List.of("4", "3"));
        int currentPlayer = 1;
        // runs minigame one more time (to appease spotbugs)
        int change = 8;
        int currentPlayerBalance = 0;
        currentPlayerBalance = players.get(currentPlayer - 1).getBalance();
        for (int i = 0; i < 7; i++) {
            when(dice.getValues()).thenReturn(List.of("4", "2"));
            jackpot.execute(casinos, players, currentPlayer, console, 0);
        }
        when(dice.getValues()).thenReturn(List.of("2", "2"));
        jackpot.execute(casinos, players, currentPlayer, console, 0);

        // makes sure the playerbalance before the game plus the jackpot
        // is equal to the players current balance
        assertEquals(currentPlayerBalance + change, players.get(currentPlayer - 1).getBalance(),
                "player balance should be updated");
    }
}
