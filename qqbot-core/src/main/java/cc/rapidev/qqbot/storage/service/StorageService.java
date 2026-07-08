package cc.rapidev.qqbot.storage.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * 存储服务接口
 *
 * @author leibrother
 */
public interface StorageService {

    /**
     * 存放文件
     *
     * @param file 文件
     * @return 资源标识
     * @throws IOException 可能会发生IO异常
     */
    String put(File file) throws IOException;

    /**
     * 存放流
     *
     * @param stream 输入流
     * @return 资源标识
     * @throws IOException 可能会发生IO异常
     */
    String put(InputStream stream) throws IOException;

    /**
     * 通过资源标识符给出访问路径
     *
     * @param key 资源标识
     * @return 可访问路径
     */
    URI resolve(String key);

}
