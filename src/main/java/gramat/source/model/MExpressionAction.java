package gramat.source.model;

import gramat.source.formatting.SourceWriter;

import java.util.List;

public class MExpressionAction implements MExpression {

    public String keyword;

    public List<MActionAttribute> attributes;

    public MExpression content;

    @Override
    public void write(SourceWriter writer) {
        writer.write('@');
        writer.write(keyword);

        if (attributes != null && attributes.size() > 0) {
            writer.write('[');
            for (int i = 0; i < attributes.size(); i++) {
                if (i > 0) {
                    writer.write(", ");
                }
                var attribute = attributes.get(i);

                writer.write(attribute.key);
                if (attribute.value != null) {
                    writer.write(": ");
                    writer.write(attribute.value);
                }
            }
            writer.write(']');
        }

        if (content != null) {
            writer.write('(');
            content.write(writer);
            writer.write(')');
        }
    }
}
