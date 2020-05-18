package gramat.automata.ndfa;

import gramat.eval.Action;
import gramat.output.GrammarWriter;
import gramat.util.GramatWriter;
import gramat.util.parsing.Source;

import java.util.*;
import java.util.function.Function;

public class DState {

    boolean accepted;

    DTransition[] transitions;

    public Action[] actions; // TODO remove public

    DTransition wildTransition;

    public DState move(int symbol, List<Action> actions) {
        for (var trn : transitions) {
            if (trn.accepts(symbol)) {
                Collections.addAll(actions, trn.actions);
                return trn.target;
            }
        }

        if (wildTransition != null) {
            Collections.addAll(actions, wildTransition.actions);
            return wildTransition.target;
        }

        return null;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public boolean isFinal() {
        return transitions.length == 0;
    }

    public void write(GrammarWriter writer) {
        if (writer.open(this, "state")) {
            writer.attribute("accepted", accepted);
            for (var t : transitions) {
                t.write(writer);
            }
            writer.close();
        }
    }

    public String getAmCode() {
        var output = new StringBuilder();
        var control = new HashSet<DState>();
        var queue = new LinkedList<DState>();
        var statesIds = new HashMap<DState, String>();
        var idGetter = (Function<DState, String>) (state) -> {
            var id = statesIds.get(state);

            if (id == null) {
                id = String.valueOf(statesIds.size() + 1);

                statesIds.put(state, id);
            }

            return id;
        };

        queue.add(this);

        do {
            var state = queue.remove();

            if (control.add(state)) {
                var sourceID = idGetter.apply(state);

                if (state == this) {
                    output.append("I ");
                    output.append(sourceID);
                    output.append("\n");
                }

                if (state.actions.length == 0) {
                    appendState(output, sourceID, state, null);
                }
                else {
                    for (var action : state.actions) {
                        appendState(output, sourceID, state, action);
                    }
                }

                for (var transition : state.transitions) {
                    var targetID = idGetter.apply(transition.target);

                    appendTransition(output, sourceID, targetID, transition);

                    queue.add(transition.target);
                }

                if (state.wildTransition != null) {
                    var targetID = idGetter.apply(state.wildTransition.target);

                    appendTransition(output, sourceID, targetID, state.wildTransition);

                    queue.add(state.wildTransition.target);
                }
            }
        } while (queue.size() > 0);

        return output.toString();
    }

    private void appendTransition(StringBuilder output, String sourceID, String targetID, DTransition transition) {
        if (transition.actions.length == 0) {
            appendTransition(output, sourceID, targetID, transition, null);
        }
        else {
            for (var action : transition.actions) {
                appendTransition(output, sourceID, targetID, transition, action);
            }
        }
    }

    private void appendTransition(StringBuilder output, String sourceID, String targetID, DTransition transition, Action action) {
        output.append("T ");
        output.append(sourceID);
        output.append(" -> ");
        output.append(targetID);

        output.append(" : ");

        if (transition instanceof DTransitionChar) {
            var tc = (DTransitionChar)transition;

            if (tc.symbol == Source.EOF) {
                output.append("$");
            }
            else {
                var symbol = GramatWriter.toDelimitedString(String.valueOf((char)tc.symbol), '\"');

                output.append(GramatWriter.toDelimitedString(symbol, '\"'));
            }
        }
        else if (transition instanceof DTransitionRange) {
            var tr = (DTransitionRange)transition;
            output.append("[");
            output.append(GramatWriter.toDelimitedString(String.valueOf((char)tr.begin), '\0'));
            output.append("-");
            output.append(GramatWriter.toDelimitedString(String.valueOf((char)tr.end), '\0'));
            output.append("]");
        }
        else if (transition instanceof DTransitionWild) {
            output.append("*");
        }
        else {
            throw new RuntimeException("unsupported transition");
        }

        if (action != null) {
            output.append(" ! ");
            output.append(GramatWriter.toDelimitedString(action.toString(), '\"'));
        }

        output.append("\n");
    }

    private void appendState(StringBuilder output, String id, DState state, Action action) {
        if (state.accepted) {
            output.append("A ");
        }
        else {
            output.append("S ");
        }

        output.append(id);

        if (action != null) {
            output.append(" ! ");
            output.append(GramatWriter.toDelimitedString(action.toString(), '\"'));
        }

        output.append("\n");
    }
}
