package scrumroyale;

import java.util.List;
import java.util.concurrent.Callable;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import scrumroyale.vegas.Vegas;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Spec;

/**
 * Runs the game at its different phases.
 */
@Command(description = "Play ScrumRoyale.", name = "scrumroyale", mixinStandardHelpOptions = true, version = "ScrumRoyale 1.0")
public class App implements Callable<Integer> {
    private Vegas vegas;

    @Spec
    CommandSpec spec;

    /**
     * The Phase 0 implementation of Scrum Royale
     */
    @Command(name = "phase0", description = "Play the Phase 0 version of ScrumRoyale")
    void phase0() {
        // player, rounds, large die?, die amount, chips
        vegas = new Vegas(1, false, 1, false, List.of());
        vegas.play();
    }

    /**
     * The Phase 1 implementation of Scrum Royale
     */
    @Command(name = "phase1", description = "Play the Phase 1 version of ScrumRoyale")
    void phase1() {
        // player, rounds, large die?, die amount, chips, minigames
        vegas = new Vegas(1, false, 1, true, List.of());
        vegas.play();
    }

    /**
     * The Phase 2 implementation of Scrum Royale
     */
    @Command(name = "phase2", description = "Play the Phase 2 version of ScrumRoyale")
    void phase2() {
        // player, rounds, large die?, die amount, chips
        vegas = new Vegas(1, true, 1, true, List.of());
        vegas.play();
    }

    /**
     * The Phase 3 implementation of Scrum Royale
     */
    @Command(name = "phase3", description = "Play the Phase 3 version of ScrumRoyale")
    void phase3() {
        // player, rounds, large die?, die amount, chips
        vegas = new Vegas(3, true, 1, true, List.of());
        vegas.play();
    }

    /**
     * The Phase 4 implementation of Scrum Royale
     */
    @Command(name = "phase4", description = "Play the Phase 4 version of ScrumRoyale")
    void phase4() {
        // player, rounds, large die?, die amount, chips
        vegas = new Vegas(3, true, 3, true, List.of());
        vegas.play();
    }

    /**
     * The Phase 5 implementation of Scrum Royale
     */
    @Command(name = "phase5", description = "Play the Phase 5 version of ScrumRoyale")
    void phase5() {
        // player, rounds, large die?, die amount, chips
        vegas = new Vegas(3, true, 3, true, List.of(0));
        vegas.play();
    }

    /**
     * The Phase 6 implementation of Scrum Royale
     */
    @Command(name = "phase6", description = "Play the Phase 6 version of ScrumRoyale")
    void phase6() {
        // player, rounds, large die?, die amount, chips
        vegas = new Vegas(3, true, 3, true, List.of(0, 1));
        vegas.play();
    }

    @Command(name = "phase7", description = "Play the Phase 7 version of ScrumRoyale")
    void phase7() {
        // player, rounds, large die?, die amount, chips
        vegas = new Vegas(3, true, 3, true, List.of(0, 1, 2));
        vegas.play();
    }

    @Command(name = "phase8", description = "Play the Phase 8 version of ScrumRoyale")
    void phase8() {
        // player, rounds, large die?, die amount, chips
        vegas = new Vegas(3, true, 3, true, List.of(1, 2, 3));
        vegas.play();
    }

    @Command(name = "phase9", description = "Play the Phase 9 version of ScrumRoyale")
    void phase9() {
        // player, rounds, large die?, die amount, chips
        vegas = new Vegas(3, true, 3, true, List.of(2, 3, 4));
        vegas.play();
    }

    @Command(name = "phase10", description = "Play the Phase 10 version of ScrumRoyale")
    void phase10() {
        // player, rounds, large die?, die amount, chips
        vegas = new Vegas(3, true, 3, true, List.of(3, 4, 5));
        vegas.play();
    }

    @Command(name = "phase11", description = "Play the Phase 11 version of ScrumRoyale")
    void phase11() {
        // player, rounds, large die?, die amount, chips
        vegas = new Vegas(3, true, 3, true, List.of(4, 5, 6));
        vegas.play();
    }

    @Command(name = "phase12", description = "Play the Phase 12 version of ScrumRoyale")
    void phase12() {
        // player, rounds, large die?, die amount, chips
        vegas = new Vegas(3, true, 3, true, List.of(5, 6, 7));
        vegas.play();
    }

    // @Command(name = "phase14", description = "Play the Phase 14 version of ScrumRoyale")
    // void phase14() {
    //     // player, rounds, large die?, die amount, chips
    //     vegas = new Vegas(3, true, 3, true, List.of(5, 6, 7), 2);
    //     vegas.play();
    // }

    /**
     * Starts the game with the specified arguments.
     * 
     * @param args The arguments.
     * @throws Exception If an error was thrown.
     */
    public static void main(String... args) throws Exception {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        throw new ParameterException(spec.commandLine(), "Specify a subcommand");
    }
}
