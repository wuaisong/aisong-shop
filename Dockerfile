# 基础镜像
FROM openjdk:8u302-jdk
# 作者信息
MAINTAINER wuaisogn
# 容器目录分配数据卷
# VOLUME /tmp
# 设置时区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone
# 拷贝jar 把可执行jar包复制到基础镜像的根目录下
ADD /root/workspace/aisong-shop/api/target/aisong-shop.jar /aisong-shop.jar
# 设置暴露的端口号
EXPOSE 8082 8082
# 在镜像运行为容器后执行的命令
ENTRYPOINT ["java","-jar","aisong-shop.jar","-XX:+UnlockExperimentalVMOptions","XX:+UseCGroupMemoryLimitForHeap"]
