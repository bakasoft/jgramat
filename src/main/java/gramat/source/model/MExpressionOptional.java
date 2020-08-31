package gramat.source.model;

import gramat.source.formatting.SourceWriter;

public class MExpressionOptional implements MExpression {

    public MExpression content;

    @Override
    public void write(SourceWriter writer) {
        writer.write('[');
        content.write(writer);
        writer.write(']');
    }
}
