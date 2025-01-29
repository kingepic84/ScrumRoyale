package gradle.cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.Before;
import io.cucumber.java.After;

import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@SuppressWarnings("unused")
public class ConsoleFeatureSteps {

    //=======================================================================
    //********************          DEBUG MODE           ********************
    //=======================================================================

    private static final boolean DEBUG_MODE = false;

    //=======================================================================





    private static final String IMAGINARY_PLAYER = "P0";
    private static final int BIGGIE_CODE = 999;
    private static Map<String, List<String>> phaseMap = Map.ofEntries(
        // The entries are rounds, players, regular dice, small dice, chips per round, and minigames.
        new AbstractMap.SimpleEntry<String, List<String>>("phase0", Arrays.asList("1", "1", "7", "0", "0")),
        new AbstractMap.SimpleEntry<String, List<String>>("phase1", Arrays.asList("1", "1", "7", "0", "2")),
        new AbstractMap.SimpleEntry<String, List<String>>("phase2", Arrays.asList("1", "1", "7", "1", "2")),
        new AbstractMap.SimpleEntry<String, List<String>>("phase3", Arrays.asList("1", "3", "7", "1", "2")),
        new AbstractMap.SimpleEntry<String, List<String>>("phase4", Arrays.asList("3", "3", "7", "1", "2")),
        new AbstractMap.SimpleEntry<String, List<String>>("phase5", Arrays.asList("3", "3", "7", "1", "2", "Jackpot")),
        new AbstractMap.SimpleEntry<String, List<String>>("phase6", Arrays.asList("3", "3", "7", "1", "2", "Jackpot", "Pay Day")),
        new AbstractMap.SimpleEntry<String, List<String>>("phase7", Arrays.asList("3", "3", "7", "1", "2", "Jackpot", "Pay Day", "Fifty Fifty")),
        new AbstractMap.SimpleEntry<String, List<String>>("phase8", Arrays.asList("3", "3", "7", "1", "2", "Pay Day", "Fifty Fifty", "Lucky Punch")),
        new AbstractMap.SimpleEntry<String, List<String>>("phase9", Arrays.asList("3", "3", "7", "1", "2", "Fifty Fifty", "Lucky Punch", "High Five")),
        new AbstractMap.SimpleEntry<String, List<String>>("phase10", Arrays.asList("3", "3", "7", "1", "2", "Lucky Punch", "High Five", "Black Box")),
        new AbstractMap.SimpleEntry<String, List<String>>("phase11", Arrays.asList("3", "3", "7", "1", "2", "High Five", "Black Box", "Block It")),
        new AbstractMap.SimpleEntry<String, List<String>>("phase12", Arrays.asList("3", "3", "7", "1", "2", "Black Box", "Block It", "Bad Luck"))
    );
    private String actualOutput;
    private static String currentPhase;

    private static class Result {
        public enum Type { SUCCESS, FAILURE };
        public Type type;
        public String info;
        public Result() {
            this(Type.SUCCESS, "");
        }
        public Result(String info) {
            this(Type.FAILURE, info);
        }
        public Result(Type type, String info) {
            this.type = type;
            this.info = info;
        }
    }

    private static class PlayerInfo {
        public String name;
        public int money;
        public int smallDice;
        public int largeDice;
        public int chips;
        
        public PlayerInfo() {
            name = "";
            money = 0;
            smallDice = 0;
            largeDice = 0;
            chips = 0;
        }
        
        @Override
        public String toString() {
            return String.format("name: %s  $: %d  s: %d  L: %d  c: %d", name, money, smallDice, largeDice, chips);
        }
    }
    
    private static class CasinoInfo {
        public int id;
        public int largePayout;
        public int smallPayout;
        public Map<String, Integer> playerDice;
        public String largePlayer;
        public String smallPlayer;
        public String minigame;
        
