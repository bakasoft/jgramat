package gramat.pipeline.decoding;

import gramat.scheme.core.actions.transactions.*;
import gramat.eval.transactions.Transaction;
import gramat.exceptions.UnsupportedValueException;
import gramat.scheme.models.automata.ModelTransaction;
import gramat.parsers.ParserSource;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class TransactionDecoder {

    private final ParserSource parsers;
    private final Map<ModelTransaction, Transaction> repository;

    public TransactionDecoder(ParserSource parsers) {
        this.parsers = parsers;
        this.repository = new LinkedHashMap<>();
    }

    public Transaction build(ModelTransaction model) {
        return repository.computeIfAbsent(model, k -> {
            if (Objects.equals(model.type, ArrayTransaction.NAME)) {
                return new ArrayTransaction(model.id, model.typeHint);
            }
            else if (Objects.equals(model.type, AttributeTransaction.NAME)) {
                return new AttributeTransaction(model.id, model.defaultName);
            }
            else if (Objects.equals(model.type, NameTransaction.NAME)) {
                return new NameTransaction(model.id);
            }
            else if (Objects.equals(model.type, ObjectTransaction.NAME)) {
                return new ObjectTransaction(model.id, model.typeHint);
            }
            else if (Objects.equals(model.type, ValueTransaction.NAME)) {
                var parser = parsers.findParser(model.parserName);
                return new ValueTransaction(model.id, parser);
            }
            else {
                throw new UnsupportedValueException(model.type, "transaction name");
            }
        });
    }

}
