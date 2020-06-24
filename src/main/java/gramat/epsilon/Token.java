package gramat.epsilon;

public class Token {

    private final int id;

    public Token(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        else if (o == null || getClass() != o.getClass()) {
            return false;
        }
        else {
            return id == ((Token) o).id;
        }
    }

    @Override
    public int hashCode() {
        return id;
    }
}
