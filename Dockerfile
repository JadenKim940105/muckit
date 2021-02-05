FROM openjdk:11-jre-slim
COPY target/muckit-*.jar muckit.jar
ENTRYPOINT ["java", "-jar", "muckit.jar"]
