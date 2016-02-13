#!/bin/bash

host=134.213.209.158

scp -i ~/.ssh/vote_leave.pub ../web-server/target/web-server-0.0.1-SNAPSHOT.jar forge@134.213.209.158:/home/forge/projects/canvass

scp -r -i ~/.ssh/vote_leave.pub ../web-client/dist forge@134.213.209.158:/home/forge/projects/canvass
scp -r -i ~/.ssh/vote_leave.pub run-web-server.sh forge@134.213.209.158:/home/forge/projects/canvass

