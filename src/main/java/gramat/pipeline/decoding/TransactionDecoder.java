package gramat.pipeline.decoding;

import gramat.scheme.common.actions.transactions.*;
import gramat.eval.transactions.Transaction;
import gramat.exceptions.UnsupportedValueException;
import gramat.scheme.data.automata.TransactionData;
import gramat.scheme.common.parsers.ParserSource;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class TransactionDecoder {

    private final ParserSource parsers;
    private final Map<TransactionData, Transaction> repository;

    public TransactionDecoder(ParserSource parsers) {
        this.parsers = parsers;
        this.repository = new LinkedHashMap<>();
    }

    public Transaction build(TransactionData data) {
        return repository.computeIfAbsent(data, k -> {
            if (Objects.equals(data.type, ArrayTransaction.NAME)) {
                return new ArrayTransaction(data.id, data.typeHint);
            }
            else if (Objects.equals(data.type, AttributeTransaction.NAME)) {
                return new AttributeTransaction(data.id, data.defaultName);
            }
            else if (Objects.equals(data.type, NameTransaction.NAME)) {
                return new NameTransaction(data.id);
            }
            else if (Objects.equals(data.type, ObjectTransaction.NAME)) {
                return new ObjectTransaction(data.id, data.typeHint);
            }
            else if (Objects.equals(data.type, ValueTransaction.NAME)) {
                var parser = parsers.findParser(data.parserName);
                return new ValueTransaction(data.id, parser);
            }
            else {
                throw new UnsupportedValueException(data.type, "transaction name");
            }
        });
    }

}
