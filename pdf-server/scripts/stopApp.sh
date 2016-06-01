#!/usr/bin/env bash

if [ -f /etc/init.d/pdf-server ]; then
    service pdf-server stop
fi
