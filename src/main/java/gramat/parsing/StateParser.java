package gramat.parsing;

import gramat.actions.*;
import gramat.actions.transactions.*;
import gramat.badges.BadgeMode;
import gramat.exceptions.UnsupportedValueException;
import gramat.machine.Effect;
import gramat.models.automata.*;
import gramat.badges.Badge;
import gramat.framework.Component;
import gramat.framework.DefaultComponent;
import gramat.input.Tape;
import gramat.machine.State;
import gramat.symbols.Symbol;
import gramat.util.Args;
import gramat.util.PP;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StateParser extends DefaultComponent {

    private final Map<String, State> idNodes;

    public StateParser(Component parent) {
        super(parent);
        this.idNodes = new HashMap<>();
    }

    public State parse(Tape tape) {
        return parse(tape, false);
    }

    public State parse(Tape tape, boolean runTests) {
        var parser = new AmParser(this);

        var machine = parser.parseMachine(tape);

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
        var initial = machine.findInitialState();

        return build_state(machine, initial);
    }

    private State build_state(ModelMachine machine, ModelState state) {
        if (idNodes.containsKey(state.id)) {
            return idNodes.get(state.id);
        }

        var node = new State(state.id);

        if (state.accepted != null && state.accepted) {
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
        var args = Args.of(data.arguments);
        if (Objects.equals(data.name, "enter")) {
            var token = args.pullAs(String.class);
            var badge = gramat.badges.badge(token);
            return new RecursionEnter(badge);
        }
        else if (Objects.equals(data.name, "exit")) {
            var token = args.pullAs(String.class);
            var badge = gramat.badges.badge(token);
            return new RecursionExit(badge);
        }
        else if (Objects.equals(data.name, "begin")) {
            var type = args.pullAs(String.class);
            var trxID = Integer.parseInt(args.pullAs(String.class));

            if (Objects.equals(type, "Object")) {
                return new BeginAction(new ObjectTransaction(trxID));
            }
            else if (Objects.equals(type, "Value")) {
                return new BeginAction(new ValueTransaction(trxID, null));
            }
            else if (Objects.equals(type, "Name")) {
                return new BeginAction(new NameTransaction(trxID));
            }
            else if (Objects.equals(type, "Attribute")) {
                return new BeginAction(new AttributeTransaction(trxID, null));
            }
            else if (Objects.equals(type, "Array")) {
                return new BeginAction(new ArrayTransaction(trxID, null));
            }
            else {
                throw new UnsupportedValueException(type);
            }
        }
        else if (Objects.equals(data.name, "end")) {
            var trxID = Integer.parseInt(data.arguments.get(0));
            // TODO find transaction by ID
            return new EndAction(null);
        }
        else {
            throw new RuntimeException("Unsupported action: " + data.name);
        }
    }

    private Symbol make_symbol(ModelSymbol symbol) {
        if (symbol.type == ModelSymbolType.WILD) {
            if (symbol.arguments != null && symbol.arguments.size() > 0) {
                throw new RuntimeException();
            }
            return gramat.symbols.wild();
        }
        else if (symbol.type == ModelSymbolType.CHAR) {
            if (symbol.arguments == null || symbol.arguments.size() != 1) {
                throw new RuntimeException();
            }
            var chr = symbol.arguments.get(0);
            if (chr.length() != 1) {
                throw new RuntimeException("invalid symbol: " + PP.str(chr));
            }
            return gramat.symbols.character(chr.charAt(0));
        }
        else if (symbol.type == ModelSymbolType.RANGE) {
            if (symbol.arguments == null || symbol.arguments.size() != 2) {
                throw new RuntimeException();
            }
            var begin = symbol.arguments.get(0);
            var end = symbol.arguments.get(1);
            if (begin.length() != 1) {
                throw new RuntimeException();
            }
            else if (end.length() != 1) {
                throw new RuntimeException();
            }
            return gramat.symbols.range(begin.charAt(0), end.charAt(0));
        }
        else {
            throw new RuntimeException();
        }
    }

    private Badge make_badge(ModelBadge badge) {
        if (badge == null) {
            return gramat.badges.empty();
        }

        return gramat.badges.badge(badge.token);
    }

}
