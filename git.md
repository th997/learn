# git

* 保存密码. 在.git/config中添加:
```
  [credential]
  helper = store

或者命令,注意会明文保存
git config --global credential.helper store
```

## 代理设置和取消
git config --global http.proxy http://127.0.0.1:1080

git config --global https.proxy http://127.0.0.1:1080

git config --global --unset http.proxy

git config --global --unset https.proxy

## git fork golang 
git remote add fork https://github.com/th997/dog-tunnel

git push fork

## win to linux
git config --global core.autocrlf false

## vimdiff
```
git config --global diff.tool vimdiff
git config --global difftool.prompt false
git config --global alias.d difftool
git d  <commit1> <commit2>
```
quit :qa

vimdiff : https://www.cnblogs.com/xuxm2007/archive/2010/10/22/1858139.html

## git delete history
```
git checkout --orphan latest_branch
git add -A
git commit -am "add"
git branch -D master
git branch -m master
git push -f origin master
```

## pull on working
git stash
git pull
git stash pop

## git fork update
git remote add upstream https://github.com/xxx/xxx.git
git fetch upstream
git merge upstream/master


## git 提交到上次修改
git commit -a --amend
git push -f 

## 放弃本地
git fetch --all
git reset --hard origin/master
git pull


