package cc.rapidev.qqbot.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author leibrother
 */
public class Config {

    private final Properties properties = new Properties();

    public static Config empty() {
        return new Config();
    }

    public void loadProperties(Properties properties) {
        if (properties != null) {
            this.properties.putAll(properties);
        }
    }

    public void loadFromClasspath(String filename) {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream(filename)) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config file: " + filename, e);
        }
    }

    /**
     * 设置属性值
     *
     * @param key   属性键
     * @param value 属性值
     */
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    /**
     * 获取属性值
     * 优先级为：系统属性 > 属性文件 > 环境变量
     *
     * @param key 属性键
     * @return 属性值
     */
    public String getProperty(String key) {
        // 从系统属性获取
        String systemProperty = System.getProperty(key);
        if (systemProperty != null) return systemProperty;
        // 从属性文件获取
        String property = properties.getProperty(key);
        if (property != null) return property;
        // 从环境变量获取
        return System.getenv(key);
    }

    /**
     * 获取属性值
     * 如果未找到则返回默认值
     *
     * @param key          属性键
     * @param defaultValue 默认值
     * @return 属性值或默认值
     */
    public String getProperty(String key, String defaultValue) {
        String property = this.getProperty(key);
        if (property != null) {
            return property;
        }
        return defaultValue;
    }

    /**
     * 获取属性值并转为int类型
     *
     * @param key          属性键
     * @param defaultValue 默认值
     * @return 属性值或默认值
     */
    public int getPropertyAsInt(String key, int defaultValue) {
        String property = this.getProperty(key);
        if (property == null) {
            return defaultValue;
        }
        return Integer.parseInt(property);
    }

    /**
     * 获取属性值并转为boolean类型
     *
     * @param key          属性键
     * @param defaultValue 默认值
     * @return 属性值或默认值
     */
    public boolean getPropertyAsBoolean(String key, boolean defaultValue) {
        String property = this.getProperty(key);
        if (property == null) {
            return defaultValue;
        }
        return switch (property.toLowerCase()) {
            case "1", "true", "y", "yes" -> true;
            default -> false;
        };
    }

}
