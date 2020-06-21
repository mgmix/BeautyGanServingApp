import os
from flask import Flask, render_template, request, send_file, jsonify
from werkzeug.datastructures import FileStorage
from werkzeug.utils import secure_filename
from execute import Model

app = Flask(__name__)

UPLOAD_FOLDER = 'path/beauty/imgs/'
ALLOWED_EXTENSIONS = set(['png', 'jpg', 'jpeg', 'gif'])
def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1] in ALLOWED_EXTENSIONS

@app.route('/')
def home():
    return 'Hello World'

@app.route('/test')
def test():
    model = Model()
    model.execute(UPLOAD_FOLDER+'hj.jpg', UPLOAD_FOLDER+'/makeup/440.jpg')
    return {'Hello': 'Hi'}

@app.route('/upload', methods=['POST'])
def uploadImage():
    model = Model()

    srcImgfile = request.files['srcImage']
    refImgfile = request.files['refImage']

    srcPath = UPLOAD_FOLDER + srcImgfile.filename;
    refPath = UPLOAD_FOLDER + refImgfile.filename;

    if srcImgfile and allowed_file(srcImgfile.filename):
        srcImgfile.save(srcPath)
        
    if refImgfile and allowed_file(refImgfile.filename):
        refImgfile.save(refPath)

    result = model.execute(srcPath, refPath)
    return jsonify({ 'path': result })

## sourceImage, ReferenceImage upload  ->

@app.route('/image/<fileName>')
def loadImage(fileName):
    print('fileName:'+ fileName)
    return send_file(UPLOAD_FOLDER+fileName, mimetype='image')

if __name__ == '__main__':
    app.run(debug=True)