package gramat.parsing;

import gramat.framework.Context;
import gramat.models.source.ModelFile;
import gramat.input.Tape;

import java.util.ArrayList;

public class AmParser implements AmMachineParser, AmExpressionParser, AmCallParser, AmRuleParser {

    private final Context ctx;

    public AmParser(Context ctx) {
        this.ctx = ctx;
    }

    public ModelFile parseFile(Parser parser) {
        var file = new ModelFile();
        file.rules = new ArrayList<>();
        file.calls = new ArrayList<>();

        skipVoid(parser);

        while (true) {
            var call = tryCall(parser);

            if (call != null) {
                file.calls.add(call);
            }
            else {
                var rule = tryRule(parser);

                if (rule != null) {
                    file.rules.add(rule);
                }
                else {
                    break;
                }
            }

            expectToken(parser, ';');
        }

        return file;
    }

}
