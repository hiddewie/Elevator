
FROM openjdk:8-alpine as build

ENV APP_HOME=/usr/src/app/

WORKDIR $APP_HOME

COPY build.gradle settings.gradle gradlew $APP_HOME
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

#ENV DB_HOST 10.0.75.1

EXPOSE 8080

CMD java -jar /usr/app/elevator-0.1.0.jar
