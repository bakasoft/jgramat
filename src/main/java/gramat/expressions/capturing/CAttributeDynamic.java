package gramat.expressions.capturing;

import gramat.engine.actions.capturing.catalog.*;
import gramat.engine.nodet.NCompiler;
import gramat.engine.nodet.NState;
import gramat.expressions.Expression;

import java.util.List;

public class CAttributeDynamic extends Expression {

    private final Expression name;
    private final Expression value;

    public CAttributeDynamic(Expression name,Expression value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public List<Expression> getChildren() {
        return List.of(name, value);
    }

    @Override
    public NState build(NCompiler compiler, NState initial) {
        var nameData = name.findData();

        if (nameData.isEmpty()) {
            throw new RuntimeException("Expected data");
        }

        var valueData = value.findData();

        if (valueData.isEmpty()) {
            throw new RuntimeException("Expected data");
        }

        // the order of action instances matter
        var namePress = new AttributeNamePress();
        var nameAccepted = name.build(compiler, initial);
        var nameRelease = new AttributeNameRelease(namePress);
        var nameSustain = new AttributeNameSustain(namePress);

        // setup overrides
        namePress.overrides(nameSustain);
        nameRelease.overrides(nameSustain);
        nameRelease.overrides(namePress);  // TODO Confirm if this is true (double check recursive expressions)

        TRX2.applyActions(compiler, initial, nameAccepted, namePress, nameRelease, nameSustain);

        // the order of action instances matter
        var valuePress = new AttributeValuePress();
        var valueAccepted = value.build(compiler, nameAccepted);
        var valueRelease = new AttributeValueRelease(valuePress);
        var valueSustain = new AttributeValueSustain(valuePress);

        // setup overrides
        valuePress.overrides(valueSustain);
        valueRelease.overrides(valueSustain);
        valueRelease.overrides(valuePress);  // TODO Confirm if this is true (double check recursive expressions)

        TRX2.applyActions(compiler, nameAccepted, valueAccepted, valuePress, valueRelease, valueSustain);

        return valueAccepted;
    }

}
