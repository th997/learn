# 查看更新软件
softwareupdate --list

softwareupdate --install --all

# 安装 brew
/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"

brew -v


# 替换brew源 (https://lug.ustc.edu.cn/wiki/mirrors/help/brew.git)
```
cd "$(brew --repo)"
git remote set-url origin https://mirrors.ustc.edu.cn/brew.git
cd "$(brew --repo)/Library/Taps/homebrew/homebrew-core"
git remote set-url origin https://mirrors.ustc.edu.cn/homebrew-core.git
brew update
```

# 查看内存
top -l 1 | head -n 10 | grep PhysMem
vm_stat

# 添加开机启动
cd /Library/LaunchAgents

vim dl_my.plist
```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple Computer//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
   <key>Label</key>
   <string>dl_my</string>
   <key>ProgramArguments</key>
   <array><string>/Volumes/d/Program/dl/mac_my.sh</string></array>
   <key>RunAtLoad</key>
   <true/>
</dict>
</plist>
```
aunchctl load dl_my.plist

aunchctl start dl_my

aunchctl stop dl_my

aunchctl remove dl_my

# 磁盘挂载
diskutil list

sudo mount_ntfs -o rw,nobrowse /dev/disk0s5 /Volumes/f/

sudo mount_ntfs -o rw,nobrowse /dev/disk0s6 /Volumes/g/

# 目录授权给th
sudo chown th /tmp

# 查看监听端口
netstat -ant | grep LISTEN

# ext4 
```
https://github.com/alperakcan/fuse-ext2
install fuse -- https://osxfuse.github.io/
isstall fuse-ext2 -- sudo .. script
fdiskutil list 
sudo mount -t fuse-ext2 /dev/disk2s2 /Volumes/ubu
```

# profile
```
vim .bash_profile
xxx
vim .zshrc
source /Users/xx/.bash_profile
```

# 多线程下载
brew install aria2c

alias d='aria2c -x 8'

# ubuntu下安装黑苹果
```
1 dmg转img
dmg2img xxx.dmg
2 U盘制作
dd if=/xxx.dmg of=/dev/sdx
3 U盘启动,磁盘管理格式化,安装,重启几次..
4 复制U盘ClOVER文件夹到 EFI目录下 
5 用easyefi工具,添加efi启动文件ClOVER/ClOVER64.efi
6 修复config.plist文件,添加ubuntu启动项目
<key>Entries</key>
<array>
   <dict>
      <key>Disabled</key>
      <false/>
      <key>Ignore</key>
      <false/>
      <key>Path</key>
      <string>\EFI\ubuntu\grubx64.efi</string>
      <key>Title</key>
      <string>Ubuntu</string>
      <key>Type</key>
      <string>Linux</string>
      <key>VolumeType</key>
      <string>Internal</string>
   </dict>
</array>
```
