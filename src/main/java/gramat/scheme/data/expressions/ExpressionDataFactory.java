package gramat.scheme.data.expressions;

import gramat.scheme.data.parsing.CallData;
import gramat.util.PP;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExpressionDataFactory {

    public static WildData wild() {
        return new WildData();
    }

    public static LiteralData literal(String value) {
        var literal = new LiteralData();
        literal.value = value;
        return literal;
    }

    public static ExpressionData repetition(ExpressionData content, int minimum) {
        return repetition(content, null, minimum);
    }

    public static RepetitionData repetition(ExpressionData content) {
        return repetition(content, null);
    }

    public static RepetitionData repetition(ExpressionData content, ExpressionData separator) {
        return repetition(content, separator, 0);
    }

    public static RepetitionData repetition(ExpressionData content, ExpressionData separator, int minimum) {
        var repetition = new RepetitionData();
        repetition.content = content;
        repetition.separator = separator;
        repetition.minimum = minimum;
        return repetition;
    }

    public static ValueData value(ExpressionData content) {
        var value = new ValueData();
        value.content = content;
        return value;
    }

    public static ObjectData object(ExpressionData content) {
        return object(content, null);
    }

    public static ArrayData array(ExpressionData content) {
        return array(content, null);
    }

    public static ObjectData object(ExpressionData content, String type) {
        var obj = new ObjectData();
        obj.type = type;
        obj.content = content;
        return obj;
    }

    public static ArrayData array(ExpressionData content, String type) {
        var obj = new ArrayData();
        obj.type = type;
        obj.content = content;
        return obj;
    }

    public static AttributeData attribute(ExpressionData content) {
        return attribute(content, null);
    }

    public static AttributeData attribute(ExpressionData content, String name) {
        var attr = new AttributeData();
        attr.name = name;
        attr.content = content;
        return attr;
    }

    public static NameData name(ExpressionData content) {
        var name = new NameData();
        name.content = content;
        return name;
    }

    public static ReferenceData reference(String name) {
        var reference = new ReferenceData();
        reference.name = name;
        return reference;
    }

    public static SequenceData sequence(ExpressionData... expressions) {
        return sequence(List.of(expressions));
    }

    public static SequenceData sequence(List<ExpressionData> expressions) {
        var sequence = new SequenceData();
        sequence.items = new ArrayList<>(expressions);
        return sequence;
    }

    public static AlternationData alternation(ExpressionData... expressions) {
        return alternation(List.of(expressions));
    }

    public static AlternationData alternation(List<ExpressionData> expressions) {
        var alternation = new AlternationData();
        alternation.options = new ArrayList<>(expressions);
        return alternation;
    }

    public static OptionalData optional(ExpressionData content) {
        var optional = new OptionalData();
        optional.content = content;
        return optional;
    }

    public static RangeData range(char begin, char end) {
        var range = new RangeData();
        range.begin = begin;
        range.end = end;
        return range;
    }

    public static ExpressionData characterClass(String charClass) {
        // TODO how to accept spaces?
        var options = new ArrayList<ExpressionData>();

        for (var item : charClass.split(" +")) {
            if (item.length() == 3 && item.charAt(1) == '-') {
                var begin = item.charAt(0);
                var end = item.charAt(2);
                options.add(range(begin, end));
            }
            else if (item.length() == 1) {
                var chr = item.charAt(0);
                options.add(literal(String.valueOf(chr)));
            }
            else {
                throw new RuntimeException("invalid character class: " + PP.str(charClass));
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

    public static ExpressionData call(CallData call) {
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
        else if (Objects.equals(call.keyword, "array")) {
            return array(call.expression);
        }
        else {
            throw new RuntimeException("not supported call: " + call.keyword);
        }
    }
}
