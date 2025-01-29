package scrumroyale.ui;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import scrumroyale.UI;
import scrumroyale.vegas.GameState.CasinoRecord;
import scrumroyale.vegas.GameState.Minishot;
import scrumroyale.vegas.GameState.PlayerRecord;
import scrumroyale.vegas.GameState.Snapshot;

/**
 * The ConsoleUI Class to control the Console Games.
 */
public class ConsoleUI implements UI {

    // The Scanner to use for inputs.
    private Scanner scanner;

    // determines whether console uses ANSI
    private boolean ANSI = false;

    /**
     * The ConsoleUI constructor.
     * 
     */
    public ConsoleUI() {
        scanner = new Scanner(System.in, StandardCharsets.UTF_8);
    }

    /**
     * The ConsoleUI constructor.
     * 
     * @param ANSI {@code boolean} that determines whether or not the terminal uses
     *             ANSI.
     */
    // public ConsoleUI(boolean ANSI) {
    //     scanner = new Scanner(System.in, StandardCharsets.UTF_8);
    //     this.ANSI = ANSI;
    // }

    /**
     * Prints the Game to the console.
     *
     * @param printInstructions A {@code List} of the current
     *                          instructions.
     * @param snapshot          The GameState Snapshot to use.
     */
    @Override
    public void display(List<String> printInstructions, Snapshot snapshot) {
        StringBuilder print = new StringBuilder();
        for (String i : printInstructions) {
            switch (i) {
                case "intro":
                    // start of game
                    if (ANSI) {
                        // bold yellow black background
                        print.append("\u001B[1;33m");
                    }
                    print.append(printStart(snapshot));
                    // resetcolor
                    break;
                case "round start":
                    // round start
                    if (ANSI) {
                        print.append("\u001B[1;36m");
                    }
                    print.append(printRoundStart(snapshot));
                    break;
                case "status update":
                    // status update
                    if (ANSI) {
                        print.append("\u001B[1;32m");
                    }
                    print.append(printStatusUpdate(snapshot));

                    break;
                case "dice roll results":
                    // rolled die
                    if (ANSI) {
                        print.append("\u001B[1;30m");
                    }
                    print.append(printRolled(snapshot));

                    break;
                case "user input":
                    // user's input
                    if (ANSI) {
                        print.append("\u001B[0;30m");
                    }
                    print.append(printUserInput(snapshot));

                    break;
                case "round end":
                    // round ended
                    if (ANSI) {
                        print.append("\u001B[0;31m");
                    }
                    print.append(printRoundEnd(snapshot));

                    break;
                case "results":
                    // end of game results
                    if (ANSI) {
                        print.append("\u001B[1;33m");
                    }
                    print.append(printResults(snapshot));

                    break;
                default:
                    break;
            }
        }
        System.out.println(wrapLines(print.toString()));
    }

    /**
     * Prints the Game to the console.
     *
     * @param printInstructions A {@code String} of the current
     *                          instructions.
     * @param snapshot          The GameState Minishot to use.
     */
    @Override
    public void displayMinigameInfo(String printInstructions, Minishot snapshot) {
        StringBuilder printer = new StringBuilder();
        switch (printInstructions) {
            case "minigame update":
                if (ANSI) {
                    printer.append("\u001B[1;35m");
                }
                printer.append(displayMiniGameUpdate(snapshot));
                break;
            case "minigame payout":
                printer.append("\u001B[1;32m");
                printer.append(displayMiniGamePayoutGain(snapshot));
                break;
            case "minigame loss":
                printer.append("\u001B[1;32m");
                printer.append(displayMiniGamePayoutLoss(snapshot));
                break;
            default:
                break;
        }
        System.out.println(wrapLines(printer.toString()));
    }

    private StringBuilder displayMiniGameUpdate(Minishot info) {
        StringBuilder sb = new StringBuilder();
        sb.append("The minigame " + info.name() + " at Casino " + info.casino() + " has the following update: "
                + info.statusString() + ".");
        return sb;
    }

    private StringBuilder displayMiniGamePayoutGain(Minishot info) {
        StringBuilder sb = new StringBuilder();
        sb.append("P" + info.currPlayer() + " gained "
                + ((info.isChips()) ? (info.payout() + ((info.payout() != 1) ? " chips" : " chip"))
                        : ("$" + info.payout()))
                + ": " + info.name() + info.statusString() + ".");
        return sb;
    }

