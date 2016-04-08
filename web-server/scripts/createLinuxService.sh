#!/usr/bin/env bash

# Copies the deployment artefacts to the deployment
# and initialise as a linux service (supported by Spring boot by default
# see https://docs.spring.io/spring-boot/docs/current/reference/html/deployment-install.html#deployment-service)

chmod +x /tmp/web-server-0.0.1.jar
mkdir -p /var/tmp
cp -f /tmp/web-server-0.0.1.jar /var/tmp/vics.jar
cp -f /tmp/web-server-0.0.1.conf /var/tmp/vics.conf
cp -f /tmp/target.properties /var/tmp/application.properties
ln -fs /var/tmp/vics.jar /etc/init.d/vics
update-rc.d vics defaults
