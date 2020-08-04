package gramat.engine;

import gramat.engine.indet.IMachine;
import gramat.engine.indet.IState;
import gramat.engine.nodet.NMachine;
import gramat.engine.nodet.NState;
import gramat.engine.nodet.NStateList;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class AmCode {

    public static void writeMachine(Appendable output, NMachine machine) {
        write(output, machine.initial, new NStateList(machine.accepted));
    }

    public static void write(Appendable output, NState initial, NStateList accepted) {
        try {
            var control = new HashSet<NState>();
            var queue = new LinkedList<NState>();

            queue.add(initial);

            do {
                var state = queue.remove();

                if (control.add(state)) {
                    if (state == initial) {
                        writeState(output, initial.id, true, false, null);
                    }

                    for (var transition : state.getTransitions()) {
                        var sourceID = transition.source.id;
                        var targetID = transition.target.id;
                        var symbol = transition.symbol.toString();
                        var check = transition.check.toString();

                        if (check != null) {
                            symbol = symbol + " / " + check;
                        }

                        if (transition.actions.isEmpty()) {
                            writeTransition(output, sourceID, targetID, symbol, null, null);
                        }
                        else {
                            for (var action : transition.actions) {
                                writeTransition(output, sourceID, targetID, symbol, null, action.getDescription());
                            }
                        }

                        queue.add(transition.target);
                    }

                    if (accepted.contains(state)) {
                        writeState(output, state.id, false, true, null);
                    }
                }
            } while (queue.size() > 0);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeState(Appendable output, String stateID, boolean initial, boolean accepted, String comment) throws IOException {
        if (initial) {
            output.append("-> ");
        }

        output.append(stateID);

        if (accepted) {
            output.append(" <=");
        }

        if (comment != null && !comment.isBlank()) {
            output.append(" # ");
            output.append(comment);
        }

        output.append("\n");
    }

    private static void writeTransition(Appendable output, String sourceID, String targetID, String symbol, String beforeAction, String afterAction) throws IOException {
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

    private static void writeValue(Appendable output, String text) throws IOException {
        output.append(text
                .replace("\\", "\\\\")
                .replace("\n", "\\n")
                .replace("!", "\\!")
                .replace(":", "\\:")
                .replace(",", "\\,")
        );
    }

    public static String escape(String value) {
        var output = new StringBuilder();

        for (var c : value.toCharArray()) {
            if (c == '\n') {
                output.append("\\n");
            }
            else if (c == '\t') {
                output.append("\\t");
            }
            else if (c == '\f') {
                output.append("\\f");
            }
            else if (c == '\b') {
                output.append("\\b");
            }
            else if (c == '\\') {
                output.append("\\\\");
            }
            else if (c == '\"') {
                output.append("\\\"");
            }
            else if (c == '\'') {
                output.append("\\'");
            }
            else if (Character.isISOControl(c)) {
                var hex = Integer.toHexString(c);
                output.append("\\u");
                if (hex.length() < 4) {
                    output.append("0".repeat(4 - hex.length()));
                }
                output.append(hex);
            }
            else {
                output.append(c);
            }
        }

        return output.toString();
    }

    public static void write(PrintStream output, IMachine machine) {
        try {
            var control = new HashSet<IState>();
            var queue = new LinkedList<IState>();

            queue.add(machine.initial);

            do {
                var state = queue.remove();

                if (control.add(state)) {
                    if (state == machine.initial) {
                        writeState(output, state.computeID(), true, false, null);
                    }

                    for (var transition : machine.findTransitionsBySource(state)) {
                        var sourceID = transition.source.computeID();
                        var targetID = transition.target.computeID();
                        var symbol = transition.symbol.toString();
                        var check = transition.check.toString();

                        if (check != null) {
                            symbol = symbol + " / " + check;
                        }

                        if (transition.actions.isEmpty()) {
                            writeTransition(output, sourceID, targetID, symbol, null, null);
                        }
                        else {
                            for (var action : transition.actions) {
                                writeTransition(output, sourceID, targetID, symbol, null, action.getDescription());
                            }
                        }

                        queue.add(transition.target);
                    }

                    if (state.accepted) {
                        writeState(output, state.computeID(), false, true, null);
                    }
                }
            } while (queue.size() > 0);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
