package gramat;

import gramat.binary.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class BinaryFormatTest {

    public enum TestTypes implements IndexedType {
        SONG(1),
        ;

        private final int index;

        TestTypes(int index) {
            this.index = index;
        }

        @Override
        public int getIndex() { return index; }
    }

    public enum SongField implements IndexedField {
        TITLE(1),
        ARTIST(2),
        DURATION(3),
        TRACK(4),
        INSTRUMENTAL(5),
        ;

        private final int index;

        SongField(int index) {
            this.index = index;
        }

        @Override
        public int getIndex() { return index; }

    }

    public static class Song {
        public String title;
        public String artist;
        public Integer duration;
        public int track;
        public boolean instrumental;
    }

    public static class SongEditor implements ObjectEditor {

        @Override
        public IndexedType getType() {
            return TestTypes.SONG;
        }

        @Override
        public Object newInstance() {
            return new Song();
        }

        @Override
        public void setValue(Object object, IndexedField field, Value value) {
            var song = (Song)object;

            if (field == SongField.TITLE) {
                song.title = value.asString();
            }
            else if (field == SongField.ARTIST) {
                song.artist = value.asString();
            }
            else if (field == SongField.TRACK) {
                song.track = value.asInt();
            }
            else if (field == SongField.DURATION) {
                song.duration = value.asIntOrNull();
            }
            else if (field == SongField.INSTRUMENTAL) {
                song.instrumental = value.asBoolean();
            }
            else {
                throw new AssertionError("Unknown field: " + field);
            }
        }

        @Override
        public Value getValue(Object object, IndexedField field) {
            var song = (Song)object;

            if (field == SongField.TITLE) {
                return Value.of(song.title);
            }
            else if (field == SongField.ARTIST) {
                return Value.of(song.artist);
            }
            else if (field == SongField.TRACK) {
                return Value.of(song.track);
            }
            else if (field == SongField.DURATION) {
                return Value.of(song.duration);
            }
            else if (field == SongField.INSTRUMENTAL) {
                return Value.of(song.instrumental);
            }
            else {
                throw new AssertionError("Unknown field: " + field);
            }
        }

        @Override
        public Collection<IndexedField> getFields() {
            return List.of(SongField.values());
        }
    }

    @Test
    public void test() throws IOException {
        var songEditor = new SongEditor();
        var buffer = new ByteArrayOutputStream();
        var writer = new BinaryWriter(buffer);

        writer.addEditor(Song.class, songEditor);

        var song1 = buildSong(7, "Sayonara Baby", "サンボマスター", null, false);
        var song2 = buildSong(12, "キラーチューン", "東京事変", 222, false);
        var song3 = buildSong(1, "Oddloop", "frederic", 375, false);
        var songs = List.of(song1, song2, song3);

        writer.wirteList(Value.list(songs));

        var reader = new BinaryReader(new ByteArrayInputStream(buffer.toByteArray()));

        reader.addEditor(songEditor);

        var resultRaw = reader.readValue();

        Assert.assertEquals(ValueType.LIST, resultRaw.getType());

        var result = resultRaw.asList();

        Assert.assertEquals("Result length", songs.size(), result.size());

        var copy1 = result.get(0).asObject(Song.class);
        var copy2 = result.get(1).asObject(Song.class);
        var copy3 = result.get(2).asObject(Song.class);

        assertSong(song1, copy1);
        assertSong(song2, copy2);
        assertSong(song3, copy3);
    }

    private void assertSong(Song expected, Song actual) {
        Assert.assertEquals("Title", expected.title, actual.title);
        Assert.assertEquals("Artist", expected.artist, actual.artist);
        Assert.assertEquals("Track", expected.track, actual.track);
        Assert.assertEquals("Duration", expected.duration, actual.duration);
    }

    private Song buildSong(int track, String title, String artist, Integer duration, boolean instrumental) {
        var song = new Song();
        song.track = track;
        song.title = title;
        song.artist = artist;
        song.duration = duration;
        song.instrumental = instrumental;
        return song;
    }

}
