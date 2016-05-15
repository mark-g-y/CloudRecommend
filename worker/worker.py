
import sys
import socket
import json
import recommendation

args = sys.argv
schedulerhost = args[1]
schedulerport = int(args[2])
hdfshost = args[3]
hdfsport = int(args[4])
hbasehost = args[5]
hbaseport = int(args[6])
mongohost = args[7]
mongoport = int(args[8])

BUFFER_SIZE = 1024

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((schedulerhost, schedulerport))

recommendation.init(hdfsuri='hdfs://{}:{}/'.format(hdfshost, hdfsport), 
    hbaseuri='{}:{}'.format(hbasehost, hbaseport), 
    mongouri='mongodb://{}:{}/'.format(mongohost, mongoport))

while True:
    s.sendall(json.dumps({'message' : 'worker_ready'}) + '\n')
    messageobj = json.loads(s.recv(BUFFER_SIZE))
    message = messageobj['message']
    print('received message' + str(message))
    if message == 'new_task':
        group_id = messageobj['group_id']
        recommendation.run(group_id)

s.close()
