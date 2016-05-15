
import uuid
from flask import (
    render_template,
    url_for,
    redirect,
    request,
    flash,
    Blueprint,
    Flask,
    jsonify
)
from models.models import *
import task_sender

sites = Blueprint('sites', __name__)


@sites.route('/', methods=["GET"])
def view_all():
    sites = Site.objects
    return render_template('sites/view_all.html', sites=sites)


@sites.route('/new', methods=["GET"])
def new_site():
    return render_template('sites/edit.html')


@sites.route('/edit/<uid>', methods=["GET"])
def edit_site(uid):
    site = Site.objects(uid=uid).get()
    return render_template('sites/edit.html', uid=uid, title=site.title, delay_between_runs=site.delay_between_runs, events=site.events)


@sites.route('/edit', methods=['POST'])
def update_site_submit():
    uid = request.form.get('uid')
    title = request.form.get('title')
    delay_between_exec = request.form.get('delayBetweenExec')
    delay_between_exec_dict = {
        'daily' : 1000 * 60 * 60 * 24,
        'weekly' : 1000 * 60 * 60 * 24 * 7,
        'biweekly' : 1000 * 60 * 60 * 24 * 14
    }

    event_names = request.form.getlist('events[name]') + request.form.getlist('newevents[name]')
    event_scores = request.form.getlist('events[score]') + request.form.getlist('newevents[score]')
    events = []
    for i in range(0, len(event_names)):
        events.append(Event(name=event_names[i], score=float(event_scores[i])))

    sites = Site.objects(uid=uid)
    if len(sites) == 0:
        uid = uuid.uuid4()
        site = Site(uid=uid)
        new_task = True
    else:
        site = sites.get()
        new_task = False

    site.title = title
    site.events = events
    site.delay_between_runs = delay_between_exec
    site.save()

    if new_task:
        # can only do this after site is already saved to avoid race conditions
        task_sender.add_task(str(uid), delay_between_exec_dict[delay_between_exec])

    flash('Success!')
    return redirect(url_for('sites.view_all'))
