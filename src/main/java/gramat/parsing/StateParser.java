package gramat.parsing;

import gramat.actions.*;
import gramat.badges.BadgeMode;
import gramat.framework.Context;
import gramat.machine.Effect;
import gramat.models.automata.*;
import gramat.badges.Badge;
import gramat.input.Tape;
import gramat.machine.State;
import gramat.parsers.ParserSource;
import gramat.symbols.Symbol;

import java.util.LinkedHashMap;
import java.util.Map;

public class StateParser {

    private final Context ctx;
    private final Map<String, State> idNodes;

    public StateParser(Context ctx) {
        this.ctx = ctx;
        this.idNodes = new LinkedHashMap<>();
    }

    public State parse(Tape tape, ParserSource parsers) {
        return parse(tape, false, parsers);
    }

    public State parse(Tape tape, boolean runTests, ParserSource parsers) {
        var parser = new AmParser(ctx);
        var p = new Parser(tape, parsers);
        var machine = parser.parseMachine(p);

        var node = build_machine(machine);

//        if (runTests) {
//            for (var test : machine.tests) {
//                boolean mustPass;
//
//                if (test.type == AmTestType.PASS) {
//                    mustPass = true;
//                }
//                else if (test.type == AmTestType.FAIL) {
//                    mustPass = false;
//                }
//                else {
//                    throw new RuntimeException("unknown test type");
//                }
//
//                logger.debug("testing input: %s", test.input);
//
//                if (test.hasValue) {
//                    var result = node.evalValue(test.input, logger);
//
//                    if (mustPass) {
//                        Comparer.assertEquals(result, test.value);
//                    }
//                    else {
//                        Comparer.assertNotEquals(result, test.value);
//                    }
//                }
//                else {
//                    var result = node.evalMatch(test.input, logger);
//
//                    if (result != mustPass) {
//                        throw new AssertionError("not match");
//                    }
//                }
//            }
//        }

        return node;
    }

    private State build_machine(ModelMachine machine) {
        var initial = machine.initial;

        return build_state(machine, initial);
    }

    private State build_state(ModelMachine machine, ModelState state) {
        if (idNodes.containsKey(state.id)) {
            return idNodes.get(state.id);
        }

        var node = new State(state.id);

        if (state.accepted) {
            node.accepted = true;
        }

        idNodes.put(state.id, node);

        for (var transition : machine.findTransitionsFrom(state)) {
            var symbol = make_symbol(transition.symbol);
            var badge = make_badge(transition.badge);
            var mode = BadgeMode.NONE; // TODO how to parse the mode?
            var target = build_state(machine, transition.target);
            var event = Event.empty();

            if (transition.preActions != null) {
                for (var action : transition.preActions) {
                    event.prepend(make_action(action));
                }
            }

            if (transition.postActions != null) {
                for (var action : transition.postActions) {
                    event.append(make_action(action));
                }
            }

            node.transition.add(badge, symbol, new Effect(target, event.before.toArray(), event.after.toArray()));
        }

        return node;
    }

    private Action make_action(ModelAction data) {
        // TODO use ActionAssembler
        throw new RuntimeException();
    }

    private Symbol make_symbol(ModelSymbol symbol) {
        // TODO use SymbolAssembler
        throw new RuntimeException();
    }

    private Badge make_badge(ModelBadge badge) {
        // TODO BadgeAssembler
        throw new RuntimeException();
    }

}
