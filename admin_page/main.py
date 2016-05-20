from flask import Flask
from mongoengine import connect
from parseutils import isint
from sys import argv
import task_sender

args = argv;
if len(args) == 3 and isint(args[2]):
    # default MongoDB configurations
    connect('cloudrecommend')
    task_sender.start(args[1], int(args[2]))
elif len(args) == 5 and isint(args[2]) and isint(args[4]):
    connect('cloudrecommend', host=args[3], port=int(args[4]))
    task_sender.start(args[1], int(args[2]))
else:
    print('ERROR - arguments are <scheduler_host> <scheduler_port> <mongodb_host (optional)> <mongodb_port (optional)>')
    exit(1)

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