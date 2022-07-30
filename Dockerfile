FROM maven:3.6.0-jdk-11-slim AS build

RUN apt-get update && \
    apt-get install -y git mongodb

COPY . /webprotege

WORKDIR /webprotege

ARG SKIP_TEST=true

RUN mkdir -p /data/db \
    && mongod --fork --syslog \
    && mvn clean package -Dmaven.test.skip=$SKIP_TEST

FROM tomcat:8-jre11-slim

RUN rm -rf /usr/local/tomcat/webapps/* \
    && mkdir -p /srv/webprotege \
    && mkdir -p /usr/local/tomcat/webapps/ROOT

WORKDIR /usr/local/tomcat/webapps/ROOT

# TODO: names of these artifacts are version-dependent
COPY --from=build /webprotege/webprotege-cli/target/webprotege-cli-4.0.2.jar /webprotege-cli.jar
COPY --from=build /webprotege/webprotege-server/target/webprotege-server-4.0.2.war ./webprotege.war
RUN unzip webprotege.war \
    && rm webprotege.war
