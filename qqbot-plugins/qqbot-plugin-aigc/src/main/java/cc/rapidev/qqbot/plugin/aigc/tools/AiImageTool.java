package cc.rapidev.qqbot.plugin.aigc.tools;

import cc.rapidev.qqbot.api.model.MessageMedia;
import cc.rapidev.qqbot.common.utils.JsonUtils;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.plugin.aigc.client.AiImageClient;
import com.fasterxml.jackson.databind.JsonNode;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;

/**
 * @author leibrother
 */
public class AiImageTool implements AiTool {

    private final AiImageClient client;

    public AiImageTool(AiImageClient client) {
        this.client = client;
    }

    @Override
    public ToolSpecification specification() {
        return ToolSpecification.builder()
                .name("ai_image")
                .description("根据描述，向用户发送一张近似的图片")
                .parameters(
                        JsonObjectSchema.builder()
                                .addStringProperty("description", "图片描述，如：风格、色彩、内容等")
                                .build()
                )
                .build();
    }

    @Override
    public String invoke(MessageContext context, String arguments) {
        JsonNode node = JsonUtils.fromJson(arguments, JsonNode.class);
        String description = node.get("description").asText();
        if (description != null) {
            Image image = client.generate(description);
            context.reply(MessageMedia.image(image.url().toString()));
            return "success";
        }
        return "fail";
    }

}
