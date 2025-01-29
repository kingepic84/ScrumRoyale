package scrumroyale.minigames;

import java.util.List;

import scrumroyale.Minigame;
import scrumroyale.Player;
import scrumroyale.RNG;
import scrumroyale.UI;
import scrumroyale.vegas.Casino;
import scrumroyale.vegas.GameState;
import scrumroyale.vegas.GameState.Minishot;
import scrumroyale.vegas.Vegas.Effect;

/////////////////////////////////////////////////////////////////////////////////////////////////*
// * AI opponents will play this game as follows:                                              //*
// * If the payout is $0, they roll.                                                           //*
// * If the mark is between 5 and 8, inclusive, they stop and take the payout.                 //*
// * If the mark is less than 5, they choose higher/more.                                      //*
// * If the mark is greater than 8, they choose lower/less.                                    //*
// * The informative prompt for players should contain the following:                          //*
//   "Would you like to take the payout (0), roll less (1), or roll more (2)? (0, 1, 2): "     //*
//                                                                                             //*
// * Game update to console:                                                                   //*
//   "The minigame Fifty Fifty at Casino 6 has the following update: P3's choice               //*
//   was more. The old mark was 6 and the new mark is 10. The payout is 1 chip."               //*
/////////////////////////////////////////////////////////////////////////////////////////////////*

/* 
// build command: ./gradlew build
// build cucumber command: ./gradlew build cucumber 
// run game with Fifty Fifty command: java -jar app/build/libs/ScrumRoyale.jar phase7
*/

/**
 * Fifty Fifty class. The Fifty Fifty minigame implementation.
 */
public class FiftyFifty extends Minigame {

  /** The Random Number Generator */
  private RNG rand = RNG.getInstance();

  /**
   * The Fifty Fifty constructor.
   */
  public FiftyFifty() {
    super("Fifty Fifty", "Keep rolling versus target or stop and take payout.", Effect.ACTIVATED);
  }

  @Override
  public void execute(List<Casino> casinos, List<Player> players, int player, UI console, int casino) {

    // initalize variables
    boolean isPlaying = true; // initalize the boolean variable to control the minigame run status
    int payout = 0; // initalize the game's payout
    int station = 0; // initalize the station counter, which will effect the current payout
    int oldmark; // initalize the old (or previous) mark value
    String decision = "NA"; // initalize the decision string
    String prompt = "Would you like to take the payout (0), roll less (1), or roll more (2)? ~0~2"; // prompt for user
                                                                                                    // input, if a
                                                                                                    // cucumber failure
                                                                                                    // occurs, add "(0,
                                                                                                    // 1, 2): "
    String info = "This will be the info string.";
    Player minigamePlayer = players.get(player - 1); // initalize the minigame player

    // the initial roll value:
    int mark = rand.nextInt(1, 7) + rand.nextInt(1, 7); // find the most efficient to get correct odds for rolls

    while (isPlaying) {
      station++; // increment station counter

      switch (station) {
        case 1:
          payout = 0; // sets playout to $0
          break;

        case 2:
          payout = 1; // sets playout to $1 (handled as 1 chip)
          break;

        case 3:
          payout = 3; // sets playout to $3
          break;

        case 4:
          payout = 4; // sets playout to $4
          break;

        case 5:
          payout = 6; // sets playout to $6
          break;

        default:
          System.out.println("An invalid station number is causing an issue with assigning payout.");
      }

      if (station == 5) { // ends the game because the final station has been reached
        isPlaying = false;
        minigamePlayer.addToBalance(payout); // adds $6 to the player's balance
        break;
      }

      // gets the player's decision: stop (0), less (1), or more (2)
      int choice = minigamePlayer.getUserInput(Type.FIFTYFIFTY,
          "The current payout would be $" + payout + " and the mark is " + mark + ".\n" + prompt, console);
      // System.out.println("The current payout would be $" + payout + " and the mark
      // is " + mark +".\n" + prompt); // debugging
      if (choice == 0) {
        isPlaying = false; // the player chose to stop
        decision = "stop";
        if (payout == 1) { // if statement to handle the chip payout
          minigamePlayer.addChips(1); // adds the chip to the player's balance
        } else {
          minigamePlayer.addToBalance(payout); // adds the current payout to the player's balance
        }
      } else if (choice == 1) {
        isPlaying = true; // continues playing
        decision = "less"; // sets roll decision string for the update
      } else if (choice == 2) {
        isPlaying = true;
        decision = "more";
      }

      // stores the old mark and rolls a new mark value
      oldmark = mark;
      mark = rand.nextInt(1, 7) + rand.nextInt(1, 7);

      // check the player's choice and outcome
      if (choice == 1) { // the player chose less
        if (mark < oldmark) { // result was less
          isPlaying = true;
          switch (station + 1) {
            case 1:
              payout = 0; // sets playout to $0
              break;

            case 2:
              payout = 1; // sets playout to $1 (handled as 1 chip)
              break;

            case 3:
              payout = 3; // sets playout to $3
              break;

            case 4:
              payout = 4; // sets playout to $4
              break;

            case 5:
              payout = 6; // sets playout to $6
              break;

            default:
              System.out.println("An invalid station number is causing an issue with assigning payout.");
          }
        } else { // result was more OR push
          isPlaying = false;
          payout = 0;
        }
      } else if (choice == 2) { // the player chose more
        if (mark > oldmark) { // result was more
          isPlaying = true;
          switch (station + 1) {
            case 1:
              payout = 0; // sets playout to $0
              break;

            case 2:
              payout = 1; // sets playout to $1 (handled as 1 chip)
              break;

            case 3:
              payout = 3; // sets playout to $3
              break;

            case 4:
              payout = 4; // sets playout to $4
              break;

            case 5:
              payout = 6; // sets playout to $6
              break;

            default:
              System.out.println("An invalid station number is causing an issue with assigning payout.");
          }
        } else { // results was more OR push
          isPlaying = false;
          payout = 0;
        }
      }

      // output the update message before continuing
      if (choice == 0) {
        if (payout == 1) {
          info = "P" + player + "'s choice was stop. The payout is 1 chip";
        } else {
          info = "P" + player + "'s choice was stop. The payout is $" + payout;
        }
      } else {
        info = "P" + player + "'s choice was " + decision + ". The old mark was " + oldmark + " and the new mark is "
            + mark + ". The payout is $" + payout;
      }

      // minishot update
      boolean chipPayout = payout == 1; // set chipPayout to true/false depending on payout status
      Minishot minishotUpdate = GameState.takeMinishot(casinos, casino, players, this, info, player, chipPayout,
          payout); // take minishot
      console.displayMinigameInfo("minigame update", minishotUpdate); // display minigame update
    }

    boolean chipPayout = payout == 1;
    Minishot minishotUpdate2 = GameState.takeMinishot(casinos, casino, players, this, "", player, chipPayout, payout); // take
                                                                                                                       // minishot
    console.displayMinigameInfo("minigame payout", minishotUpdate2); // display minigame update

    // System.out.println("-------------------------------------------------"); //
    // debugging

  }

}
