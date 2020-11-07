package gramat.assemblers;

import gramat.badges.Badge;
import gramat.badges.BadgeSource;
import gramat.models.automata.ModelBadge;

import java.util.Objects;

public class BadgeAssembler {

    private final BadgeSource source;

    public BadgeAssembler() {
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
