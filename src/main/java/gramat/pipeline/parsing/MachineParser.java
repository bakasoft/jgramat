package gramat.pipeline.parsing;

import gramat.exceptions.UnexpectedCharException;
import gramat.scheme.data.automata.*;

import java.util.ArrayList;
import java.util.List;

public interface MachineParser extends BaseParser, ValueParser {

    default MachineData parseMachine() {
        var machine = new MachineData();
        var tape = getTape();

        skipVoid();

        while (tape.alive()) {
            var transition = tryTransition(machine);

            if (transition != null) {
                machine.transitions.add(transition);
                continue;
            }

            if (tryToken('I')) {
                var stateId = readString();
                var state = machine.mergeState(stateId);

                machine.initial = state;

                expectToken(';');

                continue;
            }

            if (tryToken('A')) {
                var stateId = readString();
                var state = machine.mergeState(stateId);

                state.accepted = true;

                expectToken(';');
                continue;
            }

            throw new UnexpectedCharException(tape);
        }

        return machine;
    }

    default TransitionData tryTransition(MachineData machine) {
        if (!tryToken('T')) {
            return null;
        }

        var sourceId = readString();

        expectToken(',');

        var targetId = readString();

        var transition = new TransitionData();
        transition.source = machine.mergeState(sourceId);
        transition.target = machine.mergeState(targetId);

        while (true) {
            if (tryToken(';')) {
                break;
            }
            else if (tryToken('R')) {
                var action = read_action();

                if (transition.symbol == null) {
                    transition.preActions.add(action);
                }
                else {
                    transition.postActions.add(action);
                }
            }
            else if (tryToken('S')) {
                var symbol = read_symbol();

                if (transition.symbol != null) {
                    throw new RuntimeException("symbol already defined");
                }

                transition.symbol = symbol;
            }
            else {
                throw new UnexpectedCharException(getTape());
            }

        }

        return transition;
    }

    default ActionData read_action() {
        // TODO
        throw new UnsupportedOperationException();
    }

    default SymbolData read_symbol() {
        SymbolData symbol;
        var tape = getTape();
        if (tape.peek() == '*') {
            tape.move();
            symbol = new SymbolWildData();
        }
        else {
            var arguments = read_arguments();

            if (arguments.size() == 1) {
                symbol = new SymbolCharData();
                ((SymbolCharData)symbol).value = arguments.get(0).charAt(0);
            }
            else if (arguments.size() == 2) {
                symbol = new SymbolRangeData();
                ((SymbolRangeData)symbol).begin = arguments.get(0).charAt(0);
                ((SymbolRangeData)symbol).end = arguments.get(1).charAt(0);
            }
            else {
                throw new RuntimeException("unexpected number of arguments: " + tape.getLocation());
            }
        }

        return symbol;
    }

    default List<String> read_arguments() {
        var arguments = new ArrayList<String>();

        while (true) {
            var argument = tryString();

            if (argument == null) {
                break;
            }

            arguments.add(argument);

            if (!tryToken(',')) {
                break;
            }
        }

        return arguments;
    }

}
