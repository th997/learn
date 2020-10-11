# cdh

https://blog.51cto.com/hsbxxl/2117405

http://archive.cloudera.com/cm5/cm/5

http://archive.cloudera.com/cdh5/parcels/latest/


tar -zxvf ..tar.gz

cp mysql-connector-java-5.1.45-bin.jar share/cmf/lib/

share/cmf/schema/scm_prepare_database.sh  -uroot -pmysql_666888 -h10.10.10.106 -P53306 --scm-host localhost mysql cdh root

// create database cdh CHARACTER SET utf8 COLLATE utf8_general_ci;

etc/init.d/cloudera-scm-server start

etc/init.d/cloudera-scm-agent start  ？

pip uninstall setuptools
#rm -rf /usr/lib/python2.7/dist-packages/setuptools*
pip install setuptools==19.2

异常看日志
ls log/cloudera-scm-agent/

http://localhost:7180
admin admin 

fail!

# docker images 
docker run --hostname=quickstart.cloudera --privileged=true -t -i -p 8888:8888 -p 80:80 -p 7180:7180 cloudera/quickstart /usr/bin/docker-quickstart
http://localhost:8888
