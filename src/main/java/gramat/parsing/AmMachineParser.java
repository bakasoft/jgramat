package gramat.parsing;

import gramat.models.automata.*;
import gramat.input.Tape;
import gramat.exceptions.UnexpectedCharException;

import java.util.ArrayList;
import java.util.List;

public interface AmMachineParser extends AmBase, AmValue {

    default ModelMachine parseMachine(Parser parser) {
        var machine = new ModelMachine();

        skipVoid(parser);

        while (parser.tape.alive()) {
            var transition = tryTransition(machine, parser);

            if (transition != null) {
                machine.transitions.add(transition);
                continue;
            }

            if (tryToken(parser, 'I')) {
                var stateId = readString(parser);
                var state = machine.mergeState(stateId);

                machine.initial = state;

                expectToken(parser, ';');

                continue;
            }

            if (tryToken(parser, 'A')) {
                var stateId = readString(parser);
                var state = machine.mergeState(stateId);

                state.accepted = true;

                expectToken(parser, ';');
                continue;
            }

            throw new UnexpectedCharException(parser.tape);
        }

        return machine;
    }

    default ModelTransition tryTransition(ModelMachine machine, Parser parser) {
        if (!tryToken(parser, 'T')) {
            return null;
        }

        var sourceId = readString(parser);

        expectToken(parser, ',');

        var targetId = readString(parser);

        var transition = new ModelTransition();
        transition.source = machine.mergeState(sourceId);
        transition.target = machine.mergeState(targetId);

        while (true) {
            if (tryToken(parser, ';')) {
                break;
            }
            else if (tryToken(parser, 'R')) {
                var action = read_action(parser);

                if (transition.symbol == null) {
                    transition.preActions.add(action);
                }
                else {
                    transition.postActions.add(action);
                }
            }
            else if (tryToken(parser, 'S')) {
                var symbol = read_symbol(parser);

                if (transition.symbol != null) {
                    throw new RuntimeException("symbol already defined");
                }

                transition.symbol = symbol;
            }
            else {
                throw new UnexpectedCharException(parser.tape);
            }

        }

        return transition;
    }

    default ModelAction read_action(Parser parser) {
        // TODO
        throw new UnsupportedOperationException();
    }

    default ModelSymbol read_symbol(Parser parser) {
        ModelSymbol symbol;

        if (parser.tape.peek() == '*') {
            parser.tape.move();
            symbol = new ModelSymbolWild();
        }
        else {
            var arguments = read_arguments(parser);

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
                throw new RuntimeException("unexpected number of arguments: " + parser.tape.getLocation());
            }
        }

        return symbol;
    }

    default List<String> read_arguments(Parser parser) {
        var arguments = new ArrayList<String>();

        while (true) {
            var argument = tryString(parser);

            if (argument == null) {
                break;
            }

            arguments.add(argument);

            if (!tryToken(parser, ',')) {
                break;
            }
        }

        return arguments;
    }

}
