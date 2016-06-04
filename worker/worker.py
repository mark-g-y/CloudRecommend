
import sys
import socket
import json
import recommendation
from parseutils import isint

args = sys.argv

if len(args) != 9 or not isint(args[2]) or not isint(args[4]) or not isint(args[6]) or not isint(args[8]):
    print('ERROR - arguments are <scheduler_host> <scheduler_port> <hdfs_host> <hdfs_port> <hbase_host> <hbase_port> <mongodb_host> <mongodb_port>')
    exit(1)

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
    hbasezkuri='{}:{}'.format(hbasehost, hbaseport), 
    mongouri='mongodb://{}:{}/'.format(mongohost, mongoport))

while True:
    s.sendall(json.dumps({'message' : 'worker_ready'}) + '\n')
    messageobj = json.loads(s.recv(BUFFER_SIZE))
    message = messageobj['message']
    print('received message' + str(message))
    if message == 'new_task':
        site_id = messageobj['site_id']
        recommendation.run(site_id)

s.close()
