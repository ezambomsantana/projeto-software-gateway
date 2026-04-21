FROM amazoncorretto:25-alpine-jdk

WORKDIR /app

# Copy the JAR file
COPY target/gateway-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java","-jar","app.jar"]