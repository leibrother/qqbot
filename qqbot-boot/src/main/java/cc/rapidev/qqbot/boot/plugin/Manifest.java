package cc.rapidev.qqbot.boot.plugin;

import cc.rapidev.qqbot.common.Version;
import cc.rapidev.qqbot.common.utils.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author leibrother
 */
public record Manifest(
        // 插件名称
        String name,
        // 插件说明
        String description,
        // 插件作者
        String author,
        // 插件版本
        Version version,
        // 插件依赖定义
        Map<String, String> depends,
        // 插件钩子定义
        Map<String, String> hooks,
        // 插件注入器定义
        List<String> injectors
) {

    public static Manifest parse(String data) {
        JsonNode json = JsonUtils.fromJson(data, JsonNode.class);
        if (!json.has("name") || !json.has("version")) {
            throw new IllegalArgumentException("");
        }
        String name = json.get("name").asText();
        String description = json.get("description").asText("");
        String author = json.get("author").asText("Unknown");
        String version = json.get("version").asText();
        Map<String, String> depends = new HashMap<>();
        if (json.has("depends")) {
            json.get("depends").fields().forEachRemaining(entry -> {
                String v = entry.getValue().asText();
                depends.put(entry.getKey(), v);
            });
        }
        Map<String, String> hooks = new HashMap<>();
        if (json.has("hooks")) {
            json.get("hooks").fields().forEachRemaining(entry -> hooks.put(entry.getKey(), entry.getValue().asText()));
        }
        List<String> injectors = new ArrayList<>();
        if (json.has("injectors")) {
            if (!json.get("injectors").isArray()) {
                throw new IllegalArgumentException("");
            }
            json.get("injectors").elements().forEachRemaining(entry -> injectors.add(entry.asText()));
        }
        return new Manifest(
                name,
                description,
                author,
                Version.parse(version),
                depends,
                hooks,
                injectors
        );
    }

}
