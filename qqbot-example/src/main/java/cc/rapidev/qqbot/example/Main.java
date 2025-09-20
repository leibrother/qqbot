package cc.rapidev.qqbot.example;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.common.Constant;

/**
 * @author leibrother
 */
public class Main {

    public static void printWelcome() {
        System.out.println("---------------------------------------");
        System.out.println("Welcome Rapidev QQBot");
        System.out.println("Framework Version: " + Constant.version);
        System.out.println("请设置启动参数或者环境变量来配置你的机器人！");
        System.out.println("Appid:\t" + Constant.PROPERTY_APPID);
        System.out.println("Secret:\t" + Constant.PROPERTY_SECRET);
        System.out.println("---------------------------------------");
        System.out.println();
    }

    public static void main(String[] args) {
        printWelcome();
        Bot bot = new Bot();
        bot.run(true);
    }

}
