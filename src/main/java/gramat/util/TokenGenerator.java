package gramat.util;

import java.util.HashSet;

public class TokenGenerator {

    private final int PESSIMISTIC_LIMIT = 10000;

    private final String separator;
    private final HashSet<Integer> hashes;

    public TokenGenerator() {
        this("-");
    }

    public TokenGenerator(String separator) {
        this.separator = separator;
        this.hashes = new HashSet<>();
    }

    public String next() {
        return next(null);
    }

    public String next(String base) {
        var count = 0;

        for (var i = 0; i < PESSIMISTIC_LIMIT; i++) {
            var token = (base != null ? base + separator + count : String.valueOf(count));
            var hash = token.hashCode();

            if (hashes.add(hash)) {
                return token;
            }

            count++;
        }

        throw new RuntimeException();
    }

}
