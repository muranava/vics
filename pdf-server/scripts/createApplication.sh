#!/usr/bin/env bash

# Copies the deployment artifacts to the deployment
# and initialise as a linux service (supported by Spring boot by default
# see https://docs.spring.io/spring-boot/docs/current/reference/html/deployment-install.html#deployment-service)

function error_exit
{
	echo "$1" 1>&2
	exit 1
}

chmod +x /tmp/pdf-server-0.0.1.jar
mkdir -p /var/tmp
cp -f /tmp/pdf-server-0.0.1.jar /var/tmp/pdf-server.jar

if [ "$DEPLOYMENT_GROUP_NAME" == "StagingWebPDF" ]
then
    aws s3 cp s3://993854-config-staging/pdf-server/staging.properties /var/tmp/application.properties
    aws s3 cp s3://993854-config-staging/pdf-server/logback.xml /var/tmp/logback.xml
    cp -f /tmp/scripts/pdf-server-0.0.1.conf /var/tmp/pdf-server.conf
elif [ "$DEPLOYMENT_GROUP_NAME" == "ProductionWebPDF" ]
then
    aws s3 cp s3://993854-config-production/pdf-server/production.properties /var/tmp/application.properties
    aws s3 cp s3://993854-config-production/pdf-server/logback.xml /var/tmp/logback.xml
    cp -f /tmp/scripts/pdf-server-0.0.1.conf /var/tmp/pdf-server.conf
else
    error_exit "Could not determine environment!  Aborting. DEPLOYMENT_GROUP_NAME=$DEPLOYMENT_GROUP_NAME"
fi

ln -fs /var/tmp/pdf-server.jar /etc/init.d/pdf-server
update-rc.d pdf-server defaults
