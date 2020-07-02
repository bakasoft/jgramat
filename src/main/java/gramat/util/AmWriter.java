package gramat.util;

import gramat.epsilon.Machine;
import gramat.epsilon.State;

import gramat.epsilon.Transition;

import java.util.*;

public class AmWriter {

    public static String getAmCode(Machine machine) {
        var control = new HashSet<State>();
        var output = new StringBuilder();
        var queue = new LinkedList<State>();

        queue.add(machine.initial);

        while(queue.size() > 0) {
            var state = queue.remove();

            if (control.add(state)) {
                var id = String.valueOf(state.id);

                if (machine.initial == state) {
                    writeInitial(output, id);
                }

                if (machine.accepted == state) {
                    writeAccepted(output, id);
                }

                for (var transition : state.transitions) {
                    writeTransition(output, transition);

                    queue.add(transition.target);
                }
            }
        }

        return output.toString();
    }

    private static void writeTransition(StringBuilder output, Transition transition) {
        var sourceID = String.valueOf(transition.source.id);
        var targetID = String.valueOf(transition.target.id);
        var symbol = transition.symbol.toString();

        if (transition.actions.isEmpty()) {
            writeTransition(output, sourceID, targetID, symbol, null, null);
        }
        else {
            for (var action : transition.actions) {
                writeTransition(output, sourceID, targetID, symbol, null, action.toString());
            }
        }
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
