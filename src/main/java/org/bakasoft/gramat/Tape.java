package org.bakasoft.gramat;

import org.bakasoft.gramat.inspect.Inspector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Tape {

    private final String name;
    private final char[] content;
    private final int length;

    private int position;

    public Tape(String content) {
        this(content.toCharArray(), null);
    }

    public Tape(char[] content) {
        this(content, null);
    }

    public Tape(String content, String name) {
        this(content.toCharArray(), name);
    }

    public Tape(char[] content, String name) {
        this.name = name;
        this.content = content;
        this.length = content.length;
        this.position = 0;
    }

    public String getName() {
        return name;
    }

    public boolean alive() {
        return position < length;
    }

    public char peek() {
        if (position >= length) {
            throw new RuntimeException();
        }

        return content[position];
    }

    public void moveForward() {
        setPosition(position + 1);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int newPosition) {
        if (newPosition <= length) {
            this.position = newPosition;

//            System.out.println(generateSample());
        }
        else {
            throw new RuntimeException();
        }
    }

    public Location getLocation() {
        return getLocationOf(position);
    }

    public Location getLocationOf(int p) {
        int column = 0;
        int line = 1;

        for (int i = 0; i <= p; i++) {
            // if last char is a line feed
            if (i > 0 && i - 1 < length && content[i - 1] == '\n') {
                line++;
                column = 0;
            }

            column++;
        }

        return new Location(this, p, line, column);
    }

    public String substring(int startIndex, int endIndex) {
        int count = endIndex - startIndex;
        return new String(content, startIndex, count);
    }

    public static Tape fromFile(String path) {
        return fromPath(Paths.get(path));
    }

    public static Tape fromPath(Path path) {
        try {
            byte[] data = Files.readAllBytes(path);
            String content = new String(data);

            return new Tape(content, path.toString());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateSample(int sampleSize) {
        Inspector inspector = new Inspector();
        int start = Math.max(0, position - sampleSize / 2);
        int end = Math.min(length, start + sampleSize);

        if (start == 0) {
            inspector.appendChar('"');
        }
        else {
            inspector.appendChar('…');
        }

        for (int i = 0; i < end; i++) {
            inspector.appendStringChar(content[i], '"');
        }

        if (end == length) {
            inspector.appendChar('"');
        }
        else {
            inspector.appendChar('…');
        }

        return inspector.getOutput();
    }

    @Override
    public String toString() {
        return name != null ? name : generateSample(10);
    }
}
