package cc.rapidev.qqbot;

import cc.rapidev.qqbot.common.Constant;
import cc.rapidev.qqbot.common.Events;
import cc.rapidev.qqbot.common.utils.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author leibrother
 */
public class BotPayload {

    private final ObjectNode content;

    public BotPayload() {
        this.content = JsonUtils.node();
    }

    public BotPayload(String json) {
        if (json == null || json.isEmpty()) {
            this.content = JsonUtils.node();
        } else {
            this.content = JsonUtils.fromJson(json, ObjectNode.class);
        }
    }

    public BotPayload(int opcode) {
        this();
        this.content.put(Constant.PAYLOAD_OPCODE, opcode);
    }

    public String getId() {
        if (this.content.has(Constant.PAYLOAD_ID)) {
            return this.content.get(Constant.PAYLOAD_ID).asText();
        }
        return null;
    }

    /**
     * 获取消息序列号
     *
     * @return payload.s
     */
    public int getSerialNumber() {
        if (this.content.has(Constant.PAYLOAD_SERIAL_NUMBER)) {
            return this.content.get(Constant.PAYLOAD_SERIAL_NUMBER).asInt();
        }
        return -1;
    }

    /**
     * 获取操作码
     *
     * @return payload.op
     */
    public int getOpCode() {
        if (this.content.has(Constant.PAYLOAD_OPCODE)) {
            return this.content.get(Constant.PAYLOAD_OPCODE).asInt();
        }
        return -1;
    }

    /**
     * 获取事件类型
     *
     * @return payload.t
     */
    public String getEvent() {
        if (this.content.has(Constant.PAYLOAD_EVENT_TYPE)) {
            return this.content.get(Constant.PAYLOAD_EVENT_TYPE).asText();
        }
        return null;
    }

    /**
     * 获取消息内容
     *
     * @param clazz 对应的实体类
     * @return payload.d
     */
    public <T> T getData(Class<T> clazz) {
        if (this.content.has(Constant.PAYLOAD_DATA)) {
            JsonNode node = this.content.get(Constant.PAYLOAD_DATA);
            return JsonUtils.convert(node, clazz);
        }
        return null;
    }

    public JsonNode getData() {
        return this.getData(JsonNode.class);
    }

    public String json() {
        return this.content.toString();
    }

    @Override
    public String toString() {
        return this.json();
    }

    public static BotPayload webhookACK() {
        return new BotPayload(12);
    }

    public static BotPayload websocketACK(int serial) {
        BotPayload payload = new BotPayload(1);
        payload.content.put(Constant.PAYLOAD_DATA, serial);
        return payload;
    }

    public static BotPayload broadcast(Events event) {
        BotPayload payload = new BotPayload(0);
        payload.content.put(Constant.PAYLOAD_EVENT_TYPE, event.name());
        return payload;
    }

}
