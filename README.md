# CassandraUpgradeHelper

This is an scala sbt stand alone app.
It is created to simulate basic operation on cassandra database.

Number of read/write can be configured in `application.conf` file

Purpose of creating this app is use while doing some maitainance activity on cassandra like:
* Upgrade
* Migration
* Add new node to clusture
* Remove a node from cluster

etc
  
## Step to run this app
### Without creating a jar:-
* clone the project and run command `sbt run` after doing required config changes.
### With jar:
* clone the project
* run command `sbt one-jar` - it will generate jar files in target directory
* copy `cassandraupgradehelper_2.11-0.1-one-jar.jar` file 
* Create/copy the `application.conf` file 
* create `run` file and paste following line into it
```
#!/bin/sh

export SBT_OPTS="-Xmx2G -XX:+UseConcMarkSweepGC -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=2G -Xss2M"

java -Dconfig.file=application.conf -jar cassandraupgradehelper_2.11-0.1-one-jar.jar &> logs/application.out &

tailf logs/application.out

```
* execute command `chmod 777 run` - make run file executable
* execute `./run` - it will start the application

  
