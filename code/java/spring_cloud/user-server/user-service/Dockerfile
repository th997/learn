FROM openjdk:8-jre
# FROM openjdk:8-jre-alpine  # not firendly while set timezone
ENV TZ=Asia/Shanghai
EXPOSE 32001
ARG JAR_FILE
ADD target/${JAR_FILE} app.jar
ENV JAVA_OPTS="\
-XX:+UnlockExperimentalVMOptions \
-XX:+UseCGroupMemoryLimitForHeap \
"
ENTRYPOINT java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app.jar