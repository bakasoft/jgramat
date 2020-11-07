package gramat.machine.binary;


import stone.StoneSchema;
import stone.binary.input.BinaryDeserializer;
import stone.binary.input.ObjectCreator;
import stone.binary.input.impl.Creators;
import stone.binary.input.impl.DefaultCreatorRepository;
import stone.binary.output.BinarySerializer;
import stone.binary.output.ObjectDescriber;
import stone.binary.output.impl.DefaultDescriberRepository;
import stone.binary.output.impl.Describers;
import stone.binary.output.impl.HashReferenceStrategy;
import gramat.machine.State;
import gramat.models.automata.*;
import gramat.models.factories.MachineFactory;
import gramat.assemblers.MachineAssembler;
import gramat.parsers.ParserSource;
import stone.Stone;
import stone.io.StoneCharInput;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public interface Format {

    int GMC_VERSION_MAJ = 1;
    int GMC_VERSION_MIN = 0;

    byte[] MAGIC = {'G', 'M', 'C', GMC_VERSION_MAJ, GMC_VERSION_MIN};

    int MCH_T = 0x10;
    int MCH_INI_F = 0x11;
    int MCH_STS_F = 0x12;
    int MCH_TNS_F = 0x13;
    int MCH_TXS_F = 0x14;

    int STE_T = 0x20;
    int STE_UID_F = 0x21;
    int STE_ACC_F = 0x22;

    int TRN_T = 0x30;
    int TRN_SRC_F = 0x31;
    int TRN_TRG_F = 0x32;
    int TRN_SYM_F = 0x33;
    int TRN_BDG_F = 0x34;
    int TRN_PRE_F = 0x35;
    int TRN_POS_F = 0x36;

    int SCH_T = 0x40;
    int SCH_VAL_F = 0x41;

    int SRG_T = 0x50;
    int SRG_BGN_F = 0x51;
    int SRG_END_F = 0x52;

    int ATX_T = 0xA0;
    int ATX_TYP_F = 0xA1;
    int ATX_UID_F = 0xA2;

    int ARE_T = 0xB0;
    int ARE_TYP_F = 0xB1;
    int ARE_BDG_F = 0xB2;

    int BDG_T = 0xC0;
    int BDG_TKN_F = 0xC1;
    int BDG_WLD_F = 0xC2;

    int TXN_T = 0xE0;
    int TXN_TYP_F = 0xE1;
    int TXN_UID_F = 0xE2;
    int TXN_TYH_F = 0xE3;
    int TXN_DFN_F = 0xE4;
    int TXN_PRN_F = 0xE5;

    static void write(State initial, Appendable output) throws IOException {
        var factory = new MachineFactory();
        var schema = buildSchema();
        var machine1 = factory.createMachine(initial);
        var encoder = schema.createTextEncoder();
        encoder.write(machine1, Stone.prettyOutput(output));

        var encoder1 = schema.createTextEncoder();
        var stone1 = new StringBuilder();
        encoder1.write(machine1, Stone.prettyOutput(stone1));

        System.out.println(stone1);

        var decoder = schema.createTextDecoder();
        var machine2 = decoder.read(Stone.charInput(stone1));

        var stone2 = new StringBuilder();
        var encoder2 = schema.createTextEncoder();
        encoder2.write(machine2, Stone.prettyOutput(stone2));
        System.out.println(stone2);

        if (!Objects.equals(stone1.toString(), stone2.toString())) {
            throw new RuntimeException();
        }

//        writeMagicData(output);

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
//        readMagicData(input);

        var schema = buildSchema();
        var decoder = schema.createTextDecoder();
        var machine = (ModelMachine)decoder.read(input);
        var parsers = new ParserSource();

        validate(machine);

        var builder = new MachineAssembler(parsers);

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

    static void readMagicData(InputStream input) throws IOException {
        for (var expected : MAGIC) {
            var actual = input.read();

            if (actual == -1) {
                throw new IOException();
            }

            if (actual != expected) {
                throw new IOException();
            }
        }
    }

    private static void writeMagicData(OutputStream output) throws IOException {
        output.write(MAGIC);
    }

    private static BinarySerializer buildSerializer() {
        var repository = new DefaultDescriberRepository();
        var references = new HashReferenceStrategy(
                // Allow references only of:
                obj -> obj instanceof ModelState || obj instanceof ModelSymbol || obj instanceof ModelBadge);

        repository.addDescriber(buildMachineDescriber());
        repository.addDescriber(buildStateDescriber());
        repository.addDescriber(buildTransactionDescriber());
        repository.addDescriber(buildTransitionDescriber());
        repository.addDescriber(buildSymbolCharDescriber());
        repository.addDescriber(buildSymbolRangeDescriber());
        repository.addDescriber(buildBadgeDescriber());
        repository.addDescriber(buildActionRecursiveDescriber());
        repository.addDescriber(buildActionTransactionDescriber());

        return repository.createSerializer(references);
    }

    static BinaryDeserializer buildDeserializer() {
        var repository = new DefaultCreatorRepository();

        repository.addCreator(buildMachineCreator());
        repository.addCreator(buildStateCreator());
        repository.addCreator(buildTransactionCreator());
        repository.addCreator(buildTransitionCreator());
        repository.addCreator(buildSymbolCharCreator());
        repository.addCreator(buildSymbolRangeCreator());
        repository.addCreator(buildBadgeCreator());
        repository.addCreator(buildActionRecursionCreator());
        repository.addCreator(buildActionTransactionCreator());

        return repository.createDeserializer();
    }

    private static ObjectDescriber buildMachineDescriber() {
        return Describers.builder(ModelMachine.class)
                .withTypeIndex(MCH_T)
                .withGetter(MCH_STS_F, machine -> machine.states)
                .withGetter(MCH_TNS_F, machine -> machine.transitions)
                .withGetter(MCH_INI_F, machine -> machine.initial)
                .build();
    }

    static ObjectCreator buildMachineCreator() {
        return Creators.builder(ModelMachine.class)
                .withTypeIndex(MCH_T)
                .withSetter(MCH_TNS_F, (machine, value) -> machine.transitions = value.asListOf(ModelTransition.class))
                .withSetter(MCH_STS_F, (machine, value) -> machine.states = value.asListOf(ModelState.class))
                .withSetter(MCH_INI_F, (machine, value) -> machine.initial = value.asObject(ModelState.class))
                .build();
    }

    private static ObjectDescriber buildStateDescriber() {
        return Describers.builder(ModelState.class)
                .withTypeIndex(STE_T)
                .withGetter(STE_UID_F, state -> state.id)
                .withGetter(STE_ACC_F, state -> state.accepted)
                .build();
    }

    static ObjectCreator buildStateCreator() {
        return Creators.builder(ModelState.class)
                .withTypeIndex(STE_T)
                .withSetter(STE_UID_F, (state, value) -> state.id = value.asString())
                .withSetter(STE_ACC_F, (state, value) -> state.accepted = value.asBoolean())
                .build();
    }

    private static ObjectDescriber buildTransitionDescriber() {
        return Describers.builder(ModelTransition.class)
                .withTypeIndex(TRN_T)
                .withGetter(TRN_SRC_F, transition -> transition.source)
                .withGetter(TRN_TRG_F, transition -> transition.target)
                .withGetter(TRN_SYM_F, transition -> transition.symbol)
                .withGetter(TRN_BDG_F, transition -> transition.badge)
                .withGetter(TRN_PRE_F, transition -> transition.preActions)
                .withGetter(TRN_POS_F, transition -> transition.postActions)
                .build();
    }

    static ObjectCreator buildTransitionCreator() {
        return Creators.builder(ModelTransition.class)
                .withTypeIndex(TRN_T)
                .withSetter(TRN_SRC_F, (transition, value) -> transition.source = value.asObject(ModelState.class))
                .withSetter(TRN_TRG_F, (transition, value) -> transition.target = value.asObject(ModelState.class))
                .withSetter(TRN_SYM_F, (transition, value) -> transition.symbol = value.asObject(ModelSymbol.class))
                .withSetter(TRN_BDG_F, (transition, value) -> transition.badge = value.asObject(ModelBadge.class))
                .withSetter(TRN_PRE_F, (transition, value) -> transition.preActions = value.asListOf(ModelAction.class))
                .withSetter(TRN_POS_F, (transition, value) -> transition.postActions = value.asListOf(ModelAction.class))
                .build();
    }

    private static ObjectDescriber buildSymbolCharDescriber() {
        return Describers.builder(ModelSymbolChar.class)
                .withTypeIndex(SCH_T)
                .withGetter(SCH_VAL_F, symbol -> (int)symbol.value)
                .build();
    }

    static ObjectCreator buildSymbolCharCreator() {
        return Creators.builder(ModelSymbolChar.class)
                .withTypeIndex(SCH_T)
                .withSetter(SCH_VAL_F, (symbol, value) -> symbol.value = (char)value.asInt())  // TODO create toChar
                .build();
    }

    private static ObjectDescriber buildSymbolRangeDescriber() {
        return Describers.builder(ModelSymbolRange.class)
                .withTypeIndex(SRG_T)
                .withGetter(SRG_BGN_F, symbol -> (int)symbol.begin)
                .withGetter(SRG_END_F, symbol -> (int)symbol.end)
                .build();
    }

    static ObjectCreator buildSymbolRangeCreator() {
        return Creators.builder(ModelSymbolRange.class)
                .withTypeIndex(SRG_T)
                .withSetter(SRG_BGN_F, (symbol, value) -> symbol.begin = (char)value.asInt())
                .withSetter(SRG_END_F, (symbol, value) -> symbol.end = (char)value.asInt())
                .build();
    }

    private static ObjectDescriber buildBadgeDescriber() {
        return Describers.builder(ModelBadge.class)
                .withTypeIndex(BDG_T)
                .withGetter(BDG_TKN_F, badge -> badge.token)
                .withGetter(BDG_WLD_F, badge -> badge.wild)
                .build();
    }

    static ObjectCreator buildBadgeCreator() {
        return Creators.builder(ModelBadge.class)
                .withTypeIndex(BDG_T)
                .withSetter(BDG_TKN_F, (badge, value) -> badge.token = value.asStringOrNull())
                .withSetter(BDG_WLD_F, (badge, value) -> badge.wild = value.asBoolean())
                .build();
    }

    private static ObjectDescriber buildActionRecursiveDescriber() {
        return Describers.builder(ModelActionRecursion.class)
                .withTypeIndex(ARE_T)
                .withGetter(ARE_TYP_F, action -> action.type)
                .withGetter(ARE_BDG_F, action -> action.badge)
                .build();
    }

    private static ObjectDescriber buildActionTransactionDescriber() {
        return Describers.builder(ModelActionTransaction.class)
                .withTypeIndex(ATX_T)
                .withGetter(ATX_TYP_F, action -> action.type)
                .build();
    }

    static ObjectCreator buildActionRecursionCreator() {
        return Creators.builder(ModelActionRecursion.class)
                .withTypeIndex(ARE_T)
                .withSetter(ARE_TYP_F, (action, value) -> action.type = value.asString())
                .withSetter(ARE_BDG_F, (action, value) -> action.badge = value.asObject(ModelBadge.class))
                .build();
    }

    static ObjectCreator buildActionTransactionCreator() {
        return Creators.builder(ModelActionTransaction.class)
                .withTypeIndex(ATX_T)
                .withSetter(ATX_TYP_F, (action, value) -> action.type = value.asString())
                .build();
    }

    private static ObjectDescriber buildTransactionDescriber() {
        return Describers.builder(ModelTransaction.class)
                .withTypeIndex(TXN_T)
                .withGetter(TXN_TYP_F, action -> action.type)
                .withGetter(TXN_UID_F, action -> action.id)
                .withGetter(TXN_TYH_F, action -> action.typeHint)
                .withGetter(TXN_DFN_F, action -> action.defaultName)
                .withGetter(TXN_PRN_F, action -> action.parserName)
                .build();
    }

    static ObjectCreator buildTransactionCreator() {
        return Creators.builder(ModelTransaction.class)
                .withTypeIndex(TXN_T)
                .withSetter(TXN_TYP_F, (trx, value) -> trx.type = value.asString())
                .withSetter(TXN_UID_F, (trx, value) -> trx.id = value.asInt())
                .withSetter(TXN_TYH_F, (trx, value) -> trx.typeHint = value.asStringOrNull())
                .withSetter(TXN_DFN_F, (trx, value) -> trx.defaultName = value.asStringOrNull())
                .withSetter(TXN_PRN_F, (trx, value) -> trx.parserName = value.asStringOrNull())
                .build();
    }


}
