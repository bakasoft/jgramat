package gramat.compiling;

import gramat.expressions.NamedExpression;

public interface LinkContext {

    NamedExpression getExpression(String name);

}
