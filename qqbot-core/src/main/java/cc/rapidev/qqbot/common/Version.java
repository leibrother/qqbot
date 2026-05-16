package cc.rapidev.qqbot.common;

import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author leibrother
 */
public record Version(
        int major,
        int minor,
        int patch
) {

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

    @Override
    public @NonNull String toString() {
        return String.join(".", List.of(String.valueOf(major), String.valueOf(minor), String.valueOf(patch)));
    }

    public boolean compare(String expr) {
        if (expr == null) {
            throw new IllegalArgumentException("version cannot be null");
        }
        if ("*".equals(expr)) {
            return true;
        } else if (expr.startsWith("^")) {
            Version parse = Version.parse(expr.substring(1));
            if (this.major == parse.major) {
                return this.minor > parse.minor || (this.minor == parse.minor && this.patch >= parse.patch);
            }
            return false;
        } else if (expr.startsWith("~")) {
            Version parse = Version.parse(expr.substring(1));
            if (this.major == parse.major && this.minor == parse.minor) {
                return this.patch >= parse.patch;
            }
            return false;
        } else {
            Version parse = Version.parse(expr);
            return this.major == parse.major && this.minor == parse.minor && this.patch == parse.patch;
        }
    }

}