    private StringBuilder displayMiniGamePayoutLoss(Minishot info) {
        StringBuilder sb = new StringBuilder();
        sb.append("P" + info.currPlayer() + " lost "
                + ((info.isChips()) ? (info.payout() + ((info.payout() != 1) ? " chips" : " chip"))
                        : ("$" + info.payout()))
                + ": " + info.name() + info.statusString() + ".");
        return sb;
    }

    /**
     * Wraps an input {@code String} to 76 characters per line.
     * 
     * @param input The {@code String} to wrap.
     * @return The wrapped {@code String}.
     */
    private String wrapLines(String input) {
        List<String> lines = new ArrayList<>();
        String[] splitInput = input.split("\n", -1);
        for (String segment : splitInput) {
            lines.add(segment.isEmpty() ? "" : wrapSegment(segment));
        }
        return String.join("\n", lines);
    }

    /**
     * Wraps a single segment of string text if it is over 76 characters.
     * 
     * @param segment The segment to wrap.
     */
    private String wrapSegment(String segment) {
        List<String> lines = new ArrayList<>();
        int start = 0;
        int lineMax = 76;
        if (ANSI) {
            lineMax = 88;
        }
        while (start < segment.length()) {
            int end = Math.min(start + lineMax, segment.length());
            if (end == segment.length()) {
                lines.add(segment.substring(start));
                break;
            }
            int lastSpace = segment.lastIndexOf(' ', end);
            if (lastSpace > start) {
                lines.add(segment.substring(start, lastSpace));
                start = lastSpace + 1;
            } else {
                lines.add(segment.substring(start, end));
                start = end;
            }
        }
        return String.join("\n", lines);
    }

    /**
     * The Start of the Game {@code StringBuilder}.
     *
     * @param info The snapshot to use.
     * @return The {@code StringBuilder} to then be printed to console.
     */
    private StringBuilder printStart(Snapshot info) {
        StringBuilder startString = new StringBuilder();
        // get beginning game info
        List<Integer> gameInfo = List.of(info.vegas().numRounds(), info.vegas().numPlayers(), info.vegas().numDice(),
                (info.vegas().largeDie()) ? 1 : 0, (info.vegas().chips()) ? 2 : 0);

        startString.append("The game has started. It will consist of ");
        // round num
        startString.append(gameInfo.get(0));
        String roundString = ((int) gameInfo.get(0) == 1) ? "round" : "rounds";
        if (gameInfo.get(1) == 1) {
            startString.append(
                    String.format((" " + roundString + " and has %d player.%nEach player has "), gameInfo.get(1)));
        } else {
            startString.append(
                    String.format((" " + roundString + " and has %d players.%nEach player has "), gameInfo.get(1)));
        }
        // amount of norm die
        startString.append(String.format("%d regular dice and ", gameInfo.get(2)));
        // amount of large die
        if (!info.vegas().chips()) {
            startString.append(String.format(("%d large " + ((gameInfo.get(3) == 1) ? "die" : "dice") + ".%n"),
                    gameInfo.get(3)));
        } else {
            startString.append(
                    String.format(("%d large " + ((gameInfo.get(3) == 1) ? "die" : "dice")), gameInfo.get(3)));
            // amount of start chip
            startString.append(String.format(" with %d chips per round.%n", gameInfo.get(4)));
        }
        // minigames start here
        return startString;
    }

    /**
     * The Start of the Round {@code StringBuilder}.
     *
     * @param info The snapshot to use.
     * @return The {@code StringBuilder} to then be printed to console.
     */
    private StringBuilder printRoundStart(Snapshot info) {
        StringBuilder roundString = new StringBuilder();
        // chips go here
        int round = info.vegas().currentRound();
        for (CasinoRecord record : info.casinos()) {
            if (!record.minigame().equals("")) {
                roundString.append("Casino " + (info.casinos().indexOf(record) + 1) + " added the minigame "
                        + record.minigame().split(" - ")[0] + ".\n");
            }
        }
        if (info.vegas().chips()) {
            for (int i = 0; i < info.vegas().numPlayers(); i++) {
                roundString.append("P" + (i + 1) + " gained 2 chips: Chips for Round "
                        + ((round > 0) ? round : info.vegas().numRounds()) + ".\n");
            }
        }
        roundString.append("Round ");
        // round variable
        roundString.append((round > 0) ? round : info.vegas().numRounds());
        roundString.append(" has started.\n");

        return roundString;
    }

