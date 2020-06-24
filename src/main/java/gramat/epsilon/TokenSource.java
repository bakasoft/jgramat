package gramat.epsilon;

import java.util.ArrayList;

public class TokenSource {

    private int next_id;

    public TokenSource() {
        this.next_id = 1;
    }

    public Token make() {
        var token = new Token(next_id);

        next_id++;

        return token;
    }
}
