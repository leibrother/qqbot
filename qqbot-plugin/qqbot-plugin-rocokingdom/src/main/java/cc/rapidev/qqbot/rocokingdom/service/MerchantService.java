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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author leibrother
 */
public class MerchantService {

    public final static int START_HOUR = 8;
    public final static int ROUND_HOUR = 4;
    public final static Set<String> items = new LinkedHashSet<>();

    static {
        items.add("棱镜球");
        items.add("祝福项坠");
        items.add("炫彩精灵蛋");
        items.add("国王球");
        items.add("首领血脉秘药");
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
        data.put("service", this);
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
        return passed / ROUND_HOUR + 1;
    }

    public Optional<Merchant> nowadaysMerchant() {
        int round = nowadaysRound();
        if (round <= 0) {
            return Optional.empty();
        }
        JsonNode data = data();
        String lastUpdate = data.get("lastUpdate").textValue();
        // 如果更新时间早于当前轮开始时间则代表数据未更新
        LocalTime updateTime = LocalTime.parse(lastUpdate, DateTimeFormatter.ISO_DATE_TIME);
        LocalTime shouldTime = LocalTime.of(START_HOUR + (round - 1) * ROUND_HOUR, 0);
        if (updateTime.isBefore(shouldTime)) {
            return Optional.empty();
        }
        // 筛选当前轮的数据并返回
        JsonNode rounds = data.get("rounds");
        List<Merchant> merchants = new ObjectMapper().convertValue(rounds, new TypeReference<>() {
        });
        return merchants.stream().filter(m -> m.round() == round).findFirst();
    }

    public void pushNowadaysRound() {
        int round = nowadaysRound();
        if (round <= 0) {
            return;
        }
        String date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
        MerchantPushLog log = new MerchantPushLog(date, round);
        if (this.pushLogRepository.exists(log)) {
            return;
        }
        PushService pushService = this.bot.use(PushService.class);
        List<Topic> topics = pushService.getOpenTopicList();
        if (topics.isEmpty()) {
            return;
        }
        Optional<Merchant> optional = this.nowadaysMerchant();
        if (optional.isEmpty()) {
            return;
        }
        Merchant merchant = optional.get();
        Message message = new MerchantView(merchant).render();
        List<String> names = merchant.items().stream().map(Merchant.MerchantItem::name).toList();
        SettingPersistenceService persistence = this.bot.use(SettingPersistenceService.class);
        for (Topic topic : topics) {
            Set<String> values = this.setting.getValues(persistence, topic);
            if (!values.stream().filter(names::contains).toList().isEmpty()) {
                pushService.push(topic, message);
                pushService.push(topic, Message.text("远行商人刷新了订阅的物品，快去购买吧~"));
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
            MerchantService service = (MerchantService) data.get("service");
            service.pushNowadaysRound();
        }

    }

}
