package gramat.models.factories;

import gramat.expressions.models.*;
import gramat.expressions.models.*;
import gramat.readers.models.ModelCall;
import gramat.util.PP;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExpressionFactory {

    public static ModelWild wild() {
        return new ModelWild();
    }

    public static ModelLiteral literal(String value) {
        var literal = new ModelLiteral();
        literal.value = value;
        return literal;
    }

    public static ModelExpression repetition(ModelExpression content, int minimum) {
        return repetition(content, null, minimum);
    }

    public static ModelRepetition repetition(ModelExpression content) {
        return repetition(content, null);
    }

    public static ModelRepetition repetition(ModelExpression content, ModelExpression separator) {
        return repetition(content, separator, 0);
    }

    public static ModelRepetition repetition(ModelExpression content, ModelExpression separator, int minimum) {
        var repetition = new ModelRepetition();
        repetition.content = content;
        repetition.separator = separator;
        repetition.minimum = minimum;
        return repetition;
    }

    public static ModelValue value(ModelExpression content) {
        var value = new ModelValue();
        value.content = content;
        return value;
    }

    public static ModelObject object(ModelExpression content) {
        return object(content, null);
    }

    public static ModelArray array(ModelExpression content) {
        return array(content, null);
    }

    public static ModelObject object(ModelExpression content, String type) {
        var obj = new ModelObject();
        obj.type = type;
        obj.content = content;
        return obj;
    }

    public static ModelArray array(ModelExpression content, String type) {
        var obj = new ModelArray();
        obj.type = type;
        obj.content = content;
        return obj;
    }

    public static ModelAttribute attribute(ModelExpression content) {
        return attribute(content, null);
    }

    public static ModelAttribute attribute(ModelExpression content, String name) {
        var attr = new ModelAttribute();
        attr.name = name;
        attr.content = content;
        return attr;
    }

    public static ModelName name(ModelExpression content) {
        var name = new ModelName();
        name.content = content;
        return name;
    }

    public static ModelReference reference(String name) {
        var reference = new ModelReference();
        reference.name = name;
        return reference;
    }

    public static ModelSequence sequence(ModelExpression... expressions) {
        return sequence(List.of(expressions));
    }

    public static ModelSequence sequence(List<ModelExpression> expressions) {
        var sequence = new ModelSequence();
        sequence.items = new ArrayList<>(expressions);
        return sequence;
    }

    public static ModelAlternation alternation(ModelExpression... expressions) {
        return alternation(List.of(expressions));
    }

    public static ModelAlternation alternation(List<ModelExpression> expressions) {
        var alternation = new ModelAlternation();
        alternation.options = new ArrayList<>(expressions);
        return alternation;
    }

    public static ModelOptional optional(ModelExpression content) {
        var optional = new ModelOptional();
        optional.content = content;
        return optional;
    }

    public static ModelRange range(char begin, char end) {
        var range = new ModelRange();
        range.begin = begin;
        range.end = end;
        return range;
    }

    public static ModelExpression characterClass(String charClass) {
        // TODO how to accept spaces?
        var options = new ArrayList<ModelExpression>();

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

    public static ModelExpression call(ModelCall call) {
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
