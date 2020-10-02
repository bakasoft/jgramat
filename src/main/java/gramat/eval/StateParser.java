package gramat.eval;

import gramat.actions.*;
import gramat.am.*;
import gramat.data.Comparer;
import gramat.framework.Component;
import gramat.framework.DefaultComponent;
import gramat.input.Tape;
import gramat.parsers.StringParser;
import gramat.symbols.Symbol;
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

        parser.parse(tape);

        var machine = parser.getMachine();

        var node = build_machine(machine);

        if (runTests) {
            for (var test : machine.tests) {
                boolean mustPass;

                if (test.type == AmTestType.PASS) {
                    mustPass = true;
                }
                else if (test.type == AmTestType.FAIL) {
                    mustPass = false;
                }
                else {
                    throw new RuntimeException("unknown test type");
                }

                logger.debug("testing input: %s", test.input);

                if (test.hasValue) {
                    var result = node.evalValue(test.input, logger);

                    if (mustPass) {
                        Comparer.assertEquals(result, test.value);
                    }
                    else {
                        Comparer.assertNotEquals(result, test.value);
                    }
                }
                else {
                    var result = node.evalMatch(test.input, logger);

                    if (result != mustPass) {
                        throw new AssertionError("not match");
                    }
                }
            }
        }

        return node;
    }

    private State build_machine(AmMachine machine) {
        var initial = machine.findInitialState();

        return build_state(machine, initial);
    }

    private State build_state(AmMachine machine, AmState state) {
        if (idNodes.containsKey(state.id)) {
            return idNodes.get(state.id);
        }

        var node = new State(state.id);

        if (state.accepted != null && state.accepted) {
            node.markAccepted();
        }

        idNodes.put(state.id, node);

        for (var transition : machine.findTransitionsFrom(state)) {
            var symbol = make_symbol(transition.symbol);
            var target = build_state(machine, transition.target);
            var link = node.addTransition(symbol, target);

            if (transition.preActions != null) {
                for (var action : transition.preActions) {
                    link.addBefore(make_action(action));
                }
            }

            if (transition.postActions != null) {
                for (var action : transition.postActions) {
                    link.addAfter(make_action(action));
                }
            }
        }

        return node;
    }

    private Action make_action(AmAction data) {
        if (Objects.equals(data.name, "enter")) {
            var token = data.arguments.get(0);

            return new RecursionEnter(token);
        }
        else if (Objects.equals(data.name, "exit")) {
            var token = data.arguments.get(0);

            return new RecursionExit(token);
        }
        else if (Objects.equals(data.name, "beginObject")) {
            var trxID = Integer.parseInt(data.arguments.get(0));

            return new ObjectBegin(trxID);
        }
        else if (Objects.equals(data.name, "beginValue")) {
            var trxID = Integer.parseInt(data.arguments.get(0));

            return new ValueBegin(trxID);
        }
        else if (Objects.equals(data.name, "beginName")) {
            var trxID = Integer.parseInt(data.arguments.get(0));

            return new NameBegin(trxID);
        }
        else if (Objects.equals(data.name, "beginAttribute")) {
            var trxID = Integer.parseInt(data.arguments.get(0));

            return new AttributeBegin(trxID, null);
        }
        else if (Objects.equals(data.name, "endObject")) {
            var trxID = Integer.parseInt(data.arguments.get(0));

            var token = data.arguments.isEmpty() ? null : data.arguments.get(0);

            return new ObjectEnd(trxID, token);
        }
        else if (Objects.equals(data.name, "endValue")) {
            var trxID = Integer.parseInt(data.arguments.get(0));
            var token = (String)null;// TODO data.arguments.get(1);
            var parser = token != null ? gramat.parsers.findParser(token) : new StringParser("string"); // TODO???
            return new ValueEnd(trxID, parser);
        }
        else if (Objects.equals(data.name, "endAttribute")) {
            var trxID = Integer.parseInt(data.arguments.get(0));
            return new AttributeEnd(trxID, null);
        }
        else if (Objects.equals(data.name, "endName")) {
            var trxID = Integer.parseInt(data.arguments.get(0));
            return new NameEnd(trxID);
        }
        else {
            throw new RuntimeException("Unsupported action: " + data.name);
        }
    }

    private Symbol make_symbol(AmSymbol symbol) {
        if (symbol.type == AmSymbolType.WILD) {
            if (symbol.arguments != null && symbol.arguments.size() > 0) {
                throw new RuntimeException();
            }
            return gramat.symbols.makeWild();
        }
        else if (symbol.type == AmSymbolType.CHAR) {
            if (symbol.arguments == null || symbol.arguments.size() != 1) {
                throw new RuntimeException();
            }
            var chr = symbol.arguments.get(0);
            if (chr.length() != 1) {
                throw new RuntimeException("invalid symbol: " + PP.str(chr));
            }
            return gramat.symbols.makeChar(chr.charAt(0));
        }
        else if (symbol.type == AmSymbolType.RANGE) {
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
            return gramat.symbols.makeRange(begin.charAt(0), end.charAt(0));
        }
        else {
            throw new RuntimeException();
        }
    }

}