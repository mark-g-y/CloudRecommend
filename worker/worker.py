
import sys
import socket
import json
import recommendation

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
        recommendation.run(hdfsuri, group_id)

s.close()
