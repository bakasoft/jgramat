package gramat.parsing;

import gramat.models.source.ModelFile;
import gramat.framework.Component;
import gramat.framework.DefaultComponent;
import gramat.input.Tape;

import java.util.ArrayList;

public class AmParser extends DefaultComponent implements AmMachineParser, AmExpressionParser, AmCallParser, AmRuleParser {

    public AmParser(Component parent) {
        super(parent);
    }

    public ModelFile parseFile(Tape tape) {
        var file = new ModelFile();
        file.rules = new ArrayList<>();
        file.calls = new ArrayList<>();

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

        return file;
    }

}
