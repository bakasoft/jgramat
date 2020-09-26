package gramat.pivot;

import gramat.input.Tape;
import gramat.pivot.data.*;

import java.util.*;

public class XDeterminer {

    private final XLanguage lang;

    public XDeterminer(XLanguage lang) {
        this.lang = lang;
    }

    public XSegment transform(String machineName, List<XActionBlock> blocks) {
        var segment = resolve_machine(machineName, blocks);

//        remove_transient_transitions(segment);

        System.out.println("N-DFA >>>>>>>>>>");
        segment.printAmCode(System.out);
        System.out.println("<<<<<<<<<< N-DFA");
        System.out.println();

        return segment;
    }

    private XSegment resolve_machine(String machineName, List<XActionBlock> blocks) {
        var segment = resolve_machine(machineName, new HashMap<>(), blocks);

        // wrap machine
        var newInitial = lang.createState();
        var newAccepted = lang.createState();
        lang.createTransitionChar(newInitial, segment.initial, Tape.STX);
        lang.createTransitionChar(segment.accepted, newAccepted, Tape.ETX);
        return new XSegment(lang, newInitial, newAccepted);
    }

    private XSegment resolve_machine(String name, Map<String, XSegment> context, List<XActionBlock> blocks) {
        if (context.containsKey(name)) {
            var segment = context.get(name);
            var newInitial = lang.createState();
            var newAccepted = lang.createState();

            lang.createTransitionEmpty(newInitial, segment.initial);
            lang.createTransitionEmpty(segment.accepted, newAccepted);

            return new XSegment(lang, newInitial, newAccepted);
        }
        else {
            var machine = lang.findMachine(name);
            var segment = machine.deepCopy(blocks);

            context.put(machine.name, segment);

            for (var transition : segment.listTransitions()) {
                if (transition.data instanceof TDReference) {
                    var data = (TDReference) transition.data;
                    var source = transition.source;
                    var target = transition.target;

                    transition.delete();

                    var nested = resolve_machine(data.reference, context, blocks);

                    // apply reference actions over resolved machine
                    // var nestedInitial = apply_pre_actions(nested.initial, data.preActions);
                    // var nestedAccepted = apply_post_actions(nested.accepted, data.postActions);

                    lang.createTransitionEmpty(source, nested.initial);
                    lang.createTransitionEmpty(nested.accepted, target);
                }
            }

            context.remove(machine.name);

            return segment;
        }
    }

}
