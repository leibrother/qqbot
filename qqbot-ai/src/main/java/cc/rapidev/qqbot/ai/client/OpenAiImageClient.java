package cc.rapidev.qqbot.ai.client;

import cc.rapidev.qqbot.ai.AiConfig;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.openai.OpenAiImageModel;
import dev.langchain4j.model.output.Response;
import org.jspecify.annotations.NullMarked;

/**
 * @author leibrother
 */
public class OpenAiImageClient implements AiImageClient {

    private final ImageModel imageModel;

    public OpenAiImageClient(AiConfig config) {
        this.imageModel = OpenAiImageModel.builder()
                .baseUrl(config.getImageBaseUrl())
                .apiKey(config.getImageApiKey())
                .modelName(config.getImageModel())
                .build();
    }

    @Override
    @NullMarked
    public Image generate(String text) {
        Response<Image> response = imageModel.generate(text);
        return response.content();
    }

}
