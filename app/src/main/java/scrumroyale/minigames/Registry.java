package scrumroyale.minigames;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import scrumroyale.Minigame;

/** Minigame Registry. */
public class Registry {

    /**
     * Assigns Minigames to a hashmap using a list of integers passed in.
     * 
     * @param games The List of integers representing minigames.
     * @return A hashmap representing minigame assignments.
     */
    public static Map<Integer, Minigame> assignMinigames(List<Integer> games) {
        Map<Integer, Minigame> gameMap = new HashMap<>();
        List<Integer> casinosList = new ArrayList<>(Arrays.asList(1, 2, 3));
        casinosList = casinosList.subList(0, games.size());
        Collections.shuffle(casinosList);
        int index = 0;
        for (Integer i : games) {
            int casino = casinosList.get(index);
            index++;
            switch (i) {
                case 0:
                    gameMap.put(casino, new Jackpot());
                    break;
                case 1:
                    gameMap.put(casino, new PayDay());
                    break;
                case 2:
                    gameMap.put(casino, new FiftyFifty());
                    break;
                case 3:
                    gameMap.put(casino, new LuckyPunch());
                    break;
                case 4:
                    gameMap.put(casino, new HighFive());
                    break;
                case 5:
                    gameMap.put(casino, new BlackBox());
                    break;
                case 6:
                    gameMap.put(casino, new BlockIt());
                    break;
                case 7:
                    gameMap.put(casino, new BadLuck());
                    break;
                default:
                    break;
            }
        }
        return Collections.unmodifiableMap(gameMap);
    }
}
