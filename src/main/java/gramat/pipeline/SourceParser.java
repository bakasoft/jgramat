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

        return new Source(parser.rules, parser.tests);
    }

    private final NameMap<ModelExpression> rules;
    private final List<ModelTest> tests;
    private final Component parent;
    private final Tape tape;

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
                var args = Args.of(call.arguments);
                if (Objects.equals(call.keyword, "pass")) {
                    tests.add(makePass(args, call.expression));
                }
                else if (Objects.equals(call.keyword, "fail")) {
                    tests.add(makeFail(args, call.expression));
                }
                else {
                    throw new RuntimeException("unknown call: " + call.keyword);
                }
            }
        }
    }

    private ModelTest makePass(Args args, ModelExpression expression) {
        var test = new ModelEvalPass();
        test.input = args.pullAs(String.class);
        test.expression = expression;
        return test;
    }

    private ModelTest makeFail(Args args, ModelExpression expression) {
        var test = new ModelEvalFail();
        test.input = args.pullAs(String.class);
        test.expression = expression;
        return test;
    }

}
