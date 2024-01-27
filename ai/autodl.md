# gpu
https://www.autodl.com/console/instance/list?_random_=1702436984824

# export
```shell
export TRANSFORMERS_CACHE=/root/autodl-tmp
alias proxy='source /etc/network_turbo'
alias noproxy='unset http_proxy;unset https_proxy;unset HTTP_PROXY;unset HTTPS_PROXY;unset ALL_PROXY;unset all_proxy'
```

# conda config
```shell
echo """
envs_dirs:                                                                                                                                 
  - /root/autodl-tmp/envs                                                                                                                              
pkgs_dirs:                                                                                                                                 
  - /root/autodl-tmp/pkgs 
""" >> ~/.condarc

mkdir ~/.pip/
echo '''
 [global]
index-url = https://pypi.mirrors.ustc.edu.cn/simple/
''' >  ~/.pip/pip.conf

ln -s /root/autodl-tmp/.cache /root/.cache

conda remove --name gpt --all
conda create --name gpt python=3.9
source activate
conda deactivate
conda activate gpt
```
