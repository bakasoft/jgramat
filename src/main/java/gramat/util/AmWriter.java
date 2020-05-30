package gramat.util;

import gramat.automata.dfa.*;
import gramat.automata.ndfa.NMachine;
import gramat.automata.ndfa.NSegment;
import gramat.automata.ndfa.NState;
import gramat.eval.Action;
import gramat.util.parsing.Source;

import java.util.*;
import java.util.function.Function;

public class AmWriter {

    public static String getAmCode(DMachine machine) {
        var control = new HashSet<DState>();
        var output = new StringBuilder();
        var statesIds = new HashMap<DState, String>();
        var idGetter = (Function<DState, String>) (state) -> {
            var id = statesIds.get(state);

            if (id == null) {
                id = String.valueOf(statesIds.size() + 1);

                statesIds.put(state, id);
            }

            return id;
        };

        var queue = new LinkedList<>(machine.initial);

        while(queue.size() > 0) {
            var state = queue.remove();

            if (control.add(state)) {
                var id = idGetter.apply(state);

                if (machine.initial.contains(state)) {
                    writeInitial(output, id);
                }

                if (machine.accepted.contains(state)) {
                    writeAccepted(output, id);
                }

                for (var transition : state.transitions) {
                    if (machine.transitions.contains(transition)) {
                        var targetID = idGetter.apply(transition.target);
                        var symbol = transition.getSymbol();
                        writeTransition(output, id, targetID, symbol, null, null);
                    }

                    if (machine.states.contains(transition.target)) {
                        queue.add(transition.target);
                    }
                }
            }
        }

        return output.toString();
    }

    public static String getAmCode(NSegment segment) {
        var output = new StringBuilder();

        writeInitial(output, String.valueOf(segment.initial.id));

        var control = new HashSet<NState>();
        var queue = new LinkedList<NState>();

        queue.add(segment.initial);

        while (queue.size() > 0) {
            var state = queue.remove();

            if (control.add(state)) {
                if (state == segment.accepted) {
                    writeAccepted(output, String.valueOf(segment.accepted.id));
                }

                for (var transition : state.getTransitions()) {
                    var source = String.valueOf(transition.source.id);
                    var target = String.valueOf(transition.target.id);
                    var symbol = transition.symbol;
                    List<Action> actions;  // TODO render before actions

                    if (transition.actions.isEmpty()) {
                        actions = new ArrayList<>();
                        actions.add(null);
                    } else {
                        actions = transition.actions;
                    }

                    for (var action : actions) {
                        var actionStr = (action != null ? action.getDescription() : null);
                        var symbolStr = (symbol != null ? symbol.toString() : null);
                        writeTransition(output, source, target, symbolStr, null, actionStr);
                    }

                    queue.add(transition.target);
                }
            }
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

                    for (var option : state.options) {
                        var optionID = statesIds.get(option);

                        if (optionID == null) {
                            optionID = idGetter.apply(option);

                            queue.add(option);
                        }

                        writeTransition(output, sourceID, targetID, null, "EVAL " + optionID, null);
                    }

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
                symbol = GramatWriter.toDelimitedString(String.valueOf((char)tc.symbol), '\"');
            }
        }
        else if (transition instanceof DTransitionRange) {
            var tr = (DTransitionRange)transition;
            var beginStr = GramatWriter.toDelimitedString(String.valueOf(((char)tr.begin)), '\0');
            var endStr = GramatWriter.toDelimitedString(String.valueOf(((char)tr.end)), '\0');
            symbol = "[" + beginStr + "-" + endStr + "]";
        }
        else if (transition instanceof DTransitionWild) {
            symbol = "*";
        }
        else {
            symbol = null;
        }

        writeTransition(output, sourceID, targetID, symbol, null, action != null ? action.toString() : null);
    }

    private static void writeTransition(StringBuilder output, String sourceID, String targetID, String symbol, String beforeAction, String afterAction) {
        output.append(sourceID);
        output.append(" -> ");
        output.append(targetID);

        if (symbol != null) {
            output.append(" : ");
            writeValue(output, symbol);
        }

        if (beforeAction != null) {
            output.append(" !< ");
            writeValue(output, beforeAction);
        }

        if (afterAction != null) {
            output.append(" !> ");
            writeValue(output, afterAction);
        }

        output.append("\n");
    }

    private static void writeState(StringBuilder output, String stateID) {
        output.append(stateID);
        output.append("\n");
    }

    private static void writeInitial(StringBuilder output, String stateID) {
        output.append("-> ");
        output.append(stateID);
        output.append("\n");
    }

    private static void writeAccepted(StringBuilder output, String stateID) {
        output.append(stateID);
        output.append(" <=\n");
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
