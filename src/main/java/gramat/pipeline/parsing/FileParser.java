package gramat.pipeline.parsing;

import gramat.models.parsing.ModelFile;

import java.util.ArrayList;

public interface FileParser extends MachineParser, ExpressionParser, CallParser, RuleParser {

    default ModelFile parseFile() {
        var file = new ModelFile();
        file.rules = new ArrayList<>();
        file.calls = new ArrayList<>();

        skipVoid();

        while (true) {
            var call = tryCall();

            if (call != null) {
                file.calls.add(call);
            }
            else {
                var rule = tryRule();

                if (rule != null) {
                    file.rules.add(rule);
                }
                else {
                    break;
                }
            }

            expectToken(';');
        }

        return file;
    }

}
