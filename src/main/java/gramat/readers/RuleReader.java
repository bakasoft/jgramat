package gramat.readers;

import gramat.readers.models.ModelRule;

public interface RuleReader extends BaseReader, ValueReader, ExpressionReader {
    default ModelRule tryRule() {
        var keyword = tryString();

        if (keyword != null) {
            var rule = new ModelRule();

            rule.keyword = keyword;

            expectToken('=');

            rule.expression = readExpression();

            return rule;
        }

        return null;
    }
}
