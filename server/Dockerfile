FROM azul/zulu-openjdk-alpine:11-latest
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]