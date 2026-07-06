package cc.rapidev.qqbot.rocokingdom.service;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.common.request.Requester;
import cc.rapidev.qqbot.extension.command.CommandHandlerSet;
import cc.rapidev.qqbot.extension.command.Keyword;
import cc.rapidev.qqbot.extension.job.JobService;
import cc.rapidev.qqbot.extension.push.PushService;
import cc.rapidev.qqbot.extension.settings.SettingGroup;
import cc.rapidev.qqbot.extension.settings.component.Checkbox;
import cc.rapidev.qqbot.extension.settings.persistence.SettingPersistenceService;
import cc.rapidev.qqbot.rocokingdom.command.MerchantHandler;
import cc.rapidev.qqbot.rocokingdom.model.Merchant;
import cc.rapidev.qqbot.rocokingdom.repository.MerchantPushLogRepository;
import cc.rapidev.qqbot.rocokingdom.repository.entity.MerchantPushLog;
import cc.rapidev.qqbot.rocokingdom.view.MerchantView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author leibrother
 */
public class MerchantService {

    public final static int START_HOUR = 8;
    public final static int ROUND_HOUR = 4;
    public final static Set<String> items = new HashSet<>();

    static {
        items.add("祝福项链");
        items.add("炫彩精灵蛋");
        items.add("神奇的蛋");
        items.add("黑晶琉璃");
        items.add("紫莲刚玉");
    }

    private final Bot bot;
    private final Checkbox setting;
    private final MerchantPushLogRepository pushLogRepository;

    public MerchantService(Bot bot, CommandHandlerSet mainCommand, SettingGroup mainSetting) {
        this.bot = bot;
        this.setting = Checkbox.builder()
                .key("subscribe")
                .name("远行商人订阅")
                .description("选中任意物品即开启订阅（需开启主动推送）")
                .options(items)
                .build();
        this.pushLogRepository = new MerchantPushLogRepository(bot.database());
        // 添加设置项
        mainSetting.append(setting);
        // 注册命令处理器
        mainCommand.add(new Keyword("远行商人", "查询远行商人正在出售的物品"), new MerchantHandler(this));
        // 开启定时推送任务
        JobDataMap data = new JobDataMap();
        data.put("bot", bot);
        data.put("merchant_service", this);
        bot.use(JobService.class).addIntervalJob(Pusher.class, data, 10 * 60);
    }

    private JsonNode data() {
        Requester requester = Requester.getInstance();
        HttpUrl url = requester.https("static.gamecenter.qq.com", "/game_tool/dynamic_backend_data/1110613799-faraway-merchant.json");
        return requester.json().get(url);
    }

    public int nowadaysRound() {
        LocalTime now = LocalTime.now();
        int passed = now.getHour() - START_HOUR;
        if (passed < 0) {
            return -1;
        }
        return (int) Math.ceil((double) passed / ROUND_HOUR);
    }

    public Merchant nowadaysMerchant() {
        int round = nowadaysRound();
        if (round < 0) {
            return null;
        }
        JsonNode data = data();
        JsonNode rounds = data.get("rounds");
        List<Merchant> merchants = new ObjectMapper().convertValue(rounds, new TypeReference<>() {
        });
        return merchants.stream().filter(m -> m.round() == round).findFirst().orElse(null);
    }

    public void pushNowadaysRound() {
        String date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
        int round = nowadaysRound();
        MerchantPushLog log = new MerchantPushLog(date, round);
        if (this.pushLogRepository.exists(log)) {
            return;
        }
        PushService pushService = this.bot.use(PushService.class);
        List<Topic> topics = pushService.getOpenTopicList();
        if (topics.isEmpty()) {
            return;
        }
        Merchant merchant = this.nowadaysMerchant();
        if (merchant == null) {
            return;
        }
        MerchantView view = new MerchantView(merchant);
        Message message = view.render();
        SettingPersistenceService persistence = this.bot.use(SettingPersistenceService.class);
        for (Topic topic : topics) {
            Set<String> values = this.setting.getValues(persistence, topic);
            for (Merchant.MerchantItem item : merchant.items()) {
                if (values.contains(item.name())) {
                    pushService.push(topic, message);
                    break;
                }
            }
        }
        this.pushLogRepository.insert(log);
    }

    /**
     * 推送任务
     */
    public static class Pusher implements Job {

        @Override
        public void execute(JobExecutionContext context) {
            JobDataMap data = context.getJobDetail().getJobDataMap();
            MerchantService service = (MerchantService) data.get("merchant_service");
            service.pushNowadaysRound();
        }

    }

}
