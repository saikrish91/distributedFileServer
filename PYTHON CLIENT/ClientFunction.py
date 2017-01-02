import socket
import common_pb2
import pipe_pb2

from asyncore import read
import struct
import time
import sys
from datetime import datetime

class ClientFunction:
    def __init__(self,host,port):
        self.host = host
        self.port = port
        self.sd  = socket.socket(socket.AF_INET, socket.SOCK_STREAM)


    def startSession(self):
        self.sd.connect(("127.0.0.1", 4568))
        print("Connected to Host:", self.host, "@ Port:", self.port)


    def read_in_chunks(self,file_object,chunk_size=1024 * 1024):
        while True:
            data = file_object.read(chunk_size)
            if not data:
                break
            yield data

    def chunkFile(self,file, filename, req):
        fileChunk = []
        #divide chunks in 1mb size
        file = "C:\\Users\\mukul\\git\\fluffyglobal\\png\\DSC_0607.jpg"
        print file
        f = open(file)
        i=0
        for piece in self.read_in_chunks(f):
            i=i+1
            fileChunk.append(piece)

        self.process_data(filename,fileChunk,i, req)


    def process_data(self, filename, chunks, totalchunks, req):
        chunkid=0
        for chunk in chunks:
            data = self.convChunkedMsg(filename, chunk, totalchunks, chunkid,req);
            chunkid += 1
            result = self.sendData(data , self.host, self.port)

    def convChunkedMsg(self, filename, filecontent, noofchunks, chunkId,req):
        cm = pipe_pb2.CommandMessage()
        cm.globalHeader.cluster_id =3
        cm.globalHeader.time = 10000
        cm.globalHeader.destination_id = 6
        cm.request.requestId ="2"
        if(req.upper()=="WRITE"):
         cm.request.requestType = pipe_pb2.RequestType.Value("WRITE")
        if (req.upper() == "UPDATE"):
            cm.request.requestType = pipe_pb2.RequestType.Value("UPDATE")
        cm.request.fileName = filename
        cm.request.file.chunkId = chunkId;
        cm.request.file.data = filecontent
        cm.request.file.filename = filename
        cm.request.file.chunkCount = noofchunks
        print cm
        var = cm.SerializeToString()
        return var

    def getFile(self, filename):
        cm = pipe_pb2.CommandMessage()
        cm.globalHeader.cluster_id =3
        cm.globalHeader.time = 10000
        cm.globalHeader.destination_id = 6
        cm.request.requestId ="2"
        cm.request.requestType = pipe_pb2.RequestType.Value("READ")
        cm.request.fileName = filename
        var = cm.SerializeToString()
        result = self.sendNreadData(var, self.host, self.port)

    def deleteFile(self, filename):
        cm = pipe_pb2.CommandMessage()
        cm.globalHeader.cluster_id =3
        cm.globalHeader.time = 10000
        cm.globalHeader.destination_id = 6
        cm.request.requestId ="2"
        print (filename)
        print ("116")
        cm.request.requestType = pipe_pb2.RequestType.Value("DELETE")
        cm.request.file.filename = filename
        var = cm.SerializeToString()
        result = self.sendData(var, self.host, self.port)

    def sendData(self, data, host, port):
        msg_len = struct.pack('>L', len(data))
        self.sd.sendall(msg_len + data)
        self.sd.close
        return

    def sendNreadData(self,data,host,port):
        msg_len = struct.pack('>L', len(data))
        self.sd.sendall(msg_len + data)
        len_buf = self.receiveMsg(self.sd, 4)
        msg_len = struct.unpack('>L', len_buf)[0]
        msg_in = self.receiveMsg(self.sd, msg_len)
        r = pipe_pb2.CommandMessage();
        msg_read = r.ParseFromString(msg_in)
        count = msg_read.request.file.chunkCount
        print count
        filestack = range(count)
        filestack[msg_read.request.file.chunkId] = msg_read
        start=1
        filename = r.request.file.filename
        path = "C:\\Users\\mukul\\git\\fluffyglobal\\png\\"

        msg_len = struct.pack('>L', len(data))

        while start<count :
            len_buf = self.receiveMsg(self.sd, 4)
            msg_len = struct.unpack('>L', len_buf)[0]
            msg_in = self.receiveMsg(self.sd, msg_len)
            cl = pipe_pb2.CommandMessage();
            parsedmsg = cl.ParseFromString(msg_in)
            countid = parsedmsg.request.file.chunkId
            filestack[countid] = parsedmsg.request.file.data
            start= start+1
        result = self.mergefile(filestack)
        with open((path).append(filename), "w") as outfile:
          outfile.write(result)

        self.sd.close
        return r

    def receiveMsg(socket, n):
        buf = ''
        while n > 0:
            data = socket.recv(n)
            if data == '':
                raise RuntimeError('data not received!')
            buf += data
            n -= len(data)
        return buf

    def mergefile(self, stack):
        finaldata = ""
        for f in stack:
         finaldata.append(f)
        return ''.join(finaldata)


