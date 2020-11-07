package gramat.source;

import gramat.framework.Context;
import gramat.input.Tape;
import gramat.models.expressions.ModelExpression;
import gramat.models.source.ModelSource;
import gramat.models.test.ModelEvalFail;
import gramat.models.test.ModelEvalPass;
import gramat.models.test.ModelTest;
import gramat.parsers.ParserSource;
import gramat.parsers.ValueParser;
import gramat.util.Args;
import gramat.util.NameMap;
import gramat.util.Resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SourceParser implements AmParser {

    private final Context ctx;
    private final Tape tape;
    private final ParserSource parsers;

    public SourceParser(Context ctx, Tape tape, ParserSource parsers) {
        this.ctx = ctx;
        this.tape = tape;
        this.parsers = parsers;
    }

    public ModelSource parseSource() {
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

    public String loadValue(String valueDirective, List<Object> arguments) {
        if (Objects.equals(valueDirective, "readFile")) {
            var args = Args.of(arguments, List.of("path"));
            var path = args.getString("path");

            return Resources.loadText(path);
        }
        else {
            throw new RuntimeException("unsupported value directive: " + valueDirective);
        }
    }

    @Override
    public ValueParser findParser(String name) {
        return parsers.findParser(name);
    }

    @Override
    public Tape getTape() {
        return tape;
    }

}
