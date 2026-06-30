package cc.rapidev.qqbot.common.request;

import cc.rapidev.qqbot.common.request.wrapper.RequestWrapper;
import okhttp3.HttpUrl;

/**
 * @author leibrother
 */
public interface Wrap<W extends RequestWrapper<R>, R> {

    R get(HttpUrl url);

    R put(HttpUrl url, Object data);

    R post(HttpUrl url, Object data);

    R delete(HttpUrl url);

}
