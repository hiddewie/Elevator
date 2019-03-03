
FROM openjdk:8-alpine as build

ENV APP_HOME=/usr/src/app/

WORKDIR $APP_HOME

COPY build.gradle.kts settings.gradle.kts gradlew $APP_HOME
COPY gradle $APP_HOME/gradle
RUN ./gradlew build || return 0

COPY . $APP_HOME
RUN ./gradlew bootJar
RUN ls -la /usr/src/app/build/libs

#

FROM openjdk:8-alpine

COPY --from=build /usr/src/app/build/libs /usr/app/
WORKDIR /usr/app

RUN ls -la

EXPOSE 8080

CMD java -jar /usr/app/Elevator-0.1.0.jar
