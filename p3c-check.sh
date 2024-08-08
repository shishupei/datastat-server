#!/bin/bash

mvn clean test pmd:pmd -Dpmd.rule.file=p3c.xml -Dpmd.exclude.pattern=**/target/**
