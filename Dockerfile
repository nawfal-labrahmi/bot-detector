FROM adoptopenjdk/openjdk11:alpine-jre
VOLUME /tmp
ADD target/bot-detector-1.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]