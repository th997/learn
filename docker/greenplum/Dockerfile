FROM ubuntu:18.04 

ADD greenplum-db-6.24.5-ubuntu18.04-amd64.deb . 
RUN apt-get update && apt-get install -y ./greenplum-db-6.24.5-ubuntu18.04-amd64.deb 

RUN apt-get install -y openssh-server sudo

RUN groupadd -g 530 gpadmin && \
useradd -g 530 -G sudo -u 530 -m -d /home/gpadmin -s /bin/bash gpadmin && \
chown -R gpadmin:gpadmin /home/gpadmin && \
echo "gpadmin ALL=(ALL) NOPASSWD:ALL" >> /etc/sudoers 

RUN locale-gen en_US.UTF-8

USER gpadmin
RUN ssh-keygen -t rsa -f ~/.ssh/id_rsa -P '' && \
cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys

COPY --chown=gpadmin:gpadmin ./ssh_config  /home/gpadmin/.ssh/config
COPY ./ssh_config  /root/.ssh/config
RUN chmod 600 ~/.ssh/config
RUN ls -hla ~/.ssh

ENV GPHOME=/usr/local/greenplum-db
ENV PATH=${GPHOME}/bin:${PATH} 

ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8

ENV DATAHOME=/gp
WORKDIR ${DATAHOME}
COPY entrypoint.sh gpinitsystem_config hostfile_gpinitsystem ${DATAHOME}/
ENTRYPOINT ["/gp/entrypoint.sh"]
CMD [ "sh", "-c", "bash"]

EXPOSE 5432

# docker build -t th9976/greenplumdb . --build-arg http_proxy=http://10.10.10.106:1080 --build-arg https_proxy=http://10.10.10.106:1080

# gpinitsystem -c ./gpinitsystem_config -h ./hostfile_gpinitsystem

