#!/bin/bash

# Web server startup script

DIST_DIR=.
APP_JAR=$DIST_DIR/web-server-0.0.1.jar

PORT=18080
CONTEXT_PATH=/api/canvass

DATA_SOURCE_URL=jdbc:postgresql://localhost:5432/canvassapp
DATA_SOURCE_USER=forge
DATA_SOURCE_PASSWD=$DB_PASSWD

nohup java -Dlogging.level.=ERROR -Dserver.port=$PORT -Dserver.context-path=$CONTEXT_PATH -Dspring.datasource.username=$DATA_SOURCE_USER -Dspring.datasource.password=$DATA_SOURCE_PASSWD -Dspring.datasource.url=$DATA_SOURCE_URL -jar $APP_JAR
