
import thread
import socket
import json
from Queue import Queue


queue = Queue()


def send_tasks(receiver_host, receiver_port, queue):
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    while True:
        task = queue.get(block=True)
        try:
            s.connect((receiver_host, receiver_port))
            s.sendall(task + '\n')
            s.close()
        except:
            # this means receiver is down
            # no worries, receiver will query DB for updated info on restart
            print('error connecting')


def add_task(site, delay_between_runs):
    queue.put(json.dumps({'message':'new_task', 'site':site, 'delayBetweenExec':delay_between_runs}))


def start(receiver_host, receiver_port):
    thread.start_new_thread(send_tasks, (receiver_host, receiver_port, queue))
