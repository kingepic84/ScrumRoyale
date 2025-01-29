package scrumroyale.minigames;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import scrumroyale.Dice;
import scrumroyale.Player;
import scrumroyale.ui.ConsoleUI;
import scrumroyale.vegas.Casino;
import scrumroyale.vegas.Vegas;

public class FiftyFiftyTest {
    
    // private class mockConsole extends ConsoleUI {
    //     private int forcedInput;

    //     @Override
    //     public int getMinigameInput(String prompt, int max, int min) {
    //         return forcedInput;
    //     }
        
    //     public void forceInput(int num){
    //         num = forcedInput;
    //     }
    // }
    
    private FiftyFifty fiftyfifty;
    private Vegas vegas;
    private List<Casino> casinos;
    private List<Player> players;
    private ConsoleUI console;

    @BeforeEach
    void setUp() {
        vegas = new Vegas(1, true, 1, true, List.of(0));
        fiftyfifty = new FiftyFifty();
        casinos = List.of(vegas.getCasino(1), vegas.getCasino(2), vegas.getCasino(3), vegas.getCasino(4),
                vegas.getCasino(5), vegas.getCasino(6));
        players = List.of(new Player(new Dice(), 1, 0, true));
        console = new ConsoleUI();
    }
                           
    // test pay player and ExecuteMiniGame
    @Test
    void testExecuteMiniGame() {
        int startingBalance = players.get(0).getBalance();
        int startingChips = players.get(0).getChips();
        int currentPlayer = 1;
        int counter = 0;
        int wins = 0;

        while (counter <= 100) {
            fiftyfifty.execute(casinos, players, currentPlayer, console, 0);
            int currentBalance = players.get(0).getBalance();
            int currentChips = players.get(0).getChips();
            if (currentBalance > startingBalance || currentChips > startingChips) {
                wins += 1;
            }
            counter++;
        }

        assertTrue(wins > 0, "Minigame Executed");
        // assertTrue(counter == 0);

    }

}
