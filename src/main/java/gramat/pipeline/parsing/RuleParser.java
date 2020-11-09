package gramat.pipeline.parsing;

import gramat.models.parsing.ModelRule;

public interface RuleParser extends BaseParser, ValueParser, ExpressionParser {
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
