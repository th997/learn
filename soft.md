
## ffmpeg 
ffmpeg -i https://xxx/playlist.m3u8  -threads 4 xxx.mp4

ffmpeg -i https://xxx.com/ppvod/9ED41D9D26B5685A1964EA82B8735259.m3u8  -filter_threads 6 -filter_complex_threads 6 -thread 6 aqyly.mp4

## ari2c 多线程下载 
aria2c -x 8 http://xxx.com/xxx.mp4

##


