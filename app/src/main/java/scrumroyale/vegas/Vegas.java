package scrumroyale.vegas;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import scrumroyale.Dice;
import scrumroyale.Minigame;
import scrumroyale.Player;
import scrumroyale.UI;
import scrumroyale.minigames.Registry;
import scrumroyale.ui.ConsoleUI;
import scrumroyale.vegas.GameState.Snapshot;

/**
 * Vegas class. Controls most of the game logic.
 */
public class Vegas {

    // The current number of players.
    private int numPlayers;

    // A {@code List} of {@code Player}s.
    private List<Player> players = new ArrayList<Player>();

    // A {@code List} of {@code Casino}s.
    private List<Casino> casinos = new ArrayList<Casino>();

    // The Card Dealer.
    private Dealer dealer = new Dealer();

    // The current player whose turn it is currently.
    private int currentPlayer = -1;

    // The current round.
    private int currentRound = 1;

    // The number of rounds.
    private int numRounds;

    // Whether there is a Large die or not in this game.
    private boolean largeDie;

    // The ConsoleUI Instance.
    private UI userInterface;

    // Whether or not this game uses chips.
    private boolean chips;

    // The first player in the player order that still has dice in their hand.
    private int firstPlayer = 0;

    /** Minigame Enum. Represents the activation condition of each minigame. */
    public enum Effect {
        /** Before Payout. */
        BEFORE_PAYOUT,
        /** When Activated */
        ACTIVATED,
        /** Only happens once. */
        TRANSITORY,
    }

    // Map of Minigames and which casino they correspond to.
    private Map<Integer, Minigame> minigameMap;

    /** List of minigames represented as integers. */
    private List<Integer> games;

