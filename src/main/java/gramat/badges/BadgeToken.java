package gramat.badges;

public class BadgeToken extends Badge {

    public final String token;

    public BadgeToken(String token) {
        this.token = token;
    }

    @Override
    public boolean isWild() {
        return false;
    }

    @Override
    public String toString() {
        return String.valueOf(token);
    }
}
