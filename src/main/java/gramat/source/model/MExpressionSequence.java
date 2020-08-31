package gramat.source.model;

import gramat.source.formatting.SourceWriter;

import java.util.List;

public class MExpressionSequence implements MExpression {

    public List<MExpression> items;

    @Override
    public void write(SourceWriter writer) {
        writer.write('(');
        for (var i = 0; i < items.size(); i++) {
            if (i > 0) {
                if (writer.getColumn() > writer.getGuideColumn()) {
                    writer.write("\n\t");
                }
                else {
                    writer.write(' ');
                }
            }
            items.get(i).write(writer);
        }
        writer.write(')');
    }
}
