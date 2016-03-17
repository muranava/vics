FROM alpine:3.3

MAINTAINER Stein Fletcher <steinfletcher@gmail.com>

RUN apk --update add openjdk8-jre

ADD server /app

EXPOSE 18080

CMD ["java", "-jar", "/app/server/web-server-0.0.1.jar"]
