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

nohup java -d64 -Xms512m -Xmx4g -Dlogging.level.=ERROR -Dlogging.level.com.infinityworks=ERROR -Dserver.port=$PORT -Dserver.context-path=$CONTEXT_PATH -Dspring.datasource.username=$DATA_SOURCE_USER -Dspring.datasource.password=$DATA_SOURCE_PASSWD -Dspring.datasource.url=$DATA_SOURCE_URL -jar $APP_JAR
