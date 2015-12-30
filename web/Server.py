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
#140.112.91.221

#DB_IP = "localhost"
DB_IP = "140.112.91.221"
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
            (r"/android", AndroidHandler),
            (r"/admin", AdminHandler),
            (r"/user", UserHandler),
            (r"/test", TestHandler),
            (r"/testajax", AjaxHandler),
        ]

        settings = dict(
            template_path=os.path.join(os.path.dirname(__file__), "templates"),
            static_path=os.path.join(os.path.dirname(__file__), "static"),
            cookie_secret = "bZJc2sWbQLKos6GkHn/VB9oXwQt8S0R0kRvJ5/xJ89E=",
            login_url = '/login',
        )
        super(Application, self).__init__(handlers, **settings)

class BaseHandler(tornado.web.RequestHandler):
    def get_current_user(self):
        return self.get_secure_cookie("user")

class IndexHandler(BaseHandler):
    def get(self):
        createMessage = ""
        abc = ["Item 1", "Item 2", "Item 3"]
        self.render("index.html", abc = abc, createMessage = createMessage)

    def post(self):
        newusername = self.get_argument("newusername")
        newpassword = self.get_argument("newpassword")
        print newusername, newpassword
        newUser = collection.find_one({"username": newusername})
        if newUser == None:
            post = {
            "username": newusername,
            "password": newpassword
            }
            collection.insert_one(post)
            createMessage = "new user"
        else:
            createMessage = "old user"
        self.render("index.html", createMessage = createMessage)


class LoginHandler(BaseHandler):
    def get(self):
        incorrectMessage = ""
        self.render("login.html", incorrectMessage = incorrectMessage)

    @gen.coroutine
    def post(self):
        username = self.get_argument('username')
        password = self.get_argument('password')
        print username, password
        print 'Post data received'

        loginUser = collection.find_one({"username": username})
        if username == 'admin' and password == 'admin':
            self.set_current_admin('admin')
            self.redirect("/admin")
        elif loginUser != None and loginUser["password"] == password:
            print 'go user'
            self.set_current_user(username)
            self.redirect("/user") 

        else:
            incorrectMessage = "Incorrect username or password."
            self.render("login.html", incorrectMessage = incorrectMessage)

    def set_current_admin(self, admin):
        if user:
            self.set_secure_cookie('admin', tornado.escape.json_encode(admin))
        else:
            self.clear_cookie('admin')

    def set_current_user(self, user):
        if user:
            self.set_secure_cookie('user', tornado.escape.json_encode(user))
        else:
            self.clear_cookie('user')

class AndroidHandler(BaseHandler):
    def __init__(self, application, request, **kwargs):
        self.deviceInfo = {}
        self.deviceInfo['temperature'] = '17C'
        self.deviceInfo['humidity'] = '80%'
        self.userInfo = []
        super(AndroidHandler, self).__init__(application, request, **kwargs)

    def get(self):
        self.write(json.dumps(self.deviceInfo)) 
        #self.write(self.deviceInfo)

    def post(self):
        self.write(json.dumps(self.deviceInfo)) 

class AdminHandler(BaseHandler):
    def __init__(self, application, request, **kwargs):
        self.deviceInfo = {}
        self.deviceInfo['temperature'] = '16C'
        self.deviceInfo['humidity'] = '80%'
        self.userInfo = []
        super(AdminHandler, self).__init__(application, request, **kwargs)

    @tornado.web.authenticated
    def get(self):
        for post in collection.find():
            self.userInfo.append(post)

        self.render("admin.html", userInfo = self.userInfo, deviceInfo = self.deviceInfo)

    def post(self):
        for post in collection.find():
            self.userInfo.append(post)
        delusername = self.get_argument('delusername')
        result = collection.delete_one({"username": delusername})
        #collection.delete_many({})
        self.render("admin.html", userInfo = self.userInfo, deviceInfo = self.deviceInfo)

class UserHandler(BaseHandler):
    @tornado.web.authenticated
    def get(self):
        deviceInfo = {}
        deviceInfo['temperature'] = '15C'
        deviceInfo['humidity'] = '80%'
        self.render("user.html", deviceInfo = deviceInfo)

class TestHandler(BaseHandler):
    def get(self):
        abc = ["Item 1", "Item 2", "Item 3"]
        self.render("test.html",title="My title", abc = abc)

class ErrorHandler(BaseHandler):  
    def get(self):                               
        self.render("error.html")

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
