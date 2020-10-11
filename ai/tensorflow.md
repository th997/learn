# tensorflow 

## install
https://www.tensorflow.org/install

## install on docker 
https://hub.docker.com/r/tensorflow/tensorflow

docker pull tensorflow/tensorflow:latest-gpu-py3-jupyter

docker run --gpus all -itd --rm --name tensorflow  -v $(realpath ~/temp):/tf/notebooks -p 50188:8888 tensorflow/tensorflow:latest-gpu-py3-jupyter

http://localhost:50188

!pip --proxy=http://10.10.10.106:1080 install captcha

### get tocken or set password
docker exec -it tensorflow /bin/bash

jupyter notebook list

jupyter notebook --generate-config

jupyter notebook password

docker restart tensorflow

## install on conda
https://www.anaconda.com/distribution/#download-section

download https://repo.anaconda.com/archive/Anaconda3-2019.10-Linux-x86_64.sh
```
sh xxx.sh
conda create --name tfgpu2 python=3.7
conda activate tfgpu2
pip install tensorflow-gpu
pip install numpy scipy scikit-learn 
pip install captcha
```

conda cmd
```
conda list
conda env list
conda activate venv
conda deactivate 
conda create --name venv1 --clone venv
conda remove --name venv --all
conda install cudnn cudatoolkit numba
conda install opencv 
```

## hello world
```python
import tensorflow as tf
A = tf.constant([[1,2],[3,4]])
B = tf.constant([[5,6],[7,8]])
C = tf.matmul(A,B)
print(C)
D = tf.add(A,B)
print(D)
```

### vscode 
``` json
{
  "python.pythonPath": "~/anaconda3/envs/venv/bin/python3",
}
```

## 

## tensorflow serving
docker pull tensorflow/serving

git clone https://github.com/tensorflow/serving

docker run -itd --rm -p 8501:8501  -v "/home/th/project/serving/tensorflow_serving/servables/tensorflow/testdata/saved_model_half_plus_two_cpu:/models/half_plus_two"   -e MODEL_NAME=half_plus_two   tensorflow/serving

curl -d '{"instances": [1.0, 2.0, 5.0]}'  -X POST http://localhost:8501/v1/models/half_plus_two:predict

## tensorflow api
https://www.tensorflow.org/api_docs/python/tf/

## GPU
https://medium.com/@Oysiyl/install-tensorflow-2-with-gpu-support-on-ubuntu-19-10-f502ae85593c
#https://www.tensorflow.org/install/gpu
#https://blog.csdn.net/wf19930209/article/details/95332984#NVIDIA_10
#https://blog.csdn.net/qq_35976351/article/details/89178917

python3 -c "import tensorflow as tf; print('Num GPUs Available:', len(tf.config.experimental.list_physical_devices('GPU')))"
