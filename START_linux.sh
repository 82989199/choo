#! /bin/sh

java -Xms256m -Xmx256m -XX:-UseParallelGC -XX:+AggressiveOpts -XX:+UseConcMarkSweepGC -cp lib/json-simple-1.1.1.jar:lib/c3p0-0.9.2-pre5.jar:lib/mchange-commons-java-0.2.3.jar:lib/mysql-connector-java-5.1.22-bin.jar:lib/netty-3.5.8.Final.jar:lib/org.eclipse.swt.gtk.linux.x86_64-4.3.jar:server.jar sp.Main gui


echo Pause ...
read
