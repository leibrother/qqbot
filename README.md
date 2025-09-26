<div style="text-align: center;">
    <h3>QQBot</h3>
    <p>一个QQ机器人快速开发框架</p>
    <div>
        <a style="text-decoration: none" href="https://github.com/leibrother/qqbot/network/members">
            <img src="https://img.shields.io/github/forks/leibrother/qqbot.svg?style=flat-square" alt="forks"/>
        </a>
        <a style="text-decoration: none"  href="https://github.com/leibrother/qqbot/stargazers">
            <img src="https://img.shields.io/github/stars/leibrother/qqbot.svg?style=flat-square" alt="stars"/>
        </a>
        <a style="text-decoration: none"  href="https://github.com/leibrother/qqbot/issues">
            <img src="https://img.shields.io/github/issues/leibrother/qqbot.svg?style=flat-square" alt="issues"/>
        </a>
        <a style="text-decoration: none"  href="https://github.com/leibrother/blob/main/LICENSE">
            <img src="https://img.shields.io/github/license/leibrother/qqbot?style=flat-square" alt="license"/>
        </a>
    </div>
</div>



## 关于项目

本项目为QQ机器人的快速开发框架，基于此框架，可以快速开发出你的定制化机器人！

### 开发环境

项目基于 `JDK 21` 开发，核心依赖为：

* [vertx-web](https://vertx.io/)
* [httpcomponents](https://hc.apache.org/index.html)
* [jackson](https://github.com/FasterXML/jackson)
* [bcprov](https://www.bouncycastle.org/)
* [langchain4j](https://docs.langchain4j.dev/)

## 开始使用

你可以直接运行 `qqbot-example` 模块，或者依赖 `qqbot-core` 模块开发

### 一、运行 `qqbot-example`

首先，在项目根目录执行

```bash
./mvnw clean package
```

然后进入qqbot-example目录，编辑 `qqbot.properties` 文件，这里包含了所有配置项。

最后，执行

```bash
java -jar ./target/qqbot-example.jar --config=./qqbot.properties
```

> 你也可以将其编译为Docker镜像，项目中已经包含了 `Dockerfile` 与 `docker-compose.yml` 文件

### 二、基于 `qqbot-core` 开发

> 可以参考 `qqbot-ai` 模块，这个模块包含了下述的全部内容

你需要依赖 `qqbot-core` 模块

```xml
<dependency>
    <groupId>cc.rapidev</groupId>
    <artifactId>qqbot-core</artifactId>
    <scope>provided</scope>
</dependency>
```

实现 `cc.rapidev.qqbot.message.MessageHandler` 接口，编写你的消息处理器，如：

```java
public class MyMessageHandler implements MessageHandler {

    @Override
    public void handle(MessageContext context) {
        System.out.println("Hello World");
    }

}
```

实现 `cc.rapidev.qqbot.message.MessageHandlerInjector` 接口，注入你的消息处理器，如：

```java
public class MyMessageHandlerInjector implements MessageHandlerInjector {

    @Override
    public void inject(MessageDispatcher dispatcher) {
        MyMessageHandler handler = new MyMessageHandler();
        Events.messageCreateEvents.forEach(event -> dispatcher.register(event, handler));
    }

}
```

在 `resources/META-INF/qqbot.injectors` 文件中声明这个注入器

```
...
org.example.MyMessageHandlerInjector
```

至此，就是全部开发流程，机器人在启动是会根据声明去加载注入器，注入消息处理器，当机器人收到消息时，消息处理器将会被触发

## 贡献

如果你有好的建议，请复刻（fork）本仓库并且创建一个拉取请求（pull request）。你也可以简单地创建一个议题（issue），不要忘记给项目点一个 star！谢谢！