    /**
     * The End of the Round {@code StringBuilder}.
     *
     * @param info The snapshot to use.
     * @return The {@code StringBuilder} to then be printed to console.
     */
    private StringBuilder printRoundEnd(Snapshot info) {
        StringBuilder roundString = new StringBuilder();
        roundString.append("Round ");
        // round variables
        int currentRound = info.vegas().currentRound();
        int numRounds = info.vegas().numRounds();
        int round = (currentRound > 0) ? currentRound : numRounds;
        roundString.append(round);
        roundString.append(" has ended.\n");
        roundString.append(printStatusUpdate(info));
        roundString.append("\n");
        List<CasinoRecord> casinos = info.casinos();
        for (int i = 0; i < casinos.size(); i++) {
            if (casinos.get(5 - i).playerPayouts().size() > 0) {
                int player1 = casinos.get(5 - i).playerPayouts().get(0).get(0);
                int payout1 = casinos.get(5 - i).playerPayouts().get(0).get(1);
                if (payout1 > 0 && player1 != 0) {
                    roundString.append("P" + player1 + " gained $" + payout1 + ": Payout from Casino " + ((5 - i) + 1)
                            + " for Round " + round + ".\n");
                }
                if (casinos.get(5 - i).playerPayouts().size() > 1) {
                    int player2 = casinos.get(5 - i).playerPayouts().get(1).get(0);
                    int payout2 = casinos.get(5 - i).playerPayouts().get(1).get(1);
                    if (payout2 > 0 && player2 != 0) {
                        roundString
                                .append("P" + player2 + " gained $" + payout2 + ": Payout from Casino " + ((5 - i) + 1)
                                        + " for Round " + round + ".\n");
                    }

                }
            }
        }
        roundString.append("Round " + round + " payout has ended.");
        if (round == numRounds) {
            roundString.deleteCharAt(roundString.length() - 1);
        }
        return roundString;
    }

