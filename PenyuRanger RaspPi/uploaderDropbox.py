import os
path = "/home/pi/Desktop/testing/WebCam/Detection/"

def upload_files():
	if not os.path.exists(path):
		print "ga ada"
		return
	
	dir_list = os.listdir(path)
	for file_name in dir_list:
		if file_name.endswith(".jpg"):
			print "Uploaded"
			cmd = "/home/pi/Dropbox-Uploader/dropbox_uploader.sh upload  " + path + " /detected"
			os.system(cmd)
			os.remove(path + file_name)


