import dlib
import matplotlib.pyplot as plt
import matplotlib.patches as patches
import tensorflow as tf
import numpy as np
import cv2
from imageio import imread, imsave
from datetime import datetime

class Model():
    # # Load BeautyGAN Pretrained
    # - https://drive.google.com/drive/folders/1pgVqnF2-rnOxcUQ3SO4JwHUFTdiSe5t9

    def __init__(self):
    # TF Config
        gpu_options = tf.GPUOptions(per_process_gpu_memory_fraction=0.5)
        global sess
        sess = tf.Session(config=tf.ConfigProto(gpu_options=gpu_options))
        sess.run(tf.global_variables_initializer())
    
        saver = tf.train.import_meta_graph('models/model.meta')
        saver.restore(sess, tf.train.latest_checkpoint('models'))
        graph = tf.get_default_graph()
        global X,Y
        X = graph.get_tensor_by_name('X:0') # source
        Y = graph.get_tensor_by_name('Y:0') # reference
        global Xs
        Xs = graph.get_tensor_by_name('generator/xs:0') # output

    def align_faces(self, img):
        # Load Models
        detector = dlib.get_frontal_face_detector()
        sp = dlib.shape_predictor('models/shape_predictor_5_face_landmarks.dat')

        dets = detector(img, 1)
        objs = dlib.full_object_detections()
    
        for detection in dets:
            s = sp(img, detection)
            objs.append(s)
        
        faces = dlib.get_face_chips(img, objs, size=256, padding=0.35)
        return faces

    def preprocess(self, img):
        return img.astype(np.float32) / 127.5 - 1.

    def postprocess(self, img):
        return ((img + 1.) * 127.5).astype(np.uint8)

    def execute(self, srcImg, refImg):
        img1 = dlib.load_rgb_image(srcImg)
        img1_faces = self.align_faces(img1)

        img2 = dlib.load_rgb_image(refImg)
        img2_faces = self.align_faces(img2)

        src_img = img1_faces[0]
        ref_img = img2_faces[0]

        X_img = self.preprocess(src_img)
        X_img = np.expand_dims(X_img, axis=0)

        Y_img = self.preprocess(ref_img)
        Y_img = np.expand_dims(Y_img, axis=0)

        output = sess.run(Xs, feed_dict={
            X: X_img,
            Y: Y_img
        })

        output_img = self.postprocess(output[0])
        result = imsave('path/beauty/imgs/example.jpg', output_img)
        return 'example.jpg'
        
