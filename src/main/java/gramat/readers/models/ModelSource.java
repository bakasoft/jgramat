package gramat.readers.models;

import gramat.expressions.models.ModelExpression;
import gramat.models.test.ModelTest;
import gramat.util.NameMap;

import java.util.List;

public class ModelSource {

    public NameMap<ModelExpression> rules;

    public List<ModelTest> tests;

    public ModelExpression main;

}
