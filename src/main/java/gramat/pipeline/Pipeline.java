package gramat.pipeline;

import gramat.scheme.common.badges.BadgeSource;
import gramat.pipeline.assembling.ExpressionAssembler;
import gramat.framework.Context;
import gramat.scheme.models.Graph;
import gramat.input.Tape;
import gramat.scheme.State;
import gramat.pipeline.compiling.*;
import gramat.scheme.data.parsing.GrammarData;
import gramat.scheme.common.parsers.ParserSource;
import gramat.pipeline.parsing.Parser;
import gramat.scheme.common.symbols.Alphabet;
import gramat.scheme.models.Machine;
import gramat.scheme.models.Template;
import gramat.util.NameMap;

public class Pipeline {

    public static GrammarData toGrammarData(Context ctx, Tape tape, ParserSource parsers) {
        var parser = new Parser(ctx, tape, parsers);

        return parser.parseGrammar();
    }

    public static Template toTemplate(Context ctx, GrammarData grammar, Alphabet alphabet, BadgeSource badges, ParserSource parsers) {
        var graph = new Graph();
        var template = ExpressionAssembler.build(ctx, graph, grammar.main, grammar.rules, alphabet, badges, parsers);

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

    public static GrammarData toSentence(Context ctx, Tape tape, ParserSource parsers) {
        var reader = new Parser(ctx, tape, parsers);
        var grammar = new GrammarData();
        grammar.main = reader.readExpression();
        return grammar;
    }

    public static State toState(Context ctx, Tape tape, Alphabet alphabet, BadgeSource badges, ParserSource parsers) {
        var sentence = toSentence(ctx, tape, parsers);
        return toState(ctx, sentence, alphabet, badges, parsers);
    }

    public static State toState(Context ctx, GrammarData grammar, Alphabet alphabet, BadgeSource badges, ParserSource parsers) {
        var template = toTemplate(ctx, grammar, alphabet, badges, parsers);
        var machine = toMachine(ctx, template, badges);
        return toState(ctx, machine, alphabet, badges);
    }

    public static State toState(Context ctx, Machine machine, Alphabet alphabet, BadgeSource badges) {
        return StateCompiler.compile(ctx, machine, alphabet, badges);
    }

}
