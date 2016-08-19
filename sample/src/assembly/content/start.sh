#!/bin/bash

JAVA_HOME=~/opt/jre1.8

JAVA_OPTS="-Xmx2048M -Xms1024M -Xss1M"

HMS_CLASSPATH=./lib/*:./conf

#declare quiet="false"

# jmx
JMX_OPTS="${JAVA_OPTS} -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.port=2789 -Djava.rmi.server.hostname=127.0.0.1"

# debug
JAVA_OPTS="${JAVA_OPTS} -Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,address=2785,server=y,suspend=n -Djava.compiler=NONE"

rm -R log

${JAVA_HOME}/bin/java $JAVA_OPTS -cp "${HMS_CLASSPATH}" org.maxur.ddd.Launcher
