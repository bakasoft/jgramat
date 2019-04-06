package org.bakasoft.gramat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Tape {

    private final String name;
    private final char[] content;
    private final int length;

    private int position;

    public Tape(String name, String content) {
        this(name, content.toCharArray());
    }

    public Tape(String name, char[] content) {
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
            throw new GrammarException();
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
            throw new GrammarException();
        }
    }

    public Location getLocation() {
        return getLocationOf(position);
    }

    public Location getLocationOf(int p) {
        int column = 0;
        int line = 1;

        for (int i = 0; i < p; i++) {
            // if last char is a line feed
            if (i > 0 && i - 1 < length && content[i - 1] == '\n') {
                line++;
                column = 0;
            }

            column++;
        }

        return new Location(name, p, line, column);
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

            return new Tape(path.toString(), content);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateSample() {
        final int LENGTH = 30;
        int start = Math.max(0, position - LENGTH / 2);
        int end = Math.min(length, start + LENGTH);
        StringBuilder sample = new StringBuilder();

        for (int i = 0; i < end; i++) {
            if (i == position) {
                sample.append('[');
                sample.append(content[i]);
                sample.append(']');
            }
            else {
                sample.append(' ');
                sample.append(content[i]);
                sample.append(' ');
            }
        }

        return sample.toString();
    }
}
