package cc.rapidev.qqbot.common.request;

import cc.rapidev.qqbot.common.request.wrapper.JsonRequestWrapper;
import cc.rapidev.qqbot.common.request.wrapper.RequestWrapper;
import cc.rapidev.qqbot.common.utils.StringUtils;
import com.fasterxml.jackson.databind.JsonNode;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * @author leibrother
 */
public class Requester {

    private final static Requester INSTANCE = new Requester();
    public final static HttpMethod GET = HttpMethod.GET;
    public final static HttpMethod PUT = HttpMethod.PUT;
    public final static HttpMethod POST = HttpMethod.POST;
    public final static HttpMethod DELETE = HttpMethod.DELETE;


    public static Requester getInstance() {
        return INSTANCE;
    }

    private final OkHttpClient client;

    private Requester() {
        this(new OkHttpClient.Builder().build());
    }

    public Requester(OkHttpClient client) {
        this.client = Objects.requireNonNull(client);
    }

    public HttpUrl url(String scheme, String host, int port, String path, Map<String, String> parameters) {
        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme(scheme);
        builder.host(host);
        if (port != -1 && port != 80) {
            builder.port(port);
        }
        if (StringUtils.isNotEmpty(path)) {
            if (path.startsWith("/")) {
                builder.addPathSegments(path.substring(1));
            } else {
                builder.addPathSegments(path);
            }
        }
        if (parameters != null && !parameters.isEmpty()) {
            parameters.forEach(builder::addQueryParameter);
        }
        return builder.build();
    }

    public HttpUrl http(String host, String path, Map<String, String> parameters) {
        return url("http", host, -1, path, parameters);
    }

    public HttpUrl http(String host, String path) {
        return url("http", host, -1, path, null);
    }

    public HttpUrl https(String host, String path, Map<String, String> parameters) {
        return url("https", host, -1, path, parameters);
    }

    public HttpUrl https(String host, String path) {
        return url("https", host, -1, path, null);
    }

    public ResponseBody request(HttpUrl url, HttpMethod method, RequestBody body, Headers headers) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (GET.equals(method)) {
            builder.get();
        } else {
            builder.method(method.name(), body);
        }
        if (headers != null) {
            builder.headers(headers);
        }
        Request request = builder.build();
        try {
            @SuppressWarnings("resource")
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                throw new RuntimeException("request failed, code:" + response.code());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <W extends RequestWrapper<R>, R> Wrap<W, R> wrap(W wrapper) {
        return new Wrap<>() {
            @Override
            public R get(HttpUrl url) {
                return wrapper.extractResponseBody(request(url, GET, null, null));
            }

            @Override
            public R put(HttpUrl url, Object data) {
                RequestBody body = wrapper.wrapperRequestBody(data);
                return wrapper.extractResponseBody(request(url, PUT, body, null));
            }

            @Override
            public R post(HttpUrl url, Object data) {
                RequestBody body = wrapper.wrapperRequestBody(data);
                return wrapper.extractResponseBody(request(url, POST, body, null));
            }

            @Override
            public R delete(HttpUrl url) {
                return wrapper.extractResponseBody(request(url, DELETE, null, null));
            }
        };
    }

    public Wrap<JsonRequestWrapper, JsonNode> json() {
        return wrap(JsonRequestWrapper.getInstance());
    }

}
