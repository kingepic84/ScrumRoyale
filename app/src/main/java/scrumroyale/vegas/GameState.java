package scrumroyale.vegas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import scrumroyale.Minigame;
import scrumroyale.Player;

/**
 * GameState class. Holds the current game state information.
 */
public class GameState {

    /**
     * Takes and returns a {@code Snapshot} of the current game state.
     * 
     * @param casinos      A List of Casinos.
     * @param players      A List of Players.
     * @param vegasInfo    A List of info about Vegas Integers.
     * @param vegasBools   A list of info about Vegas Booleans.
     * @param diceExpended A string of dice values.
     * @param minigameInfo A List of strings of the minigame info.
     * @return A {@code Snapshot} view of the current game state.
     */
    public static Snapshot takeSnapshot(List<Casino> casinos, List<Player> players, List<Integer> vegasInfo,
            List<Boolean> vegasBools, String diceExpended, List<String> minigameInfo) {
        List<CasinoRecord> casRecords = new ArrayList<>(6);
        List<PlayerRecord> playerRecords = new ArrayList<>(vegasInfo.get(4));
        VegasRecord vegasRecord = new VegasRecord(vegasBools.get(0), vegasBools.get(1), vegasInfo.get(0),
                vegasInfo.get(1), vegasInfo.get(2), vegasInfo.get(3), vegasInfo.get(4), vegasInfo.get(5), diceExpended);
        casinos.forEach(casino -> casRecords.add(new CasinoRecord(casino.getPayouts(), casino.getPlayerPayouts(),
                minigameInfo.get(casinos.indexOf(casino)))));
        players.forEach(player -> playerRecords.add(new PlayerRecord(player.getPlayer(), player.getDice().size(),
                player.getBalance(), player.getChips(), player.hasBigDice(), player.getDice().toString(),
                player.getDice().getDiceCounts())));
        return new Snapshot(casRecords, playerRecords, vegasRecord);
    }

    /**
     * Takes and returns a {@code Snapshot} of the current game state.
     * 
     * @param casinos      A List of Casinos.
     * @param players      A List of Players.
     * @param vegasInfo    A List of info about Vegas Integers.
     * @param vegasBools   A list of info about Vegas Booleans.
     * @param diceExpended A string of dice values.
     * @return A {@code Snapshot} view of the current game state.
     */
    public static Snapshot takeSnapshot(List<Casino> casinos, List<Player> players, List<Integer> vegasInfo,
            List<Boolean> vegasBools, String diceExpended) {
        List<CasinoRecord> casRecords = new ArrayList<>(6);
        List<PlayerRecord> playerRecords = new ArrayList<>(vegasInfo.get(4));
        VegasRecord vegasRecord = new VegasRecord(vegasBools.get(0), vegasBools.get(1), vegasInfo.get(0),
                vegasInfo.get(1), vegasInfo.get(2), vegasInfo.get(3), vegasInfo.get(4), vegasInfo.get(5), diceExpended);
        casinos.forEach(casino -> casRecords.add(new CasinoRecord(casino.getPayouts(), casino.getPlayerPayouts(), "")));
        players.forEach(player -> playerRecords.add(
                new PlayerRecord(player.getPlayer(), player.getDice().size(), player.getBalance(), player.getChips(),
                        player.hasBigDice(), player.getDice().toString(), player.getDice().getDiceCounts())));
        return new Snapshot(casRecords, playerRecords, vegasRecord);
    }

    /**
     * Takes and returns a {@code Snapshot} of the current game state.
     * 
     * @param casinos      A List of Casinos.
     * @param players      A List of Players.
     * @param vegasInfo    A List of info about Vegas Integers.
     * @param vegasBools   A list of info about Vegas Booleans.
     * @param minigameInfo A List of strings of the minigame info.
     * @return A {@code Snapshot} view of the current game state.
     */
    public static Snapshot takeSnapshot(List<Casino> casinos, List<Player> players, List<Integer> vegasInfo,
            List<Boolean> vegasBools, List<String> minigameInfo) {
        List<CasinoRecord> casRecords = new ArrayList<>(6);
        List<PlayerRecord> playerRecords = new ArrayList<>(vegasInfo.get(4));
        VegasRecord vegasRecord = new VegasRecord(vegasBools.get(0), vegasBools.get(1), vegasInfo.get(0),
                vegasInfo.get(1), vegasInfo.get(2), vegasInfo.get(3), vegasInfo.get(4), vegasInfo.get(5), "");
        casinos.forEach(casino -> casRecords.add(new CasinoRecord(casino.getPayouts(), casino.getPlayerPayouts(),
                minigameInfo.get(casinos.indexOf(casino)))));
        players.forEach(player -> playerRecords.add(
                new PlayerRecord(player.getPlayer(), player.getDice().size(), player.getBalance(), player.getChips(),
                        player.hasBigDice(), player.getDice().toString(), player.getDice().getDiceCounts())));
        return new Snapshot(casRecords, playerRecords, vegasRecord);
    }

