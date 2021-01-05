package gramat.pipeline.parsing;

import gramat.scheme.data.expressions.ExpressionData;
import gramat.scheme.data.parsing.GrammarData;
import gramat.scheme.data.test.EvalFailData;
import gramat.scheme.data.test.EvalPassData;
import gramat.scheme.data.test.TestData;
import gramat.util.Args;
import gramat.util.NameMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public interface GrammarParser extends SourceParser {

    default GrammarData parseGrammar() {
        var grammar = new GrammarData();
        grammar.rules = new NameMap<>();
        grammar.tests = new ArrayList<>();

        var source = parseSource();

        if (source.rules != null) {
            for (var rule : source.rules) {
                grammar.rules.set(rule.keyword, rule.expression);
            }
        }

        if (source.calls != null) {
            for (var call : source.calls) {
                if (Objects.equals(call.keyword, "pass")) {
                    grammar.tests.add(makePass(call.arguments, call.expression));
                }
                else if (Objects.equals(call.keyword, "fail")) {
                    grammar.tests.add(makeFail(call.arguments, call.expression));
                }
                else if (Objects.equals(call.keyword, "main")) {
                    grammar.main = call.expression;
                }
                else {
                    throw new RuntimeException("unknown call: " + call.keyword);
                }
            }
        }

        return grammar;
    }

    private TestData makePass(List<Object> arguments, ExpressionData expression) {
        var test = new EvalPassData();
        var args = Args.of(arguments, List.of("input"));
        test.input = args.getString("input");
        test.expression = expression;
        return test;
    }

    private TestData makeFail(List<Object> arguments, ExpressionData expression) {
        var test = new EvalFailData();
        var args = Args.of(arguments, List.of("input"));
        test.input = args.getString("input");
        test.expression = expression;
        return test;
    }

}
