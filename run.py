
import os, time
import subprocess

#while True:
os.system('python affinity_generator.py')
os.system('pig -x local recommend.pig2')
exit()

subprocess.call(['pig'])

time.sleep(5)