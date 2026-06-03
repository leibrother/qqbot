package cc.rapidev.qqbot.message.extractor;

import cc.rapidev.qqbot.common.utils.IdentityUtils;
import cc.rapidev.qqbot.message.model.MessageAttachmentGeneric;
import cc.rapidev.qqbot.message.model.MessageGeneric;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author leibrother
 */
public class MessageExtractor extends Extractor<MessageGeneric> {

    private static final MessageExtractor INSTANCE = new MessageExtractor();

    public static MessageExtractor instance() {
        return INSTANCE;
    }

    @Override
    protected MessageGeneric byC2CMessageCreate(JsonNode data) {
        return parse(data);
    }

    @Override
    protected MessageGeneric byGroupAtMessageCreate(JsonNode data) {
        return parse(data);
    }

    @Override
    protected MessageGeneric byMessageCreate(JsonNode data) {
        return parse(data);
    }

    @Override
    protected MessageGeneric byAtMessageCreate(JsonNode data) {
        return parse(data);
    }

    @Override
    protected MessageGeneric byDirectMessageCreate(JsonNode data) {
        return parse(data);
    }

    protected MessageGeneric parse(JsonNode data) {
        if (data == null || data.isNull()) {
            return null;
        }
        JsonNode id = data.get("id");
        Optional<JsonNode> content = Optional.ofNullable(data.get("content"));
        Optional<JsonNode> timestamp = Optional.ofNullable(data.get("timestamp"));
        JsonNode attachments = data.get("attachments");

        return new MessageGeneric(
                id.asText(),
                content.map(JsonNode::asText).orElse(""),
                parseAttachments(attachments),
                timestamp.map(JsonNode::asText).orElse(null)
        );
    }

    private List<MessageAttachmentGeneric> parseAttachments(JsonNode data) {
        if (data == null || data.isNull() || !data.isArray()) {
            return List.of();
        }
        List<MessageAttachmentGeneric> attachments = new ArrayList<>();
        data.elements().forEachRemaining(attr -> {
            Optional<JsonNode> id = Optional.ofNullable(attr.get("id"));
            Optional<JsonNode> filename = Optional.ofNullable(attr.get("filename"));
            Optional<JsonNode> type = Optional.ofNullable(attr.get("content_type"));
            Optional<JsonNode> url = Optional.ofNullable(attr.get("url"));
            Optional<JsonNode> size = Optional.ofNullable(attr.get("size"));
            Optional<JsonNode> width = Optional.ofNullable(attr.get("width"));
            Optional<JsonNode> height = Optional.ofNullable(attr.get("height"));
            Optional<JsonNode> wavUrl = Optional.ofNullable(attr.get("voice_wav_url"));
            Optional<JsonNode> asrText = Optional.ofNullable(attr.get("asr_refer_text"));
            attachments.add(new MessageAttachmentGeneric(
                    id.map(JsonNode::asText).orElseGet(IdentityUtils::UUID),
                    filename.map(JsonNode::asText).orElse(null),
                    type.map(JsonNode::asText).orElse(null),
                    url.map(JsonNode::asText).orElse(null),
                    size.map(JsonNode::asInt).orElse(0),
                    width.map(JsonNode::asInt).orElse(0),
                    height.map(JsonNode::asInt).orElse(0),
                    wavUrl.map(JsonNode::asText).orElse(null),
                    asrText.map(JsonNode::asText).orElse(null)
            ));
        });
        return attachments;
    }

}
