package gramat.actions;

import gramat.util.Chain;

public interface EventEditor {

    Event getEvent();

    void setEvent(Event event);

    default void eventPrepend(Action action) {
        if (action != null) {
            setEvent(Event.of(action, getEvent(), null));
        }
    }

    default void eventPrepend(Chain<Action> action) {
        if (action != null) {
            setEvent(Event.of(action, getEvent(), null));
        }
    }

    default void eventAppend(Action action) {
        if (action != null) {
            setEvent(Event.of(null, getEvent(), action));
        }
    }

    default void eventAppend(Chain<Action> action) {
        if (action != null) {
            setEvent(Event.of(null, getEvent(), action));
        }
    }

    default void eventWrap(Event event) {
        setEvent(Event.of(event.before, getEvent(), event.after));
    }

    default void eventWrap(Action before, Action after) {
        if (before != null || after != null) {
            setEvent(Event.of(before, getEvent(), after));
        }
    }

}
