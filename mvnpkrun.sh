#!/bin/bash

mvn clean package
java -jar ./target/lmsadmin-0.0.1-SNAPSHOT.jar 

