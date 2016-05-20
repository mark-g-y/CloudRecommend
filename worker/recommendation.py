
import os
import happybase
import affinity_generator


hdfsUri = None
hbaseZkUri = None
mongoUri = None


def init(hdfsuri='hdfs://localhost:9000/', hbasezkuri='localhost:9090', mongouri='mongodb://localhost:27017/'):
    global hdfsUri, hbaseZkUri, mongoUri
    hdfsUri = hdfsuri + 'cloudrecommend/'
    hbaseZkUri = hbasezkuri
    mongoUri = mongouri


def run(site):
    print('running '  + site)
    hbconn = happybase.Connection(hbaseZkUri.split(':')[0], int(hbaseZkUri.split(':')[1]))
    site_uii = site + '__uii'
    site_itoi = site + '__itoi'

    try:
        hbconn.create_table(site_uii, {'uii':dict()})
        hbconn.create_table(site_itoi, {'itoi':dict()})
    except:
        # no worries - continue
        print('table already exists')
    hbconn.close()
    
    affinity_generator.init(mongouri=mongoUri)
    affinity_generator.generate(site)
    #<TODO> switch from local to mapreduce mode in production
    os.system('pig -x local -param filename={}{} -param hbase_zk_uri={} -param site_uii={} -param site_itoi={} recommend.pig'.format(hdfsUri, site, hbaseZkUri, site_uii, site_itoi))
