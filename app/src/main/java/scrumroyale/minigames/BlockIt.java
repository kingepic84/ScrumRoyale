package scrumroyale.minigames;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import scrumroyale.Dice;
import scrumroyale.Die;
import scrumroyale.Minigame;
import scrumroyale.Player;
import scrumroyale.UI;
import scrumroyale.vegas.Casino;
import scrumroyale.vegas.GameState;
import scrumroyale.vegas.GameState.Minishot;
import scrumroyale.vegas.Vegas.Effect;

/** Block It class. */
public class BlockIt extends Minigame {

    private Map<Integer, List<Dice>> piles = new HashMap<Integer, List<Dice>>(3);
    private Dice pile = new Dice();

    /**
     * The Block it constructor.
     */
    public BlockIt() {
        super("Block It", "", Effect.ACTIVATED);
        piles.put(1, new ArrayList<>(Arrays.asList(new Dice(1, false, 0), new Dice(1, false, 0))));
        piles.put(2, new ArrayList<>(Arrays.asList(new Dice(2, false, 0), new Dice(2, false, 0))));
        piles.put(3, new ArrayList<>(Arrays.asList(new Dice(3, false, 0))));
        this.description = "Place blank dice on a casino. " + pilesToString();
    }

    private String pilesToString() {
        StringBuilder sb = new StringBuilder();
        piles.forEach((i, dice) -> {
            for (Dice dice2 : dice) {
                sb.append(dice2.size() + ",");
            }
        });
        if (piles.size() != 0) {
            sb.delete(sb.length() - 1, sb.length());
        }
        return sb.insert(0, "[").append("]").toString();
    }

    private Dice assignProperValues(int size, int inputCasino) {
        Dice newDice = new Dice(0, false, 0);
        for (int i = 0; i < size; i++) {
            Die die = new Die(false, 0);
            die.setValue(String.valueOf(inputCasino));
            newDice.addDice(die);
        }
        return newDice;
    }

    @Override
    public void execute(List<Casino> casinos, List<Player> players, int player, UI console, int casino) {
        if (piles.size() == 0)
            return;
        Player p = players.get(player - 1);
        if (players.size() < 4) {
            players.add(new Player(new Dice(), 0));
        }
        int inputPile = p.getUserInput(Type.BLOCKIT,
                "The dice pool options are " + pilesToString() + ".\nWhich pool would you like?~1~3", console);
        int inputCasino = p.getUserInput(Type.BLOCKIT, "Which casino will you add them to?~1~6", console);
        while (!isCorrectInput(inputPile, inputCasino)) {
            inputPile = p.getUserInput(Type.BLOCKIT,
                    "The dice pool options are " + pilesToString() + ".\nWhich pool would you like?~1~3", console);
        }
        casinos.get(inputCasino - 1).addDice(pile);
        console.display(List.of("user input"), GameState.takeSnapshot(casinos, players,
                List.of(players.size(), 0, 0, 0, 0, inputCasino), List.of(false, false), pile.toString()));
        String updateString = String.format("P%d added %d imaginary dice to Casino %d", player, pile.size(),
                inputCasino);
        Minishot updateMinishot = GameState.takeMinishot(casinos, casino, players, this, updateString, player, false,
                0);
        console.displayMinigameInfo("minigame update", updateMinishot);
        this.description = "Place blank dice on a casino. " + pilesToString();
    }

    private boolean isCorrectInput(int inputPile, int inputCasino) {
        try {
            if (!piles.containsKey(inputPile)) {
                throw new IndexOutOfBoundsException();
            }
            Dice pileToRemove = piles.get(inputPile).get(0);
            pile = assignProperValues(pileToRemove.size(), inputCasino);
            piles.get(inputPile).remove(pileToRemove);
            if (piles.get(inputPile).size() == 0) {
                piles.remove(inputPile);
            }
            return true;
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid Input. Please choose an input from the dice choices available.");
            return false;
        }
    }
}
