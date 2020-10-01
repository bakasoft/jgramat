package gramat.expressions;

import gramat.expressions.impl.*;

import java.util.List;

public class ExpressionFactory {

    public WildExpression wild() {
        return new WildExpression();
    }

    public LiteralExpression literal(String value) {
        var literal = new LiteralExpression();
        literal.value = value;
        return literal;
    }

    public Expression repetition(Expression content, int minimum) {
        return repetition(content, null, minimum);
    }

    public RepetitionExpression repetition(Expression content) {
        return repetition(content, null);
    }

    public RepetitionExpression repetition(Expression content, Expression separator) {
        return repetition(content, separator, 0);
    }

    public RepetitionExpression repetition(Expression content, Expression separator, int minimum) {
        var repetition = new RepetitionExpression();
        repetition.content = content;
        repetition.separator = separator;
        repetition.minimum = minimum;
        return repetition;
    }

    public ValueExpression value(Expression content) {
        var value = new ValueExpression();
        value.content = content;
        return value;
    }

    public ObjectExpression object(Expression content) {
        return object(content, null);
    }

    public ObjectExpression object(Expression content, String type) {
        var obj = new ObjectExpression();
        obj.type = type;
        obj.content = content;
        return obj;
    }

    public AttributeExpression attribute(Expression content) {
        return attribute(content, null);
    }

    public AttributeExpression attribute(Expression content, String name) {
        var attr = new AttributeExpression();
        attr.name = name;
        attr.content = content;
        return attr;
    }

    public NameExpression name(Expression content) {
        var name = new NameExpression();
        name.content = content;
        return name;
    }

    public ReferenceExpression reference(String name) {
        var reference = new ReferenceExpression();
        reference.name = name;
        return reference;
    }

    public SequenceExpression sequence(Expression... expressions) {
        var sequence = new SequenceExpression();
        sequence.items = List.of(expressions);
        return sequence;
    }

    public AlternationExpression alternation(Expression... expressions) {
        var alternation = new AlternationExpression();
        alternation.options = List.of(expressions);
        return alternation;
    }

    public OptionalExpression optional(Expression content) {
        var optional = new OptionalExpression();
        optional.content = content;
        return optional;
    }

    public RangeExpression range(char begin, char end) {
        var range = new RangeExpression();
        range.begin = begin;
        range.end = end;
        return range;
    }

}
