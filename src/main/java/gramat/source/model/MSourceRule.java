package gramat.source.model;

import gramat.source.formatting.SourceWriter;

public class MSourceRule implements MSourceMember {

    public String name;
    public MExpression expression;

    @Override
    public void write(SourceWriter writer) {
        writer.write(name); // TODO escape
        writer.write('=');
        expression.write(writer);
        writer.write('\n');
    }
}
