
import os, sys
import socket
import json

args = sys.argv
schedulerhost = args[1]
schedulerport = int(args[2])
hdfsuri = 'hdfs://{}/'.format(args[3])

BUFFER_SIZE = 1024

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((schedulerhost, schedulerport))

while True:
    s.sendall(json.dumps({'message' : 'worker_ready'}) + '\n')
    messageobj = json.loads(s.recv(BUFFER_SIZE))
    message = messageobj['message']
    if message == 'new_task':
        group_id = messageobj['group_id']
        os.system('python affinity_generator.py')
        os.system('pig -x local -param filename={}{} recommend.pig'.format(hdfsuri, group_id))

s.close()
