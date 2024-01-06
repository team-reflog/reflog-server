#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/app"
JAR_FILE="$PROJECT_ROOT/build/libs/reflog-server.jar"

APP_LOG="$PROJECT_ROOT/application.log"
ERROR_LOG="$PROJECT_ROOT/error.log"

cp $PROJECT_ROOT/build/libs/*.jar $JAR_FILE
nohup java -jar -Dspring.profiles.active=dev $JAR_FILE > $APP_LOG 2> $ERROR_LOG &

