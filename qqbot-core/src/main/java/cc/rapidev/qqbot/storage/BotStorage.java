package cc.rapidev.qqbot.storage;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.common.utils.qrcode.QRCodeGenerator;
import cc.rapidev.qqbot.storage.service.LocalStorageService;
import cc.rapidev.qqbot.storage.service.StorageService;
import com.google.zxing.WriterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.util.Optional;

/**
 * 机器人存储模块
 *
 * @author leibrother
 */
public class BotStorage {

    private final Logger logger = LoggerFactory.getLogger(BotStorage.class);
    private final Bot bot;

    public BotStorage(Bot bot) {
        this.bot = bot;
        // 本地存储服务
        if (bot.server().accessibleUri().isEmpty()) {
            logger.warn("由于未配置可访问URI，本地存储服务未启用");
        } else {
            this.bot.add(new LocalStorageService(bot.server(), bot.datadir()));
        }
    }

    private StorageService getStorageService() {
        Optional<StorageService> optional = this.bot.get(StorageService.class);
        if (optional.isEmpty()) {
            throw new IllegalStateException("无可用的存储服务");
        }
        return optional.get();
    }

    public URI put(File file) throws IOException {
        StorageService storage = getStorageService();
        String key = storage.put(file);
        return storage.resolve(key);
    }

    public URI put(InputStream stream) throws IOException {
        StorageService storage = getStorageService();
        String key = storage.put(stream);
        return storage.resolve(key);
    }

    public URI createQRCode(String link) {
        try {
            BufferedImage image = QRCodeGenerator.generate(link, 300, 300, 1);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image, "png", out);
            ByteArrayInputStream stream = new ByteArrayInputStream(out.toByteArray());
            return put(stream);
        } catch (WriterException | IOException e) {
            logger.error("生成二维码失败", e);
            throw new RuntimeException(e);
        }
    }

}
