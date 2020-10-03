package gramat.pipeline;

import gramat.Gramat;
import gramat.am.expression.AmExpression;
import gramat.am.test.AmTest;
import gramat.framework.DefaultComponent;
import gramat.input.Tape;
import gramat.parsing.AmParser;
import gramat.util.Args;
import gramat.util.NameMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Step0Compiler extends DefaultComponent {

    public final NameMap<AmExpression> rules;

    public final List<AmTest> tests;

    public Step0Compiler(Gramat gramat) {
        super(gramat);
        rules = new NameMap<>();
        tests = new ArrayList<>();
    }

    public NameMap<AmExpression> compile(Tape tape) {
        var grammar = new NameMap<AmExpression>();
        var parser = new AmParser(gramat);
        var file = parser.parseFile(tape);

        if (file.rules != null) {
            for (var rule : file.rules) {
                grammar.set(rule.keyword, rule.expression);
            }
        }

        if (file.calls != null) {
            for (var call : file.calls) {
                var args = new Args(call.arguments);
                if (Objects.equals(call.keyword, "pass")) {
                    process_pass(grammar, args, call.expression);
                }
                else if (Objects.equals(call.keyword, "fail")) {
                    process_fail(grammar, args, call.expression);
                }
                else {
                    throw new RuntimeException("unknown call: " + call.keyword);
                }
            }
        }

        return grammar;
    }

    private void process_pass(NameMap<AmExpression> grammar, Args args, AmExpression expression) {
        var input = args.pullAs(String.class);
        var state = Pipeline.compile(gramat, expression, grammar);

        if (state.evalMatch(input, logger)) {
            logger.debug("pass");
        }
        else {
            logger.debug("fail");
        }
    }

    private void process_fail(NameMap<AmExpression> grammar, Args args, AmExpression expression) {
        throw new RuntimeException();
    }


}
