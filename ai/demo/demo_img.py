from captcha.image import ImageCaptcha
from PIL import Image
import numpy as np 
import tensorflow as tf

text = '1234'
image = ImageCaptcha()
captcha = image.generate(text)
captcha_image = Image.open(captcha)
captcha_image.show()

arr_image=np.array(captcha_image)
print(arr_image.shape)

gray_image=np.mean(arr_image,-1)
print(gray_image.shape)
Image.fromarray(gray_image).show()



