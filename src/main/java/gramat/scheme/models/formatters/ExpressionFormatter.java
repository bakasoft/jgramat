package gramat.scheme.models.formatters;

import gramat.exceptions.UnsupportedValueException;
import gramat.formatting.BaseFormatter;
import gramat.models.expressions.*;
import gramat.scheme.models.expressions.*;
import gramat.util.DataUtils;

public class ExpressionFormatter extends BaseFormatter {

    public static String format(ModelExpression expression) {
        var builder = new StringBuilder();
        var formatter = new ExpressionFormatter(builder);

        formatter.writeExpression(expression);

        return builder.toString();
    }

    public ExpressionFormatter(Appendable output) {
        super(output);
    }

    public void writeExpression(ModelExpression expression) {
        if (expression instanceof ModelAlternation) {
            writeAlternation((ModelAlternation)expression);
        }
        else if (expression instanceof ModelArray) {
            writeArray((ModelArray)expression);
        }
        else if (expression instanceof ModelAttribute) {
            writeAttribute((ModelAttribute)expression);
        }
        else if (expression instanceof ModelLiteral) {
            writeLiteral((ModelLiteral)expression);
        }
        else if (expression instanceof ModelName) {
            writeName((ModelName)expression);
        }
        else if (expression instanceof ModelObject) {
            writeObject((ModelObject)expression);
        }
        else if (expression instanceof ModelOptional) {
            writeOptional((ModelOptional)expression);
        }
        else if (expression instanceof ModelRange) {
            writeRange((ModelRange)expression);
        }
        else if (expression instanceof ModelReference) {
            writeReference((ModelReference)expression);
        }
        else if (expression instanceof ModelRepetition) {
            writeRepetition((ModelRepetition)expression);
        }
        else if (expression instanceof ModelSequence) {
            writeSequence((ModelSequence)expression);
        }
        else if (expression instanceof ModelValue) {
            writeValue((ModelValue)expression);
        }
        else if (expression instanceof ModelWild) {
            writeWild((ModelWild)expression);
        }
        else {
            throw new UnsupportedValueException(expression);
        }
    }

    private void writeAlternation(ModelAlternation expression) {
        throw new UnsupportedOperationException();
    }

    private void writeArray(ModelArray expression) {
        throw new UnsupportedOperationException();
    }

    private void writeAttribute(ModelAttribute expression) {
        throw new UnsupportedOperationException();
    }

    private void writeLiteral(ModelLiteral expression) {
        throw new UnsupportedOperationException();
    }

    private void writeName(ModelName expression) {
        throw new UnsupportedOperationException();
    }

    private void writeObject(ModelObject expression) {
        throw new UnsupportedOperationException();
    }

    private void writeOptional(ModelOptional expression) {
        throw new UnsupportedOperationException();
    }

    private void writeRange(ModelRange expression) {
        throw new UnsupportedOperationException();
    }

    private void writeReference(ModelReference expression) {
        writeKeyword(expression.name);
    }

    private void writeRepetition(ModelRepetition expression) {
        throw new UnsupportedOperationException();
    }

    private void writeSequence(ModelSequence expression) {
        DataUtils.iterate(
                expression.items,
                this::writeExpression,
                () -> write(' '));
    }

    private void writeValue(ModelValue expression) {
        throw new UnsupportedOperationException();
    }

    private void writeWild(ModelWild expression) {
        throw new UnsupportedOperationException();
    }

    private void writeKeyword(String name) {
        // TODO validate and escape name
        write(name);
    }
}
