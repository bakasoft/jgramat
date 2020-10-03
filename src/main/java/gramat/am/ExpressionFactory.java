package gramat.am;

import gramat.am.expression.*;
import gramat.am.source.AmCall;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ExpressionFactory {

    public static WildExpression wild() {
        return new WildExpression();
    }

    public static LiteralExpression literal(String value) {
        var literal = new LiteralExpression();
        literal.value = value;
        return literal;
    }

    public static AmExpression repetition(AmExpression content, int minimum) {
        return repetition(content, null, minimum);
    }

    public static RepetitionExpression repetition(AmExpression content) {
        return repetition(content, null);
    }

    public static RepetitionExpression repetition(AmExpression content, AmExpression separator) {
        return repetition(content, separator, 0);
    }

    public static RepetitionExpression repetition(AmExpression content, AmExpression separator, int minimum) {
        var repetition = new RepetitionExpression();
        repetition.content = content;
        repetition.separator = separator;
        repetition.minimum = minimum;
        return repetition;
    }

    public static ValueExpression value(AmExpression content) {
        var value = new ValueExpression();
        value.content = content;
        return value;
    }

    public static ObjectExpression object(AmExpression content) {
        return object(content, null);
    }

    public static ObjectExpression object(AmExpression content, String type) {
        var obj = new ObjectExpression();
        obj.type = type;
        obj.content = content;
        return obj;
    }

    public static AttributeExpression attribute(AmExpression content) {
        return attribute(content, null);
    }

    public static AttributeExpression attribute(AmExpression content, String name) {
        var attr = new AttributeExpression();
        attr.name = name;
        attr.content = content;
        return attr;
    }

    public static NameExpression name(AmExpression content) {
        var name = new NameExpression();
        name.content = content;
        return name;
    }

    public static ReferenceExpression reference(String name) {
        var reference = new ReferenceExpression();
        reference.name = name;
        return reference;
    }

    public static SequenceExpression sequence(AmExpression... expressions) {
        return sequence(List.of(expressions));
    }

    public static SequenceExpression sequence(List<AmExpression> expressions) {
        var sequence = new SequenceExpression();
        sequence.items = new ArrayList<>(expressions);
        return sequence;
    }

    public static AlternationExpression alternation(AmExpression... expressions) {
        return alternation(List.of(expressions));
    }

    public static AlternationExpression alternation(List<AmExpression> expressions) {
        var alternation = new AlternationExpression();
        alternation.options = new ArrayList<>(expressions);
        return alternation;
    }

    public static OptionalExpression optional(AmExpression content) {
        var optional = new OptionalExpression();
        optional.content = content;
        return optional;
    }

    public static RangeExpression range(char begin, char end) {
        var range = new RangeExpression();
        range.begin = begin;
        range.end = end;
        return range;
    }

    public static AmExpression characterClass(String charClass) {
        var options = new ArrayList<AmExpression>();

        for (var item : charClass.split(" +")) {
            if (item.length() == 3 && item.charAt(1) == '-') {
                var begin = item.charAt(0);
                var end = item.charAt(2);
                options.add(range(begin, end));
            }
            else {
                throw new RuntimeException("invalid character class: " + charClass);
            }
        }

        if (options.isEmpty()) {
            throw new RuntimeException("character class cannot be empty");
        }
        else if (options.size() == 1) {
            return options.get(0);
        }
        else {
            return alternation(options);
        }
    }

    public static AmExpression call(AmCall call) {
        if (Objects.equals(call.keyword, "value")) {
            return value(call.expression);
        }
        else if (Objects.equals(call.keyword, "set")) {
            return attribute(call.expression);
        }
        else if (Objects.equals(call.keyword, "object")) {
            return object(call.expression);
        }
        else if (Objects.equals(call.keyword, "name")) {
            return name(call.expression);
        }
        else {
            throw new RuntimeException("not supported call: " + call.keyword);
        }
    }
}
