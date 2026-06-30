package cc.rapidev.qqbot.common.request;

import okhttp3.HttpUrl;

import java.util.Objects;

/**
 * @author leibrother
 */
public class ApiUrl {

    private final HttpUrl.Builder builder;

    public ApiUrl() {
        this.builder = new HttpUrl.Builder();
    }

    public ApiUrl(String baseUrl) {
        this.builder = Objects.requireNonNull(HttpUrl.parse(baseUrl)).newBuilder();
    }

    public static ApiUrl create() {
        return new ApiUrl();
    }

    public static ApiUrl create(String baseUrl) {
        return new ApiUrl(baseUrl);
    }

    public ApiUrl path(String path, String... args) {
        String template = path;
        for (int i = 0; i < args.length; i++) {
            String placeholder = "{" + i + "}";
            template = template.replace(placeholder, String.valueOf(args[i]));
        }
        this.builder.encodedPath(template);
        return this;
    }

    public ApiUrl addQueryParameter(String name, String value) {
        this.builder.addQueryParameter(name, value);
        return this;
    }

    public HttpUrl build() {
        return this.builder.build();
    }

}
