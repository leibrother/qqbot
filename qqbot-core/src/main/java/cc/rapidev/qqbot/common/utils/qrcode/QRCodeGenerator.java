package cc.rapidev.qqbot.common.utils.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * @author leibrother
 */
public class QRCodeGenerator {

    public static BufferedImage generate(
            String content,
            int width,
            int height,
            int margin
    ) throws WriterException {
        return generate(content, width, height, margin, ErrorCorrectionLevel.L, Color.BLACK, Color.WHITE);
    }

    public static BufferedImage generate(
            String content,
            int width,
            int height,
            int margin,
            ErrorCorrectionLevel errorLevel,
            Color foreground,
            Color background
    ) throws WriterException {
        // 1. 设置编码参数
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");       // 字符集
        hints.put(EncodeHintType.MARGIN, margin);               // 边距
        hints.put(EncodeHintType.ERROR_CORRECTION, errorLevel); // 纠错等级
        // 2. 生成Matrix
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        // 3. 自定义颜色
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? foreground.getRGB() : background.getRGB());
            }
        }
        return image;
    }

}
