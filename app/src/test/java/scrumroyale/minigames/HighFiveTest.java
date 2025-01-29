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

 public class HighFiveTest {
     private HighFive highfive;
     private Vegas vegas;
     private List<Casino> casinos;
     private List<Player> players;
     private ConsoleUI console;

     @BeforeEach
     void setUp(){
         highfive = new HighFive();
         vegas = new Vegas(3, false, 4, false, List.of(4));
         casinos = List.of(vegas.getCasino(1), vegas.getCasino(2), vegas.getCasino(3), vegas.getCasino(4), vegas.getCasino(5), vegas.getCasino(6));
         players = List.of(vegas.getPlayer(1),vegas.getPlayer(2),vegas.getPlayer(3));
         console = new ConsoleUI();
     }

     @Test
     void testExecuteMinigame() {
         for (Casino casino : casinos) {
             Dice dice = new Dice(1, false, 1);
             dice.rollDice();
             casino.addDice(dice);
         }
         highfive.execute(casinos, players, 1, console, 1);
         assertEquals(0,vegas.getPlayer(1).getBalance());
         Dice dice5= new Dice(5,false, 1);
         vegas.getCasino(1).addDice(dice5);
         highfive.execute(casinos, players, 1, console, 1);
         assertEquals(10, vegas.getPlayer(1).getBalance());
     }
    
     @Test
     void testDescription() {
         for (Casino casino : casinos) {
             Dice dice = new Dice(1, false, 1);
             dice.rollDice();
             casino.addDice(dice);
         }
         assertTrue(highfive.getDescription().equals("High Five - The first player to place at least 5 dice here wins $10."));
         Dice dice5= new Dice(5,false, 1);
         vegas.getCasino(1).addDice(dice5);
         highfive.execute(casinos, players, 1, console, 1);
         assertTrue(highfive.getDescription().equals("High Five - The payout has already been claimed."));
     }
    
     @Test
     void testNoPayAfterClaim() {
         for (Casino casino : casinos) {
             Dice dice = new Dice(1, true, 1);
             dice.rollDice();
             casino.addDice(dice);
         }
         assertTrue(highfive.getDescription().equals( "High Five - The first player to place at least 5 dice here wins $10."));
         highfive.execute(casinos, players, 1, console, 1);
         assertEquals(vegas.getPlayer(1).getBalance(),0);
         Dice dice5= new Dice(5,false, 1);
         vegas.getCasino(1).addDice(dice5);
         highfive.execute(casinos, players, 1, console, 1);
         assertEquals(vegas.getPlayer(1).getBalance(),10);
         assertTrue(highfive.getDescription().equals("High Five - The payout has already been claimed."));
         Dice dice4= new Dice(5,false, 2);
         vegas.getCasino(1).addDice(dice4);
         highfive.execute(casinos, players, 1, console, 1);
         assertEquals(10,vegas.getPlayer(1).getBalance());
         assertEquals(0,vegas.getPlayer(2).getBalance());
     }
 }
