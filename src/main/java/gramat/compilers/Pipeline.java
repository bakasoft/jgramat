package gramat.compilers;

import gramat.models.ModelFactory;
import gramat.models.expressions.ModelExpression;
import gramat.formatting.NodeFormatter;
import gramat.formatting.StateFormatter;
import gramat.framework.Component;
import gramat.graph.Line;
import gramat.machine.State;
import gramat.util.NameMap;

public class Pipeline {

    public static State compile(Component component, String name, NameMap<ModelExpression> grammar) {
        var rule = new ExpressionInput(component, ModelFactory.reference(name), grammar);

        return compile(rule);
    }

    public static State compile(Component component, ModelExpression expression, NameMap<ModelExpression> grammar) {
        var rule = new ExpressionInput(component, expression, grammar);

        return compile(rule);
    }

    public static State compile(ExpressionInput rule) {
        var step1 = compileStep1(rule);
        var step2 = compileStep2(step1);
        var step3 = compileStep3(step2);
        return compileStep4(rule.parent, step3);
    }

    public static SegmentInput compileStep1(ExpressionInput input) {
        var compiler = new GeneralGraphCompiler(input.parent);
        var result = compiler.compile(input);

        System.out.println("========== SEGMENT");
        new NodeFormatter(System.out).write(result.main);

        for (var name : result.dependencies.keySet()) {
            var segment = result.dependencies.find(name);

            System.out.println("========== SEGMENT " + name);
            new NodeFormatter(System.out).write(segment);
        }


        return result;
    }

    public static LineInput compileStep2(SegmentInput input) {
        var compiler = new LinearGraphCompiler(input.parent, input.dependencies);
        var result = compiler.compile(input.main);

        System.out.println("========== LINE");
        new NodeFormatter(System.out).write(result.main);

        for (var entry : result.dependencies.entrySet()) {
            System.out.println("========== LINE " + entry.getKey());

            new NodeFormatter(System.out).write(entry.getValue());
        }

        return result;
    }

    public static Line compileStep3(LineInput graph) {
        var resolver = new RecursiveGraphCompiler(graph.parent, graph.dependencies);
        var line = resolver.resolve(graph.main);

        System.out.println("========== RESOLVED");
        new NodeFormatter(System.out).write(line);

        return line;
    }

    public static State compileStep4(Component parent, Line line) {
        var stateCompiler = new StateCompiler(parent, line.graph);
        var state = stateCompiler.compile(line.graph.segment(line.source, line.target));

        System.out.println("========== STATE");
        new StateFormatter(System.out).write(state);
        System.out.println("==========");

        return state;
    }

}
