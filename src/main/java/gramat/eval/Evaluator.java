package gramat.eval;

import gramat.epsilon.Action;
import gramat.epsilon.ActionRuntime;
import gramat.runtime.*;
import gramat.util.Debugger;
import gramat.util.GramatWriter;
import gramat.util.parsing.Source;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Evaluator implements ActionRuntime {

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

    @Override
    public void run(Action action) {
        if (action instanceof TRXAction) {
            var a = (TRXAction) action;

            a.run(this);
        }
    }
}
