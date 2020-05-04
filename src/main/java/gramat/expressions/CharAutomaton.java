package gramat.expressions;

import gramat.automata.raw.RawAutomatable;
import gramat.automata.State;
import gramat.automata.raw.RawAutomaton;
import gramat.compiling.Compiler;
import gramat.output.GrammarWriter;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.List;

public class CharAutomaton extends Expression implements RawAutomatable {

    private final RawAutomaton automaton;

    private State root;

    public CharAutomaton(Location location, RawAutomaton automaton) {
        super(location);
        this.automaton = automaton;
    }

    @Override
    public boolean eval(EvalContext context) {
        var pos0 = context.source.getPosition();
        var state = root;

        while(true) {
            if (state.isFinal()) {
                if (state.isAccepted()) {
                    return true;
                }
                else {
                    context.source.setPosition(pos0);
                    return false;
                }
            }
            else {
                var value = context.source.peek();
                var newState = value != null ? state.move(value) : null;

                if (newState == null) {
                    if (state.isAccepted()) {
                        return true;
                    }
                    else {
                        context.source.setPosition(pos0);
                        return false;
                    }
                }

                context.source.moveNext();

                state = newState;
            }
        }
    }

    @Override
    public RawAutomaton makeAutomaton() {
        return automaton;
    }

    @Override
    public Expression _custom_optimize(Compiler context) {
        root = automaton.collapse().compile();

        return this;
    }

    @Override
    public List<Expression> getInnerExpressions() {
        return listOf();
    }

    @Override
    public String getDescription() {
        return "Char-Automaton";
    }

    @Override
    public void write(GrammarWriter writer) {
        if (writer.open(this, "char-automaton")) {
            root.write(writer);
            writer.close();
        }
    }

}
