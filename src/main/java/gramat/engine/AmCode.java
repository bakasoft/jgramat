package gramat.engine;

import gramat.engine.nodet.NContainer;
import gramat.engine.nodet.NRoot;
import gramat.engine.nodet.NMachine;

import java.io.IOException;

public class AmCode {
    public static void writeMachine(Appendable output, NMachine machine) {
        try {
            writeInitial(output, String.valueOf(machine.initial.id));
            writeAccepted(output, String.valueOf(machine.accepted.id));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        writeContainer(output, machine);
    }

    public static void writeContainer(Appendable output, NContainer container) {
        try {
            for (var transition : container.transitions) {
                var sourceID = String.valueOf(transition.source.id);
                var targetID = String.valueOf(transition.target.id);
                var symbol = transition.symbol.toString();

                if (transition.actions.isEmpty()) {
                    writeTransition(output, sourceID, targetID, symbol, null, null);
                }
                else {
                    for (var action : transition.actions) {
                        writeTransition(output, sourceID, targetID, symbol, null, action.getDescription());
                    }
                }
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeInitial(Appendable output, String stateID) throws IOException {
        output.append("-> ");
        output.append(stateID);
        output.append("\n");
    }

    private static void writeAccepted(Appendable output, String stateID) throws IOException {
        output.append(stateID);
        output.append(" <=\n");
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
}
