package gramat.readers;

import gramat.models.automata.*;
import gramat.exceptions.UnexpectedCharException;

import java.util.ArrayList;
import java.util.List;

public interface MachineReader extends BaseReader, ValueReader {

    default ModelMachine parseMachine() {
        var machine = new ModelMachine();
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

    default ModelTransition tryTransition(ModelMachine machine) {
        if (!tryToken('T')) {
            return null;
        }

        var sourceId = readString();

        expectToken(',');

        var targetId = readString();

        var transition = new ModelTransition();
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

    default ModelAction read_action() {
        // TODO
        throw new UnsupportedOperationException();
    }

    default ModelSymbol read_symbol() {
        ModelSymbol symbol;
        var tape = getTape();
        if (tape.peek() == '*') {
            tape.move();
            symbol = new ModelSymbolWild();
        }
        else {
            var arguments = read_arguments();

            if (arguments.size() == 1) {
                symbol = new ModelSymbolChar();
                ((ModelSymbolChar)symbol).value = arguments.get(0).charAt(0);
            }
            else if (arguments.size() == 2) {
                symbol = new ModelSymbolRange();
                ((ModelSymbolRange)symbol).begin = arguments.get(0).charAt(0);
                ((ModelSymbolRange)symbol).end = arguments.get(1).charAt(0);
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
