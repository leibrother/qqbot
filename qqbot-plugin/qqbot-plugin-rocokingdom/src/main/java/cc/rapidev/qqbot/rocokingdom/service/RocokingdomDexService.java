package cc.rapidev.qqbot.rocokingdom.service;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.common.request.Requester;
import cc.rapidev.qqbot.rocokingdom.repository.RocokingdomDexRepository;
import cc.rapidev.qqbot.rocokingdom.repository.entity.RocokingdomDex;
import com.fasterxml.jackson.databind.JsonNode;
import okhttp3.HttpUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author leibrother
 */
public class RocokingdomDexService {

    private final Logger logger = LoggerFactory.getLogger(RocokingdomDexService.class);
    private final RocokingdomDexRepository repository;

    public RocokingdomDexService(Bot bot) {
        boolean sync = !bot.database().tableIfExists("bot_rocokingdom_dex");
        this.repository = new RocokingdomDexRepository(bot.database());
        if (sync) {
            this.sync();
        }
    }

    public void sync() {
        logger.info("开始同步精灵图鉴...");
        Requester requester = Requester.getInstance();
        HttpUrl url = HttpUrl.get("https://static.gamecenter.qq.com/xgame/roco-kingdom/compendium/d.json");
        JsonNode node = requester.json().get(url);
        JsonNode list = node.get("l");
        JsonNode details = node.get("d");
        list.elements().forEachRemaining(item -> {
            int id = item.get("i").intValue();
            JsonNode detail = details.get(String.valueOf(id));
            RocokingdomDex dex = new RocokingdomDex();
            dex.setId(id);
            dex.setNo(item.get("n").textValue());
            dex.setName(item.get("nm").textValue());
            dex.setForm(item.get("f").textValue());
            dex.setFullname(item.get("fn").textValue());
            dex.setDesc(detail.get("desc").textValue());
            dex.setE1(item.get("e").textValue());
            dex.setE2(item.get("e2").textValue());
            dex.setTxname(detail.get("tn").textValue());
            dex.setTxdesc(detail.get("te").textValue());
            String image = url.newBuilder()
                    .removePathSegment(3)
                    .addPathSegments(item.get("img").textValue())
                    .build()
                    .toString();
            dex.setImage(image);
            dex.setHp(detail.get("hp").intValue());
            dex.setPatk(detail.get("atk").intValue());
            dex.setMatk(detail.get("matk").intValue());
            dex.setPdf(detail.get("df").intValue());
            dex.setMdf(detail.get("mdf").intValue());
            dex.setSpd(detail.get("spd").intValue());
            repository.store(dex);
        });
    }

    public List<RocokingdomDex> findByFullnameLike(String name) {
        return this.repository.findByFullnameLike(name);
    }

}
