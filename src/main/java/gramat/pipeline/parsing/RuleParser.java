package gramat.pipeline.parsing;

import gramat.scheme.data.parsing.RuleData;

public interface RuleParser extends BaseParser, ValueParser, ExpressionParser {
    default RuleData tryRule() {
        var keyword = tryString();

        if (keyword != null) {
            var rule = new RuleData();

            rule.keyword = keyword;

            expectToken('=');

            rule.expression = readExpression();

            return rule;
        }

        return null;
    }
}
