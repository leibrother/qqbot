package cc.rapidev.qqbot.message.converter;

import cc.rapidev.qqbot.BotPayload;
import cc.rapidev.qqbot.common.Events;
import cc.rapidev.qqbot.common.interfaces.Converter;
import cc.rapidev.qqbot.message.model.Author;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author leibrother
 */
public class AuthorConverter implements Converter<BotPayload, Author> {

    public static final AuthorConverter INSTANCE = new AuthorConverter();

    @Override
    public Author convert(BotPayload payload) {
        Events event = payload.e();
        JsonNode data = payload.data();
        if (data == null || data.isNull()) {
            return null;
        }
        return switch (event) {
            case C2C_MESSAGE_CREATE -> ofC2CMessageCreate(data);
            case GROUP_AT_MESSAGE_CREATE -> ofGroupAtMessageCreate(data);
            case MESSAGE_CREATE, AT_MESSAGE_CREATE, DIRECT_MESSAGE_CREATE -> ofMessageCreate(data);
            default -> null;
        };
    }

    private Author ofC2CMessageCreate(JsonNode data) {
        JsonNode author = data.get("author");
        return new Author(
                author.get("user_openid").textValue(),
                "",
                "",
                false
        );
    }

    private Author ofGroupAtMessageCreate(JsonNode data) {
        JsonNode author = data.get("author");
        return new Author(
                author.get("member_openid").textValue(),
                "",
                "",
                false
        );
    }

    private Author ofMessageCreate(JsonNode data) {
        JsonNode author = data.get("author");
        return new Author(
                author.get("union_openid").textValue(),
                author.get("avatar").textValue(),
                author.get("username").textValue(),
                author.get("bot").booleanValue()
        );
    }

}
