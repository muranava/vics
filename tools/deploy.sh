#!/bin/bash

host=134.213.209.158

scp -r -i ~/.ssh/vote_leave.pub ../dist forge@134.213.209.158:/home/forge/projects/canvass

