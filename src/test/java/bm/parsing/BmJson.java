package bm.parsing;

import bm.BmException;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BmJson {

    private static final GsonBuilder builder;

    static {
        builder = new GsonBuilder();
        builder.registerTypeAdapter(BmVersion.class, new BmVersionAdapter());
    }

    public static <T> T loadFrom(Path file, Class<T> type) {
        var gson = builder.create();

        try (var reader = Files.newBufferedReader(file)) {
            return gson.fromJson(reader, type);
        }
        catch (IOException e) {
            throw new BmException("Cannot read file: " + file, e);
        }
    }

    public static <T> void saveTo(Path file, T object) {
        var gson = builder.create();

        try (var writer = Files.newBufferedWriter(file)) {
            gson.toJson(object, object.getClass(), writer);
        }
        catch (IOException e) {
            throw new BmException("Cannot write file: " + file, e);
        }
    }

    private static class BmVersionAdapter extends TypeAdapter<BmVersion> {

        @Override
        public BmVersion read(JsonReader in) throws IOException {
            String value = in.nextString();

            return BmVersion.parse(value);
        }

        @Override
        public void write(JsonWriter out, BmVersion value) throws IOException {
            out.value(value.toString());
        }

    }

}
