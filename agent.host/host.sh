#!/usr/bin/env bash
JAVA_HOME="jdkpath"

nohup $JAVA_HOME/bin/java -classpath ./scouter.host.jar scouter.boot.Boot ./lib > nohup.out &
sleep 1
tail -100 nohup.out
