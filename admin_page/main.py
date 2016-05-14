from flask import Flask
from mongoengine import connect
from sys import argv

args = argv;
if len(args) == 1:
    # default MongoDB configurations
    connect('recommendengineaas')
else:
    connect('recommendengineaas', host=args[1], port=int(args[2]))

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