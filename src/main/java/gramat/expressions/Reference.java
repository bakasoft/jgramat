package gramat.expressions;

import gramat.Grammar;
import gramat.engine.nodet.*;

import java.util.List;

public class Reference extends Expression {

    private final Grammar grammar;
    private final String name;

    public Reference(Grammar grammar, String name) {
        this.grammar = grammar;
        this.name = name;
    }

    public Rule getRule() {
        return grammar.findRule(name);
    }

    @Override
    public NState build(NCompiler compiler, NState initial) {
        var rule = getRule();

        return rule.build(compiler, initial);
    }

    @Override
    public List<Expression> getChildren() {
        var rule = getRule();

        return List.of(rule);
    }

}
