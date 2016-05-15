
import os
import happybase
import affinity_generator


hdfsUri = None
hbaseUri = None
mongoUri = None


def init(hdfsuri='hdfs://localhost:9000/', hbaseuri='localhost:9090', mongouri='mongodb://localhost:27017/'):
    global hdfsUri, hbaseUri, mongoUri
    hdfsUri = hdfsuri
    hbaseUri = hbaseuri
    mongoUri = mongouri


def run(group):
    print('running '  + group)
    hbconn = happybase.Connection(hbaseUri.split(':')[0], int(hbaseUri.split(':')[1]))
    group_uii = group + '__uii'
    group_itoi = group + '__itoi'

    try:
        hbconn.create_table(group_uii, {'uii':dict()})
        hbconn.create_table(group_itoi, {'itoi':dict()})
    except:
        # no worries - continue
        print('table already exists')
    hbconn.close()
    
    affinity_generator.init(mongouri=mongoUri)
    affinity_generator.generate(group)
    #<TODO> switch from local to mapreduce mode in production
    os.system('pig -x local -param filename={}{} -param group_uii={} -param group_itoi={} recommend.pig'.format(hdfsUri, group, group_uii, group_itoi))
