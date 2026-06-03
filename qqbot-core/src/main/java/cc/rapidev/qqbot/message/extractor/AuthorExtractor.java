package cc.rapidev.qqbot.message.extractor;

import cc.rapidev.qqbot.message.model.Author;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author leibrother
 */
public class AuthorExtractor extends Extractor<Author> {

    private static final AuthorExtractor INSTANCE = new AuthorExtractor();

    public static AuthorExtractor instance() {
        return INSTANCE;
    }

    @Override
    protected Author byC2CMessageCreate(JsonNode data) {
        JsonNode author = data.get("author");
        return new Author(author.get("user_openid").textValue(), "", "", false);
    }

    @Override
    protected Author byGroupAtMessageCreate(JsonNode data) {
        JsonNode author = data.get("author");
        return new Author(author.get("member_openid").textValue(), "", "", false);
    }

    @Override
    protected Author byMessageCreate(JsonNode data) {
        JsonNode author = data.get("author");
        return new Author(
                author.get("union_openid").textValue(),
                author.get("avatar").textValue(),
                author.get("username").textValue(),
                author.has("bot") && author.get("bot").booleanValue()
        );
    }

    @Override
    protected Author byAtMessageCreate(JsonNode data) {
        return byMessageCreate(data);
    }

    @Override
    protected Author byDirectMessageCreate(JsonNode data) {
        return byMessageCreate(data);
    }

}