    /**
     * The Game Status {@code StringBuilder}.
     *
     * @param info The snapshot to use.
     * @return The {@code StringBuilder} to then be printed to console.
     */
    private StringBuilder printStatusUpdate(Snapshot info) {
        // have no blank lines between casinos
        // info
        StringBuilder sb = new StringBuilder();
        // Header for players section
        int numPlayers = info.vegas().numPlayers();
        int numCasinos = info.casinos().size();
        List<PlayerRecord> players = info.players();
        boolean noDiceleft = true;
        for (PlayerRecord player : players) {
            if (player.diceLeft() > 0) {
                noDiceleft = false;
            }
        }
        List<CasinoRecord> casinos = info.casinos();
        sb.append("----------------------------      PLAYERS      ----------------------------\n");
        int bigDiceLeft = info.vegas().numPlayers();
        for (int i = 0; i < numPlayers; i++) {
            if (!info.players().get(i).hasBigDice()) {
                bigDiceLeft--;
            }
        }
        if (bigDiceLeft == 0) {
            sb.append(" player : money  dice  chips\n");
        } else {
            sb.append(" player : money  dice  DICE  chips\n");

        }

        // Player information
        for (int i = 0; i < players.size(); i++) {
            if (bigDiceLeft > 0) {
                String formatString = (info.players().get(i).balance() > 9) ? "     P%s :    %d     %d     %d      %d%n"
                        : "     P%s :     %d     %d     %d      %d%n";
                sb.append(String.format(formatString, (i >= 3) ? 0 : i + 1, players.get(i).balance(),
                        players.get(i).hasBigDice() ? Integer.valueOf(players.get(i).diceLeft() - 1)
                                : players.get(i).diceLeft(),
                        (players.get(i).hasBigDice() ? 1 : 0), players.get(i).chipsLeft()));
            } else {
                String formatString = (players.get(i).balance() > 9) ? "     P%s :    %d     %d      %d%n"
                        : "     P%s :     %d     %d      %d%n";
                sb.append(String.format(formatString,
                        (i >= 3) ? 0 : i + 1, players.get(i).balance(), players.get(i).diceLeft(),
                        players.get(i).chipsLeft()));
            }
        }

        // Header for casinos section
        sb.append("\n----------------------------      CASINOS      ----------------------------\n");

        // Dynamically build the header for players in the casino section
        sb.append("  #   $$   $    ");
        for (int i = 0; i < players.size(); i++) {
            sb.append(String.format(" P%s  ", (i >= 3) ? 0 : i + 1));
        }
        sb.append("\n");

        // Casino information
        for (int i = 0; i < numCasinos; i++) {
            // Format the basic casino info
            if (casinos.get(i).payouts().get(0) == 10) {
                if (casinos.get(i).payouts().get(1) == 10) {
                    sb.append(String.format(" [%d]  %d  %d :  ", i + 1, casinos.get(i).payouts().get(0),
                            casinos.get(i).payouts().get(1)));
                } else {
                    sb.append(String.format(" [%d]  %d   %d :  ", i + 1, casinos.get(i).payouts().get(0),
                            casinos.get(i).payouts().get(1)));
                }
            } else {
                sb.append(String.format(" [%d]   %d   %d :  ", i + 1, casinos.get(i).payouts().get(0),
                        casinos.get(i).payouts().get(1)));
            }
            // Add the player's presence for each player
            StringBuilder playerPresence = new StringBuilder();
            playerPresence.append("                                                           ");
            if (casinos.get(i).playerPayouts().size() > 0) {
                int player1 = casinos.get(i).playerPayouts().get(0).get(0) - 1;
                player1 = (player1 < 0) ? 3 : player1;
                int payout1 = casinos.get(i).playerPayouts().get(0).get(1);
                int diceLeft = casinos.get(i).playerPayouts().get(0).get(2);
                if (payout1 > 0) {
                    playerPresence.setCharAt(5 * player1, '$');
                    playerPresence.setCharAt(5 * player1 + 1, '$');
                }
                playerPresence.setCharAt(5 * player1 + 2, Character.forDigit(diceLeft, 10));
                if (casinos.get(i).playerPayouts().size() > 1) {
                    int player2 = casinos.get(i).playerPayouts().get(1).get(0) - 1;
                    player2 = (player2 < 0) ? 3 : player2;
                    int payout2 = casinos.get(i).playerPayouts().get(1).get(1);
                    int diceLeft2 = casinos.get(i).playerPayouts().get(1).get(2);
                    if (payout2 > 0) {
                        playerPresence.setCharAt(5 * player2, '$');
                    }
                    playerPresence.setCharAt(5 * player2 + 2, Character.forDigit(diceLeft2, 10));
                    if (casinos.get(i).playerPayouts().size() > 2) {
                        for (int j = 2; j < casinos.get(i).playerPayouts().size(); j++) {
                            int p = (casinos.get(i).playerPayouts().get(j).get(0) - 1);
                            p = (p < 0) ? 3 : p;
                            playerPresence.setCharAt((5 * p) + 2,
                                    Character.forDigit(casinos.get(i).playerPayouts().get(j).get(2), 10));
                        }
                    }
                }
            }
            sb.append(playerPresence);
            sb.append("\n");
            String miniGameDesc = "";
            // adds minigame if there is one
            if (i < 3) {
                miniGameDesc = casinos.get(i).minigame();
                if (!miniGameDesc.equals("")) {
                    sb.append(" " + miniGameDesc + "\n");
                }
            }
        }
        if (!noDiceleft) {
            sb.append("\n");
        }
        return sb;

    }

    /**
     * The Dice Status {@code StringBuilder}.
     *
     * @param info The snapshot to use.
     * @return The {@code StringBuilder} to then be printed to console.
     */
    private StringBuilder printRolled(Snapshot info) {
        StringBuilder rolled = new StringBuilder();
        // player name
        PlayerRecord player = info.players().get(info.vegas().currentPlayer() - 1);
        rolled.append("P" + player.id());
        rolled.append(" rolled ");
        // values
        rolled.append(player.dice());
        rolled.append(".");
        return rolled;

    }

