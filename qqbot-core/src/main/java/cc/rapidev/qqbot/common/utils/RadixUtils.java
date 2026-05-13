package cc.rapidev.qqbot.common.utils;

import java.math.BigInteger;

/**
 * 基于 BigInteger 的进制转换工具类
 * 支持 2 ~ 62 进制
 */
public class RadixUtils {

    private static final String CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int MIN_RADIX = 2;
    private static final int MAX_RADIX = CHARS.length();

    /**
     * BigInteger 转任意进制（2~62）
     */
    public static String toRadix(BigInteger number, int radix) {
        validateRadix(radix);

        if (number == null) {
            throw new IllegalArgumentException("number cannot be null");
        }

        if (number.equals(BigInteger.ZERO)) {
            return "0";
        }

        boolean negative = number.compareTo(BigInteger.ZERO) < 0;
        number = number.abs();

        StringBuilder sb = new StringBuilder();

        BigInteger base = BigInteger.valueOf(radix);
        while (number.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] result = number.divideAndRemainder(base);
            sb.append(getChar(result[1].intValue()));
            number = result[0];
        }

        String result = sb.reverse().toString();
        return negative ? "-" + result : result;
    }

    /**
     * 任意进制转 BigInteger
     */
    public static BigInteger fromRadix(String value, int radix) {
        validateRadix(radix);

        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("value cannot be empty");
        }

        boolean negative = value.startsWith("-");
        if (negative) {
            value = value.substring(1);
        }

        BigInteger result = BigInteger.ZERO;
        BigInteger base = BigInteger.valueOf(radix);

        for (char c : value.toCharArray()) {
            int digit = getDigit(c, radix);
            result = result.multiply(base).add(BigInteger.valueOf(digit));
        }

        return negative ? result.negate() : result;
    }

    /* ================= 私有方法 ================= */

    private static void validateRadix(int radix) {
        if (radix < MIN_RADIX || radix > MAX_RADIX) {
            throw new IllegalArgumentException(
                    "radix must be between " + MIN_RADIX + " and " + MAX_RADIX
            );
        }
    }

    private static char getChar(int index) {
        return CHARS.charAt(index);
    }

    private static int getDigit(char c, int radix) {
        int index = CHARS.indexOf(c);
        if (index < 0 || index >= radix) {
            throw new IllegalArgumentException("Invalid character: " + c);
        }
        return index;
    }

    public static String toBinary(BigInteger number) {
        return toRadix(number, 2);
    }

    public static BigInteger fromBinary(String value) {
        return fromRadix(value, 2);
    }

    public static String toHEX(BigInteger number) {
        return toRadix(number, 16).toUpperCase();
    }

    public static BigInteger formHEX(String value) {
        return fromRadix(value.toUpperCase(), 16);
    }

    public static String toBase62(BigInteger number) {
        return toRadix(number, 62);
    }

    public static BigInteger fromBase62(String value) {
        return fromRadix(value, 62);
    }

}