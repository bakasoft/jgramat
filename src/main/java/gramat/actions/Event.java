package gramat.actions;

import java.util.Objects;

public class Event {

    public static Event empty() {
        return new Event(ActionList.empty(), ActionList.empty());
    }

    public static Event collect(Iterable<? extends Event> events) {
        var result = empty();

        for (var event : events) {
            result.wrap(event);
        }

        return result;
    }

    public static Event copy(Event event) {
        return new Event(ActionList.copy(event.before), ActionList.copy(event.after));
    }

    public final ActionList before;

    public final ActionList after;

    private Event(ActionList before, ActionList after) {
        this.before = before;
        this.after = after;
    }

    public void wrap(Action beforeAction, Action afterAction) {
        before.prepend(beforeAction);
        after.append(afterAction);
    }

    public void prepend(Action beforeAction) {
        before.prepend(beforeAction);
    }

    public void prepend(ActionList actions) {
        before.prepend(actions);
    }

    public void append(Action afterAction) {
        after.append(afterAction);
    }

    public void append(ActionList actions) {
        after.append(actions);
    }

    public void wrap(Event event) {
        wrap(event.before, event.after);
    }

    private void wrap(ActionList beforeActions, ActionList afterActions) {
        before.prepend(beforeActions);
        after.append(afterActions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(before, event.before) &&
                Objects.equals(after, event.after);
    }

    @Override
    public int hashCode() {
        return Objects.hash(before, after);
    }
}
