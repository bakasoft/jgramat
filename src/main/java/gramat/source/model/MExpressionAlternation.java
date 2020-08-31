package gramat.source.model;

import gramat.source.formatting.SourceWriter;

import java.util.List;

public class MExpressionAlternation implements MExpression {

    public List<MExpression> options;

    @Override
    public void write(SourceWriter writer) {
        writer.write('(');
        for (var i = 0; i < options.size(); i++) {
            if (i > 0) {
                if (writer.getColumn() > writer.getGuideColumn()) {
                    writer.write("\n\t");
                }

                writer.write('|');
            }
            options.get(i).write(writer);
        }
        writer.write(')');
    }
}
