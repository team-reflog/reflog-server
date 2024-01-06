#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/app"
JAR_FILE="$PROJECT_ROOT/build/libs/reflog-server.jar"

cp $PROJECT_ROOT/build/libs/*.jar $JAR_FILE
nohup java -jar -Dspring.profiles.active=dev $JAR_FILE &

