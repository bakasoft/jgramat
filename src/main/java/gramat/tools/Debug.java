package gramat.tools;

public class Debug {

    private static int tab = 0;

    public static void clear() {
        tab = 0;
    }

    public static void log(String message) {
        for (int i = 0; i < tab; i++) {
            System.out.print("- ");
        }
        System.out.println("- " + message);
    }

    public static void log(String message, int tabDiff) {
        log(message);

        tab += tabDiff;
    }

    public static void log(int tabDiff, String message) {
        tab += tabDiff;

        log(message);
    }
}
