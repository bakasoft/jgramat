package gramat.expressions.flat;

import gramat.automata.ndfa.DState;
import gramat.automata.ndfa.Language;
import gramat.automata.raw.*;
import gramat.compiling.Compiler;
import gramat.expressions.Expression;
import gramat.output.GrammarWriter;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.List;

public class CharAutomaton extends Expression {

    private final RawAutomaton automaton;

    private DState _root;

    public CharAutomaton(Location location, String literal) {
        this(location, new RawLiteralAutomaton(literal));
    }

    public CharAutomaton(Location location, char literal) {
        this(location, new RawCharAutomaton(literal));
    }

    public CharAutomaton(Location location, char begin, char end) {
        this(location, new RawRangeAutomaton(begin, end));
    }

    public CharAutomaton(Location location, RawAutomaton automaton) {
        super(location);
        this.automaton = automaton;
    }

    private DState getRoot() {
        if (_root == null) {
            var collapsed = automaton.collapse();
            var lang = new Language();
            var am = collapsed.build(lang);

            lang.applyPostBuild();

            _root = am.compile();
        }
        return _root;
    }

    @Override
    public boolean eval(EvalContext context) {
        var pos0 = context.source.getPosition();
        var state = getRoot();

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

    public RawAutomaton getAutomaton() {
        return automaton;
    }

    @Override
    public Expression _custom_optimize(Compiler context) {
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
            getRoot().write(writer);
            writer.close();
        }
    }

}
