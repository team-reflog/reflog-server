#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/app"
JAR_FILE="$PROJECT_ROOT/build/libs/reflog-server.jar"
CURRENT_PID=$(pgrep -f $JAR_FILE)

if [ps -p $CURRENT_PID > /dev/null]; then
    kill -15 $CURRENT_PID
fi
