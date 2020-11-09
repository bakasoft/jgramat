package gramat.pipeline.decoding;

import gramat.scheme.core.badges.Badge;
import gramat.scheme.core.badges.BadgeSource;
import gramat.scheme.models.automata.ModelBadge;

import java.util.Objects;

public class BadgeDecoder {

    private final BadgeSource source;

    public BadgeDecoder() {
        source = new BadgeSource();
    }

    public BadgeSource getSource() {
        return source;
    }

    public Badge build(ModelBadge model) {
        Objects.requireNonNull(model);

        if(model.wild) {
            return source.empty();
        }
        var token = Objects.requireNonNull(model.token);

        return source.badge(token);
    }

    public Badge build(String token) {
        Objects.requireNonNull(token);

        return source.badge(token);
    }

}
