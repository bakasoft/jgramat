package gramat.scheme.models.parsing;

import gramat.scheme.models.expressions.ModelExpression;
import gramat.scheme.models.test.ModelTest;
import gramat.util.NameMap;

import java.util.List;

public class ModelSource {

    public NameMap<ModelExpression> rules;

    public List<ModelTest> tests;

    public ModelExpression main;

}
