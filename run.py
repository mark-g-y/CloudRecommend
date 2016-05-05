
import os, time
import subprocess

while True:
    os.system('python affinity_generator.py')

    subprocess.call(['pig'])

    time.sleep(5)