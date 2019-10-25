#!/bin/bash

mvn clean install -f $1/pom.xml
java -jar $1/target/lmsadmin-0.0.1-SNAPSHOT.jar 

