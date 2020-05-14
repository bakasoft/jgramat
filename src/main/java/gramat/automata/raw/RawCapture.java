package gramat.automata.raw;

import gramat.automata.ndfa.Language;
import gramat.automata.ndfa.NAutomaton;
import gramat.automata.ndfa.Utils;
import gramat.compiling.ValueParser;

import java.util.Set;

public class RawCapture extends RawAutomaton {

    private final RawAutomaton content;
    private final ValueParser parser;

    public RawCapture(RawAutomaton content, ValueParser parser) {
        this.content = content;
        this.parser = parser;
    }

    @Override
    public RawAutomaton collapse() {
        return new RawCapture(content.collapse(), parser);
    }

    @Override
    public NAutomaton build(Language lang) {
        var am = content.build(lang);
        var initial = Set.of(am.initial);
        var accepted = am.accepted;
        var states = am.getStates();
        lang.postBuild(() -> {
            var beginAction = "BEGIN";
            var commitAction = "COMMIT";
            var rollbackAction = "ROLLBACK";

            var beginTrs = Utils.findAllTransitionsBySource(lang, initial);
            var commitTrs = Utils.findAllTransitionsBySource(lang, accepted);
            var rollbackTrs = Utils.findExitTransitions(lang, states);  // TODO exclude initial?

            beginTrs = Utils.removeIf(beginTrs, t -> t.symbol == null);
            commitTrs = Utils.removeIf(commitTrs, t -> t.symbol == null);
            rollbackTrs = Utils.removeIf(rollbackTrs, t -> t.symbol == null);

            Utils.addAction(beginTrs, beginAction);

            commitTrs = Utils.removeIf(commitTrs, t -> t.actions.contains(beginAction));

            Utils.addAction(commitTrs, commitAction);

            rollbackTrs = Utils.removeIf(rollbackTrs, t -> t.actions.contains(beginAction));
            rollbackTrs = Utils.removeIf(rollbackTrs, t -> t.actions.contains(commitAction));

            Utils.addAction(rollbackTrs, rollbackAction);
        });

        return am;
    }

}
