package gramat.util;

import gramat.automata.dfa.*;
import gramat.automata.ndfa.NMachine;
import gramat.eval.Action;
import gramat.util.parsing.Source;

import java.util.*;
import java.util.function.Function;

public class AmWriter {

    public static String getAmCode(NMachine machine) {
        var output = new StringBuilder();

        for (var initial : machine.initial) {
            writeInitial(output, String.valueOf(initial.id));
        }

        for (var transition : machine.transitions) {
            var source = String.valueOf(transition.source.id);
            var target = String.valueOf(transition.target.id);
            var symbol = transition.symbol.toString();
            List<Action> actions;

            if (transition.actions.isEmpty()) {
                actions = new ArrayList<>();
                actions.add(null);
            }
            else {
                actions = transition.actions;
            }

            for (var action : actions) {
                var actionStr = (action != null ? action.getDescription() : null);
                writeTransition(output, source, target, symbol, actionStr);
            }
        }

        for (var initial : machine.accepted) {
            writeAccepted(output, String.valueOf(initial.id));
        }

        return output.toString();
    }

    public static String getAmCode(DState root) {
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

        queue.add(root);

        do {
            var state = queue.remove();

            if (control.add(state)) {
                var sourceID = idGetter.apply(state);

                if (state == root) {
                    writeInitial(output, sourceID);
                }

                if (state.accepted) {
                    writeAccepted(output, sourceID);
                }

                for (var transition : state.transitions) {
                    var targetID = idGetter.apply(transition.target);

                    writeTransition(output, sourceID, targetID, transition);

                    queue.add(transition.target);
                }
            }
        } while (queue.size() > 0);

        return output.toString();
    }

    private static void writeTransition(StringBuilder output, String sourceID, String targetID, DTransition transition) {
        if (transition.actions.isEmpty()) {
            writeTransition(output, sourceID, targetID, transition, null);
        }
        else {
            for (var action : transition.actions) {
                writeTransition(output, sourceID, targetID, transition, action);
            }
        }
    }

    private static void writeTransition(StringBuilder output, String sourceID, String targetID, DTransition transition, Action action) {
        String symbol;

        if (transition instanceof DTransitionChar) {
            var tc = (DTransitionChar)transition;

            if (tc.symbol == Source.EOF) {
                symbol = "$";
            }
            else {
                symbol = String.valueOf((char)tc.symbol);
            }
        }
        else if (transition instanceof DTransitionRange) {
            var tr = (DTransitionRange)transition;

            symbol = "[" + ((char)tr.begin) + "-" + ((char)tr.end) + "]";
        }
        else if (transition instanceof DTransitionWild) {
            symbol = "*";
        }
        else {
            throw new RuntimeException("unsupported transition");
        }

        writeTransition(output, sourceID, targetID, symbol, action != null ? action.toString() : null);
    }

    private static void writeTransition(StringBuilder output, String sourceID, String targetID, String symbol, String action) {
        output.append(sourceID);
        output.append(" -> ");
        output.append(targetID);

        output.append(" : ");
        writeValue(output, symbol);

        if (action != null) {
            output.append(" ! ");
            writeValue(output, action);
        }

        output.append("\n");
    }

    private static void writeInitial(StringBuilder output, String stateID) {
        output.append("-> ");
        output.append(stateID);
        output.append("\n");
    }

    private static void writeAccepted(StringBuilder output, String stateID) {
        output.append("=> ");
        output.append(stateID);
        output.append("\n");
    }

    private static void writeValue(StringBuilder output, String text) {
        output.append(text
                .replace("\\", "\\\\")
                .replace("\n", "\\n")
                .replace("!", "\\!")
                .replace(":", "\\:")
                .replace(",", "\\,")
        );
    }
}
