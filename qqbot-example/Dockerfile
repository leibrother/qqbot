FROM openjdk:21
USER root
WORKDIR /qqbot
# 载入JAR
ADD ./target/qqbot-example.jar ./qqbot-example.jar
# 载入默认配置
RUN mkdir ./config
ADD ./qqbot.properties ./config/qqbot.properties
# 设置时区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone
# 暴露端口
EXPOSE 8080
# 启动JAR
CMD ["java","-jar","qqbot-example.jar","--config=./config/qqbot.properties"]
