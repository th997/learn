from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from sklearn.feature_extraction import DictVectorizer
from sklearn.feature_extraction.text import CountVectorizer,TfidfVectorizer
from sklearn.preprocessing import MinMaxScaler,StandardScaler
from sklearn.feature_selection import VarianceThreshold
from sklearn.decomposition import PCA
from scipy.stats import pearsonr
import jieba
import pandas as pd 

def datasets_demo():
    iris=load_iris()
    print(iris)
    print(iris['DESCR'])
    print(iris.target)
    print(iris.target_names)
    print(iris.data)
    print(iris.feature_names)

def dict_demo():
    data=[{"city":"北京","temperature":100,"level":1},{"city":"上海","level":2,"temperature":60},{"city":"深圳","temperature":30},{"city":"广州","temperature":30}]
    transfer=DictVectorizer(sparse=False)
    data_new=transfer.fit_transform(data)
    print(data_new)
    print(transfer.get_feature_names())

def count_demo():
    data=["life is short ,i like like python","life is too long,i dislike python" ]
    transfer=CountVectorizer()
    data_new=transfer.fit_transform(data)
    print(data_new.toarray())
    print(transfer.get_feature_names())

# pip3 install jieba
def count_chinese_demo():
    data_ch=["我一直惦记着贫困地区的乡亲们","习近平总书记访贫问苦的脚步遍布全国14个集中连片特困地区" ]
    data=[]
    for sent in data_ch:
        data.append(" ".join(list(jieba.cut(sent))))
    transfer=CountVectorizer()
    data_new=transfer.fit_transform(data)
    print(data_new.toarray())
    print(transfer.get_feature_names())

def tfidf_demo():
    data_ch=["我一直惦记着贫困地区的乡亲们","习近平总书记访贫问苦的脚步遍布全国14个集中连片特困地区" ]
    data=[]
    for sent in data_ch:
        data.append(" ".join(list(jieba.cut(sent))))
    transfer=TfidfVectorizer()
    data_new=transfer.fit_transform(data)
    print(data_new.toarray())
    print(transfer.get_feature_names())    

# 归一化
def minmax_demo():
    data = pd.read_csv("data.txt")
    print(data)
    transfer =MinMaxScaler()
    data_new = transfer.fit_transform(data)
    print(data_new)

# 标准化
def stand_demo():
    data = pd.read_csv("data.txt")
    print(data)
    transfer =StandardScaler()
    data_new = transfer.fit_transform(data)
    print(data_new)

# 过滤低方差特征
def variance_demo():
    data = pd.read_csv("data.txt")
    print(data)
    data=data.iloc[:,0:-1]
    print(data)
    transfer =VarianceThreshold(threshold=1)
    data_new = transfer.fit_transform(data)
    print(data_new)
    # 计算2个变量之间的相关系数
    r=pearsonr(data["c"],data["d"])
    print(r)


def pca_demo():
    data=[[2,8,4,5],[6,3,0,8],[5,4,9,1]]
    transfer=PCA(n_components=0.9)
    data_new=transfer.fit_transform(data)
    print(data_new)

if __name__ == "__main__":
    #datasets_demo()
    #dict_demo()
    #count_demo()
    #count_chinese_demo()
    #tfidf_demo()
    #minmax_demo()
    #stand_demo()
    #variance_demo()
    pca_demo()


