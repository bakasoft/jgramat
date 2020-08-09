package gramat.engine;

import gramat.engine.deter.DState;
import gramat.engine.indet.ILanguage;
import gramat.engine.indet.IState;
import gramat.engine.nodet.NMachine;
import gramat.engine.nodet.NState;
import gramat.engine.nodet.NStateList;

import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.LinkedList;

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
                        var symbol = transition.symbol != null ? transition.symbol.toString() : "¶";

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

    public static void write(PrintStream output, ILanguage lang, IState initial) {
        try {
            var control = new HashSet<IState>();
            var queue = new LinkedList<IState>();

            queue.add(initial);

            do {
                var state = queue.remove();

                if (control.add(state)) {
                    if (state == initial) {
                        writeState(output, state.id, true, false, null);
                    }

                    for (var transition : lang.findTransitionsBySource(state)) {
                        var sourceID = transition.source.id;
                        var targetID = transition.target.id;
                        var symbol = transition.symbol.toString();

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
                        writeState(output, state.id, false, true, null);
                    }
                }
            } while (queue.size() > 0);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void write(PrintStream output, DState initial) {
        try {
            var control = new HashSet<DState>();
            var queue = new LinkedList<DState>();

            queue.add(initial);

            do {
                var source = queue.remove();

                if (control.add(source)) {
                    if (source == initial) {
                        writeState(output, String.valueOf(source.id), true, false, null);
                    }

                    if (source.transitions != null) {
                        for (var transition : source.transitions) {
                            var sourceID = String.valueOf(source.id);
                            var targetID = String.valueOf(transition.target.id);
                            var symbol = transition.symbol.toString();

                            if (transition.actions.length == 0) {
                                writeTransition(output, sourceID, targetID, symbol, null, null);
                            } else {
                                for (var action : transition.actions) {
                                    writeTransition(output, sourceID, targetID, symbol, null, action.getDescription());
                                }
                            }

                            queue.add(transition.target);
                        }
                    }

                    if (source.accepted) {
                        writeState(output, String.valueOf(source.id), false, true, null);
                    }
                }
            } while (queue.size() > 0);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
