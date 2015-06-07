import cv2
import os

from datetime import datetime

path = "/home/pi/Desktop/testing/WebCam/Detection/"
 
class Webcam(object):
 
    WINDOW_NAME = "Anti maling SatwaLiar"
 
    def __init__(self):
        self.webcam = cv2.VideoCapture(0)
         

    def _save_image(self, path, image):
        filename = datetime.now().strftime('%Y%m%d_%Hh%Mm%Ss%f') + '.jpg'
        cv2.imwrite(path + filename, image)
 
    
    def _delta(self, t0, t1, t2):
        d1 = cv2.absdiff(t2, t1)
        d2 = cv2.absdiff(t1, t0)
        return cv2.bitwise_and(d1, d2)
 

    def detect_faces(self):
 
        
        img = self.webcam.read()[1]
         
        # file xml satukan degan 3 file lainnya
        face_cascade = cv2.CascadeClassifier('haarcascade_frontalface_default.xml')
        eye_cascade = cv2.CascadeClassifier('haarcascade_eye.xml')
 
        gray = cv2.cvtColor(img, cv2.COLOR_RGB2GRAY)
        faces = face_cascade.detectMultiScale(gray, 1.3, 5)
 
        for (x,y,w,h) in faces:
 
            cv2.rectangle(img,(x,y),(x+w,y+h),(255,0,0),2)
 
            roi_gray = gray[y:y+h, x:x+w]
            roi_color = img[y:y+h, x:x+w]
            eyes = eye_cascade.detectMultiScale(roi_gray)
 
            for (ex,ey,ew,eh) in eyes:
                cv2.rectangle(roi_color,(ex,ey),(ex+ew,ey+eh),(0,255,0),2)
 
        
        self._save_image('WebCam/Detection/', img)
#	dir_list = os.listdir(path)
#	for file_name in dir_list:
#		if file_name.endswith(".jpg"):
#			print "Done, data terupload"
#			cmd = "/home/pi/Dropbox-Uploader/dropbox_uploader.sh upload " + path + " /detected "
#			os.system(cmd)
#			os.remove(path + file_name)
 
        
        cv2.imshow(self.WINDOW_NAME, img)
        cv2.waitKey(1000)
 
       
        cv2.destroyAllWindows()
 
        if len(faces) == 0:
            return False
 
        return True
 
    
    def detect_motion(self):
 
        
        threshold = 170000
 
        
        t_minus = cv2.cvtColor(self.webcam.read()[1], cv2.COLOR_RGB2GRAY)
        t = cv2.cvtColor(self.webcam.read()[1], cv2.COLOR_RGB2GRAY)
        t_plus = cv2.cvtColor(self.webcam.read()[1], cv2.COLOR_RGB2GRAY)
 
        
        while True:
   
          
          delta = self._delta(t_minus, t, t_plus)
   
          
          cv2.imshow(self.WINDOW_NAME, delta)
          cv2.waitKey(10)
 
          
          count = cv2.countNonZero(delta)
 
          
 
          
          if (count > threshold):
 
              self._save_image('WebCam/Motion/', delta)
              self._save_image('WebCam/Photograph/', self.webcam.read()[1])
 
              cv2.destroyAllWindows()
              return True
 
          
          t_minus = t
          t = t_plus
          t_plus = cv2.cvtColor(self.webcam.read()[1], cv2.COLOR_RGB2GRAY)
