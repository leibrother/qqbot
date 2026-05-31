package cc.rapidev.qqbot.agent.model;

/**
 * @author leibrother
 */
public record ModelValues(
        ApiFormat apiFormat,
        String baseUrl,
        String apiKey,
        String modelName
) {

}
