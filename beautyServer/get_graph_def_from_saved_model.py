import tensorflow as tf 

saved_model_dir = 'D:/result/new'

with tf.Session() as session:
   meta_graph_def = tf.saved_model.loader.load(
   session,
   tags=[tag_constants.SERVING],
   export_dir=saved_model_dir
