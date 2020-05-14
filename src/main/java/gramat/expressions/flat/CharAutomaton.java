package gramat.expressions.flat;

import gramat.automata.actions.*;
import gramat.automata.ndfa.DState;
import gramat.automata.ndfa.Language;
import gramat.automata.raw.*;
import gramat.compiling.Compiler;
import gramat.expressions.Expression;
import gramat.output.GrammarWriter;
import gramat.runtime.*;
import gramat.util.parsing.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CharAutomaton extends Expression {

    private final RawAutomaton automaton;

    private DState _root;

    public CharAutomaton(Location location, String literal) {
        this(location, new RawLiteralAutomaton(literal));
    }

    public CharAutomaton(Location location, char literal) {
        this(location, new RawCharAutomaton(literal));
    }

    public CharAutomaton(Location location, char begin, char end) {
        this(location, new RawRangeAutomaton(begin, end));
    }

    public CharAutomaton(Location location, RawAutomaton automaton) {
        super(location);
        this.automaton = automaton;
    }

    private DState getRoot() {
        if (_root == null) {
            var collapsed = automaton.collapse();
            var lang = new Language();
            var am = collapsed.build(lang);

            lang.applyPostBuild();

            _root = am.compile();
        }
        return _root;
    }

    @Override
    public boolean eval(EvalContext context) {
        var pos0 = context.source.getPosition();
        var state = getRoot();

        while(true) {
            if (state.isFinal()) {
                if (state.isAccepted()) {
                    return true;
                }
                else {
                    context.source.setPosition(pos0);
                    return false;
                }
            }
            else {
                var value = context.source.peek();
                var actions = new ArrayList<Action>();
                var newState = value != null ? state.move(value, actions) : null;

                for (var action : actions) {
                    performAction(context, action);
                }

                if (newState == null) {
                    if (state.isAccepted()) {
                        return true;
                    }
                    else {
                        context.source.setPosition(pos0);
                        return false;
                    }
                }

                context.source.moveNext();

                state = newState;
            }
        }
    }

    private final HashMap<Action, Integer> actionsPositions = new HashMap<>();

    private void performAction(EvalContext context, Action action) {
        System.out.println("ACTION " + action + " @" + context.source.getLocation());
        if (action instanceof PositionBegin) {
            int position = context.source.getPosition();

            actionsPositions.put(action, position);

            context.begin(null);
        }
        else if (action instanceof PositionRollback) {
            var rollback = (PositionRollback)action;
            if (actionsPositions.remove(rollback.beginAction) == null) {
                throw new RuntimeException("cannot rollback without begin");
            }

            context.rollback(null);
        }
        else if (action instanceof CommitCapture) {
            var commit = (CommitCapture)action;
            var pos0 = actionsPositions.remove(commit.beginAction);

            if (pos0 == null) {
                throw new RuntimeException("cannot commit without begin");
            }

            var posF = context.source.getPosition();

            context.add(new EditSendSegment(context.source, pos0, pos0, posF, commit.parser));

            context.commit(null);
        }
        else if (action instanceof CommitAttribute) {
            var commit = (CommitAttribute) action;
            var pos0 = actionsPositions.remove(commit.beginAction);

            if (pos0 == null) {
                throw new RuntimeException("cannot commit without begin");
            }

            context.add(new EditSet(context.source, pos0, commit.name));

            context.commit(null);
        }
        else if (action instanceof ObjectBegin) {
            context.begin(null);
            context.add(new EditOpenWildObject(context.source, context.source.getPosition(), null));
        }
        else if (action instanceof ObjectCommit) {
            context.add(new EditCloseValue(context.source, context.source.getPosition()));
            context.commit(null);
        }
        else if (action instanceof ObjectRollback) {
            context.rollback(null);
        }
        else {
            throw new RuntimeException("not implemented action: " + action);
        }
    }

    public RawAutomaton getAutomaton() {
        return automaton;
    }

    @Override
    public Expression _custom_optimize(Compiler context) {
        return this;
    }

    @Override
    public List<Expression> getInnerExpressions() {
        return listOf();
    }

    @Override
    public String getDescription() {
        return "Char-Automaton";
    }

    @Override
    public void write(GrammarWriter writer) {
        if (writer.open(this, "char-automaton")) {
            getRoot().write(writer);
            writer.close();
        }
    }

}
