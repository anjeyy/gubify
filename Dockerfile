FROM openjdk:17-alpine

LABEL application="gubify"
LABEL maintainer="Andjelko Perisic"

WORKDIR /app

RUN apk --update add curl

# https://github.com/mhart/alpine-node/issues/48#issuecomment-370171836
RUN addgroup -g 1000 -S unbendable && \
    adduser -u 1000 -S unbendable -G unbendable && \
    chmod 777 -R /tmp && \
    chown -R unbendable:unbendable /app
USER unbendable

COPY target/gubify*.jar /app/gubify.jar

HEALTHCHECK CMD curl -f http://localhost:8080/actuator/health || exit 1

CMD java -Dspring.profiles.active=default \
         -jar /app/gubify.jar
