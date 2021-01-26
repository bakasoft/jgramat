package gramat.pipeline.common;


import gramat.scheme.State;
import gramat.scheme.data.automata.*;
import org.beat.Beat;
import gramat.pipeline.encoding.MachineEncoder;
import gramat.pipeline.decoding.MachineDecoder;
import gramat.scheme.common.parsers.ParserSource;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

public interface Format {

    static void write(State initial, Appendable output) throws IOException {
        var factory = new MachineEncoder();
        var beat = buildSchema();
        var machine = factory.createMachine(initial);

        beat.writeText(machine, output, true);
    }

    static Beat buildSchema() {
        return Beat.builder()
                .withObject(MachineData.class)
                .withObject(StateData.class)
                .withObject(TransactionData.class)
                .withObject(ActionRecursionData.class)
                .withObject(ActionTransactionData.class)
                .withObject(BadgeData.class)
                .withObject(SymbolCharData.class)
                .withObject(SymbolRangeData.class)
                .withObject(SymbolWildData.class)
                .withObject(TransitionData.class)
                .build();
    }

    static State read(Path path) throws IOException {
        try (var input = Files.newBufferedReader(path)) {
            return read(input);
        }
    }

    static State read(Reader input) {
        var beat = buildSchema();
        var machine = beat.readText(input, MachineData.class);
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
