package cc.rapidev.qqbot.common;

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
    public String toString() {
        return major + "." + minor + "." + patch;
    }

    public boolean match(String version) {
        if (version == null) {
            throw new IllegalArgumentException("version cannot be null");
        }
        if ("*".equals(version)) {
            return true;
        } else if (version.startsWith("^")) {
            Version parse = Version.parse(version.substring(1));
            if (this.major == parse.major) {
                return this.minor > parse.minor || (this.minor == parse.minor && this.patch >= parse.patch);
            }
            return false;
        } else if (version.startsWith("~")) {
            Version parse = Version.parse(version.substring(1));
            if (this.major == parse.major && this.minor == parse.minor) {
                return this.patch >= parse.patch;
            }
            return false;
        } else {
            Version parse = Version.parse(version);
            return this.major == parse.major && this.minor == parse.minor && this.patch == parse.patch;
        }
    }

}
