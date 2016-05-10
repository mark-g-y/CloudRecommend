
import os, sys

args = sys.argv

os.system('python affinity_generator.py')
#os.system('pig -x local recommend2.pig')
os.system('pig -x local -param filename={} recommend.pig'.format(args[1]))
