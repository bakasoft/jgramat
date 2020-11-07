package gramat.source;

import gramat.models.source.ModelRule;

public interface AmRuleParser extends AmBase, AmValue, AmExpressionParser {
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
