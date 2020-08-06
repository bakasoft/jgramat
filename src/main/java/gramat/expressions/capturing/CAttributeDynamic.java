package gramat.expressions.capturing;

import gramat.engine.actions.capturing.*;
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
        var nameAccepted = name.build(compiler, initial);
        var namePress = new AttributeNamePress();
        var nameRelease = new AttributeNameRelease(namePress);
        var nameSustain = new AttributeNameSustain(namePress);

        // setup overrides
        namePress.overrides(nameSustain);
        nameRelease.overrides(nameSustain);

        TRX2.applyActions(compiler, initial, nameAccepted, namePress, nameRelease, nameSustain);

        var valueAccepted = value.build(compiler, nameAccepted);
        var valuePress = new AttributeValuePress();
        var valueRelease = new AttributeValueRelease(valuePress, nameRelease);
        var valueSustain = new AttributeValueSustain(valuePress);

        // setup overrides
        valuePress.overrides(valueSustain);
        valueRelease.overrides(valueSustain);

        TRX2.applyActions(compiler, nameAccepted, valueAccepted, valuePress, valueRelease, valueSustain);

        return valueAccepted;
    }

}
