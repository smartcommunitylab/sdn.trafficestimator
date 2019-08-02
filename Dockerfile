FROM maven:3-jdk-11 AS mvn
WORKDIR /tmp
COPY ./pom.xml /tmp/traffic/pom.xml
COPY ./src /tmp/traffic/src
WORKDIR /tmp/traffic
RUN mvn clean install -Dmaven.test.skip=true

FROM adoptopenjdk/openjdk11:alpine
ENV FOLDER=/tmp/traffic/target
ENV APP=traffic-estimator-1.0.0.jar
ARG USER=traffic
ARG USER_ID=3002
ARG USER_GROUP=traffic
ARG USER_GROUP_ID=3002
ARG USER_HOME=/home/${USER}

RUN  addgroup -g ${USER_GROUP_ID} ${USER_GROUP}; \
     adduser -u ${USER_ID} -D -g '' -h ${USER_HOME} -G ${USER_GROUP} ${USER} ;

WORKDIR  /home/${USER}/app
RUN chown ${USER}:${USER_GROUP} /home/${USER}/app
RUN mkdir indexes && chown ${USER}:${USER_GROUP} indexes
COPY --from=mvn --chown=traffic:traffic ${FOLDER}/${APP} /home/${USER}/app/traffic.jar

USER traffic
CMD ["java", "-jar", "traffic.jar"]