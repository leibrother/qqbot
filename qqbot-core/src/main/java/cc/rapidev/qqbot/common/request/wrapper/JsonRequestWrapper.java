package cc.rapidev.qqbot.common.request.wrapper;

import cc.rapidev.qqbot.common.utils.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import java.io.IOException;

/**
 * @author leibrother
 */
public class JsonRequestWrapper implements RequestWrapper<JsonNode> {

    private static final JsonRequestWrapper INSTANCE = new JsonRequestWrapper();

    public static JsonRequestWrapper getInstance() {
        return INSTANCE;
    }

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    @Override
    public RequestBody wrapperRequestBody(Object object) {
        String json = JsonUtils.toJson(object);
        return RequestBody.create(json, MEDIA_TYPE);
    }

    @Override
    public JsonNode extractResponseBody(ResponseBody body) {
        try (body) {
            String string = body.string();
            return JsonUtils.fromJson(string, JsonNode.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
