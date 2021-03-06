# angular

# install 

## install or update node
sudo apt-get install nodejs-dev
sudo apt-get install npm
node -v
npm -v
sudo npm cache clean -f
sudo npm install -g n
sudo n stable

## install angular
npm install -g @angular/cli
ng new test1 --strict
cd test1 
ng serve

if error (
sudo vim /etc/sysctl.conf
fs.inotify.max_user_watches=524288
sudo sysctl -p
)

## 