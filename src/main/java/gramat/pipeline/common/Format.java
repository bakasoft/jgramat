package gramat.pipeline.common;


import gramat.scheme.State;
import gramat.scheme.data.automata.*;
import stone.StoneSchema;
import gramat.pipeline.encoding.MachineEncoder;
import gramat.pipeline.decoding.MachineDecoder;
import gramat.scheme.common.parsers.ParserSource;
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
        schema.addType(MachineData.class);
        schema.addType(StateData.class);
        schema.addType(TransactionData.class);
        schema.addType(ActionRecursionData.class);
        schema.addType(ActionTransactionData.class);
        schema.addType(BadgeData.class);
        schema.addType(SymbolCharData.class);
        schema.addType(SymbolRangeData.class);
        schema.addType(SymbolWildData.class);
        schema.addType(TransitionData.class);
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
        var machine = (MachineData)decoder.read(input);
        var parsers = new ParserSource();

        validate(machine);

        var builder = new MachineDecoder(parsers);

        return builder.build(machine);
    }

    private static void validate(MachineData machine) {
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
