package gramat.pipeline;

import gramat.formatting.NodeFormatter;
import gramat.formatting.StateFormatter;
import gramat.framework.Component;
import gramat.graph.Graph;
import gramat.input.Tape;
import gramat.machine.State;
import gramat.pipeline.blueprint.ExpressionBuilder;

public class Pipeline {

    public static Source toSource(Component parent, Tape tape) {
        return SourceParser.parse(parent, tape);
    }

    public static Template toTemplate(Component parent, Sentence sentence) {
        var graph = new Graph();
        var template = ExpressionBuilder.build(parent, graph, sentence.expression, sentence.dependencies);

        System.out.println("########## TEMPLATE");
        new NodeFormatter(System.out).write(graph, template.main);
        System.out.println("##########");

        for (var entry : template.extensions.entrySet()) {
            System.out.println("########## EXTENSION " + entry.getKey());
            new NodeFormatter(System.out).write(graph, entry.getValue());
            System.out.println("##########");
        }

        return template;
    }

    public static Machine toMachine(Component parent, Template template) {
        var machine = MachineCompiler.compile(parent, template);

        machine.validate();

        System.out.println("########## MACHINE");
        new NodeFormatter(System.out).write(machine.graph, machine.root);
        System.out.println("##########");

        return machine;
    }

    public static State toState(Component parent, Sentence sentence) {
        var template = toTemplate(parent, sentence);
        var machine = toMachine(parent, template);
        return toState(parent, machine);
    }

    public static State toState(Component parent, Machine machine) {
        var state = StateCompiler.compile(parent, machine);

        System.out.println("########## STATE");
        new StateFormatter(System.out).write(state);
        System.out.println("##########");

        return state;
    }

}
