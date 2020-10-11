# AI

## 基本概念
人工智能:
    机器学习:
        深度学习:

传统预测,图像识别,自然语言处理

数据->模型->预测

数据:特征值+目标值

机器学习流程：获取数据,数据处理,特征工程,机器学习双方训练,模型,模型评估,应用

机器学习算法:
监督学习(有目标值):分类算法,回归算法
无监督学习(没有目标值):聚类

业界广泛流传:数据和特征决定了机器学习的上限,而模型和算法只是逼近这个上限而已

特征工程:使用专业背景知识和技巧处理数据,使得特征能在机器学习算法上发挥更好作用的过程
特征工程会影响机器学习的效果
特征工程:特征提取,特征预处理,特征降维

## Scikit-learn
pip3 install Scikit-learn==0.19.1
pip3 install numpy scipy pandas

``` python 
from sklearn.datasets import load_iris
iris=load_iris()
print(iris)
print(iris['DESCR'])
print(iris.target)
print(iris.target_names)
print(iris.data)
print(iris.feature_names)
```

主成分分析(PCA):
高维数据转化成低纬数据的过程,在此过程中可能舍弃原有数据,创造新的变量. 
作用:数据纬度压缩,尽可能降低原数据的纬度(复杂性),损失少量信息
应用:回归分析或者聚类分析当中

