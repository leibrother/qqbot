package cc.rapidev.qqbot.common.feature;

import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.common.utils.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author leibrother
 */
public class Features {

    private final Map<Feature, Set<Topic.Type>> features;

    public Features(Map<Feature, Set<Topic.Type>> features) {
        Map<Feature, Set<Topic.Type>> map = new HashMap<>(features);
        features.forEach((feature, topics) -> {
            map.put(feature, new LinkedHashSet<>(topics));
        });
        this.features = map;
    }

    public void add(Feature feature, Topic.Type scope) {
        this.features.computeIfAbsent(feature, _ -> new LinkedHashSet<>()).add(scope);
    }

    public boolean has(Feature feature, Topic.Type scope) {
        return this.features.containsKey(feature) && features.get(feature).contains(scope);
    }

    public boolean has(Feature feature, Topic topic) {
        return has(feature, topic.type());
    }

    public static Features parse(String expr) {
        if (StringUtils.isEmpty(expr)) {
            return new Features(Collections.emptyMap());
        }
        Map<Feature, Set<Topic.Type>> features = new HashMap<>();
        for (String text : expr.toUpperCase().split(",")) {
            String[] arr = text.split(":");
            if (arr.length > 2) {
                throw new IllegalArgumentException("");
            }
            Feature feature = Feature.valueOf(arr[0]);
            Set<Topic.Type> scopes;
            if (arr.length == 2) {
                scopes = Stream.of(arr[1].split("\\|"))
                        .map(Topic.Type::valueOf)
                        .collect(Collectors.toSet());
            } else {
                scopes = Set.of(Topic.Type.values());
            }
            features.put(feature, scopes);
        }
        return new Features(features);
    }

}
