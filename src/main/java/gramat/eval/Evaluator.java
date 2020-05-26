package gramat.eval;

import gramat.automata.dfa.DState;
import gramat.runtime.*;
import gramat.util.parsing.Source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Evaluator {

    public final Source source;

    private final Stack<Assembler> assemblerStack;

    public Evaluator(EvalContext context) {
        this.source = context.source;
        this.assemblerStack = new Stack<>();
    }

    public void pushAssembler() {
        assemblerStack.add(new Assembler());
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
                    newState = state.move(value, actions);
                    for (var action : actions) {
                        action.run(this);
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

}
