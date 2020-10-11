import tensorflow as tf
from tensorflow.keras import datasets, layers, optimizers

# os.environ['http_proxy'] = 'http://10.10.10.106:1081'
# os.environ['https_proxy'] = 'http://10.10.10.106:1081'

(xs, ys), _ = datasets.mnist.load_data()
print('datasets:', xs.shape, ys.shape)

xs = tf.convert_to_tensor(xs, dtype=tf.float32)
db = tf.data.Dataset.from_tensor_slices((xs, ys))

#for step, (x, y) in enumerate(db):
    # print(step, x.shape, y, y.shape)
