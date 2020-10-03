package gramat.parsing;

import gramat.models.source.ModelRule;
import gramat.input.Tape;

public interface AmRuleParser extends AmBase, AmValue, AmExpressionParser {
    default ModelRule tryRule(Tape tape) {
        var keyword = tryString(tape);

        if (keyword != null) {
            var rule = new ModelRule();

            rule.keyword = keyword;

            expectToken(tape, '=');

            rule.expression = readExpression(tape);

            return rule;
        }

        return null;
    }
}
