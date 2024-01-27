 # llm

## 常见的大模型和应用场景
https://github.com/HqWu-HITCS/Awesome-Chinese-LLM


## 企业落地方向
**自建服务器训练/部署**
企业应用一般涉及一些机密数据，不适合将这些数据上传到第三方机构，所以可以考虑自己训练/部署大模型

**企业定制模型**
从头训练大模型对一般企业来说成本巨大，可以考虑基于一些开源模型做继续训练，定制某个专业方向的模型

**模型微调与知识库**
* 对于一些专业的场景（比如涉及企业内部信息，专业方向的非公开信息），大模型是不知道的，这时就需要将这些知识灌输到大模型。
* 模型微调（Fine-tuning）：在已经训练好的机器学习模型基础上，针对特定任务或数据集进行进一步的优化调整。
* 知识库：使用向量数据库（或者其他数据库）存储的数据库，可以外挂，作为模型的行业信息提供方。
* 微调相当于让大模型去学习了新的一门学科，在回答的时候完成闭卷考试。知识库相当于为大模型提供了新学科的课本，回答的时候为开卷考试。

## 安装驱动
以下命令是否存在，没有的话google搜索安装方式

nvidia-smi 

nvcc -V  

conda

## conda
conda create --name gpt python=3.10

conda activate gpt

## ChatGLM3尝试

git clone https://github.com/THUDM/ChatGLM3

cd basic_demo

显存少于13G的话加上 vim web_demo.py 加上 quantize(4)
AutoModel.from_pretrained(MODEL_PATH, trust_remote_code=True).quantize(4).half()...

python web_demo.py

## ChatGLM3微调
https://github.com/THUDM/ChatGLM3/blob/main/finetune_chatmodel_demo/README.md 

