#!/bin/bash

# Web server startup script

DIST_DIR=.
APP_JAR=$DIST_DIR/web-server-0.0.1.jar

PORT=18080
CONTEXT_PATH=/api/canvass

DATA_SOURCE_URL=$DB_URL
DATA_SOURCE_USER=$DB_USER
DATA_SOURCE_PASSWD=$DB_PASSWD

PAF_API_URL=$PAF_URL
PAF_API_PASSWD=$PAF_PW

LOGSTASH_URL=$LS_URL
LOGSTASH_PORT=$LOGSTASH_PORT

nohup java -d64 -Xms512m -Xmx4g -Dlogging.level.=INFO -Dlogging.level.com.infinityworks=DEBUG \
    -Dserver.port=$PORT -Dserver.context-path=$CONTEXT_PATH \
    -Dspring.datasource.username=$DATA_SOURCE_USER -Dspring.datasource.password=$DATA_SOURCE_PASSWD -Dspring.datasource.url=$DATA_SOURCE_URL \
    -Dcanvass.pafApiBaseUrl=$PAF_API_URL -Dcanvass.pafApiToken=$PAF_API_PASSWD \
    -Dlogstash.url=$LOGSTASH_URL -Dlogstash.port=$LOGSTASH_PORT \
    -jar $APP_JAR
