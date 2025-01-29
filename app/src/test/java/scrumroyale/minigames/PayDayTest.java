 package scrumroyale.minigames;

 import static org.junit.jupiter.api.Assertions.assertEquals;

 import java.util.List;

 import org.junit.jupiter.api.BeforeEach;
 import org.junit.jupiter.api.Test;

 import scrumroyale.Dice;
 import scrumroyale.Player;
import scrumroyale.ui.ConsoleUI;
import scrumroyale.vegas.Casino;
import scrumroyale.vegas.Vegas;

 public class PayDayTest {
     private PayDay payDay;
     private Vegas vegas;
     private List<Casino> casinos;
     private List<Player> players;
     private ConsoleUI console;

     @BeforeEach
     void setUp(){
         payDay = new PayDay();
         vegas = new Vegas(1, false, 1, false, List.of(1));
         casinos = List.of(vegas.getCasino(1), vegas.getCasino(2), vegas.getCasino(3), vegas.getCasino(4), vegas.getCasino(5), vegas.getCasino(6));
         players = List.of(vegas.getPlayer(1));
         console = new ConsoleUI();
     }

     @Test
     void testExecuteMinigame() {
         for (Casino casino : casinos) {
             Dice dice = new Dice(3, true, 1);
             dice.rollDice();
             casino.addDice(dice);
         }
         payDay.execute(casinos, players, 1, console, 1);
         assertEquals(6, players.get(0).getBalance());
         for (Casino casino : casinos) {
             casino.resetDice();
             if(casinos.indexOf(casino)%2 == 0){
                 Dice dice = new Dice(3, true, 1);
                 dice.rollDice();
                 casino.addDice(dice);
             }
         }
         payDay.execute(casinos, players, 1, console, 1);
         assertEquals(9, players.get(0).getBalance());
         for (Casino casino : casinos) {
             casino.resetDice();
             if(casinos.indexOf(casino)%3 == 0){
                 Dice dice = new Dice(3, true, 1);
                 dice.rollDice();
                 casino.addDice(dice);
             }
         }
         payDay.execute(casinos, players, 1, console, 1);
         assertEquals(11, players.get(0).getBalance());
         for (Casino casino : casinos) {
             casino.resetDice();
             if(casinos.indexOf(casino) < 4){
                 Dice dice = new Dice(3, true, 1);
                 dice.rollDice();
                 casino.addDice(dice);
             }
         }
         payDay.execute(casinos, players, 1, console, 1);
         assertEquals(15, players.get(0).getBalance());
     }
 }
