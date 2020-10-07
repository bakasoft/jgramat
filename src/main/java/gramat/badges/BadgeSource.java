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

        emptyBadge = new BadgeWild();

        badges.add(emptyBadge);
    }

    public Badge empty() {
        return emptyBadge;
    }

    public Badge badge(String token) {
        for (var badge : badges) {
            if (badge instanceof BadgeToken) {
                var badgeToken = (BadgeToken)badge;
                if (Objects.equals(badgeToken.token, token)) {
                    return badge;
                }
            }
        }

        var badge = new BadgeToken(token);
        badges.add(badge);
        return badge;
    }

    @Override
    public Iterator<Badge> iterator() {
        return badges.iterator();
    }
}
