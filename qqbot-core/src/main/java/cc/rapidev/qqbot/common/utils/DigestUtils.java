package cc.rapidev.qqbot.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author leibrother
 */
public class DigestUtils {

    public static String md5(InputStream stream) throws IOException {
        return digest(stream.readAllBytes(), "MD5");
    }

    public static String sha256(InputStream stream) throws IOException {
        return digest(stream.readAllBytes(), "SHA-256");
    }

    public static String sha512(InputStream stream) throws IOException {
        return digest(stream.readAllBytes(), "SHA-512");
    }

    public static String digest(byte[] bytes, String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(bytes);
            return toHEX(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("不支持的算法: " + algorithm, e);
        }
    }

    private static String toHEX(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}
