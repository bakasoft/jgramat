package gramat.eval.trx;

import java.util.Objects;

public class TransactionID {

    private final int id;
    private final int level;
    private final String token;

    public TransactionID(int id, int level, String token) {
        this.id = id;
        this.level = level;
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        else if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TransactionID that = (TransactionID) o;
        return this.id == that.id &&
                this.level == that.level &&
                Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, level, token);
    }

    @Override
    public String toString() {
        return "TRX:" + id + ":" + level + ":" + token;
    }
}
