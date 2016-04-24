#!/usr/bin/env bash

# Copies the deployment artefacts to the deployment
# and initialise as a linux service (supported by Spring boot by default
# see https://docs.spring.io/spring-boot/docs/current/reference/html/deployment-install.html#deployment-service)

function error_exit
{
	echo "$1" 1>&2
	exit 1
}

chmod +x /tmp/web-server-0.0.1.jar
mkdir -p /var/tmp
cp -f /tmp/web-server-0.0.1.jar /var/tmp/vics.jar
cp -f /tmp/web-server-0.0.1.conf /var/tmp/vics.conf

if [ "$DEPLOYMENT_GROUP_NAME" == "Production" ]
then
    cp -f /tmp/production.properties /var/tmp/application.properties
elif [ "$DEPLOYMENT_GROUP_NAME" == "Staging" ]
then
    cp -f /tmp/staging.properties /var/tmp/application.properties
elif [ "$DEPLOYMENT_GROUP_NAME" == "Development" ]
then
    cp -f /tmp/development.properties /var/tmp/application.properties
else
    error_exit "Could not determine environment!  Aborting."
fi

ln -fs /var/tmp/vics.jar /etc/init.d/vics
update-rc.d vics defaults
