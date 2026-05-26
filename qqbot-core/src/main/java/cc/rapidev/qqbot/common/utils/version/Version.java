package cc.rapidev.qqbot.common.utils.version;

import org.jspecify.annotations.NonNull;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author leibrother
 */
public record Version(
        int major,
        int minor,
        int patch
) implements Comparator<Version> {

    private static final Pattern VERSION_PATTERN = Pattern.compile("^(\\d+)\\.(\\d+)\\.(\\d+)(?:[-+].*)?$");

    public static Version parse(String version) {
        if (version == null) {
            throw new IllegalArgumentException("version cannot be null");
        }
        Matcher matcher = VERSION_PATTERN.matcher(version);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("invalid version format: " + version);
        }
        int major = Integer.parseInt(matcher.group(1));
        int minor = Integer.parseInt(matcher.group(2));
        int patch = Integer.parseInt(matcher.group(3));
        return new Version(major, minor, patch);
    }

    public int compare(Version version) {
        int majorCompared = Integer.compare(this.major, version.major);
        if (majorCompared == 0) {
            int minorCompared = Integer.compare(this.minor, version.major);
            if (minorCompared == 0) {
                return Integer.compare(this.patch, version.patch);
            }
            return minorCompared;
        }
        return majorCompared;
    }

    @Override
    public int compare(Version v1, Version v2) {
        return v1.compare(v2);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Version version = (Version) o;
        return major == version.major && minor == version.minor && patch == version.patch;
    }

    @Override
    public int hashCode() {
        return Objects.hash(major, minor, patch);
    }

    @Override
    public @NonNull String toString() {
        return String.join(".", List.of(String.valueOf(major), String.valueOf(minor), String.valueOf(patch)));
    }

}