        public CasinoInfo() {
            id = -1;
            largePayout = -1;
            smallPayout = -1;
            largePlayer = "";
            smallPlayer = "";
            minigame = "";
            playerDice = new LinkedHashMap<>();
        }
        
        @Override
        public String toString() {
            return String.format("id: %d  lrg: %d  sml: %d   dice: %s  winL: %s  winS: %s  mini: %s", id, largePayout, smallPayout, playerDice, largePlayer, smallPlayer, minigame);
        }
    }

    private static boolean equalsPMap(Map<String, PlayerInfo> p, Map<String, PlayerInfo> q) {
        for (String key : p.keySet()) {
            if (!key.contains(IMAGINARY_PLAYER)) { 
                if (!q.keySet().contains(key)) return false;
                if (!equals(p.get(key), q.get(key))) return false;
            }
        }
        return true;
    }
    
    private static boolean equals(PlayerInfo p, PlayerInfo q) {
        return p.name.equals(q.name)
               && p.money == q.money
               && p.smallDice == q.smallDice
               && p.largeDice == q.largeDice
               && p.chips == q.chips;
    }

    private static boolean equalsCMap(Map<Integer, CasinoInfo> a, Map<Integer, CasinoInfo> b) {
        if (!a.keySet().equals(b.keySet())) return false;
        for (Integer key : a.keySet()) {
            if (!equals(a.get(key), b.get(key))) {
                return false;
            }
        }
        return true;
    }
    
    private static boolean equals(CasinoInfo a, CasinoInfo b) {
        return a.id == b.id
               && (b.largePlayer.contains(IMAGINARY_PLAYER) || a.largePlayer.equals(b.largePlayer))
               && (b.smallPlayer.contains(IMAGINARY_PLAYER) || a.smallPlayer.equals(b.smallPlayer))
               && equals(a.playerDice, b.playerDice);
        
    }

    private static boolean equals(Map<String, Integer> p, Map<String, Integer> q) {
        for (String key : q.keySet()) {
            if (!key.contains(IMAGINARY_PLAYER)) { 
                if (!p.keySet().contains(key)) return false;
                if (p.get(key) != q.get(key)) return false;
            }
        }
        return true;
    }

    private static Result isPlacementLegal(Map<Integer, Integer> placement) {
        if (placement.size() == 0) {
            return new Result("No dice placed " + placement);
        } else if (placement.size() == 1) {
            int val = placement.get((new ArrayList<Integer>(placement.keySet())).get(0));
            if (val < 1 || val > 6) return new Result("Invalid dice " + placement);
        } else if (placement.size() == 2) {
            if (!placement.containsKey(BIGGIE_CODE)) {
                return new Result("Invalid dice " + placement);
            }
            int val = placement.get(BIGGIE_CODE);
            if (!placement.containsKey(val)) {
                return new Result("Invalid dice " + placement);
            }
        } else {
            return new Result("Too many types of dice placed " + placement);
        }
        return new Result();
    }

