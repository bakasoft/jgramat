package gramat.engine;

public class BadgeActionPush extends Action {

    public final Badge badge;

    public BadgeActionPush(Badge badge) {
        this.badge = badge;
    }

    @Override
    public String getDescription() {
        return "PUSH " + badge;
    }

}
