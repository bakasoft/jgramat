package gramat.engine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BadgeSource implements Iterable<Badge> {

    private final List<Badge> badges;

    public final Badge global;

    private int next_id;

    public BadgeSource() {
        this.badges = new ArrayList<>();
        this.global = new Badge(0);
        this.next_id = 1;

        badges.add(global);
    }

    public BadgeSource(BadgeSource other) {
        this.badges = new ArrayList<>(other.badges);
        this.global = other.global;
        this.next_id = other.next_id;
    }

    public Badge newBadge() {
        var id = next_id;
        var badge = new Badge(id);

        next_id++;

        badges.add(badge);

        return badge;
    }

    @Override
    public Iterator<Badge> iterator() {
        return badges.iterator();
    }

    public BadgeSource copy() {
        return new BadgeSource(this);
    }
}
