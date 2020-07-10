package gramat.engine.deter;

import gramat.Rejection;
import gramat.Rule;
import gramat.engine.Badge;
import gramat.engine.BadgeActionPop;
import gramat.engine.BadgeActionPush;
import gramat.engine.Input;
import org.junit.Test;

public class DRunnerTest {

    @Test
    public void runner_test() throws Rejection {
        var builder = new DBuilder();
        var groupBadge = new Badge(0);

        builder.accept(3);
        builder.transition(0, 1, Input.STX);
        builder.transition(1, 1, '(', new BadgeActionPush(groupBadge));
        builder.transition(1, 2, 'x');
        builder.transition(2, 2, ')', groupBadge, new BadgeActionPop());
        builder.transition(2, 3, Input.ETX);

        var rule = new Rule(builder.getState(0));

        assert rule.eval("(((x)))") == null;
    }

}
