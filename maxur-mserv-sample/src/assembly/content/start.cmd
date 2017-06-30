@echo on

set JAVA_OPTS=-Xmx1024M -Xms1024M -Xss1M

set HMS_CLASSPATH=.\lib\*;.\conf

set JAVA_OPTS=%JAVA_OPTS% -Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,address=2785,server=y,suspend=n -Djava.compiler=NONE

"%JAVA_HOME%"\bin\java %JAVA_OPTS% -cp "%HMS_CLASSPATH%" org.maxur.mserv.sample.Launcher