    @SuppressWarnings("resource")
    private static Map<String, PlayerInfo> parsePlayerInfo(String text) {
        List<String> header = new ArrayList<>(); 
        Map<String, PlayerInfo> map = new HashMap<>();

        Scanner scanner = new Scanner(text);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.contains(":")) {
                String[] parts = line.split(":", 2);
                if (parts.length != 2) { throw new RuntimeException("Invalid player information"); }
                if (header.size() == 0) {
                    parts = parts[1].trim().split("\\s+");
                    for (String p : parts) {
                        header.add(p.trim());
                    }
                } else {
                    PlayerInfo info = new PlayerInfo();
                    info.name = parts[0].trim();
                    parts = parts[1].trim().split("\\s+");
                    for (int i = 0; i < parts.length; i++) {
                        int value = Integer.parseInt(parts[i].trim());
                        if (header.get(i).equals("money")) { info.money = value; }
                        else if (header.get(i).equals("dice")) {info.smallDice = value; }
                        else if (header.get(i).equals("DICE")) {info.largeDice = value; }
                        else if (header.get(i).equals("chips")) {info.chips = value; }
                    }
                    map.put(info.name, info);
                }
            }
        }
        return map;
    }

    @SuppressWarnings("resource")
    private static Map<Integer, CasinoInfo> parseCasinoInfo(String text) {
        String header = text.substring(0, text.indexOf('\n'));
        String[] hparts = header.split("\\s+");
        List<String> hnames = new ArrayList<>();
        List<String> pnames = new ArrayList<>();
        for (String h : hparts) {
            if (h.startsWith("P")) {
                pnames.add(h.trim());
            } else if (h.trim().length() > 0) {
                hnames.add(h.trim());
            }
        }
        
        text = text.substring(text.indexOf('\n') + 1);
        Map<Integer, CasinoInfo> map = new HashMap<>();
        Scanner scanner = new Scanner(text);
        int currentId = -1;
        while (scanner.hasNextLine()) {
            CasinoInfo info = new CasinoInfo();
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            if (parts.length < 2) {
                map.get(currentId).minigame = parts[0].split(" - ")[0].trim();
                continue;
            }
            String[] firstParts = parts[0].trim().split("\\s+");
            for (int i = 0; i < firstParts.length; i++) {
                if (hnames.get(i).equals("#")) {
                    info.id = Integer.parseInt(firstParts[i].replace("[", "").replace("]", "").trim());
                    currentId = info.id;
                } else if (hnames.get(i).equals("$$")) {
                    info.largePayout = Integer.parseInt(firstParts[i].trim());
                } else if (hnames.get(i).equals("$")) {
                    info.smallPayout = Integer.parseInt(firstParts[i].trim());
                }
            }
            int index = 0;
            for (String pname : pnames) {
                String value = parts[1].substring(index + 2, index + 5);
                if (value.startsWith("$$")) {
                    info.largePlayer = pname;
                    info.playerDice.put(pname, Integer.parseInt(value.replace("$", "").trim()));
                } else if (value.startsWith("$")) {
                    info.smallPlayer = pname;
                    info.playerDice.put(pname, Integer.parseInt(value.replace("$", "").trim()));
                } else if (value.trim().length() > 0) {
                    info.playerDice.put(pname, Integer.parseInt(value.trim()));
                }
                
                index += 5;
            }
            map.put(info.id, info);
        }
        return map;
    }

    private static Map<Integer, Integer> parseDiceOptions(String s) {
        Map<Integer, Integer> map = new LinkedHashMap<>();
        for (int i = 1; i <= 6; i++) { map.put(i, 0); }
        Pattern p = Pattern.compile("\\[.*\\]");
        Matcher m = p.matcher(s);
        if (m.find()) {
            String rolls = s.substring(m.start() + 1, m.end() - 1);
            for (String n : rolls.split(",")) {
                n = n.trim();
                if (n.contains("+")) {
                    int num = Integer.parseInt(n.substring(0, 1));
                    int occurrences = n.length() - n.replace("+", "").length();
                    map.put(num, map.get(num) + occurrences + 1);
                    map.put(BIGGIE_CODE, num);
                } else {
                    int num = Integer.parseInt(n);
                    map.put(num, map.get(num) + 1);
                }
            }
        }
        List<Integer> zeroKeys = new ArrayList<>();
        for (Integer k : map.keySet()) {
            if (map.get(k) == 0) {
                zeroKeys.add(k);
            }
        }
        for (Integer k : zeroKeys) {
            map.remove(k);
        }
        return map;
    }

    private static List<String> parsePoolOptions(String s) {
        List<String> opts = new ArrayList<>();
        Pattern p = Pattern.compile("\\[.*\\]");
        Matcher m = p.matcher(s);
        if (m.find()) {
            String pools = s.substring(m.start() + 1, m.end() - 1);
            for (String n : pools.split(",")) {
                n = n.trim();
                opts.add(n);
            }
        }
        return opts;
    }

    private static List<String> getWinners(Map<String, Integer> dice) {
        List<String> nameList = new ArrayList<>(dice.keySet());
        Map<Integer, Integer> scoreMap = new LinkedHashMap<>();
        for (String key : dice.keySet()) {
            int index = nameList.indexOf(key);
            if (!scoreMap.containsKey(index)) {
                scoreMap.put(index, 0);
            }
            scoreMap.put(index, scoreMap.get(index) + dice.get(key));
        }
        Map<Integer, Integer> countMap = new HashMap<>(); // score-count map
        for (Integer index : scoreMap.keySet()) {
            int tmp = scoreMap.get(index);
            if (!countMap.containsKey(tmp)) {
                countMap.put(tmp, 0);
            }
            countMap.put(tmp, countMap.get(tmp) + 1);
        }
        List<Integer> tmp = new ArrayList<>();
        for (Integer i : countMap.keySet()) {
            if (countMap.get(i) > 1) {
                scoreMap.values().removeAll(Collections.singleton(i));
            }
        }
        List<java.awt.Point> scores = new ArrayList<>();
        for (Integer index : scoreMap.keySet()) {
            scores.add(new java.awt.Point(index, scoreMap.get(index)));
        }
        scores = scores.stream()
            .sorted((p1, p2)->Integer.compare(p2.y, p1.y))
            .collect(Collectors.toList());
        
        List<String> winners = new ArrayList<>();
        for (int i = 0; i < 2 && i < scores.size(); i++) {
            winners.add(nameList.get(scores.get(i).x));
        }
        
        return winners;
    }

    @SuppressWarnings("resource")
    private static Result evaluateGameLog(String log, int rounds, int players, int smlDice, int lrgDice, int chips, List<String> minigames) {
        
        if (DEBUG_MODE) {
            try {
                PrintWriter writer = new PrintWriter(
                    new FileWriter(System.getProperty("user.dir") + 
                                "/app/build/reports/cucumber/gamelog_" + 
                                currentPhase + 
                                ".txt")
                );
                writer.print(log);
                writer.close();
            } catch (IOException e) { }
        }


        if (log.trim().length() == 0) {
            return new Result("Game log is empty.");
        }

        Matcher matcher = null;
        int numRounds = rounds;
        int numPlayers = players;
        int smallDice = smlDice;
        int largeDice = lrgDice;
        int chipsPerRound = chips;
        int actualRounds = 0;
        boolean gameEnded = false;

        boolean inPlayerInfo = false;
        String playerInfoString = "";
        Map<String, PlayerInfo> actualPlayerInfo = new HashMap<>();
        Map<String, PlayerInfo> expectedPlayerInfo = new HashMap<>();
        for (int i = 0; i < numPlayers; i++) {
            PlayerInfo pi = new PlayerInfo();
            pi.name = "P" + (i + 1);
            pi.smallDice = smallDice;
            pi.largeDice = largeDice;
            expectedPlayerInfo.put(pi.name, pi);
        }
        
        
        boolean inCasinoInfo = false;
        String casinoInfoString = "";
        Map<Integer, CasinoInfo> actualCasinoInfo = new HashMap<>();
        Map<Integer, CasinoInfo> expectedCasinoInfo = new HashMap<>();
        for (int i = 1; i <= 6; i++) {
            CasinoInfo c = new CasinoInfo();
            c.id = i;
            expectedCasinoInfo.put(c.id, c);
        }
        boolean casinosInitialized = false;
        
        
        List<Map<Integer, Integer>> lastPlayerRoll = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            lastPlayerRoll.add(null);
        }

        List<Map<Integer, Integer>> lastPlayerAllocation = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            lastPlayerAllocation.add(null);
        }

        Set<String> minigameSet = new HashSet<>(minigames);

        Pattern casinoPlacementPattern = Pattern.compile("Casino (?<casinoNum>\\d) gained (?<dice>\\[.+\\]) from P(?<playerNum>\\d)");
        Pattern playerRollPattern = Pattern.compile("(P(?<playerNum>\\d)|You) rolled (?<dice>\\[.+\\])");
        Pattern playerChipPattern = Pattern.compile("P(?<playerNum>\\d) used a chip and has (?<chips>\\d+) chips? left");
        Pattern playerPlacementPattern = Pattern.compile("P(?<playerNum>\\d) placed (?<dice>\\[.+\\])");
        Pattern playerRewardPattern = Pattern.compile("P(?<playerNum>\\d) gained (?<reward>((?<chips>\\d) chips?)|(\\$(?<money>\\d+))):(?<reason>.*)");
        Pattern playerLossPattern = Pattern.compile("P(?<playerNum>\\d) lost (?<reward>((?<chips>\\d) chips?)|(\\$(?<money>\\d+))):(?<reason>.*)");
        Pattern casinoPayoutPattern = Pattern.compile("Payout from Casino (?<casinoNum>\\d) for Round (?<round>\\d)");
        Pattern startOfRoundPattern = Pattern.compile("Round (?<round>\\d) has started");    
        Pattern endOfRoundPattern = Pattern.compile("Round (?<round>\\d) has ended");    
        Pattern minigameAddedPattern = Pattern.compile("Casino (?<casinoNum>\\d) added the minigame (?<minigame>.+)");    
        Pattern endOfGamePattern = Pattern.compile("The game is over.");    

    
        Scanner scanner = new Scanner(log);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.contains("----      PLAYERS      ----")) {
                inPlayerInfo = true;
            } else if (inPlayerInfo && line.trim().length() == 0) {
                actualPlayerInfo = parsePlayerInfo(playerInfoString);
                if (!equalsPMap(expectedPlayerInfo, actualPlayerInfo)) {
                    return new Result(String.format("Player information differs.\nExpected: %s\nActual: %s", expectedPlayerInfo, actualPlayerInfo));
                }
                playerInfoString = "";
                inPlayerInfo = false;
            } else if (inPlayerInfo) {
                playerInfoString += line + "\n";
            } else if (line.contains("----      CASINOS      ----")) {
                inCasinoInfo = true;
            } else if (inCasinoInfo && line.trim().length() == 0) {
                actualCasinoInfo = parseCasinoInfo(casinoInfoString);
                if (!casinosInitialized) {
                    for (Integer key : expectedCasinoInfo.keySet()) {
                        expectedCasinoInfo.get(key).largePayout = actualCasinoInfo.get(key).largePayout;
                        expectedCasinoInfo.get(key).smallPayout = actualCasinoInfo.get(key).smallPayout;
                        expectedCasinoInfo.get(key).minigame = actualCasinoInfo.get(key).minigame;
                    }
                    casinosInitialized = true;
                }
                if (!equalsCMap(expectedCasinoInfo, actualCasinoInfo)) {
                    return new Result(String.format("Casino information differs.\nExpected: %s\nActual: %s", expectedCasinoInfo, actualCasinoInfo));
                }
                casinoInfoString = "";
                inCasinoInfo = false;
            } else if (inCasinoInfo) {
                casinoInfoString += line + "\n";
            } else if ((matcher = casinoPlacementPattern.matcher(line)).find()) {
                Map<Integer, Integer> dopt = parseDiceOptions(matcher.group("dice"));
                int cnum = Integer.parseInt(matcher.group("casinoNum"));
                int pnum = Integer.parseInt(matcher.group("playerNum"));
                Result r = isPlacementLegal(dopt);
                if (r.type == Result.Type.FAILURE) {
                    return new Result(String.format("Casino allocation '%s' is not a valid placement: %s",
                                                    line.trim(), r.info));
                }
                if (pnum > 0 && lastPlayerAllocation.get(pnum - 1).get(cnum) != dopt.get(cnum)) {
                    return new Result(String.format("Casino allocation '%s' does not match player's allocation '%s'",
                                                    line.trim(), lastPlayerAllocation.get(pnum - 1)));
                }
                if (!expectedCasinoInfo.get(cnum).playerDice.containsKey("P" + pnum)) {
                    expectedCasinoInfo.get(cnum).playerDice.put("P" + pnum, 0);
                }
                expectedCasinoInfo.get(cnum).playerDice.put("P" + pnum, 
                                                            expectedCasinoInfo.get(cnum).playerDice.get("P" + pnum) + dopt.get(cnum));
                List<String> winners = getWinners(expectedCasinoInfo.get(cnum).playerDice);
                expectedCasinoInfo.get(cnum).largePlayer = "";
                expectedCasinoInfo.get(cnum).smallPlayer = "";
                if (winners.size() > 0) {
                    expectedCasinoInfo.get(cnum).largePlayer = winners.get(0);
                }
                if (winners.size() > 1) {
                    expectedCasinoInfo.get(cnum).smallPlayer = winners.get(1);
                }
            } else if ((matcher = playerPlacementPattern.matcher(line)).find()) {
                Map<Integer, Integer> dopt = parseDiceOptions(matcher.group("dice"));
                int pnum = Integer.parseInt(matcher.group("playerNum"));
                Result r = isPlacementLegal(dopt);
                if (r.type == Result.Type.FAILURE) {
                    return new Result(String.format("Player allocation '%s' is not a valid placement:",
                                                    line.trim()));
                }
                List<Integer> tmp = dopt.entrySet()
                                        .stream()
                                        .filter(entry -> entry.getKey() != BIGGIE_CODE)
                                        .map(Map.Entry::getKey)
                                        .collect(Collectors.toList());
                int cnum = tmp.get(0);
                if (lastPlayerRoll.get(pnum - 1).get(cnum) != dopt.get(cnum)) {
                    return new Result(String.format("Player allocation '%s' does not match last roll '%s'",
                                                    line.trim(), lastPlayerRoll.get(pnum - 1)));
                }
                
                if (dopt.containsKey(BIGGIE_CODE)) {
                    expectedPlayerInfo.get("P" + pnum).largeDice -= 1;
                    expectedPlayerInfo.get("P" + pnum).smallDice -= dopt.get(cnum) - 2;
                } else {
                    expectedPlayerInfo.get("P" + pnum).smallDice -= dopt.get(cnum);
                }
                
                lastPlayerAllocation.set(pnum - 1, dopt);
            } else if ((matcher = playerRollPattern.matcher(line)).find()) {
                int playerNum = 1;
                // Either the playerNum group will be legitimate or it was "You".
                if (matcher.group("playerNum") != null) {
                    playerNum = Integer.parseInt(matcher.group("playerNum"));
                }
                lastPlayerRoll.set(playerNum - 1, parseDiceOptions(matcher.group("dice")));
            } else if ((matcher = playerChipPattern.matcher(line)).find()) {
                int pnum = Integer.parseInt(matcher.group("playerNum"));
                int chipsLeft = Integer.parseInt(matcher.group("chips"));
                if (expectedPlayerInfo.get("P" + pnum).chips != chipsLeft + 1) {
                    return new Result(String.format("Incorrect chip count for P%d - Expected: %d  Actual: %d",
                                      pnum, expectedPlayerInfo.get("P" + pnum).chips - 1, chipsLeft));
                }
                expectedPlayerInfo.get("P" + pnum).chips--;
            } else if ((matcher = playerRewardPattern.matcher(line)).find()) {
                int pnum = Integer.parseInt(matcher.group("playerNum"));
                if (matcher.group("chips") != null) {
                    int val = Integer.parseInt(matcher.group("chips"));
                    expectedPlayerInfo.get("P" + pnum).chips += val;
                }
                if (matcher.group("money") != null) {
                    int val = Integer.parseInt(matcher.group("money"));
                    String reason = matcher.group("reason").trim();
                    Matcher tmp = casinoPayoutPattern.matcher(reason);
                    if (tmp.find()) {
                        int casinoId = Integer.parseInt(tmp.group("casinoNum"));
                        CasinoInfo cinfo = expectedCasinoInfo.get(casinoId);
                        if ((cinfo.largePayout != val || !cinfo.largePlayer.equals("P" + pnum))
                            && (cinfo.smallPayout != val || !cinfo.smallPlayer.equals("P" + pnum))) {
                            return new Result("Incorrect payout: " + line.trim());
                        }
                    }
                    expectedPlayerInfo.get("P" + pnum).money += val;
                }
            } else if ((matcher = playerLossPattern.matcher(line)).find()) {
                int pnum = Integer.parseInt(matcher.group("playerNum"));
                if (matcher.group("chips") != null) {
                    int val = Integer.parseInt(matcher.group("chips"));
                    expectedPlayerInfo.get("P" + pnum).chips -= val;
                }
                if (matcher.group("money") != null) {
                    int val = Integer.parseInt(matcher.group("money"));
                    expectedPlayerInfo.get("P" + pnum).money -= val;
                }
            } else if ((matcher = startOfRoundPattern.matcher(line)).find()) {
                int rnum = Integer.parseInt(matcher.group("round"));
                for (String key : expectedPlayerInfo.keySet()) {
                    expectedPlayerInfo.get(key).smallDice = smallDice;
                    expectedPlayerInfo.get(key).largeDice = largeDice;
                }
                for (Integer key : expectedCasinoInfo.keySet()) {
                    expectedCasinoInfo.get(key).largePlayer = "";
                    expectedCasinoInfo.get(key).smallPlayer = "";
                    expectedCasinoInfo.get(key).playerDice = new LinkedHashMap<>();
                }
                casinosInitialized = false;
            } else if ((matcher = endOfRoundPattern.matcher(line)).find()) {
                int rnum = Integer.parseInt(matcher.group("round"));
                actualRounds++;
                if (rnum != actualRounds) {
                    return new Result(String.format("The expected round was %d but the actual round was %d.", actualRounds, rnum));
                }
            } else if ((matcher = minigameAddedPattern.matcher(line)).find()) {
                int cid = Integer.parseInt(matcher.group("casinoNum"));
                String mini = matcher.group("minigame").trim();
                List<String> tmp = new ArrayList<>();
                for (String m : minigameSet) {
                    if (mini.contains(m)) {
                        tmp.add(m);
                    }
                }
                minigameSet.removeAll(tmp);
            } else if ((matcher = endOfGamePattern.matcher(line)).find()) {
                gameEnded = true;
            }
        }
        if (!gameEnded) {
            return new Result("The game never ended.");
        }
        if (minigames.size() - minigameSet.size() < Math.min(3, minigames.size())) {
            return new Result(String.format("The minigames from '%s' that were unused were '%s'.", minigames, minigameSet));
        }
        if (actualRounds != numRounds) {
            return new Result(String.format("The expected number of rounds was %d but the actual rounds was %d.", numRounds, actualRounds));
        }
        
        return new Result();
    }

    private static String playPhase(Scanner output, PrintWriter input) {
        StringBuilder outputBuilder = new StringBuilder();
        boolean inPlayerInfo = false;
        String playerInfoString = "";
        Map<String, PlayerInfo> playerInfo = null;
        boolean inCasinoInfo = false;
        String casinoInfoString = "";
        Map<Integer, CasinoInfo> casinoInfo;
        Map<Integer, Integer> diceOptions = null;
        boolean usedChip = false;
        List<String> poolOptions = new ArrayList<>();
        while (output.hasNext()) {
            String line = output.nextLine();

            if (DEBUG_MODE) {
                System.err.println(line);
            }
 
            if (line.contains("----      PLAYERS      ----")) {
                inPlayerInfo = true;
            } else if (inPlayerInfo && line.trim().length() == 0) {
                playerInfo = parsePlayerInfo(playerInfoString);
                playerInfoString = "";
                inPlayerInfo = false;
            } else if (inPlayerInfo) {
                playerInfoString += line + "\n";
            } else if (line.contains("----      CASINOS      ----")) {
                inCasinoInfo = true;
            } else if (inCasinoInfo && line.trim().length() == 0) {
                casinoInfo = parseCasinoInfo(casinoInfoString);
                casinoInfoString = "";
                inCasinoInfo = false;
            } else if (inCasinoInfo) {
                casinoInfoString += line + "\n";
            } else if (line.contains("P1 rolled")) {
                diceOptions = parseDiceOptions(line);
            } else if (line.contains("casino will you place")) {
                List<Integer> keys = new ArrayList<Integer>(diceOptions.keySet());
                int key = keys.get((int)(Math.random() * keys.size()));
                if ((!usedChip || Math.random() < 0.2) && playerInfo.get("P1").chips > 0) { 
                    key = 0; 
                    usedChip = true;
                } else if (key == BIGGIE_CODE) {
                    key = diceOptions.get(BIGGIE_CODE);
                }
                input.println(key);
            } else if (line.contains("you like to take the payout")) {
                input.println((int)(Math.random() * 3));
            } else if (line.contains("What is your choice?")) {
                input.println((int)(Math.random() * 3) + 1);
            } else if (line.contains("Which pile would you like?")) {
                input.println((int)(Math.random() * 2) + 1);
            } else if (line.contains("which pile would you like to place")) {
                input.println((int)(Math.random() * 2) + 1);
            } else if (line.contains("dice pool options are")) {
                poolOptions = parsePoolOptions(line);
            } else if (line.contains("Which pool would you like?")) {
                input.println(poolOptions.get((int)(Math.random() * poolOptions.size())));
            } else if (line.contains("casino will you add")) {
                input.println((int)(Math.random() * 6) + 1);
            }
            outputBuilder.append(line + "\n");
        }
        
        return outputBuilder.toString();        
    }

    private static String runProgram(List<String> args) {
        List<String> command = new ArrayList<String>();
        command.add("java");
        command.add("-jar");
        command.add(System.getProperty("user.dir") + "/app/build/libs/ScrumRoyale.jar");
        for (String arg : args) {
            command.add(arg);
        }
        ProcessBuilder builder = new ProcessBuilder(command);
        try {
            final Process process = builder.start();
            Scanner scanner = new Scanner(process.getInputStream());
            PrintWriter writer = new PrintWriter(process.getOutputStream(), true);
            return playPhase(scanner, writer);
        } catch (IOException e) { System.err.println(e); }
        return "";
    }
    
    private void runPhaseWithArg(String phase, String arg) {
        List<String> args = new ArrayList<>();
        args.add(phase);
        if (arg.length() > 0) {
            args.add(arg);
        }
        actualOutput = runProgram(args);
    }
    
    
    @Before
    public void initialize() {
        actualOutput = "";
    }

    @When("the program is run as {string}")
    public void theProgramIsRunAs(String phase) {
        currentPhase = phase;
        runPhaseWithArg(phase, "");
    }


    @Then("the game log should be accurate")
    public void theGameLogShouldBeAccurate() {
        List<String> params = phaseMap.get(currentPhase);
        Result result = evaluateGameLog(actualOutput,
                                        Integer.parseInt(params.get(0)),
                                        Integer.parseInt(params.get(1)),
                                        Integer.parseInt(params.get(2)),
                                        Integer.parseInt(params.get(3)),
                                        Integer.parseInt(params.get(4)),
                                        params.subList(5, params.size()));
        if (result.type == Result.Type.FAILURE) {
            assertThat(result.info, false);
        } else {
            assertThat("all match", true);
        }
    }

  
}  
