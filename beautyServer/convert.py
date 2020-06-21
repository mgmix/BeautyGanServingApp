import tensorflow as tf

graph_def_file = "path/beauty/frozen.pb"
input_arrays = ["X","MX","GX","Y"]
output_arrays = ["Merge/MergeSummary"]
tflite_file = 'test.tflite'

converter = tf.compat.v1.lite.TFLiteConverter.from_frozen_graph(graph_def_file, input_arrays, output_arrays)
tflite_model = converter.convert()
open(tflite_file, "wb").write(tflite_model)
#interpreter = tf.lite.Interpreter(model_content=tflite_model)
#interpreter.allocate_tensors()