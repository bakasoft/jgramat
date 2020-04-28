package bm.parsing;

import bm.BmException;

import java.util.Objects;

public class BmVersion {

    public static BmVersion parse(String input) {
        if (input == null || input.isBlank()) {
            return new BmVersion(0, 0, 0);
        }

        var items = input.trim().split("\\.");

        if (items.length == 1) {
            return new BmVersion(
                    Integer.parseInt(items[0]),
                    0, 0);
        }
        else if (items.length == 2) {
            return new BmVersion(
                    Integer.parseInt(items[0]),
                    Integer.parseInt(items[1]),
                    0);
        }
        else if (items.length == 3) {
            return new BmVersion(
                    Integer.parseInt(items[0]),
                    Integer.parseInt(items[1]),
                    Integer.parseInt(items[2]));
        }
        else {
            throw new BmException("Invalid version: " + input);
        }
    }

    private final int major;
    private final int minor;
    private final int fix;

    public BmVersion() {
        this(0, 0, 0);
    }

    public BmVersion(int major, int minor, int fix) {
        this.major = major;
        this.minor = minor;
        this.fix = fix;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getFix() {
        return fix;
    }

    @Override
    public String toString() {
        return major + "." + minor + "." + fix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        else if (o == null || getClass() != o.getClass()) { return false; }
        else {
            BmVersion bmVersion = (BmVersion) o;
            return major == bmVersion.major &&
                    minor == bmVersion.minor &&
                    fix == bmVersion.fix;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(major, minor, fix);
    }
}
