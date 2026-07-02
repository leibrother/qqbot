package cc.rapidev.qqbot.api.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author leibrother
 */
public record MessageArk(int templateId, List<KeyValue> kv) {

    public record KeyValue(String key, String value, Map<String, List<KeyValue>> obj) {

        public KeyValue(String key, String value) {
            this(key, value, null);
        }

        public KeyValue(String key, List<KeyValue> kvList) {
            Map<String, List<KeyValue>> obj = new HashMap<>();
            obj.put("obj_kv", kvList);
            this(key, null, obj);
        }
    }

}
