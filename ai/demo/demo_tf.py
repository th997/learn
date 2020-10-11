import tensorflow as tf

A = tf.constant([[1,2],[3,4]])
B = tf.constant([[5,6],[7,8]])
C = tf.matmul(A,B)
print("C=",C )
D = tf.add(A,B)
print("D=",D )

# 
x = tf.constant(0.)
y = tf.constant(1.)
for it in range(50):
    x = x+y
    y = y/2
print("x=",x.numpy())