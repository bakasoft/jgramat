package gramat.scheme.models.parsing;

import gramat.scheme.models.expressions.ModelExpression;

import java.util.List;

public class ModelCall {
    public String keyword;
    public List<Object> arguments;
    public ModelExpression expression;
}
