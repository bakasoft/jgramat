package stone.binary.translate;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public interface STR {
    static String readRawString(InputStream input) throws IOException {
        int length = INT32.readRawInt(input);
        var data = input.readNBytes(length);
        return new String(data, StandardCharsets.UTF_8);
    }
    static byte[] bytesOf(String value) {
        return value.getBytes(StandardCharsets.UTF_8);
    }
}
