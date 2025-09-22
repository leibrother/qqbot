package cc.rapidev.qqbot.message.matcher;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

/**
 * @author leibrother
 */
public class MatcherService {

    private final Vector<String> histories = new Vector<>(1);
    private final boolean ignoreCase;

    public MatcherService(String content) {
        this(content, true);
    }

    public MatcherService(String content, boolean ignoreCase) {
        String trimmed = content.trim();
        if (trimmed.startsWith("/")) {
            trimmed = trimmed.substring(1).trim();
        }
        this.ignoreCase = ignoreCase;
        if (ignoreCase) {
            this.histories.add(trimmed.toLowerCase());
        } else {
            this.histories.add(trimmed);
        }
    }

    public MatcherService branch() {
        return branch(0);
    }

    public MatcherService branch(int deep) {
        if (histories.size() <= deep) {
            throw new IndexOutOfBoundsException();
        }
        String content = histories.get(deep);
        return new MatcherService(content, this.ignoreCase);
    }

    public synchronized boolean match(String arg) {
        if (ignoreCase) {
            arg = arg.toLowerCase();
        }
        String content = histories.getLast();
        if (content.startsWith(arg)) {
            String after = content.replaceFirst(arg, "");
            histories.add(after.trim());
            return true;
        }
        return false;
    }

    public boolean match(List<String> args) {
        ArrayList<String> list = new ArrayList<>(args);
        list.sort(Comparator.comparingInt(String::length).reversed());
        for (String item : list) {
            if (match(item)) {
                return true;
            }
        }
        return false;
    }

    public boolean match(String... args) {
        return match(List.of(args));
    }


    public String origin() {
        return histories.getFirst();
    }

    public String current() {
        return histories.getLast();
    }

}
