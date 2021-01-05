package gramat.pipeline.decoding;

import gramat.scheme.common.badges.Badge;
import gramat.scheme.common.badges.BadgeSource;
import gramat.scheme.data.automata.BadgeData;

import java.util.Objects;

public class BadgeDecoder {

    private final BadgeSource source;

    public BadgeDecoder() {
        source = new BadgeSource();
    }

    public BadgeSource getSource() {
        return source;
    }

    public Badge build(BadgeData data) {
        Objects.requireNonNull(data);

        if(data.wild) {
            return source.empty();
        }
        var token = Objects.requireNonNull(data.token);

        return source.badge(token);
    }

    public Badge build(String token) {
        Objects.requireNonNull(token);

        return source.badge(token);
    }

}
