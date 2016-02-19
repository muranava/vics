#!/bin/bash

# Web server startup script

DIST_DIR=../../dist/
APP_JAR=$DIST_DIR/web-server-0.0.1-SNAPSHOT.jar

PORT=18080
CONTEXT_PATH=/api/canvass

DATA_SOURCE_URL=jdbc:postgresql://localhost:5432/canvass
DATA_SOURCE_USER=forge
DATA_SOURCE_PASSWD=

java -Dserver.port=$PORT -Dserver.context-path=$CONTEXT_PATH -Dspring.datasource.username=$DATA_SOURCE_USER -Dspring.datasource.password=$DATA_SOURCE_PASSWD -Dspring.datasource.url=$DATA_SOURCE_URL -jar $APP_JAR
