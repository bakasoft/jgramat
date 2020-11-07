package gramat.pipeline;

import gramat.framework.Component;
import gramat.input.Tape;
import gramat.models.expressions.ModelExpression;
import gramat.models.test.ModelEvalFail;
import gramat.models.test.ModelEvalPass;
import gramat.models.test.ModelTest;
import gramat.parsing.AmParser;
import gramat.util.Args;
import gramat.util.NameMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SourceParser {

    public static Source parse(Component parent, Tape tape) {
        var parser = new SourceParser(parent, tape);

        return new Source(parser.rules, parser.tests, parser.main);
    }

    private final NameMap<ModelExpression> rules;
    private final List<ModelTest> tests;
    private final Component parent;
    private final Tape tape;
    private ModelExpression main;

    private SourceParser(Component parent, Tape tape) {
        this.parent = parent;
        this.tape = tape;
        this.rules = new NameMap<>();
        this.tests = new ArrayList<>();

        parse();
    }

    private void parse() {
        var parser = new AmParser(parent);
        var file = parser.parseFile(tape);

        if (file.rules != null) {
            for (var rule : file.rules) {
                rules.set(rule.keyword, rule.expression);
            }
        }

        if (file.calls != null) {
            for (var call : file.calls) {
                if (Objects.equals(call.keyword, "pass")) {
                    tests.add(makePass(call.arguments, call.expression));
                }
                else if (Objects.equals(call.keyword, "fail")) {
                    tests.add(makeFail(call.arguments, call.expression));
                }
                else if (Objects.equals(call.keyword, "main")) {
                    main = call.expression;
                }
                else {
                    throw new RuntimeException("unknown call: " + call.keyword);
                }
            }
        }
    }

    private ModelTest makePass(List<Object> arguments, ModelExpression expression) {
        var test = new ModelEvalPass();
        var args = Args.of(arguments, List.of("input"));
        test.input = args.getString("input");
        test.expression = expression;
        return test;
    }

    private ModelTest makeFail(List<Object> arguments, ModelExpression expression) {
        var test = new ModelEvalFail();
        var args = Args.of(arguments, List.of("input"));
        test.input = args.getString("input");
        test.expression = expression;
        return test;
    }

}
