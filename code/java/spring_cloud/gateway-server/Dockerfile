FROM openjdk:8-jdk
# FROM openjdk:8-jre-alpine  # not firendly while set timezone
ENV TZ=Asia/Shanghai
EXPOSE 31003
ARG JAR_FILE
ADD target/${JAR_FILE} /app.jar
ENV JAVA_OPTS="\
-XX:+UnlockExperimentalVMOptions \
-XX:+UseCGroupMemoryLimitForHeap \
-Djava.security.egd=file:/dev/./urandom \
"
ENTRYPOINT java ${JAVA_OPTS_USER} ${JAVA_OPTS}   -jar /app.jar