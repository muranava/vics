#!/usr/bin/env bash

cp -f /tmp/web-server-0.0.1.jar /var/tmp/vics.jar

cp -f /tmp/web-server-0.0.1.conf /var/tmp/vics.conf

ln -fs /var/tmp/vics.jar /etc/init.d/vics
