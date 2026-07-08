package cc.rapidev.qqbot.plugin.s3.service;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.common.utils.StringUtils;
import cc.rapidev.qqbot.extension.settings.SettingGroup;
import cc.rapidev.qqbot.extension.settings.component.Button;
import cc.rapidev.qqbot.extension.settings.component.Input;
import cc.rapidev.qqbot.extension.settings.persistence.SettingPersistenceService;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

/**
 * @author leibrother
 */
public class S3SettingService {

    private final Bot bot;
    private final Input settingEndpoint;
    private final Input settingRegion;
    private final Input settingBucket;
    private final Input settingAccessKey;
    private final Input settingSecretKey;

    public S3SettingService(Bot bot, SettingGroup group) {
        this.bot = bot;
        this.settingEndpoint = Input.builder()
                .key("endpoint")
                .name("端点")
                .description("请设置端点")
                .global()
                .build();
        group.append(this.settingEndpoint);
        this.settingRegion = Input.builder()
                .key("region")
                .name("区域")
                .description("请设置区域")
                .global()
                .build();
        group.append(this.settingRegion);
        this.settingBucket = Input.builder()
                .key("bucket")
                .name("存储桶")
                .description("请设置存储桶")
                .global()
                .build();
        group.append(this.settingBucket);
        this.settingAccessKey = Input.builder()
                .key("access_key")
                .name("AccessKey")
                .description("请设置AccessKey")
                .password()
                .global()
                .build();
        group.append(this.settingAccessKey);
        this.settingSecretKey = Input.builder()
                .key("secret_key")
                .name("SecretKey")
                .description("请设置SecretKey")
                .password()
                .global()
                .build();
        group.append(this.settingSecretKey);
        Button buttonApply = Button.builder()
                .key("btn_apply")
                .name("应用")
                .description("测试连接并应用")
                .onclick((_) -> {
                    try {
                        this.apply();
                        return "已应用";
                    } catch (IllegalArgumentException e) {
                        return e.getMessage();
                    } catch (Exception e) {
                        return "错误:" + e.getMessage();
                    }
                })
                .global()
                .build();
        group.append(buttonApply);
    }

    public void apply() {
        S3StorageService s3StorageService = createS3StorageService();
        s3StorageService.test();
        this.bot.add(s3StorageService);
    }

    private S3StorageService createS3StorageService() {
        SettingPersistenceService persistence = this.bot.use(SettingPersistenceService.class);
        String endpoint = this.settingEndpoint.getValue(persistence, null);
        if (StringUtils.isEmpty(endpoint)) {
            throw new IllegalArgumentException("未设置端点");
        }
        String region = this.settingRegion.getValue(persistence, null);
        if (StringUtils.isEmpty(region)) {
            throw new IllegalArgumentException("未设置区域");
        }
        String bucket = this.settingBucket.getValue(persistence, null);
        if (StringUtils.isEmpty(bucket)) {
            throw new IllegalArgumentException("未设置存储桶");
        }
        String accessKey = this.settingAccessKey.getValue(persistence, null);
        if (StringUtils.isEmpty(accessKey)) {
            throw new IllegalArgumentException("未设置AccessKey");
        }
        String secretKey = this.settingSecretKey.getValue(persistence, null);
        if (StringUtils.isEmpty(accessKey)) {
            throw new IllegalArgumentException("未设置SecretKey");
        }

        // credentials
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        // client
        S3Client client = S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.of(region))
                .build();
        return new S3StorageService(client, bucket);
    }

}