    /**
     * Takes and returns a {@code Snapshot} of the current game state.
     * 
     * @param casinos    A List of Casinos.
     * @param players    A List of Players.
     * @param vegasInfo  A List of info about Vegas Integers.
     * @param vegasBools A list of info about Vegas Booleans.
     * @return A {@code Snapshot} view of the current game state.
     */
    public static Snapshot takeSnapshot(List<Casino> casinos, List<Player> players, List<Integer> vegasInfo,
            List<Boolean> vegasBools) {
        List<CasinoRecord> casRecords = new ArrayList<>(6);
        List<PlayerRecord> playerRecords = new ArrayList<>(vegasInfo.get(4));
        VegasRecord vegasRecord = new VegasRecord(vegasBools.get(0), vegasBools.get(1), vegasInfo.get(0),
                vegasInfo.get(1), vegasInfo.get(2), vegasInfo.get(3), vegasInfo.get(4), vegasInfo.get(5), "");
        casinos.forEach(casino -> casRecords.add(new CasinoRecord(casino.getPayouts(), casino.getPlayerPayouts(), "")));
        players.forEach(player -> playerRecords.add(
                new PlayerRecord(player.getPlayer(), player.getDice().size(), player.getBalance(), player.getChips(),
                        player.hasBigDice(), player.getDice().toString(), player.getDice().getDiceCounts())));
        return new Snapshot(casRecords, playerRecords, vegasRecord);
    }

    /**
     * Takes and returns a {@code Minishot} of the current game state.
     * 
     * @param casinos       A List of Casinos.
     * @param players       A List of Players.
     * @param minigame      The minigame.
     * @param minigameInfo  A string of the minigame info.
     * @param currentPlayer The current player on this tile.
     * @param chips         Whether chips are used in the payout or not.
     * @param payout        The payout.
     * @param casino        The casino this minishot is associated with.
     * @return A {@code Minishot} view of the current game state.
     */
    public static Minishot takeMinishot(List<Casino> casinos, int casino, List<Player> players, Minigame minigame,
            String minigameInfo, int currentPlayer, boolean chips, int payout) {
        return new Minishot(minigame.getClass().getSimpleName().replaceAll("(?<!^)([A-Z])", " $1"), casino,
                minigameInfo, currentPlayer, chips, payout);
    }

    /**
     * Snapshot Record. Holds all the info about the game at a certain point in
     * time.
     */
    public static record Snapshot(List<CasinoRecord> casinos, List<PlayerRecord> players, VegasRecord vegas) {
        /**
         * Snapshot record constructor.
         * 
         * @param casinos The casinos this snapshot holds as a {@code List} of
         *                {@code CasinoRecord}s.
         * @param players The players this snapshot holds as a {@code List} of
         *                {@code PlayerRecord}s.
         * @param vegas   The vegas info in the form of a {@code VegasRecord}.
         */
        public Snapshot(List<CasinoRecord> casinos, List<PlayerRecord> players, VegasRecord vegas) {
            this.casinos = Collections.unmodifiableList(casinos);
            this.players = Collections.unmodifiableList(players);
            this.vegas = vegas;
        }
    }

    /**
     * Minishot Record. Holds all information pertaining to a relevant minigame.
     */
    public static record Minishot(String name, int casino, String statusString, int currPlayer, boolean isChips,
            int payout) {
    }

    /**
     * Player Record. Holds all the information about a certain player.
     */
    public static record PlayerRecord(int id, int diceLeft, int balance, int chipsLeft, boolean hasBigDice, String dice,
            Map<Integer, String> diceAvailable) {
        /**
         * Creates an instance of a PlayerRecord record class.
         * 
         * @param id            the value for the id record component.
         * @param diceLeft      the value for the diceLeft record component.
         * @param balance       the value for the balance record component.
         * @param chipsLeft     the value for the chipsLeft record component.
         * @param hasBigDice    the value for the hasBigDice record component.
         * @param dice          the value for the dice record component.
         * @param diceAvailable the value for the diceAvailable record component.
         */
        public PlayerRecord(int id, int diceLeft, int balance, int chipsLeft, boolean hasBigDice, String dice,
                Map<Integer, String> diceAvailable) {
            this.id = id;
            this.diceLeft = diceLeft;
            this.balance = balance;
            this.chipsLeft = chipsLeft;
            this.hasBigDice = hasBigDice;
            this.dice = dice;
            this.diceAvailable = Collections.unmodifiableMap(diceAvailable);
        }
    }

    /**
     * Vegas Record. Holds all the information about the game itself.
     */
    public static record VegasRecord(boolean chips, boolean largeDie, int currentPlayer, int currentRound,
            int numRounds, int numDice, int numPlayers, int input, String diceExpended) {
    }

    /**
     * Casino Record. Holds all the relevant information pertaining a certain
     * casino.
     */
    public static record CasinoRecord(List<Integer> payouts, Map<Integer, List<Integer>> playerPayouts,
            String minigame) {
        /**
         * CasinoRecord constructor.
         * 
         * @param payouts       The payouts this casino has, as a {@code List}.
         * @param playerPayouts The payouts the players get as a {@code Map}.
         * @param minigame      The minigame attached to this casino.
         */
        public CasinoRecord(List<Integer> payouts, Map<Integer, List<Integer>> playerPayouts, String minigame) {
            this.payouts = Collections.unmodifiableList(payouts);
            this.playerPayouts = Collections.unmodifiableMap(playerPayouts);
            this.minigame = minigame;
        }
    }

}
