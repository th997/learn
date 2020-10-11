# coding:utf-8
import tensorflow.keras as keras
import os 
import numpy as np

from sklearn import datasets
from sklearn.model_selection import train_test_split

SAVE_PATH = os.path.abspath(os.path.dirname(__file__)) + '/data_demo_face/'

def get_model():
    try:
        model = keras.models.load_model(SAVE_PATH)
    except Exception as e:
        model = keras.Sequential()
        # 第一层卷积，卷积的数量为128，卷积的高和宽是3x3，激活函数使用relu
        model.add(keras.layers.Conv2D(128, kernel_size=3, activation='relu', input_shape=(64, 64, 1)))
        # 第二层卷积
        model.add(keras.layers.Conv2D(64, kernel_size=3, activation='relu'))
        #把多维数组压缩成一维，里面的操作可以简单理解为reshape，方便后面Dense使用
        model.add(keras.layers.Flatten())
        #对应cnn的全链接层，可以简单理解为把上面的小图汇集起来，进行分类
        model.add(keras.layers.Dense(40, activation='softmax'))
    model.compile(optimizer='adam', metrics=['accuracy'], loss='sparse_categorical_crossentropy')
    return model

def test():
    #人脸数据 
    faces = datasets.fetch_olivetti_faces()
    x = faces.images
    y = faces.target
    print(x[0],y[0],len(x))

    #数据转换,黑白图,深度１(RGB 3)
    x = x.reshape(400, 64, 64, 1)
    #随机分割30%的数据做测试验证的数据
    x_train, x_test, y_train, y_test = train_test_split(x, y, test_size=0.3)
    
    #训练
    model = get_model()
    model.summary()
    model.fit(x_train, y_train, epochs=4)
    model.save(SAVE_PATH)

    #预测
    model = get_model()
    y_predict = model.predict(x_test)
    for i in range(10):
        print(y_test[i], np.argmax(y_predict[i]))


# https://blog.csdn.net/iamfrankjie/article/details/101163925
if __name__ == "__main__":
    test()