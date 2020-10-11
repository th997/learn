FROM openjdk:8

# env
ENV KAFKA_HOME=/usr/local/kafka

# add file
ADD kafka_2.12-2.6.0.tgz  /usr/local/
RUN ln -s /usr/local/kafka_2.12-2.6.0 $KAFKA_HOME 
COPY server.properties $KAFKA_HOME/config/
COPY entrypoint.sh $KAFKA_HOME/entrypoint.sh
RUN chmod a+x $KAFKA_HOME/entrypoint.sh

# pre ...
WORKDIR $KAFKA_HOME
EXPOSE 9092

# start 
ENTRYPOINT ["/usr/local/kafka/entrypoint.sh"]
CMD [ "sh", "-c", "bash"]

