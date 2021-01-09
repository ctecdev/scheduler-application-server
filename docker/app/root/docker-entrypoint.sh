#!/bin/bash

JAVA_ARGS=""

JAVA_ARGS="${JAVA_ARGS} -jar ${JAR_FILE} --server.port=8080"
#JAVA_ARGS="${JAVA_ARGS} --spring.profiles.active=${APP_CONTEXT}"
#JAVA_ARGS="${JAVA_ARGS} --spring.config.location=/app/confs/"

echo "> Executing: java ${JAVA_ARGS} "
java ${JAVA_ARGS} 
