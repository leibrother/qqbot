package cc.rapidev.qqbot.common;

import org.jspecify.annotations.NonNull;

/**
 * @author leibrother
 */
public class VExpr {

    private final String symbol;
    private final Version version;

    public VExpr(String symbol, Version version) {
        this.symbol = symbol;
        this.version = version;
    }

    public VExpr(String symbol, int major, int minor, int patch) {
        this.symbol = symbol;
        this.version = new Version(major, minor, patch);
    }

    public static VExpr parse(String expr) {
        String trimmed = expr.trim();
        if ("*".equals(trimmed)) {
            return new VExpr("*", 0, 0, 0);
        } else if (trimmed.startsWith("^") || trimmed.startsWith("~") || trimmed.startsWith("=")) {
            String symbol = String.valueOf(trimmed.charAt(0));
            Version version = Version.parse(trimmed.substring(1));
            return new VExpr(symbol, version);
        } else {
            return new VExpr("=", Version.parse(trimmed));
        }
    }

    public boolean satisfy(Version version) {
        switch (symbol) {
            case "*" -> {
                return true;
            }
            case "=" -> {
                return this.version.compare(version) == 0;
            }
            case "^" -> {
                if (version.major() == this.version.major()) {
                    return version.minor() > this.version.minor()
                            || (version.minor() == this.version.minor() && version.patch() >= this.version.patch());
                }
                return false;
            }
            case "~" -> {
                if (version.major() == this.version.major() && version.minor() == this.version.minor()) {
                    return version.patch() >= this.version.patch();
                }
                return false;
            }
            case null, default -> throw new IllegalStateException("unknown version comparison symbol: %s".formatted(symbol));
        }
    }

    @Override
    public @NonNull String toString() {
        if ("*".equals(symbol)) {
            return symbol;
        } else {
            return symbol + version;
        }
    }
}
