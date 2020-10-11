python

install python27

## 安装django
pip install Django==1.9.5

pip install Pillow

## 安装mysql库
pip install MySQL-python

## 创建django工程
django-admin startproject mysite

## 启动django服务
python manage.py runserver

## 创建django应用
python manage.py startapp polls

## django数据库生成
python manage.py makemigrations app1

python manage.py migrate

## django 创建超级管理员
python manage.py createsuperuser

## 同步静态文件
python2.7 manage.py collectstatic

## version
python -m pip install -U "pylint<2.0.0" --user