    /**
     * Construct a new game of Las Vegas Royale.
     * 
     * @param numPlayers The number of players for this game.
     * @param largeDie   Whether a large die is used or not.
     * @param numRounds  The number of rounds for this game.
     * @param chipsUsed  Whether chips are used in this game or not.
     * @param games      List of minigames used in this game of Scrum Vegas Royale.
     */
    public Vegas(int numPlayers, boolean largeDie, int numRounds, boolean chipsUsed, List<Integer> games) {

        userInterface = new ConsoleUI();
        Map<Integer, List<Integer>> dealings = dealer.dealToVegas(12, 2);
        for (int i = 0; i < 6; i++) {
            casinos.add(new Casino(dealings.get(5 - i)));
        }
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player(new Dice(largeDie ? 8 : 7, largeDie, i + 1), i + 1, (i == 0) ? false : true));
        }
        this.numPlayers = numPlayers;
        this.numRounds = numRounds;
        this.largeDie = largeDie;
        chips = chipsUsed;
        minigameMap = Registry.assignMinigames(games);
        this.games = games.stream().toList();
    }

    /**
     * Construct a new game of Las Vegas Royale.
     * 
     * @param numPlayers The number of players for this game.
     * @param largeDie   Whether a large die is used or not.
     * @param numRounds  The number of rounds for this game.
     * @param chipsUsed  Whether chips are used in this game or not.
     * @param games      List of minigames used in this game of Scrum Vegas Royale.
     * @param type       type of console to use: 1 for terminal, 2 for terminal with
     *                   ANSI, 3 for GUI
     */
    // public Vegas(int numPlayers, boolean largeDie, int numRounds, boolean chipsUsed, List<Integer> games, int type) {
    //     userInterface = (type == 1) ? new ConsoleUI(false) : new ConsoleUI(true);
    //     Map<Integer, List<Integer>> dealings = dealer.dealToVegas(12, 2);
    //     for (int i = 0; i < 6; i++) {
    //         casinos.add(new Casino(dealings.get(5 - i)));
    //     }
    //     for (int i = 0; i < numPlayers; i++) {
    //         players.add(new Player(new Dice(largeDie ? 8 : 7, largeDie, i + 1), i + 1, (i == 0) ? false : true));
    //     }
    //     this.numPlayers = numPlayers;
    //     this.numRounds = numRounds;
    //     this.largeDie = largeDie;
    //     chips = chipsUsed;
    //     minigameMap = Registry.assignMinigames(games);
    //     this.games = games.stream().toList();
    // }

    /**
     * Gets the current player whose turn it is.
     * 
     * @return The current player's PlayerID.
     */
    public int getCurrentPlayer() {
        return currentPlayer + 1;
    }

    /**
     * Gets the number of players.
     * 
     * @return An integer representing the number of players.
     */
    public int getPlayers() {
        return numPlayers;
    }

    /**
     * Gets a certain player who is playing the game.
     * 
     * @param player The player to get, as an integer.
     * @return The {@code Player} object associated with the {@code player} param.
     */
    public Player getPlayer(int player) {
        if (player <= 0) {
            player = 1;
        }
        return players.get(player - 1);
    }

    /**
     * Moves the game to the next player, incrementing {@code currentPlayer} as
     * necessary.
     * 
     * @return {@code currentPlayer} if successfully moved to the next turn,
     *         {@code -1} otherwise.
     */
    public int nextTurn() {
        if (!isRoundOver()) {
            currentPlayer++;
            if (currentPlayer == numPlayers) {
                currentPlayer = currentPlayer % numPlayers;
            }
            if (players.get(currentPlayer).getDice().size() == 0) {
                nextTurn();
            }
            return currentPlayer;
        }
        return -1;
    }

    /**
     * Increments the current round in the game.
     */
    public void incrementRound() {
        currentRound++;
        if (currentRound > numRounds) {
            currentRound = -1;
        }
        currentPlayer = -1;
        firstPlayer = 0;
        if (currentRound != -1) {
            resetPlayersAndCasinos();
            minigameMap = Registry.assignMinigames(games);
            if (players.size() > numPlayers) {
                players.remove(numPlayers);
            }
        }
    }

    /**
     * Gets a {@code Casino} object from the current list of {@code Casino}s in the
     * game.
     * 
     * @param casino The Casino to get (1-6).
     * @return The {@code Casino} object associated with that index.
     */
    public Casino getCasino(int casino) {
        return casinos.get(casino - 1);
    }

    /**
     * Rolls the current player's dice.
     */
    public void roll() {
        getPlayer(currentPlayer + 1).roll();
    }

    /**
     * Checks whether the round is over or not.
     * 
     * @return Whether the round is over or not.
     */
    private boolean isRoundOver() {
        for (Player player : players) {
            if (player.getDice().size() != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the current round the game is at.
     * 
     * @return The current game round.
     */
    public int getCurrentRound() {
        return currentRound;
    }

    /**
     * Gets the total number of rounds in this game.
     * 
     * @return The number of rounds.
     */
    public int getNumRounds() {
        return numRounds;
    }

    /**
     * Returns the number of starting dice.
     * 
     * @return The number of starting dice.
     */
    public int getStartingNumDice() {
        return largeDie ? 8 : 7;
    }

    /**
     * Whether the game is a chips game or not.
     * 
     * @return {@code true} if a chips game, {@code false} otherwise.
     */
    public boolean isChipsGame() {
        return chips;
    }

    /**
     * Gets and returns the current player object.
     * 
     * @return A Player.
     */
    public Player getPlayerObj() {
        return getPlayer(getCurrentPlayer());
    }

    private void roundChips() {
        if (currentRound != -1 && chips) {
            for (Player player : players) {
                player.addChips(2);
            }
        }
    }

    /**
     * The Game Loop.
     */
    public void play() {
        // A {@code List} of {@code Integer}s to hold all the instructions necessary for
        // UI.
        List<String> instructions = new ArrayList<String>();
        instructions.add("intro");
        // Loops numRounds times.
        for (int i = 0; i < numRounds; i++) {
            roundChips();
            instructions.add("round start");
            // Goes until players run out of dice.
            while (nextTurn() >= 0) {
                Player player = getPlayerObj();
                // Only actual player needs status update.
                if (currentPlayer == firstPlayer) {
                    instructions.add("status update");
                }
                if (getPlayer(firstPlayer + 1).getDice().size() == 0) {
                    firstPlayer += 1;
                }
                roll();
                instructions.add("dice roll results");
                Snapshot info = GameState.takeSnapshot(casinos, players, getGameInfo(), getBoolInfo(),
                        processMinigames());
                userInterface.display(instructions, info);
                instructions.clear();
                int input = player.getUserInput(userInterface);
                // Get rid of chosen dice or chip.
                Dice dice = (input != 0) ? player.expendDice(input) : new Dice();
                if (input == 0) {
                    player.expendChip();
                } else {
                    getCasino(input).addDice(dice);
                }
                userInterface.display(List.of("user input"),
                        GameState.takeSnapshot(casinos, players, getGameInfo(input), getBoolInfo(), dice.toString(),
                                processMinigames()));
                // Activate on play minigames.
                if (input != 0 && input <= minigameMap.size()
                        && minigameMap.get(input).activationCondtion != Effect.BEFORE_PAYOUT) {
                    minigameMap.get(input).execute(casinos, players, getCurrentPlayer(), userInterface, input);
                }
            }
            // Activate before payout minigames.
            List<Integer> keys = minigameMap.keySet().stream().toList();
            for (int casino : keys) {
                if (minigameMap.get(casino).activationCondtion == Effect.BEFORE_PAYOUT) {
                    Map<Integer, List<Integer>> pay = casinos.get(casino - 1).getPlayerPayouts();
                    if (pay.size() > 0) {
                        if (pay.get(0).get(0) != 0) {
                            minigameMap.get(casino).execute(casinos, players, pay.get(0).get(0), userInterface, casino);
                        } else if (pay.size() > 1) {
                            minigameMap.get(casino).execute(casinos, players, pay.get(1).get(0), userInterface, casino);
                        }
                    }
                }
            }
            userInterface.display(List.of("round end"),
                    GameState.takeSnapshot(casinos, players, getGameInfo(), getBoolInfo(), processMinigames()));
            // Pay players for casino wins.
            for (Casino casino : casinos) {
                payPlayers(casino.getPlayerPayouts());
            }
            userInterface.display(List.of("status update"),
                    GameState.takeSnapshot(casinos, players, getGameInfo(), getBoolInfo(), processMinigames()));
            incrementRound();
        }
        userInterface.display(List.of("results"),
                GameState.takeSnapshot(casinos, players, getGameInfo(), getBoolInfo(), processMinigames()));
    }

    /**
     * Pays the players their respective payouts from each casino.
     * 
     * @param playerPayouts The payouts to be paid to each player.
     */
    public void payPlayers(Map<Integer, List<Integer>> playerPayouts) {
        for (List<Integer> entryList : playerPayouts.values()) {
            if (entryList.get(0) != 0) {
                getPlayer(entryList.get(0)).addToBalance(entryList.get(1));
            }
        }
    }

    /**
     * Resets Player Dice and Casinos.
     */
    private void resetPlayersAndCasinos() {
        Map<Integer, List<Integer>> dealings = dealer.dealToVegas(12, 2);
        for (Casino casino : casinos) {
            casino.resetDice();
            casino.setPayouts(dealings.get(5 - casinos.indexOf(casino)));
        }
        for (Player player : players) {
            player.setDice(new Dice(largeDie ? 8 : 7, largeDie, player.getPlayer()));
        }
    }

    /**
     * Gets the current game info to be used with ConsoleUI.
     * 
     * @param input The input received from the command line.
     * @return a {@code List} of type {@code Integer}.
     */
    public List<Integer> getGameInfo(int input) {
        return List.of(getCurrentPlayer(), getCurrentRound(), getNumRounds(), 7, getPlayers(), input);
    }

    /**
     * Gets the current game info to be used with ConsoleUI.
     * 
     * @return a {@code List} of type {@code Integer}.
     */
    public List<Integer> getGameInfo() {
        return List.of(getCurrentPlayer(), getCurrentRound(), getNumRounds(), 7, getPlayers(), -1);
    }

    /**
     * Gets the current game info booleans to be used with ConsoleUI.
     * 
     * @return a {@code List} of type {@code Integer}.
     */
    public List<Boolean> getBoolInfo() {
        return List.of(isChipsGame(), largeDie);
    }

    /**
     * Processes the minigameMap and creates a list of strings to be used with
     * ConsoleUI.
     * 
     * @return A list of strings.
     */
    public List<String> processMinigames() {
        List<String> minigames = new ArrayList<>();
        for (Minigame desc : minigameMap.values()) {
            minigames.add(desc.getDescription());
        }
        for (int i = minigames.size(); i < 6; i++) {
            minigames.add("");
        }
        return minigames;
    }
}
