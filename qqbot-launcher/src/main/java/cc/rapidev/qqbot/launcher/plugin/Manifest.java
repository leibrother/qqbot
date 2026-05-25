package cc.rapidev.qqbot.launcher.plugin;

import cc.rapidev.qqbot.common.VExpr;
import cc.rapidev.qqbot.common.Version;
import cc.rapidev.qqbot.common.utils.Asserts;
import cc.rapidev.qqbot.common.utils.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.jspecify.annotations.NonNull;

import java.util.*;

/**
 * @author leibrother
 */
public record Manifest(
        // 插件ID
        String id,
        // 插件名称
        String name,
        // 插件说明
        String description,
        // 插件作者
        String author,
        // 插件版本
        Version version,
        // 框架版本
        VExpr framework,
        // 插件依赖定义
        Map<String, VExpr> dependencies,
        // 插件钩子定义
        Map<String, String> hooks,
        // 扩展点定义
        List<String> extensions
) {

    @Override
    public @NonNull String toString() {
        return this.id + "@" + this.version;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Manifest manifest = (Manifest) o;
        return Objects.equals(id, manifest.id) && Objects.equals(version, manifest.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version);
    }

    public static Manifest parse(String data) {
        JsonNode json = JsonUtils.fromJson(data, JsonNode.class);
        Asserts.isTrue(json.has("id"), "the plugin does not declare 'id'");
        Asserts.isTrue(json.has("name"), "the plugin does not declare 'name'");
        Asserts.isTrue(json.has("version"), "the plugin does not declare 'version'");
        String id = json.get("id").asText().toLowerCase();
        String name = json.get("name").asText();
        String description = json.has("description") ? json.get("description").asText() : "";
        String author = json.has("author") ? json.get("author").asText() : "unknown";
        String version = json.get("version").asText();
        String framework = json.has("framework") ? json.get("framework").asText() : "*";
        Map<String, VExpr> dependencies = new HashMap<>();
        if (json.has("dependencies")) {
            json.get("dependencies").fields().forEachRemaining(entry -> {
                String v = entry.getValue().asText();
                dependencies.put(entry.getKey().toLowerCase(), VExpr.parse(v));
            });
        }
        Map<String, String> hooks = new HashMap<>();
        if (json.has("hooks")) {
            json.get("hooks").fields().forEachRemaining(entry -> hooks.put(entry.getKey(), entry.getValue().asText()));
        }
        List<String> extensions = new ArrayList<>();
        if (json.has("extensions")) {
            if (!json.get("extensions").isArray()) {
                throw new IllegalArgumentException("extensions field must be an array");
            }
            json.get("extensions").elements().forEachRemaining(entry -> extensions.add(entry.asText()));
        }
        return new Manifest(
                id,
                name,
                description,
                author,
                Version.parse(version),
                VExpr.parse(framework),
                dependencies,
                hooks,
                extensions
        );
    }

}
