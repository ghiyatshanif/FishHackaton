from webcam import Webcam
from speech import Speech
import RPi.GPIO as GPIO
import time
from ubidots import ApiClient
import os
import uploaderDropbox
import random

GPIO.setmode(GPIO.BCM)
PIR = 14
#SERVO = 18
GPIO.setup(PIR, GPIO.IN)
#GPIO.setup(SERVO, GPIO.OUT)
#p = GPIO.PWM(SERVO, 50)
#p.start(7.5)

webcam = Webcam()
#speech = Speech() 


#manggil api ubidots
api = ApiClient("7e9f2b61d5ca76e67d8afb1e1047643ddb42d6e7")

penyuVar = api.get_variable("5572795b76254210f1598f84")
lemparData = int(2)

webcam.detect_motion()


while True:
#	time.sleep(1)

#	p.ChangeDutyCycle(4.5)
#	time.sleep(0.5)
#	p.ChangeDutyCycle(10.5)
#	time.sleep(0.5)	

	if (webcam.detect_faces()):
#penyuVar.save_value({'value':lemparData})
		uploaderDropbox.upload_files()
		
	elif GPIO.input(PIR):
		os.system('aplay /home/pi/Desktop/testing/Dog-barking-very-loudly.wav')

		
	elif (webcam.detect_faces()) and GPIO.input(PIR):
		penyuVar.save_value({'value':lemparData})
#		else:
#			pass
