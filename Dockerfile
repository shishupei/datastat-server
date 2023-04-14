FROM ubuntu:20.04

ARG NEW_YEAR_USER
ARG NEW_YEAR_2022
ARG BRANCH

MAINTAINER zhongjun <jun.zhongjun2@gmail.com>

RUN mkdir -p /var/lib/ds
WORKDIR /var/lib/ds

RUN apt-get update && \
    apt-get install --yes software-properties-common

RUN apt install --yes openjdk-17-jdk
RUN apt-get install --yes wget
RUN apt-get install --yes git

RUN wget https://dlcdn.apache.org/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz && \
        tar -xzvf apache-maven-3.6.3-bin.tar.gz
ENV MAVEN_HOEM=/var/lib/ds/apache-maven-3.6.3
ENV PATH=$MAVEN_HOEM/bin:$PATH

RUN git clone -b ${BRANCH} https://github.com/opensourceways/datastat-server && \
        cd datastat-server && \
        mvn clean install package -Dmaven.test.skip && \
        mv ./target/ds-0.0.1-SNAPSHOT.jar ../ds.jar

RUN git clone https://${NEW_YEAR_USER}@gitee.com/lixianlin01/new-year.git
RUN git clone https://${NEW_YEAR_2022}@gitee.com/kaede10/mydata.git
RUN git clone https://gitee.com/opensourceway/om-data.git

CMD java -jar ds.jar
