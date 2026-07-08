package cc.rapidev.qqbot.storage.service;

import cc.rapidev.qqbot.common.utils.DigestUtils;
import cc.rapidev.qqbot.server.BotServer;
import io.vertx.ext.web.handler.StaticHandler;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * <h1>本地存储服务</h1>
 * <p>文件会存放在数据目录下的{@code storage}文件夹中</p>
 * <p>需要配置可访问路径，因为机器人发送文件需要外网访问地址</p>
 * <p>会开放{@code /storage/*}端点用于外部访问</p>
 *
 * @author leibrother
 */
public class LocalStorageService implements StorageService {

    private final Logger logger = LoggerFactory.getLogger(LocalStorageService.class);
    private final Tika tika = new Tika();
    private final MimeTypes mimetypes = MimeTypes.getDefaultMimeTypes();
    private final DateTimeFormatter datePathFormatter = DateTimeFormatter.ofPattern("yyyyMM/dd");
    private final URI accessibleUri;
    private final Path rootDirectory;

    public LocalStorageService(BotServer server, Path datadir) {
        URI accessible = server.accessibleUri().orElseThrow(() -> new IllegalStateException("未配置可访问URI"));
        this.accessibleUri = accessible.resolve("/storage/");
        try {
            this.rootDirectory = Files.createDirectories(datadir.resolve("storage"));
        } catch (IOException e) {
            logger.error("创建存储主目录失败", e);
            throw new RuntimeException(e);
        }
        server.route("/storage/*").handler(StaticHandler.create(this.rootDirectory.toString()));
    }

    private MimeType getMimetype(InputStream stream) throws IOException {
        String mimetypeStr = tika.detect(stream);
        try {
            return mimetypes.forName(mimetypeStr);
        } catch (MimeTypeException e) {
            try {
                return mimetypes.forName(MimeTypes.OCTET_STREAM);
            } catch (MimeTypeException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private String todayPath() throws IOException {
        String path = datePathFormatter.format(LocalDate.now());
        Files.createDirectories(this.rootDirectory.resolve(path));
        return path;
    }

    @Override
    public URI resolve(String key) {
        return this.accessibleUri.resolve(key);
    }

    @Override
    public String put(File file) throws IOException {
        return put(Files.newInputStream(file.toPath()));
    }

    @Override
    public String put(InputStream stream) throws IOException {
        return put(stream.readAllBytes());
    }

    public String put(byte[] bytes) throws IOException {
        MimeType mimetype;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
            mimetype = getMimetype(bais);
        }
        String ext = mimetype.getExtension();
        String md5 = DigestUtils.digest(bytes, "MD5");
        String path = todayPath();
        String location = "%s/%s%s".formatted(path, md5, ext);
        File file = this.rootDirectory.resolve(location).toFile();
        try (FileOutputStream out = new FileOutputStream(file)) {
            out.write(bytes);
        }
        return location;
    }


}
