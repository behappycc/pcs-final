# coding=UTF-8
# python native module
import urllib
import threading
import thread
import argparse
import os.path
import json
import pprint
# tornado module
import tornado.httpserver
import tornado.httputil as httputil
import tornado.ioloop
import tornado.web
from tornado import gen
from tornado.options import define, options
from  tornado.escape import json_decode
from  tornado.escape import json_encode
# mongodb module
import pymongo
from pymongo import MongoClient

#sudo service mongod start
#sudo service mongod stop
#localhost:8888/index

DB_IP = "localhost"
DB_PORT = 27017
client = MongoClient(DB_IP, DB_PORT)
db = client['pcs']
collection = db['account']

def main():
    parser = argparse.ArgumentParser(description='ptt-clustering server')
    parser.add_argument('port', type=int, help='listening port for ptt-clustering server')
    args = parser.parse_args()
    
    print("Server starting......")

    http_server = tornado.httpserver.HTTPServer(Application())
    http_server.listen(args.port)

    try:
        tornado.ioloop.IOLoop.instance().start()
    except KeyboardInterrupt:
        tornado.ioloop.IOLoop.instance().stop()

class Application(tornado.web.Application):
    def __init__(self):
        handlers = [
            (r"/index", IndexHandler),
            (r"/login", LoginHandler),
            (r"/admin", AdminHandler),
            (r"/user", UserHandler),
            (r"/test", TestHandler),
            (r"/testajax", AjaxHandler),
        ]

        settings = dict(
            template_path=os.path.join(os.path.dirname(__file__), "templates"),
            static_path=os.path.join(os.path.dirname(__file__), "static"),
        )
        super(Application, self).__init__(handlers, **settings)

class BaseHandler(tornado.web.RequestHandler):
    @property
    def db(self):
        pass
        #return self.settings['db']

class IndexHandler(BaseHandler):
    def get(self):
        abc = ["Item 1", "Item 2", "Item 3"]
        self.render("index.html", abc = abc)

class LoginHandler(BaseHandler):
    def get(self):
        self.render("login.html")

    @gen.coroutine
    def post(self):
        print 'yoooo'
        deviceInfo = {}
        deviceInfo['temperature'] = '15C'
        deviceInfo['humidity'] = '80%'
        #json_obj = json_decode(self.request.body)
        username = self.get_argument('username')
        password = self.get_argument('password')
        print username, password
        print 'Post data received'
        '''
        for key in list(json_obj.keys()):
            print('key: %s , value: %s' % (key, json_obj[key]))
        print json_obj['key1']
        print json_obj['key2']
        post = {
            "username": json_obj['key1'],
            "password": json_obj['key2']
        }
        '''
        #collection.insert_one(post)
        #print collection.find_one({"username" : "hub"})
        #user = collection.find_one({"username" : "hub"})
        #print x["username"] + 'yo'
        loginUser = collection.find_one({"username": username})
        if loginUser != None and loginUser["password"] == password:
            print 'go user'
            self.redirect("/user") 

        else:
            response_to_send = {}
            response_to_send['newkey'] = 'Incorrect username or password.'
            print('Response to return')
            pprint.pprint(response_to_send)
            self.write(json.dumps(response_to_send))

        '''
        # new dictionary
        response_to_send = {}
        response_to_send['newkey'] = json_obj['key1']
        print('Response to return')
        pprint.pprint(response_to_send)
        self.write(json.dumps(response_to_send))
        '''
class AdminHandler(BaseHandler):
    def get(self):
        deviceInfo = {}
        deviceInfo['temperature'] = '15C'
        deviceInfo['humidity'] = '80%'
        self.render("admin.html", deviceInfo = deviceInfo)

class UserHandler(BaseHandler):
    def get(self):
        deviceInfo = {}
        deviceInfo['temperature'] = '15C'
        deviceInfo['humidity'] = '80%'
        self.render("user.html", deviceInfo = deviceInfo)

class TestHandler(BaseHandler):
    def get(self):
        abc = ["Item 1", "Item 2", "Item 3"]
        self.render("test.html",title="My title", abc = abc)

class AjaxHandler(tornado.web.RequestHandler):
    def get(self):
        example_response = {}
        example_response['name'] = 'example'
        example_response['width'] = 1020

        self.write(json.dumps(example_response))

    def post(self):
        json_obj = json_decode(self.request.body)
        print('Post data received')

        for key in list(json_obj.keys()):
            print('key: %s , value: %s' % (key, json_obj[key]))

        # new dictionary
        response_to_send = {}
        response_to_send['newkey'] = json_obj['key1']
        print('Response to return')
        pprint.pprint(response_to_send)
        self.write(json.dumps(response_to_send))

if __name__ == '__main__':
    main()
