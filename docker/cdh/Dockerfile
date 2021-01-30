FROM th9976/ubu1804:1.0

# ssh
RUN ssh-keygen -t rsa -f ~/.ssh/id_rsa -P '' && \
cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
COPY ssh_config  /root/.ssh/config
RUN chmod 600 ~/.ssh/config

# env
ENV CDH_HOME=/usr/local/cdh
ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
ENV CLASSPATH=.:$JAVA_HOME/jre/lib:$JAVA_HOME/lib:$JAVA_HOME/lib/tools.jar
ENV PATH=$PATH:$JAVA_HOME/bin
ENV USER=root


# add file
ADD cloudera-manager-xenial-cm5.16.2_amd64.tar.gz /usr/local/
RUN ln -s /usr/local/cm-5.16.2 $CDH_HOME  && mkdir -p /opt/cloudera/parcel-repo/
COPY mysql-connector-java-5.1.47.jar $CDH_HOME/share/cmf/lib/
COPY parcel /opt/cloudera/parcel-repo
COPY etc $CDH_HOME/etc/
RUN useradd --system --home=$CDH_HOME --no-create-home --shell=/bin/false --comment "Cloudera SCM User" cloudera-scm
COPY entrypoint.sh $CDH_HOME/entrypoint.sh
RUN chmod a+x $CDH_HOME/entrypoint.sh

# pre ...
WORKDIR $CDH_HOME
EXPOSE 7180

# start 
ENTRYPOINT ["/usr/local/cdh/entrypoint.sh"]
CMD [ "sh", "-c", "bash"]

