package cc.rapidev.qqbot.common;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 订阅事件
 *
 * @author leibrother
 * @see <a href="https://bot.q.qq.com/wiki/develop/api-v2/dev-prepare/interface-framework/event-emit.html#事件订阅Intents">事件订阅
 */
public class Intents {

    @Getter
    public enum IntentValue {
        // Framework Intent
        BOT(0),
        // QQBot Intent
        GUILDS(1),
        GUILD_MEMBERS(1 << 1),
        GUILD_MESSAGES(1 << 9),
        GUILD_MESSAGE_REACTIONS(1 << 10),
        DIRECT_MESSAGE(1 << 12),
        GROUP_AND_C2C_EVENT(1 << 25),
        INTERACTION(1 << 26),
        MESSAGE_AUDIT(1 << 27),
        FORUMS_EVENT(1 << 28),
        AUDIO_ACTION(1 << 29),
        PUBLIC_GUILD_MESSAGES(1 << 30);

        private final int value;

        IntentValue(int value) {
            this.value = value;
        }

    }

    private final List<IntentValue> intentValues = new ArrayList<>();

    public Intents(IntentValue... values) {
        this.intentValues.addAll(Arrays.asList(values));
    }

    public Intents add(IntentValue value) {
        this.intentValues.add(value);
        return this;
    }

    public Intents remove(IntentValue value) {
        this.intentValues.remove(value);
        return this;
    }

    public static Intents all() {
        return new Intents(IntentValue.values());
    }

    public static Intents publics() {
        return new Intents(
                IntentValue.GUILDS,
                IntentValue.GUILD_MEMBERS,
                IntentValue.GUILD_MESSAGE_REACTIONS,
                IntentValue.DIRECT_MESSAGE,
                IntentValue.GROUP_AND_C2C_EVENT,
                IntentValue.INTERACTION,
                IntentValue.MESSAGE_AUDIT,
                IntentValue.AUDIO_ACTION,
                IntentValue.PUBLIC_GUILD_MESSAGES
        );
    }

    public static Intents privates() {
        return new Intents(
                IntentValue.GUILDS,
                IntentValue.GUILD_MEMBERS,
                IntentValue.GUILD_MESSAGES,
                IntentValue.GUILD_MESSAGE_REACTIONS,
                IntentValue.DIRECT_MESSAGE,
                IntentValue.GROUP_AND_C2C_EVENT,
                IntentValue.INTERACTION,
                IntentValue.MESSAGE_AUDIT,
                IntentValue.FORUMS_EVENT,
                IntentValue.AUDIO_ACTION
        );
    }

    public int value() {
        int value = 0;
        for (IntentValue intentValue : this.intentValues) {
            value = value | intentValue.value;
        }
        return value;
    }

}
