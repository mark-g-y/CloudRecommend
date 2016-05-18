from flask import Flask
from mongoengine import connect
from sys import argv
import task_sender

args = argv;
if len(args) == 3:
    # default MongoDB configurations
    connect('cloudrecommend')
    task_sender.start(args[1], int(args[2]))
else:
    connect('cloudrecommend', host=args[1], port=int(args[2]))
    task_sender.start(args[3], int(args[4]))

app = Flask(__name__)


import controllers.sites.sites as sites


BLUEPRINTS = [
    (sites.sites, '/sites')
]


def register_blueprints(app, blueprints):
    for blueprint, url_prefix in blueprints:
        app.register_blueprint(blueprint, url_prefix=url_prefix)

register_blueprints(app, BLUEPRINTS)


app.debug = True
app.secret_key = "testing"

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)