package gramat.parsing;

import gramat.am.source.AmCall;
import gramat.am.source.AmFile;
import gramat.am.source.AmRule;
import gramat.framework.Component;
import gramat.framework.DefaultComponent;
import gramat.input.Tape;

import java.util.ArrayList;

public class AmParser extends DefaultComponent implements AmMachineParser, AmExpressionParser, AmCallParser, AmRuleParser {

    public AmParser(Component parent) {
        super(parent);
    }

    public AmFile parseFile(Tape tape) {
        var file = new AmFile();
        file.rules = new ArrayList<>();
        file.calls = new ArrayList<>();

        expectToken(tape, Tape.STX);

        skipVoid(tape);

        while (true) {
            var call = tryCall(tape);

            if (call != null) {
                file.calls.add(call);
            }
            else {
                var rule = tryRule(tape);

                if (rule != null) {
                    file.rules.add(rule);
                }
                else {
                    break;
                }
            }

            expectToken(tape, ';');
        }

        expectToken(tape, Tape.ETX);

        return file;
    }

}
