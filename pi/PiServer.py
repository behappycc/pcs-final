# coding=UTF-8
import argparse
import os.path
import json
import pprint
import sys
# tornado module
import tornado.httpserver
import tornado.httputil as httputil
import tornado.ioloop
import tornado.web
from tornado.options import define, options
from  tornado.escape import json_decode
from  tornado.escape import json_encode
#custom files
import Pi as pi

#140.112.91.221
#140.112.42.151

def main():
    parser = argparse.ArgumentParser(description='pi server')
    parser.add_argument('-p', type=int, help='listening port for pi server')
    args = parser.parse_args()
    port = args.p

    print("Pi Server starting......")

    http_server = tornado.httpserver.HTTPServer(Application())
    http_server.listen(port)

    try:
        tornado.ioloop.IOLoop.instance().start()
    except KeyboardInterrupt:
        tornado.ioloop.IOLoop.instance().stop()

class Application(tornado.web.Application):
    def __init__(self):
        handlers = [
            (r"/", PiHandler),
            (r"/index", PiHandler),
            (r"/pi", PiHandler),
            (r"/piimage", PiImageHandler),
        ]

        settings = dict(
            template_path=os.path.join(os.path.dirname(__file__), "templates"),
            static_path=os.path.join(os.path.dirname(__file__), "static"),
        )
        super(Application, self).__init__(handlers, **settings)

class BaseHandler(tornado.web.RequestHandler):
	def hi():
		pass

class PiHandler(BaseHandler):
    def __init__(self, application, request, **kwargs):
        self.deviceInfo = {}
        self.deviceInfo['temperature'] = '18C'
        self.deviceInfo['humidity'] = '80%'
        super(PiHandler, self).__init__(application, request, **kwargs)

    def get(self):
        pi.light()
        pi.camera()
    	self.write(json.dumps(self.deviceInfo)) 

   	def post(self):
   		self.write(json.dumps(self.deviceInfo))

class PiImageHandler(BaseHandler):
    def get(self):
        self.render("piimage.html")

if __name__ == '__main__':
    main()