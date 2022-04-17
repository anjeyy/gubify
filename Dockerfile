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

COPY target/nfl-core*.jar /app/nfl-core.jar

HEALTHCHECK CMD curl -f http://localhost:8090/nfl/actuator/health || exit 1

CMD java -Dspring.profiles.active=default \
         -Dspring.datasource.url=jdbc:postgresql://${POSTGRES_ENDPOINT}/${POSTGRES_DB} \
         -Dspring.datasource.username=${POSTGRES_USER} \
         -Dspring.datasource.password=${POSTGRES_PASS} \
         -Deureka.client.service-url.defaultZone=${EUREKA_URI} \
         -jar /app/nfl-core.jar