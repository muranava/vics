#!/usr/bin/env python

import urllib2, json, sys, time

''' Validate the install using the app health check service '''

health_endpoint = "http://localhost:18081/admin/health"

def health_check():
    return urllib2.urlopen(health_endpoint)

def check_if_running(attempt_num, num_tries, check_interval):
    try:
        response = health_check()
        if response.getcode() == 200:
            print '[Running]'
        else:
            check_if_running()
    except:
        if attempt_num > num_tries:
            sys.exit(1)
        else:
            attempt_num += 1
            print "App not up, retrying (attempt={})".format(attempt_num)
            time.sleep(check_interval)
            check_if_running(attempt_num, num_tries, check_interval)
    sys.exit(0)

if __name__ == '__main__':
    attempt_num = 0
    num_tries = 9
    check_interval = 10
    check_if_running(attempt_num, num_tries, check_interval)