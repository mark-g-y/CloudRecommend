
from jinja2 import Template, Environment, FileSystemLoader
from pymongo import MongoClient
import time
import sys


mongoUri = None


def init(mongouri='mongodb://localhost:27017/'):
    global mongoUri
    mongoUri = mongouri


def get_event_score_map(site):
    mongo = MongoClient(mongoUri)
    sites = mongo.cloudrecommend.site
    site = sites.find_one({'uid' : site})
    event_to_score_map = {}
    for event in site['events']:
        event_to_score_map[event['name']] = event['score']
    mongo.close()
    return event_to_score_map


def get_time_max_limits():
    time_max_limits = []
    adjustments = []
    aff_time_adjust_file = open('affinity_time.txt')
    for line in aff_time_adjust_file.readlines():
        line = line.split(' ')
        time_max_limits.append(long(line[0]))
        adjustments.append(float(line[1]))
    return time_max_limits, adjustments


def generate(site):
    env = Environment(loader=FileSystemLoader('.'))
    template = env.get_template('affinityprocessor_template.py')

    event_to_score_map = get_event_score_map(site)
    time_max_limits, adjustments = get_time_max_limits()

    affinityprocessor = open('affinityprocessor.py', 'wb')
    affinityprocessor.write(template.render(event_to_score_map=event_to_score_map, 
        time_max_limits=time_max_limits, adjustments=adjustments, 
        current_time=int(round(time.time() * 1000))))
    affinityprocessor.close()
