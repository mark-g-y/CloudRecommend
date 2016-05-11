
from jinja2 import Template, Environment, FileSystemLoader
import time

event_to_score_map = {}
affinityfile = open('affinity.txt')
for line in affinityfile.readlines():
    line = line.split(' ')
    event_to_score_map[line[0]] = float(line[1])

time_max_limits = []
adjustments = []
aff_time_adjust_file = open('affinity_time.txt')
for line in aff_time_adjust_file.readlines():
    line = line.split(' ')
    time_max_limits.append(long(line[0]))
    adjustments.append(float(line[1]))

env = Environment(loader=FileSystemLoader('.'))
template = env.get_template('affinityprocessor_template.py')

affinityprocessor = open('affinityprocessor.py', 'wb')
affinityprocessor.write(template.render(event_to_score_map=event_to_score_map, time_max_limits=time_max_limits, adjustments=adjustments, current_time=int(round(time.time() * 1000))))
affinityprocessor.close()
