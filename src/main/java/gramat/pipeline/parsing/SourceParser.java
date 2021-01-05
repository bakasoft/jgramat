package gramat.pipeline.parsing;

import gramat.scheme.data.parsing.SourceData;

import java.util.ArrayList;

public interface SourceParser extends CallParser, RuleParser {

    default SourceData parseSource() {
        var source = new SourceData();
        source.rules = new ArrayList<>();
        source.calls = new ArrayList<>();

        skipVoid();

        while (true) {
            var call = tryCall();

            if (call != null) {
                source.calls.add(call);
            }
            else {
                var rule = tryRule();

                if (rule != null) {
                    source.rules.add(rule);
                }
                else {
                    break;
                }
            }

            expectToken(';');
        }

        return source;
    }

}
