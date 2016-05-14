
import time, datetime
import uuid
from mongoengine import Document, EmbeddedDocument
from mongoengine.fields import EmbeddedDocumentField, FloatField, ListField, LongField, StringField, UUIDField


class Event(EmbeddedDocument):
    name = StringField(required=True)
    score = FloatField(required=True)


class Site(Document):
    uid = UUIDField(required=True, unique=True, default=uuid.uuid4(), binary=False)
    title = StringField(required=True)
    events = ListField(EmbeddedDocumentField(Event))
    last_run = LongField(required=True, default=long(round(time.time() * 1000)))
    delay_between_runs = StringField(required=True)

    def last_run_str(self):
        last_run_sec = self.last_run / 1000.0
        return datetime.datetime.fromtimestamp(last_run_sec).strftime('%Y-%m-%d %H:%M:%S')
