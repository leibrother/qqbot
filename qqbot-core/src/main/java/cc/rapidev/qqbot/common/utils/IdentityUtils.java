package cc.rapidev.qqbot.common.utils;

import java.math.BigInteger;
import java.util.UUID;

/**
 * @author leibrother
 */
public class IdentityUtils {

    /**
     * 将16进制的UUID转为62进制实现较短的唯一ID
     */
    public static String shortID() {
        UUID uuid = UUID.randomUUID();
        String hex = uuid.toString().replace("-", "");
        BigInteger number = RadixUtils.formHEX(hex);
        return RadixUtils.toBase62(number);
    }

}
