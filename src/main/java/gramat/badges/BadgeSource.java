package gramat.badges;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class BadgeSource implements Iterable<Badge> {

    private final List<Badge> badges;

    private final Badge emptyBadge;

    public BadgeSource() {
        badges = new ArrayList<>();

        emptyBadge = new Badge(null);

        badges.add(emptyBadge);
    }

    public Badge empty() {
        return emptyBadge;
    }

    public Badge badge(String token) {
        for (var badge : badges) {
            if (Objects.equals(badge.token, token)) {
                return badge;
            }
        }

        var badge = new Badge(token);
        badges.add(badge);
        return badge;
    }

    @Override
    public Iterator<Badge> iterator() {
        return badges.iterator();
    }
}
