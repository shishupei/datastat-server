#!/bin/bash

# 进入 p3c 目录
cd p3c

# 编译 P3C
mvn clean install

# 回到项目根目录
cd ..

# 运行 PMD 检查
mvn clean test pmd:pmd -Dpmd.rule.file=p3c.xml -Dpmd.exclude.pattern=**/target/**
