#!/bin/bash

# Download p3c.jar if it doesn't exist
if [ ! -f "static/p3c.jar" ]; then
  wget https://github.com/alibaba/p3c/releases/download/p3c-2.2.0/p3c.jar
  mvn install:install-file -Dfile=p3c.jar -DgroupId=com.alibaba.p3c -DartifactId=p3c -Dversion=2.2.0 -Dpackaging=jar
fi

# Run P3C check
mvn clean test pmd:pmd -Dpmd.rule.file=p3c.xml -Dpmd.exclude.pattern=**/target/**
