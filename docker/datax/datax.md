# datax

## install datax
git clone https://github.com/alibaba/DataX

cd DataX

mvn -U clean package assembly:assembly -Dmaven.test.skip=true

mv target/datax/datax ~/soft/

export DATAX_HOME=/home/th/soft/datax

## install datax-web
git clone https://github.com/WeiYe-Jing/datax-web

cd datax-web

mysql : bin/db/datax_web.sql

vim datax-admin application.yml
change db config ...

run DataXAdminApplication

login http://localhost:8080/index.html#/dashboard  admin/123456


vim datax-executort application.yml
change db config ...

run DataXExecutorApplication
