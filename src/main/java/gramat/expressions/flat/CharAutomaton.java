package gramat.expressions.flat;

import gramat.automata.ndfa.NLanguage;
import gramat.automata.dfa.DState;
import gramat.automata.raw.*;
import gramat.automata.raw.units.RawCharAutomaton;
import gramat.automata.raw.units.RawLiteralAutomaton;
import gramat.automata.raw.units.RawRangeAutomaton;
import gramat.compiling.Compiler;
import gramat.eval.Evaluator;
import gramat.expressions.Expression;
import gramat.output.GrammarWriter;
import gramat.runtime.*;
import gramat.util.parsing.Location;

import java.util.List;

public class CharAutomaton extends Expression {

    private final RawAutomaton automaton;

    private DState _root;

    public CharAutomaton(Location location, String literal) {
        this(location, new RawLiteralAutomaton(literal));
    }

    public CharAutomaton(Location location, int literal) {
        this(location, new RawCharAutomaton(literal));
    }

    public CharAutomaton(Location location, int begin, int end) {
        this(location, new RawRangeAutomaton(begin, end));
    }

    public CharAutomaton(Location location, RawAutomaton automaton) {
        super(location);
        this.automaton = automaton;
    }

    private DState getRoot() {
        if (_root == null) {
            var collapsed = automaton.collapse();
            var lang = new NLanguage();
            var machine = lang.machine(collapsed);

            _root = machine.compile();
        }
        return _root;
    }

    @Override
    public boolean eval(EvalContext context) {
        var evaluator = new Evaluator(context);
        var root = getRoot();

        return evaluator.eval(root);
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
