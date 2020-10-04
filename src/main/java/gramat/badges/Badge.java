package gramat.badges;

public class Badge {

    public final String token;

    public Badge(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return String.valueOf(token);
    }
}
