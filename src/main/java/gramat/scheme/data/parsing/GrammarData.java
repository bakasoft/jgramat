package gramat.scheme.data.parsing;

import gramat.scheme.data.expressions.ExpressionData;
import gramat.scheme.data.test.TestData;
import gramat.util.NameMap;

import java.util.List;

public class GrammarData {

    public NameMap<ExpressionData> rules;

    public List<TestData> tests;

    public ExpressionData main;

}
