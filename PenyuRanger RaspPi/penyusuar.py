
from ubidots import ApiClient
from picamera.array import PiRGBArray
from picamera import PiCamera
import os
import uploaderDropbox
import argparse
import warnings
from datetime import datetime
import imutils
import json
import time
import cv2
import urllib2
import RPi.GPIO as GPIO


GPIO.setmode(GPIO.BCM)

#set pir
PIR_PIN = 7
GPIO.setup(PIR_PIN, GPIO.IN)
counter = 0
threadcount = 0

#ubidots api
api = ApiClient("7e9f2b61d5ca76e67d8afb1e1047643ddb42d6e7")
pirVar = api.get_variable("55831f7d7625421f19321cea")
faceVar = api.get_variable("55831f727625421edcf854d9")

#path simpan photo
location = "/home/pi/Desktop/PenyuRangerbeta/path/Detection/"

ap = argparse.ArgumentParser()
ap.add_argument("-c", "--conf", required=True, help="path to the JSON")
args = vars(ap.parse_args())

warnings.filterwarnings("ignore")
conf = json.load(open(args["conf"]))
client = None

save photo
def save_image(path, image):
	filename = datetime.now().strftime('%Y%m%d_%Hh%Mm%Ss%f') + '.jpg'
	cv2.imwrite(location + filename, image)


upload ke dropbox
def upload_files():
	dir_list = os.listdir(location)
	for file_name in dir_list:
		if file_name.endswith(".jpg"):
			print "**********Uploading**********"
			cmd = location
			os.system(cmd)
			os.remove(location + file_name)
#set pi camera
camera = PiCamera()
camera.resolution = tuple(conf["resolution"])
camera.framerate = conf["fps"]
rawCapture = PiRGBArray(camera, size=tuple(conf["resolution"]))

print "[INFO] Starting..."
time.sleep(conf["camera_warmup_time"])

avg = None

lastUploaded = datetime.now()
motionCounter = 0

a = int(3)
for f in camera.capture_continuous(rawCapture, format="bgr", use_video_port=True):
	frame = f.array
	timestamp = datetime.now()
	text = "Clear"

	frame = imutils.resize(frame, width=500)
	gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
	gray = cv2.GaussianBlur(gray, (21, 21,), 0)

	if avg is None:
		print "[INFO] Waiting..."
		avg = gray.copy().astype("float")
		rawCapture.truncate(0)
		continue

	cv2.accumulateWeighted(gray, avg, 0.5)
	frameDelta = cv2.absdiff(gray, cv2.convertScaleAbs(avg))
	thresh = cv2.threshold(frameDelta, conf["delta_thresh"], 255, cv2.THRESH_BINARY)[1]
	thresh = cv2.dilate(thresh, None, iterations=2)
	(cnts, _) = cv2.findContours(thresh.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

	for c in cnts:
		if cv2.contourArea(c) < conf["min_area"]:
			continue

		(x, y, w, h) = cv2.boundingRect(c)
		cv2.rectangle(frame, (x,y), (x+w, y+h), (0,255,0), 2)
		text ="Detected"

	ts = timestamp.strftime("%A %d %B %Y %I:%M:%S%p")
	cv2.putText(frame, "{}".format(text), (20,20), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 0, 255), 2)
	cv2.putText(frame, ts, (10, frame.shape[0] - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.35, (0, 0, 255), 1)


	if text == "Detected":

#		save_image(location, frame)
		filename = datetime.now().strftime('%Y%m%d_%Hh%Mm%Ss%f') + '.jpg'
		cv2.imwrite('path/Detection/' + filename, frame)
		
		print "Saved"
		pirVar.save_value({'value': a})
	
		if(timestamp - lastUploaded).seconds >= conf["min_upload_seconds"]:
	
			motionCounter +=1

			if motionCounter >= conf["min_motion_frames"]:

				upload_files()
#				dir_list = os.listdir(location)
#				for file_name in dir_list:
#					if file_name.endswith(".jpg"):
#						print "******Uploading*******"
#						cmd = "/home/pi/Dropbox-Uploader/dropbox_uploader.sh upload " + location + " /detected"
#						os.system(cmd)
#						print "******Uploaded********"
#						os.remove(location + file_name)

				lastUploaded = timestamp
				motionCounter = 0

	elif text == "Detected" and GPIO.input(PIR_PIN):
		upload_files()
		print "Human motion Detected"
		faceVar.save_value({value}:)

	elif GPIO.input(PIR_PIN):
		print "Motion Detected"
		os.system('aplay /home/pi/Desktop/PenyuRangerbeta/dogaway.mp3')
		threadcount += 1
		presence = 0
		time.sleep(1.5)
			if (counter == 10):
				print threadcount
				threadcount.save_value('{}')
		counter += 1
			
	else:
		motionCounter = 0


	
	if conf["show_video"]:
		cv2.imshow("Penyusuar Monitor v.1.1", frame)
		key = cv2.waitKey(1) & 0xFF
		

		if key == ord("q"):
			break

	rawCapture.truncate(0)

