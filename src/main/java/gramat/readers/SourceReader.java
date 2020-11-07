package gramat.readers;

import gramat.models.expressions.ModelExpression;
import gramat.models.source.ModelSource;
import gramat.models.test.ModelEvalFail;
import gramat.models.test.ModelEvalPass;
import gramat.models.test.ModelTest;
import gramat.util.Args;
import gramat.util.NameMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public interface SourceReader extends FileReader {

    default ModelSource parseSource() {
        var model = new ModelSource();
        model.rules = new NameMap<>();
        model.tests = new ArrayList<>();

        var file = parseFile();

        if (file.rules != null) {
            for (var rule : file.rules) {
                model.rules.set(rule.keyword, rule.expression);
            }
        }

        if (file.calls != null) {
            for (var call : file.calls) {
                if (Objects.equals(call.keyword, "pass")) {
                    model.tests.add(makePass(call.arguments, call.expression));
                }
                else if (Objects.equals(call.keyword, "fail")) {
                    model.tests.add(makeFail(call.arguments, call.expression));
                }
                else if (Objects.equals(call.keyword, "main")) {
                    model.main = call.expression;
                }
                else {
                    throw new RuntimeException("unknown call: " + call.keyword);
                }
            }
        }

        return model;
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
