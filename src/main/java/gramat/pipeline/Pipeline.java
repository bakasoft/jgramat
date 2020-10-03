package gramat.pipeline;

import gramat.am.ExpressionFactory;
import gramat.am.expression.AmExpression;
import gramat.formatting.NodeFormatter;
import gramat.formatting.StateFormatter;
import gramat.framework.Component;
import gramat.graph.Line;
import gramat.machine.State;
import gramat.util.NameMap;

public class Pipeline {

    public static State compile(Component component, String name, NameMap<AmExpression> grammar) {
        var rule = new Step1Input(component, ExpressionFactory.reference(name), grammar);

        return compile(rule);
    }

    public static State compile(Component component, AmExpression expression, NameMap<AmExpression> grammar) {
        var rule = new Step1Input(component, expression, grammar);

        return compile(rule);
    }

    public static State compile(Step1Input rule) {
        var step1 = compileStep1(rule);
        var step2 = compileStep2(step1);
        var step3 = compileStep3(step2);
        return compileStep4(rule.parent, step3);
    }

    public static Step2Input compileStep1(Step1Input input) {
        var compiler = new Step1Compiler(input.parent);

        return compiler.compile(input);
    }

    public static Step3Input compileStep2(Step2Input input) {
        var compiler = new Step2Compiler(input.parent, input.dependencies);
        var lines = compiler.compile(input.main);

        for (var entry : lines.dependencies.entrySet()) {
            System.out.println("========== LINE " + entry.getKey());

            new NodeFormatter(System.out).write(entry.getValue());
        }

        return lines;
    }

    public static Line compileStep3(Step3Input graph) {
        var resolver = new Step3Compiler(graph.parent, graph.dependencies);
        var line = resolver.resolve(graph.main);

        System.out.println("========== RESOLVED");
        new NodeFormatter(System.out).write(line);

        return line;
    }

    public static State compileStep4(Component parent, Line line) {
        var stateCompiler = new Step4Compiler(parent, line.graph);
        var state = stateCompiler.compile(line.graph.segment(line.source, line.target));

        System.out.println("========== STATE");
        new StateFormatter(System.out).write(state);

        return state;
    }

}
