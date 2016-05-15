
import os
import happybase

def run(hdfsuri, group):
    hbconn = happybase.Connection()
    group_uii = group + '__uii'
    group_itoi = group + '__itoi'

    try:
        hbconn.create_table(group_uii, {'uii':dict()})
        hbconn.create_table(group_itoi, {'itoi':dict()})
    except:
        # no worries - continue
        print('table already exists')
    hbconn.close()
    
    os.system('python affinity_generator.py')
    #<TODO> switch from local to mapreduce mode in production
    os.system('pig -x local -param filename={}{} -param group_uii={} -param group_itoi={} recommend.pig'.format(hdfsuri, group, group_uii, group_itoi))
