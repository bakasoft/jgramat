package gramat.compilers;

import gramat.Gramat;
import gramat.eval.Evaluator;
import gramat.models.expressions.ModelExpression;
import gramat.models.test.ModelTest;
import gramat.framework.DefaultComponent;
import gramat.input.Tape;
import gramat.parsing.AmParser;
import gramat.util.Args;
import gramat.util.NameMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExpressionCompiler extends DefaultComponent {

    public final NameMap<ModelExpression> rules;

    public final List<ModelTest> tests;

    public ExpressionCompiler(Gramat gramat) {
        super(gramat);
        rules = new NameMap<>();
        tests = new ArrayList<>();
    }

    public NameMap<ModelExpression> compile(Tape tape) {
        var grammar = new NameMap<ModelExpression>();
        var parser = new AmParser(gramat);
        var file = parser.parseFile(tape);  // TODO don't parse here, this is only compiling

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

    private void process_pass(NameMap<ModelExpression> grammar, Args args, ModelExpression expression) {
        var input = args.pullAs(String.class);
        var tape = new Tape(input);
        var state = Pipeline.compile(gramat, expression, grammar);
        var evaluator = new Evaluator(gramat, tape, logger);
        var result = evaluator.evalValue(state);

        logger.debug("result: %s", result);
    }

    private void process_fail(NameMap<ModelExpression> grammar, Args args, ModelExpression expression) {
        throw new RuntimeException();
    }


}
