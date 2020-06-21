import tensorflow as tf
#Step 1 
#import the model metagraph
saver = tf.train.import_meta_graph('models/model.meta', clear_devices=True)
#make that as the default graph
graph = tf.get_default_graph()
input_graph_def = graph.as_graph_def()
sess = tf.Session()
#now restore the variables
saver.restore(sess, "models/model")

#Step 2
# Find the output name
graph = sess.graph
print([node.name for node in graph.as_graph_def().node])
# graph = tf.get_default_graph()
# for op in graph.get_operations(): 
#   print (op.name)


  # 할일 Step 3 Step4 해보기? 
  # Checkpoint 가지고 graph 뽑기?  output_node_names ?? 
  # GRAPH pb 가 있어야 -> Tensorboard 에서 input output node 등을 찾아서
  # tflite_convert 시에 입력할게 생김  

  # 'Merge/MergeSummary'

#   3:36.949328: I T:\src\github\tensorflow\tensorflow\core\common_runtime\gpu\gpu_device.cc:958]      0 
# 2020-05-02 16:43:37.101928: I T:\src\github\tensorflow\tensorflow\core\common_runtime\gpu\gpu_device.cc:971] 0:   N 
# 2020-05-02 16:43:37.262508: I T:\src\github\tensorflow\tensorflow\core\common_runtime\gpu\gpu_device.cc:1084] Created TensorFlow device (/job:localhost/replica:0/task:0/device:GPU:0 with 1363 MB memory) -> physical GPU (device: 0, name: GeForce GTX 1050, pci bus id: 0000:01:00.0, compute capability: 6.1)
# ['X', 'MX', 'GX', 'Y', 'learning_rate', 'generator/conv2d/kernel/Initializer/truncated_normal/shape', 'generator/conv2d/kernel/Initializer/truncated_normal/mean', 'generator/conv2d/kernel/Initializer/truncated_normal/stddev', 'generator/conv2d/kernel/Initializer/truncated_normal/TruncatedNormal', 'generator/conv2d/kernel/Initializer/truncated_normal/mul', 'generator/conv2d/kernel/Initializer/truncated_normal', 'generator/conv2d/kernel', 'generator/conv2d/kernel/Assign', 'generator/conv2d/kernel


