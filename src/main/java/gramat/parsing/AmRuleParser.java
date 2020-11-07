package gramat.parsing;

import gramat.models.source.ModelRule;
import gramat.input.Tape;

public interface AmRuleParser extends AmBase, AmValue, AmExpressionParser {
    default ModelRule tryRule(Parser parser) {
        var keyword = tryString(parser);

        if (keyword != null) {
            var rule = new ModelRule();

            rule.keyword = keyword;

            expectToken(parser, '=');

            rule.expression = readExpression(parser);

            return rule;
        }

        return null;
    }
}
