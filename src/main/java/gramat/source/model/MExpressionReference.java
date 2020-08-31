package gramat.source.model;

import gramat.source.formatting.SourceWriter;

public class MExpressionReference implements MExpression {

    public String name;

    @Override
    public void write(SourceWriter writer) {
        writer.write(name);  // TODO escape
    }
}
