package gramat.parsing;

import gramat.models.automata.*;
import gramat.input.Tape;
import gramat.exceptions.UnexpectedCharException;

import java.util.ArrayList;
import java.util.List;

public interface AmMachineParser extends AmBase, AmValue {

    default ModelMachine parseMachine(Tape tape) {
        var machine = new ModelMachine();

        skipVoid(tape);

        while (tape.alive()) {
            var transition = tryTransition(machine, tape);

            if (transition != null) {
                machine.transitions.add(transition);
                continue;
            }

            if (tryToken(tape, 'I')) {
                var stateId = readString(tape);
                var state = machine.mergeState(stateId);

                machine.initial = state;

                expectToken(tape, ';');

                continue;
            }

            if (tryToken(tape, 'A')) {
                var stateId = readString(tape);
                var state = machine.mergeState(stateId);

                state.accepted = true;

                expectToken(tape, ';');
                continue;
            }

            throw new UnexpectedCharException(tape);
        }

        return machine;
    }

    default ModelTransition tryTransition(ModelMachine machine, Tape tape) {
        if (!tryToken(tape, 'T')) {
            return null;
        }

        var sourceId = readString(tape);

        expectToken(tape, ',');

        var targetId = readString(tape);

        var transition = new ModelTransition();
        transition.source = machine.mergeState(sourceId);
        transition.target = machine.mergeState(targetId);

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

    default ModelAction read_action(Tape tape) {
        // TODO
        throw new UnsupportedOperationException();
    }

    default ModelSymbol read_symbol(Tape tape) {
        ModelSymbol symbol;

        if (tape.peek() == '*') {
            tape.move();
            symbol = new ModelSymbolWild();
        }
        else {
            var arguments = read_arguments(tape);

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

    default List<String> read_arguments(Tape tape) {
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

}
