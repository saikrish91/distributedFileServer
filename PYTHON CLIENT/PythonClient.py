import common_pb2
import socket
import time
import struct
import pipe_pb2
import shlex
import subprocess
import sys
import common_pb2
import pipe_pb2
import cPickle
import sys
import os
from ClientFunction import ClientFunction



class PythonClient:
    def run(self):
        print("Enter the IP of Server ");
        # host = raw_input();
        print("Enter the port ");
        # port = int(raw_input());

        port = 4568;
        #host of leader
        host = "169.254.203.33"
        host = "127.0.0.1"
        bc = ClientFunction(host, port)
        bc.startSession()
        # bc.join(name)
        #so = socket.socket()
        #so.connect((host, port))

        currfileDir = os.path.dirname(os.path.realpath('__file__'))
        print currfileDir

        path = "C:\\Users\\mukul\\git\\fluffyglobal\\png\\"
        outputDir = "C:\\Users\\mukul\\git\\fluffyglobal\\output\\"


        print("Menu: \n");
        print("1. Write file \n");
        print("2. Update the file \n");
        print("3. Read \n");
        print("4. Delete \n");

        print("6.) exit - end session\n");
        print("\n")
        choice1 = raw_input("Please enter the choice.\n")

        forever = True;
        while (forever):

            if (choice1 == None):
                continue;
            elif choice1 == "6":
                print("Session stopped")
                bc.stopSession()
                forever = False
            elif choice1 == "1":
                # POST
                print("Enter the name of your file: ")

                filename = raw_input();
                print("wRITE file")

                #print("Enter qualified pathname of file to be uploded: ");
                path = raw_input();
                path = path + filename
                fileChunks = bc.chunkFile(path, filename,"WRITE")
                forever=False
            elif choice1 == "2":
                # PUT
                print("Enter the name of your file: ")

                filename = raw_input();
                print("update file")

                #print("Enter qualified pathname of file to be uploded: ");
                # path = raw_input();
                path = path + filename
                fileChunks = bc.chunkFile(path, filename,"UPDATE")
                forever = False
            elif choice1 == "3":
                # GET
                print("Enter the name of your file: ")

                filename = raw_input();
                print("Get file")

                bc.getFile(filename)
                forever = False
                #put the failure condition
            elif choice1 == "4":
                # delete
                print("Enter the name of your file: ")

                filename = raw_input();
                print("Delete file")

                bc.deleteFile(filename)
                forever = False


if __name__ == '__main__':

    #host = "169.254.203.33"
    host = "127.0.0.1"
    port = 4668
    ca = PythonClient();
    ca.run();
