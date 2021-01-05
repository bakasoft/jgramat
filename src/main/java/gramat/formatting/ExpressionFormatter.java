package gramat.formatting;

import gramat.exceptions.UnsupportedValueException;
import gramat.scheme.data.expressions.*;
import gramat.util.DataUtils;

public class ExpressionFormatter extends BaseFormatter {

    public static String format(ExpressionData expression) {
        var builder = new StringBuilder();
        var formatter = new ExpressionFormatter(builder);

        formatter.writeExpression(expression);

        return builder.toString();
    }

    public ExpressionFormatter(Appendable output) {
        super(output);
    }

    public void writeExpression(ExpressionData expression) {
        if (expression instanceof AlternationData) {
            writeAlternation((AlternationData)expression);
        }
        else if (expression instanceof ArrayData) {
            writeArray((ArrayData)expression);
        }
        else if (expression instanceof AttributeData) {
            writeAttribute((AttributeData)expression);
        }
        else if (expression instanceof LiteralData) {
            writeLiteral((LiteralData)expression);
        }
        else if (expression instanceof NameData) {
            writeName((NameData)expression);
        }
        else if (expression instanceof ObjectData) {
            writeObject((ObjectData)expression);
        }
        else if (expression instanceof OptionalData) {
            writeOptional((OptionalData)expression);
        }
        else if (expression instanceof RangeData) {
            writeRange((RangeData)expression);
        }
        else if (expression instanceof ReferenceData) {
            writeReference((ReferenceData)expression);
        }
        else if (expression instanceof RepetitionData) {
            writeRepetition((RepetitionData)expression);
        }
        else if (expression instanceof SequenceData) {
            writeSequence((SequenceData)expression);
        }
        else if (expression instanceof ValueData) {
            writeValue((ValueData)expression);
        }
        else if (expression instanceof WildData) {
            writeWild((WildData)expression);
        }
        else {
            throw new UnsupportedValueException(expression);
        }
    }

    private void writeAlternation(AlternationData expression) {
        throw new UnsupportedOperationException();
    }

    private void writeArray(ArrayData expression) {
        throw new UnsupportedOperationException();
    }

    private void writeAttribute(AttributeData expression) {
        throw new UnsupportedOperationException();
    }

    private void writeLiteral(LiteralData expression) {
        throw new UnsupportedOperationException();
    }

    private void writeName(NameData expression) {
        throw new UnsupportedOperationException();
    }

    private void writeObject(ObjectData expression) {
        throw new UnsupportedOperationException();
    }

    private void writeOptional(OptionalData expression) {
        throw new UnsupportedOperationException();
    }

    private void writeRange(RangeData expression) {
        throw new UnsupportedOperationException();
    }

    private void writeReference(ReferenceData expression) {
        writeKeyword(expression.name);
    }

    private void writeRepetition(RepetitionData expression) {
        throw new UnsupportedOperationException();
    }

    private void writeSequence(SequenceData expression) {
        DataUtils.iterate(
                expression.items,
                this::writeExpression,
                () -> write(' '));
    }

    private void writeValue(ValueData expression) {
        throw new UnsupportedOperationException();
    }

    private void writeWild(WildData expression) {
        throw new UnsupportedOperationException();
    }

    private void writeKeyword(String name) {
        // TODO validate and escape name
        write(name);
    }
}
