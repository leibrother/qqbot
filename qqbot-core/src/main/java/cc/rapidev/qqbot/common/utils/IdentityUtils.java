package cc.rapidev.qqbot.common.utils;

import java.math.BigInteger;
import java.util.UUID;

/**
 * @author leibrother
 */
public class IdentityUtils {

    public static String UUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }

    /**
     * 将16进制的UUID转为62进制实现较短的唯一ID
     */
    public static String shortID() {
        BigInteger number = RadixUtils.formHEX(UUID());
        return RadixUtils.toBase62(number);
    }

}
