package gramat.automata.raw.actuators;

import gramat.eval.Action;
import gramat.automata.ndfa.Language;
import gramat.automata.ndfa.NAutomaton;
import gramat.automata.ndfa.NTransition;
import gramat.automata.ndfa.Utils;
import gramat.automata.raw.RawAutomaton;

import java.util.List;
import java.util.Set;

abstract public class RawTransaction extends RawAutomaton {

    abstract public Action createBeginAction();
    abstract public Action createCommitAction(Action beginAction);
    abstract public Action createRollbackAction(Action beginAction);

    protected final RawAutomaton content;

    public RawTransaction(RawAutomaton content) {
        this.content = content;
    }

    @Override
    public NAutomaton build(Language lang) {
        var am = content.build(lang);
        var initial = Set.of(am.initial);
        var accepted = am.accepted;
        var states = am.getStates();
        lang.postBuild(() -> {
            var beginAction = createBeginAction();
            var commitAction = createCommitAction(beginAction);
            var rollbackAction = createRollbackAction(beginAction);

            List<NTransition> beginTrs = beginAction != null ? Utils.findAllTransitionsBySource(lang, initial) : List.of();
            List<NTransition> commitTrs = commitAction != null ? Utils.findAllTransitionsBySource(lang, accepted) : List.of();
            List<NTransition> rollbackTrs = rollbackAction != null ? Utils.findExitTransitions(lang, states) : List.of();  // TODO exclude initial?

            // exclude null-transitions and circular transitions
            beginTrs = Utils.removeIf(beginTrs, t -> t.symbol == null || t.source == t.target);
            commitTrs = Utils.removeIf(commitTrs, t -> t.symbol == null || t.source == t.target);
            rollbackTrs = Utils.removeIf(rollbackTrs, t -> t.symbol == null || t.source == t.target);

            Utils.addAction(beginTrs, beginAction);

            // exclude begin transitions
            commitTrs = Utils.removeIf(commitTrs, t -> t.actions.contains(beginAction));

            Utils.addAction(commitTrs, commitAction);

            // exclude begin & commit transitions
            rollbackTrs = Utils.removeIf(rollbackTrs, t -> t.actions.contains(beginAction));
            rollbackTrs = Utils.removeIf(rollbackTrs, t -> t.actions.contains(commitAction));

            Utils.addAction(rollbackTrs, rollbackAction);

            if (beginAction != null && beginTrs.isEmpty()) {
                Utils.addStateAction(initial, beginAction);
            }

            if (commitAction != null && commitTrs.isEmpty()) {
                Utils.addStateAction(accepted, commitAction);
            }
        });

        return am;
    }

}
