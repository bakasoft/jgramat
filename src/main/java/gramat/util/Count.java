package gramat.util;

public class Count {

    private int count;

    public Count() {
        count = 0;
    }

    public int next() {
        int c = count;
        count++;
        return c;
    }

    public String nextString() {
        return String.valueOf(next());
    }

}
