package gramat.parsing;

import gramat.am.source.AmRule;
import gramat.input.Tape;

public interface AmRuleParser extends AmBase, AmValue, AmExpressionParser {
    default AmRule tryRule(Tape tape) {
        var keyword = tryString(tape);

        if (keyword != null) {
            var rule = new AmRule();

            rule.keyword = keyword;

            expectToken(tape, '=');

            rule.expression = readExpression(tape);

            return rule;
        }

        return null;
    }
}
