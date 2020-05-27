FROM openjdk:11-jre-slim
COPY SpeedTester-2.0-jar-with-dependencies.jar /root/
ENTRYPOINT ["java", "-jar", "/root/SpeedTester-2.0-jar-with-dependencies.jar"]
