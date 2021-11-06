# coding:utf-8
from captcha.image import ImageCaptcha
from tensorflow.keras import datasets, layers, models
import random,os 
from PIL import Image
import numpy as np
import tensorflow as tf
from concurrent.futures import ThreadPoolExecutor
import queue

number = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9']
alphabet = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u','v', 'w', 'x', 'y', 'z']
ALPHABET = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U','V', 'W', 'X', 'Y', 'Z']
SAVE_PATH = os.path.abspath(os.path.dirname(__file__)) + '/data_demo_captcha/'
CHAR_SET = number + alphabet + ALPHABET
CHAR_SET_LEN = len(CHAR_SET)
IMAGE_HEIGHT = 60
IMAGE_WIDTH = 160
CAPTCHA_SIZE = 4

# 获取随机验证码字符串数组
def random_captcha_text(char_set=CHAR_SET, captcha_size=CAPTCHA_SIZE):
    captcha_text = []
    for i in range(captcha_size):
        c = random.choice(char_set)
        captcha_text.append(c)
    return captcha_text

#　生产验证码图片和对应的字符串
def gen_captcha_text_and_image(width=IMAGE_WIDTH, height=IMAGE_HEIGHT, char_set=CHAR_SET):
    image = ImageCaptcha(width=width, height=height)

    captcha_text = random_captcha_text(char_set)
    captcha_text = ''.join(captcha_text)

    captcha = image.generate(captcha_text)

    captcha_image = Image.open(captcha)
    captcha_image = np.array(captcha_image)
    return captcha_text, captcha_image


# 图片灰度化
def convert2gray(img):
    if len(img.shape) > 2:
        gray = np.mean(img, -1)
        return gray
    else:
        return img

# 文本转换为矩阵
def text2vec(text,captcha_size=CAPTCHA_SIZE,char_set_len=CHAR_SET_LEN):
    vector = np.zeros([captcha_size, char_set_len])
    for i, c in enumerate(text):
        idx = CHAR_SET.index(c)
        vector[i][idx] = 1.0
    return vector

# 矩阵转化为文本
def vec2text(vec):
    text = []
    for i, c in enumerate(vec):
        text.append(CHAR_SET[c])
    return "".join(text)


def get_next_batch(batch_size=128):
    batch_x = np.zeros([batch_size, IMAGE_HEIGHT, IMAGE_WIDTH, 1])
    batch_y = np.zeros([batch_size, CAPTCHA_SIZE, CHAR_SET_LEN])

    def wrap_gen_captcha_text_and_image():
        while True:
            text, image = gen_captcha_text_and_image(width=IMAGE_WIDTH, height=IMAGE_HEIGHT, char_set=CHAR_SET)
            if image.shape == (IMAGE_HEIGHT, IMAGE_WIDTH, 3):
                return text, image

    for i in range(batch_size):
        text, image = wrap_gen_captcha_text_and_image()
        image = tf.reshape(convert2gray(image), (IMAGE_HEIGHT, IMAGE_WIDTH, 1))
        batch_x[i, :] = image
        batch_y[i, :] = text2vec(text)

    return batch_x, batch_y


def cnn():
    model = tf.keras.Sequential()

    for i in range (3):
        model.add(tf.keras.layers.SeparableConv2D(32*pow(2,i), (5, 5),input_shape=(IMAGE_HEIGHT, IMAGE_WIDTH, 1)))
        model.add(tf.keras.layers.PReLU())
        model.add(tf.keras.layers.AvgPool2D((2, 2), strides=2))

    model.add(tf.keras.layers.Dropout(0.3))
    model.add(tf.keras.layers.Flatten()) 
    model.add(tf.keras.layers.Dense(CAPTCHA_SIZE * CHAR_SET_LEN))
    model.add(tf.keras.layers.Reshape([CAPTCHA_SIZE, CHAR_SET_LEN]))
    model.add(tf.keras.layers.Softmax())
    model.summary()
    model.compile(optimizer='adam', metrics=['accuracy'], loss='categorical_crossentropy')
    return model


def train():
    try:
        model = tf.keras.models.load_model(SAVE_PATH)
    except Exception as e:
        print('#######Exception', e)
        model = cnn()

    
    pool = ThreadPoolExecutor(20)
    q = queue.Queue(100)
    train_num = 101

    for i in range(train_num):
        pool.submit(lambda:q.put(get_next_batch()))

    for times in range(train_num):
        # 训练
        batch_x, batch_y =q.get()
        print('qsize',q.qsize(),'times=', times, ' batch_x.shape=', batch_x.shape, ' batch_y.shape=', batch_y.shape)
        model.fit(batch_x, batch_y, epochs=8)
        # 保存模型
        if 0 == (times+1) % 100:
            # 测试
            pool.submit(lambda:q.put(get_next_batch()))
            batch_x, batch_y = q.get()
            test_loss, test_acc = model.evaluate(batch_x, batch_y)
            print("test_loss=%.4f, test_acc=%.4f" %(test_loss, test_acc))
            print("save model at times=", times)
            model.save(SAVE_PATH)
            


def predict():
    model = tf.keras.models.load_model(SAVE_PATH)
    success = 0
    count = 1000
    for i in range(count):
        data_x, data_y = get_next_batch(1)
        prediction_value = model.predict(data_x)
        data_y = vec2text(np.argmax(data_y, axis=2)[0])
        prediction_value = vec2text(np.argmax(prediction_value, axis=2)[0])

        if data_y.upper() == prediction_value.upper():
            #print("y预测=", prediction_value, "y实际=", data_y, "预测成功。")
            success += 1
        if i%10==0:
            print("预测", i, "次", "成功率=", success / count)
    print("预测", count, "次", "成功率=", success / count)
    pass

# https://my.oschina.net/u/4105485/blog/3042460

# https://www.jianshu.com/p/aa075424d2d2

if __name__ == "__main__":
    train()
    predict()
