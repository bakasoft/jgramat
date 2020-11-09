package gramat.scheme.machine.binary;


import gramat.scheme.machine.State;
import gramat.scheme.models.automata.*;
import stone.StoneSchema;
import gramat.pipeline.encoding.MachineEncoder;
import gramat.pipeline.decoding.MachineDecoder;
import gramat.parsers.ParserSource;
import stone.Stone;
import stone.io.StoneCharInput;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public interface Format {

    static void write(State initial, Appendable output) throws IOException {
        var factory = new MachineEncoder();
        var schema = buildSchema();
        var machine = factory.createMachine(initial);
        var encoder = schema.createTextEncoder();

        encoder.write(machine, Stone.prettyOutput(output));
    }

    static StoneSchema buildSchema() {
        var schema = new StoneSchema();
        schema.addType(ModelMachine.class);
        schema.addType(ModelState.class);
        schema.addType(ModelTransaction.class);
        schema.addType(ModelActionRecursion.class);
        schema.addType(ModelActionTransaction.class);
        schema.addType(ModelBadge.class);
        schema.addType(ModelSymbolChar.class);
        schema.addType(ModelSymbolRange.class);
        schema.addType(ModelSymbolWild.class);
        schema.addType(ModelTransition.class);
        return schema;
    }

    static State read(Path path) throws IOException {
        try (var input = Files.newBufferedReader(path)) {
            return read(input);
        }
    }

    static State read(Readable input) throws IOException {
        return read(Stone.charInput(input));
    }

    static State read(StoneCharInput input) throws IOException{
        var schema = buildSchema();
        var decoder = schema.createTextDecoder();
        var machine = (ModelMachine)decoder.read(input);
        var parsers = new ParserSource();

        validate(machine);

        var builder = new MachineDecoder(parsers);

        return builder.build(machine);
    }

    private static void validate(ModelMachine machine) {
        if (machine.initial == null) {
            throw new RuntimeException();
        }

        if (machine.states == null) {
            throw new RuntimeException();
        }

        if (machine.transitions == null) {
            throw new RuntimeException();
        }

        if (!machine.states.contains(machine.initial)) {
            throw new RuntimeException();
        }

        for (var transition : machine.transitions) {
            if (!machine.states.contains(transition.source)) {
                throw new RuntimeException();
            }
            if (!machine.states.contains(transition.target)) {
                throw new RuntimeException();
            }
        }
    }

}
