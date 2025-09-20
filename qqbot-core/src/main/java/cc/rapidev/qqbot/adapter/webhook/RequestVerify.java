package cc.rapidev.qqbot.adapter.webhook;

import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import org.bouncycastle.util.encoders.Hex;

/**
 * @author leibrother
 */
public class RequestVerify {

    private static final int size = 32;

    private static byte[] seed(String secret) {
        var seed = secret;
        while (seed.length() < size) {
            seed += seed;
        }
        return seed.substring(0, size).getBytes();
    }

    public static String verify(String secret, String timestamp, String token) {
        byte[] seed = seed(secret);
        byte[] bytes = (timestamp + token).getBytes();
        Ed25519PrivateKeyParameters parameters = new Ed25519PrivateKeyParameters(seed);
        return sign(parameters, bytes);
    }

    public static String sign(Ed25519PrivateKeyParameters privateKey, byte[] content) {
        Ed25519Signer signer = new Ed25519Signer();
        signer.init(true, privateKey);
        signer.update(content, 0, content.length);
        byte[] signature = signer.generateSignature();
        return Hex.toHexString(signature);
    }

}
