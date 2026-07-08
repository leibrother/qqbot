package cc.rapidev.qqbot.plugin.s3.service;

import cc.rapidev.qqbot.common.utils.DigestUtils;
import cc.rapidev.qqbot.storage.service.StorageService;
import org.apache.tika.Tika;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author leibrother
 */
public class S3StorageService implements StorageService {

    private final Tika tika = new Tika();
    private final DateTimeFormatter datePathFormatter = DateTimeFormatter.ofPattern("yyyyMM/dd");
    private final S3Client client;
    private final String bucket;

    public S3StorageService(S3Client client, String bucket) {
        this.client = client;
        this.bucket = bucket;
    }

    public void test() {
        try {
            String key = this.put("this is a test file".getBytes());
            resolve(key);
            this.delete(key);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public URI resolve(String key) {
        GetUrlRequest request = GetUrlRequest.builder()
                .bucket(this.bucket)
                .key(key)
                .build();
        URL url = client.utilities().getUrl(request);
        try {
            return url.toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
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
        String mimetype = tika.detect(bytes);
        String md5 = DigestUtils.digest(bytes, "MD5");
        String key = LocalDate.now().format(this.datePathFormatter) + "/" + md5;
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(this.bucket)
                .key(key)
                .contentType(mimetype)
                .build();
        long size = bytes.length;
        try (InputStream stream = new ByteArrayInputStream(bytes)) {
            RequestBody body = RequestBody.fromInputStream(stream, size);
            this.client.putObject(request, body);
            return key;
        }
    }

    public void delete(String key) {
        this.client.deleteObject(builder -> builder.bucket(this.bucket).key(key));
    }

}
