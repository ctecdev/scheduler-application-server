FROM openjdk:11-jre

ARG ARG_JAR_FILE
ENV JAR_FILE ${ARG_JAR_FILE}

ADD docker/app/root/ /
ADD target/ /target/

VOLUME ["/tmp", "/var/log"]

EXPOSE 8080
WORKDIR "/target"
RUN chmod +x /docker-entrypoint.sh
ENTRYPOINT [ "/docker-entrypoint.sh" ]
