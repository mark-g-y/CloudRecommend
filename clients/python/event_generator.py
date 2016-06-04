
from kafka import KafkaProducer
import json

producer = KafkaProducer(value_serializer=lambda v: json.dumps(v))
site_id = None


def init(_site_id):
    global site_id
    site_id = _site_id


def event(user, item, event):
    event_message = {
        'site_id' : site_id,
        'user' : user,
        'item' : item,
        'event' : event
    }
    producer.send('events', event_message).get(timeout=5)
