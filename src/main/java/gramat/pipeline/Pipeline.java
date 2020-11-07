package gramat.pipeline;

import gramat.badges.BadgeSource;
import gramat.framework.Context;
import gramat.graph.Graph;
import gramat.input.Tape;
import gramat.machine.State;
import gramat.models.source.ModelSource;
import gramat.parsers.ParserSource;
import gramat.parsing.AmParser;
import gramat.parsing.Parser;
import gramat.pipeline.blueprint.ExpressionBuilder;
import gramat.symbols.Alphabet;
import gramat.util.NameMap;

public class Pipeline {

    public static ModelSource toSource(Context ctx, Tape tape, ParserSource parsers) {
        return SourceParser.parse(ctx, tape, parsers);
    }

    public static Template toTemplate(Context ctx, Sentence sentence, Alphabet alphabet, BadgeSource badges, ParserSource parsers) {
        var graph = new Graph();
        var template = ExpressionBuilder.build(ctx, graph, sentence.expression, sentence.dependencies, alphabet, badges, parsers);

//        System.out.println("########## TEMPLATE");
//        new NodeFormatter(System.out).write(graph, template.main);
//        System.out.println("##########");

        for (var entry : template.extensions.entrySet()) {
//            System.out.println("########## EXTENSION " + entry.getKey());
//            new NodeFormatter(System.out).write(graph, entry.getValue());
//            System.out.println("##########");
        }

        return template;
    }

    public static Machine toMachine(Context ctx, Template template, BadgeSource badges) {
        try (var ignore = ctx.pushLayer("Assembling State Machine")) {
            var machine = MachineCompiler.compile(ctx, template, badges);

            machine.validate();

//        System.out.println("########## MACHINE");
//        new NodeFormatter(System.out).write(machine.graph, machine.root);
//        System.out.println("##########");

            return machine;
        }
    }

    public static Sentence toSentence(Context ctx, Tape tape, ParserSource parsers) {
        var parser = new AmParser(ctx);
        var p = new Parser(tape, parsers);
        var expression = parser.readExpression(p);
        return new Sentence(expression, new NameMap<>());
    }

    public static State toState(Context ctx, Tape tape, Alphabet alphabet, BadgeSource badges, ParserSource parsers) {
        var sentence = toSentence(ctx, tape, parsers);
        return toState(ctx, sentence, alphabet, badges, parsers);
    }

    public static State toState(Context ctx, Sentence sentence, Alphabet alphabet, BadgeSource badges, ParserSource parsers) {
        var template = toTemplate(ctx, sentence, alphabet, badges, parsers);
        var machine = toMachine(ctx, template, badges);
        return toState(ctx, machine, alphabet, badges);
    }

    public static State toState(Context ctx, Machine machine, Alphabet alphabet, BadgeSource badges) {
        return StateCompiler.compile(ctx, machine, alphabet, badges);
    }

}
