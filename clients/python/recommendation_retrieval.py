
import requests
import json


uri = None
site = None


def init(_uri, site_id):
    global uri
    global site
    uri = _uri
    site = site_id


def get_for_user(user):
    r = requests.get('{}/recommendation/user'.format(uri), params={'user':user, 'site':site})
    return r.json()


def get_for_item(item):
    r = requests.get('{}/recommendation/item'.format(uri), params={'item':item, 'site':site})
    return r.json()
