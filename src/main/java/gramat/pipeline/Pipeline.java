package gramat.pipeline;

import gramat.formatting.NodeFormatter;
import gramat.formatting.StateFormatter;
import gramat.framework.Component;
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

        for (var name : blueprint.dependencies.keySet()) {
            System.out.println("########## BLUEPRINT " + name);
            var root = blueprint.dependencies.find(name);
            new NodeFormatter(System.out).write(blueprint.graph, root);
        }

        return blueprint;
    }

    public static Machine toMachine(Component parent, Blueprint blueprint) {
        var machine = MachineCompiler.compile(parent, blueprint);

        System.out.println("########## MACHINE");
        new NodeFormatter(System.out).write(machine.graph, machine.root);

        return machine;
    }

    public static State toState(Component parent, Sentence sentence) {
        var blueprint = toBlueprint(parent, sentence);
        var step3 = toMachine(parent, blueprint);
        return toState(parent, step3);
    }

    public static State toState(Component parent, Machine machine) {
        var state = StateCompiler.compile(parent, machine);

        System.out.println("########## STATE");
        new StateFormatter(System.out).write(state);
        System.out.println("##########");

        return state;
    }

}
