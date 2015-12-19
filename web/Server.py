import tornado.httpserver
import tornado.websocket
import tornado.ioloop
import tornado.web
import socket

class WSHandler(tornado.websocket.WebSocketHandler):
    def open(self):
        print 'new connection'
      
    def on_message(self, message):
        print 'message received:  %s' % message
        # Reverse Message and send it back
        print 'sending back message: %s' % str(message) + ' yo'
        self.write_message(str(message) + ' yo')
 
    def on_close(self):
        print 'connection closed'
 
    def check_origin(self, origin):
        return True
 
application = tornado.web.Application([
    (r'/ws', WSHandler),
])
 
 
if __name__ == "__main__":
    http_server = tornado.httpserver.HTTPServer(application)
    http_server.listen(8888)
    myIP = socket.gethostbyname(socket.gethostname())
    print '-----Websocket Server IP: %s-----' % myIP
    tornado.ioloop.IOLoop.instance().start()
