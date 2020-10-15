package gramat.machine;

import gramat.actions.Event;

public class Effect {

    public final State target;
    public final Event event;

    public Effect(State target) {
        this(target, null);
    }

    public Effect(State target, Event event) {
        this.target = target;
        this.event = Event.of(event);
    }
}
