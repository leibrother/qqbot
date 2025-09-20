package cc.rapidev.qqbot.api;

import cc.rapidev.qqbot.common.utils.JsonUtils;
import cc.rapidev.qqbot.common.utils.LogbackUtils;
import ch.qos.logback.classic.Level;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.core5.util.Timeout;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author leibrother
 */
public class RequestHelper {

    private final CloseableHttpClient httpClient;

    public RequestHelper(HttpClientConnectionManager manager, RequestConfig config) {
        LogbackUtils.setLogLevel("org.apache.hc.client5", Level.ERROR);
        HttpClientBuilder builder = HttpClients.custom();
        builder.setConnectionManager(manager);
        builder.setDefaultRequestConfig(config);
        this.httpClient = builder.build();
    }

    public RequestHelper() {
        this(getConnectionManager(200, 50), getRequestConfig(10, 30));
    }

    public static HttpClientConnectionManager getConnectionManager(int maxTotal, int defaultMaxPerRoute) {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(maxTotal);
        connectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
        return connectionManager;
    }

    public static RequestConfig getRequestConfig(int requestTimeout, int responseTimeout) {
        return RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.ofSeconds(requestTimeout))
                .setResponseTimeout(Timeout.ofSeconds(responseTimeout))
                .build();
    }

    public String completion(String path, Object... args) {
        return null;
    }

    public URI uri(String scheme, String host, String path, Map<String, String> parameters) {
        try {
            URIBuilder builder = new URIBuilder().setScheme(scheme).setHost(host).setPath(path);
            if (parameters != null && !parameters.isEmpty()) {
                parameters.forEach(builder::addParameter);
            }
            return builder.build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public URI http(String host, String path, Map<String, String> parameters) {
        return uri("http", host, path, parameters);
    }

    public URI http(String host, String path) {
        return http(host, path, null);
    }

    public URI https(String host, String path, Map<String, String> parameters) {
        return uri("https", host, path, parameters);
    }

    public URI https(String host, String path) {
        return https(host, path, null);
    }

    public <T> T execute(HttpUriRequest request, Map<String, String> headers, Object body, Class<T> clazz) {
        if (headers != null) {
            headers.forEach(request::setHeader);
        }
        if (body != null) {
            String json = JsonUtils.toJson(body);
            request.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
        }
        try {
            return httpClient.execute(request, (response) -> {
                HttpEntity entity = response.getEntity();
                String contentType = entity.getContentType();
                if (contentType.startsWith(ContentType.APPLICATION_JSON.getMimeType())) {
                    String content = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                    return JsonUtils.fromJson(content, clazz);
                }
                return null;
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T doGet(String url, Map<String, String> headers, Class<T> clazz) {
        HttpGet request = new HttpGet(url);
        return execute(request, headers, null, clazz);
    }

    public <T> T doGet(URI uri, Map<String, String> headers, Class<T> clazz) {
        HttpGet request = new HttpGet(uri);
        return execute(request, headers, null, clazz);
    }

    public <T> T doPost(String url, Map<String, String> headers, Object body, Class<T> clazz) {
        HttpPost request = new HttpPost(url);
        return execute(request, headers, body, clazz);
    }

    public <T> T doPost(URI uri, Map<String, String> headers, Object body, Class<T> clazz) {
        HttpPost request = new HttpPost(uri);
        return execute(request, headers, body, clazz);
    }

    public <T> T doPut(String url, Map<String, String> headers, Object body, Class<T> clazz) {
        HttpPut request = new HttpPut(url);
        return execute(request, headers, body, clazz);
    }

    public <T> T doPut(URI uri, Map<String, String> headers, Object body, Class<T> clazz) {
        HttpPut request = new HttpPut(uri);
        return execute(request, headers, body, clazz);
    }

    public <T> T doDelete(String url, Map<String, String> headers, Class<T> clazz) {
        HttpDelete request = new HttpDelete(url);
        return execute(request, headers, null, clazz);
    }

    public <T> T doDelete(URI uri, Map<String, String> headers, Class<T> clazz) {
        HttpDelete request = new HttpDelete(uri);
        return execute(request, headers, null, clazz);
    }

}
