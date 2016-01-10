import sys
import picamera
import Adafruit_DHT
import RPi.GPIO as GPIO
import time
import cv2

def camera():
    #x = 0
    camera = picamera.PiCamera()
    camera.capture('./static/PiImages/imageA.jpg')
    camera.close()

    time.sleep(30)
    #while(x<1000):
    camera = picamera.PiCamera()
    camera.capture('./static/PiImages/imageB.jpg')
    camera.close()

    imagelocationA = "./static/PiImages/imageA.jpg"    
    imagelocationB = "./static/PiImages/imageB.jpg"

    pixelCompare(imagelocationA,imagelocationB,0.1)
    #x = x+1
    #camera.start_preview()
    #camera.stop_preview()
    #time.sleep(30)
    
def pixelCompare(i1, i2, ratio):
    img1 = cv2.imread(i1)
    img2 = cv2.imread(i2)
    imgray1 = cv2.cvtColor(img1,cv2.COLOR_BGR2GRAY)
    imgray2 = cv2.cvtColor(img2,cv2.COLOR_BGR2GRAY)
    #imgInfo[0] = 1280, imgInfo[1] = 720
    imgInfo = img1.shape
    diff = 0
    for i in xrange(imgInfo[0]):
        for j in xrange(imgInfo[1]):
            if abs(int(imgray1[i, j]) - int(imgray2[i, j])) > 5:
                diff += 1
    compare = float(diff) / float((imgInfo[0] * imgInfo[1]))
    #print str(compare * 100) + '%'
    if compare > ratio:
        #there are differnt
	print 'there are different'
	#cv2.imwrite("./static/PiImages/different/Aimage" + str(x) + ".jpg" , img1)
	#cv2.imwrite("./static/PiImages/different/Bimage" + str(x) + ".jpg" , img2)
        return True
    else:
	print 'there are same'
        return False
    
def sensor():
    sensor = Adafruit_DHT.DHT11
    pin = 4
    humidity, temperature = Adafruit_DHT.read_retry(sensor, pin)
    if humidity is not None and temperature is not None:
        print 'Temp={0:0.1f}*C  Humidity={1:0.1f}%'.format(temperature, humidity)
    else:
        print 'Failed to get reading. Try again!'

def lightOn():
    GPIO.setmode(GPIO.BCM)
    GPIO.setup(4, GPIO.OUT)
    LEDon = GPIO.output(4, 0)
    '''
    while True:
        LEDon = GPIO.output(4, 0)
        time.sleep(1)
        LEDoff = GPIO.output(4, 1)
        time.sleep(1)
    '''

def lightOff():
    GPIO.setmode(GPIO.BCM)
    GPIO.setup(4, GPIO.OUT)
    LEDoff = GPIO.output(4, 1)

def main():
    camera()

if __name__ == '__main__':
    main()

'''
sensor = Adafruit_DHT.DHT22

# Example using a Beaglebone Black with DHT sensor
# connected to pin P8_11.
pin = 'P8_11'

# Example using a Raspberry Pi with DHT sensor
# connected to GPIO23.
#pin = 23

# Try to grab a sensor reading.  Use the read_retry method which will retry up
# to 15 times to get a sensor reading (waiting 2 seconds between each retry).
humidity, temperature = Adafruit_DHT.read_retry(sensor, pin)

# Note that sometimes you won't get a reading and
# the results will be null (because Linux can't
# guarantee the timing of calls to read the sensor).  
# If this happens try again!
if humidity is not None and temperature is not None:
    print 'Temp={0:0.1f}*C  Humidity={1:0.1f}%'.format(temperature, humidity)
else:
    print 'Failed to get reading. Try again!'
'''