    /**
     * Retrieves user input from the command line.
     * 
     * @param diceAvailable The dice available to pick from.
     * @param chips         The chips the player has left.
     * @return An integer representing the input received.
     */
    @Override
    public int getUserInput(Map<Integer, String> diceAvailable, int chips) {
        String input = "";
        System.out.print("Which casino will you place ");
        if (chips > 0) {
            diceAvailable.put(0, "none");
        }
        System.out.println(
                "(" + diceAvailable.toString().substring(1, diceAvailable.toString().length() - 1).replace("=", ": ")
                        + ")?");
        input = scanner.nextLine();
        while (!isCorrectInput(input, diceAvailable)) {
            input = scanner.nextLine();
        }
        return Integer.parseInt(input);
    }

    private StringBuilder printUserInput(Snapshot info) {
        StringBuilder builder = new StringBuilder();
        PlayerRecord currPlayer = info.players().get(info.vegas().currentPlayer() - 1);
        int actualInput = info.vegas().input();
        if (actualInput == 0) {
            builder.append("P" + currPlayer.id() + " used a chip and has " + currPlayer.chipsLeft()
                    + ((currPlayer.chipsLeft() == 1) ? " chip left." : " chips left."));
        } else {
            if (currPlayer.id() != 0) {
                builder.append("P" + currPlayer.id() + " placed " + info.vegas().diceExpended() + ".\n");
            }
            builder.append("Casino " + actualInput + " gained " + info.vegas().diceExpended() + " from " + "P"
                    + currPlayer.id() + ".");
        }
        return builder;
    }

    /**
     * Retrieves user input from the command line for minigames.
     * 
     * @param prompt The prompt for the user.
     * @param min    The minimum input value.
     * @param max    The maximum input value.
     * @return An {@code int} representing the player's input.
     */
    @Override
    public int getMinigameInput(String prompt, int min, int max) {
        String input = "";
        if (!ANSI) {
            System.out.println(wrapLines(prompt));
        } else {
            System.out.println("\u001B[3;34m" + wrapLines(prompt));
        }
        input = scanner.nextLine();
        while (!isCorrectMiniInput(input, min, max)) {
            input = scanner.nextLine();
        }
        return Integer.parseInt(input);
    }

    /**
     * Checks whether the given input is both an Integer, and is within the
     * available choices.
     *
     * @param input         The input string.
     * @param diceAvailable The {@code Dice} {@code Map}.
     * @return {@code true} if input is legal, false otherwise.
     */
    private boolean isCorrectInput(String input, Map<Integer, String> diceAvailable) {
        if (!(input.length() == 1) || !Character.isDigit(input.toCharArray()[0])) {
            System.out.println("Invalid input. Please enter a valid choice from the dice choices given.");
            return false;
        } else if (!diceAvailable.keySet().contains(Integer.parseInt(input))) {
            System.out.println("Invalid input. Please enter a valid choice from the dice choices given.");
            return false;
        }
        return true;
    }

    /**
     * Checks whether the given input is both an Integer, and is within the
     * available choices.
     *
     * @param input The input string.
     * @param min   The minimum input value.
     * @param max   The maximum input value.
     * @return {@code true} if input is legal, {@code false} otherwise.
     */
    private boolean isCorrectMiniInput(String input, int min, int max) {
        if (!(input.length() == 1) || !Character.isDigit(input.toCharArray()[0])) {
            System.out.println("Invalid input. Please enter a valid choice from the choices given.");
            return false;
        } else if (Integer.parseInt(input) < min || Integer.parseInt(input) > max) {
            System.out.println("Invalid input. Please enter a valid choice from the choices given.");
            return false;
        }
        return true;
    }

    /**
     * Returns a {@code StringBuilder} that contains the final Game State.
     *
     * @param info The snapshot to use.
     * @return A {@code StringBuilder} with the results.
     */
    private StringBuilder printResults(Snapshot info) {
        StringBuilder results = new StringBuilder();
        results.append("The game is over. The final game state is below.\n");
        results.append(printStatusUpdate(info));
        results.append("\nThe final scores are as follows:\n");
        for (int i = 0; i < info.vegas().numPlayers(); i++) {
            PlayerRecord player = info.players().get(i);
            results.append("P" + player.id() + ": $");
            results.append((player.balance() + player.chipsLeft()));
            if (i + 1 < info.players().size()) {
                results.append("\n");
            }
        }
        return results;
    }
}
