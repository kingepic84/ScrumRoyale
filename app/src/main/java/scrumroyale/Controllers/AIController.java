package scrumroyale.Controllers;

import scrumroyale.Controller;
import scrumroyale.Dice;
import scrumroyale.Minigame.Type;
import scrumroyale.RNG;
import scrumroyale.UI;

/** AIController class. */
public class AIController implements Controller {

    /** AIController Constructor. */
    public AIController() {
    }

    @Override
    public int getUserInput(UI ui, Dice dice, int chips) {
        return Integer.parseInt(dice.getMost());
    }

    /**
     * Input for minigame dice rolls.
     * 
     * @param prompt A {@code String} representing what the controller needs to ask
     *               of the player..
     * @param ui     The ui object.
     * @param dice   A deepcopy of the dice the player rolled.
     * @param chips  number of chips the player has.
     * @return An {@code int} representing the player's choice.
     */
    @Override
    public int getUserInput(Type game, String prompt, UI ui, Dice dice, int chips) {
        String realPrompt = prompt.split("~")[0];
        RNG rand = RNG.getInstance();
        switch (game) {
            case LUCKYPUNCH:
                return rand.nextInt(1, 4);
            case BLACKBOX:
                if (realPrompt.charAt(0) == 'T') {
                    if (realPrompt.charAt(23) == ']') {
                        return 1;
                    }

                    int bracket = realPrompt.indexOf(']');
                    int period = realPrompt.indexOf('.') + 1;
                    String subPrompt = realPrompt.substring(bracket, period);

                    if (subPrompt.charAt(7) == ']' || subPrompt.charAt(8) == ']') {
                        return 2;
                    }

                    return rand.nextInt(2) + 1;
                }

                else if (realPrompt.charAt(4) == 'o') {
                    if (realPrompt.charAt(71) > realPrompt.charAt(76)) {
                        int rand_int2 = rand.nextInt(4);
                        if (rand_int2 < 3) {
                            return 1;
                        }
                        return 2;
                    } else if (realPrompt.charAt(71) <= realPrompt.charAt(76)) {
                        int rand_int2 = rand.nextInt(4);
                        if (rand_int2 < 3) {
                            return 2;
                        }
                        return 1;
                    }
                }
            case FIFTYFIFTY:
                int mark = Integer.parseInt(prompt.substring(47, prompt.indexOf('.')));
                int payout = Character.getNumericValue(prompt.charAt(29));

                if (payout == 0) {
                    if (mark <= 6) {
                        return 2;
                    }
                    return 1;
                } else {
                    if (mark < 5) {
                        return 2;
                    } else if (mark > 8) {
                        return 1;
                    }
                    return 0;
                }
            case BLOCKIT:
                // Checking if we're asking for the dice pool or the casino.
                if (realPrompt.contains("pool")) {
                    // Indexing where the start of the pile options are
                    String[] choices = realPrompt.substring(realPrompt.indexOf("[") + 1, realPrompt.indexOf("]"))
                        .split(",");
                    return rand.choice(choices);
                }
                return rand.nextInt(1, 7);
            default:
                return -1;
        }
    }

}
