package gramat.eval;

import gramat.automata.dfa.DState;
import gramat.automata.dfa.DTransitionWild;
import gramat.epsilon.Action;
import gramat.runtime.*;
import gramat.util.Debugger;
import gramat.util.GramatWriter;
import gramat.util.parsing.Source;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Evaluator {

    public final Source source;

    private final Stack<Assembler> assemblerStack;

    public final Debugger debugger;

    public Evaluator(EvalContext context) {
        this.source = context.source;
        this.assemblerStack = new Stack<>();
        this.debugger = new Debugger();
        this.debugger.suffix(() -> {
            var ch = source.peek();
            String symbol;

            if (ch == Source.EOF) {
                symbol = "$";
            }
            else {
                symbol = Character.toString(ch);
            }

            return "@" + source.getPosition() + "=" + GramatWriter.toDelimitedString(symbol, '"');
        });

        pushAssembler();
    }

    public void pushAssembler() {
        assemblerStack.add(new Assembler(debugger));
    }

    public Assembler peekAssembler() {
        if (assemblerStack.isEmpty()) {
            throw new RuntimeException();
        }

        return assemblerStack.peek();
    }

    public Assembler popAssembler() {
        if (assemblerStack.isEmpty()) {
            throw new RuntimeException();
        }

        return assemblerStack.pop();
    }

    public DState eval(DState root) {
        var state = root;
        int lastValue = 0;

        System.out.println("---------->>");
        System.out.println(state.getAmCode());
        System.out.println("----------");

        while(true) {
            if (state.isFinal()) {
                return state;
            } else {
                var value = source.peek();

                DState newState;

                if (lastValue == Source.EOF && value == lastValue) {
                    newState = null;
                }
                else {
                    var actions = new ArrayList<Action>();
                    newState = move(state, value, actions);
                    for (var action : actions) {
//                        action.run(this);
                    }
                }

                if (newState == null) {
                    return state;
                }

                // check for infinite loop
                source.moveNext();

                state = newState;
                lastValue = value;
            }
        }
    }

    private DState move(DState state, int symbol, List<Action> actions) {
        if (state.options.size() > 0) {
            DState result = null;

            for (var option : state.options) {
                result = eval(option);

                if (result.isAccepted()) {
                    break;
                }
            }

            if (!result.isAccepted()) {
                return null;
            }
        }

        DState wild = null;

        for (var trn : state.transitions) {
            if (trn instanceof DTransitionWild) {
                wild = trn.target;
            }
            else if (trn.accepts(symbol)) {
                actions.addAll(trn.actions);
                return trn.target;
            }
        }

        return wild;
    }

}
