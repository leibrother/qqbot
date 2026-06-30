package cc.rapidev.qqbot.common.request.wrapper;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * @author leibrother
 */
public interface RequestWrapper<R> {

    RequestBody wrapperRequestBody(Object object);

    R extractResponseBody(ResponseBody body);

}
