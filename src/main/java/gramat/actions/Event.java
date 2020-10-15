package gramat.actions;

import gramat.util.Chain;

public class Event {

    public static Event of() {
        return new Event(null, null);
    }

    public static Event of(Chain<Action> before, Chain<Action> after) {
        return new Event(before, after);
    }

    public static Event of(Event event) {
        if (event == null) {
            return of();
        }
        return event;
    }

    public static Event of(Event outside, Event inside) {
        if (outside == null) {
            return of(inside);
        }
        else if (inside == null) {
            return of(outside);
        }
        return new Event(
                Chain.merge(outside.before, inside.before),
                Chain.merge(inside.after, outside.after));
    }

    public static Event of(Chain<Action> before, Event event, Chain<Action> after) {
        return new Event(Chain.merge(before, event.before), Chain.merge(event.after, after));
    }

    public static Event of(Action before, Event event, Action after) {
        return new Event(Chain.merge(before, event.before), Chain.merge(event.after, after));
    }

    public final Chain<Action> before;

    public final Chain<Action> after;

    private Event(Chain<Action> before, Chain<Action> after) {
        this.before = before;
        this.after = after;
    }


}
