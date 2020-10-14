package gramat.pipeline;

import gramat.Gramat;
import gramat.formatting.NodeFormatter;
import gramat.formatting.StateFormatter;
import gramat.framework.Component;
import gramat.graph.Graph;
import gramat.input.Tape;
import gramat.machine.State;

public class Pipeline {

    public static Source toSource(Component parent, Tape tape) {
        return SourceParser.parse(parent, tape);
    }

    public static Blueprint toBlueprint(Component parent, Sentence sentence) {
        var blueprint = BlueprintCompiler.compile(parent, sentence);

        System.out.println("########## BLUEPRINT");
        new NodeFormatter(System.out).write(blueprint.graph, blueprint.root);
        System.out.println("##########");

        for (var name : blueprint.dependencies.keySet()) {
            System.out.println("########## BLUEPRINT " + name);
            var root = blueprint.dependencies.find(name);
            new NodeFormatter(System.out).write(blueprint.graph, root);
            System.out.println("##########");
        }

        return blueprint;
    }

    public static Template toTemplate(Component parent, Blueprint blueprint) {
        var graph = new Graph();
        var template = TemplateCompiler.compile(parent, blueprint, graph);

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
        var blueprint = toBlueprint(parent, sentence);
        var template = toTemplate(parent, blueprint);
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
