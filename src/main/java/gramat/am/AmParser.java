package gramat.am;

import gramat.am.parsing.AmBase;
import gramat.am.parsing.AmValue;
import gramat.framework.Component;
import gramat.framework.DefaultComponent;
import gramat.input.Tape;
import gramat.input.errors.UnexpectedCharException;

import java.util.ArrayList;
import java.util.List;

public class AmParser extends DefaultComponent implements AmBase, AmValue {

    private final AmMachine machine;

    public AmParser(Component parent) {
        super(parent);
        machine = new AmMachine();
    }

    private AmState merge_state(String id) {
        var state = machine.searchState(id);

        if (state == null) {
            state = new AmState();
            state.id = id;
            machine.states.add(state);
        }

        return state;
    }

    public void parse(Tape tape) {
        expectToken(tape, Tape.STX);

        skipVoid(tape);

        while (tape.alive()) {
            var transition = tryTransition(tape);

            if (transition != null) {
                machine.transitions.add(transition);
                continue;
            }

            var test = tryTest(tape);

            if (test != null) {
                machine.tests.add(test);
                continue;
            }

            if (tryToken(tape, 'I')) {
                var stateId = readString(tape);
                var state = merge_state(stateId);

                state.initial = true;

                expectToken(tape, ';');

                continue;
            }

            if (tryToken(tape, 'A')) {
                var stateId = readString(tape);
                var state = merge_state(stateId);

                state.accepted = true;

                expectToken(tape, ';');
                continue;
            }

            throw new UnexpectedCharException(tape);
        }
    }

    private AmTest tryTest(Tape tape) {
        AmTestType type;

        if (tryToken(tape, 'P')) {
            type = AmTestType.PASS;
        }
        else if (tryToken(tape, 'F')) {
            type = AmTestType.FAIL;
        }
        else {
            return null;
        }

        var test = new AmTest();

        test.type = type;
        test.input = readString(tape);

        if (tryToken(tape, 'V')) {
            test.value = readValue(tape);
            test.hasValue = true;
        }

        expectToken(tape, ';');

        return test;
    }

    private AmTransition tryTransition(Tape tape) {
        if (!tryToken(tape, 'T')) {
            return null;
        }

        var sourceId = readString(tape);

        expectToken(tape, ',');

        var targetId = readString(tape);

        var transition = new AmTransition();
        transition.source = merge_state(sourceId);
        transition.target = merge_state(targetId);

        while (true) {
            if (tryToken(tape, ';')) {
                break;
            }
            else if (tryToken(tape, 'R')) {
                var action = read_action(tape);

                if (transition.symbol == null) {
                    transition.preActions.add(action);
                }
                else {
                    transition.postActions.add(action);
                }
            }
            else if (tryToken(tape, 'S')) {
                var symbol = read_symbol(tape);

                if (transition.symbol != null) {
                    throw new RuntimeException("symbol already defined");
                }

                transition.symbol = symbol;
            }
            else {
                throw new UnexpectedCharException(tape);
            }

        }

        return transition;
    }

    private AmAction read_action(Tape tape) {
        var action = new AmAction();

        action.name = readString(tape);

        if (tryToken(tape, '(')) {
            action.arguments = read_arguments(tape);

            expectToken(tape, ')');
        }

        return action;
    }

    private AmSymbol read_symbol(Tape tape) {
        var symbol = new AmSymbol();

        if (tape.peek() == '*') {
            tape.move();
            symbol.type = AmSymbolType.WILD;
        }
        else {
            symbol.arguments = read_arguments(tape);

            if (symbol.arguments.size() == 1) {
                symbol.type = AmSymbolType.CHAR;
            }
            else if (symbol.arguments.size() == 2) {
                symbol.type = AmSymbolType.RANGE;
            }
            else {
                throw new RuntimeException("unexpected number of arguments: " + tape.getLocation());
            }
        }

        return symbol;
    }

    private List<String> read_arguments(Tape tape) {
        var arguments = new ArrayList<String>();

        while (true) {
            var argument = tryString(tape);

            if (argument == null) {
                break;
            }

            arguments.add(argument);

            if (!tryToken(tape, ',')) {
                break;
            }
        }

        return arguments;
    }

    public AmMachine getMachine() {
        return machine;
    }
}
