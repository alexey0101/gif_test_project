FROM openjdk:11
VOLUME /tmp
ADD build/libs/test-0.0.1-SNAPSHOT.jar gifApp.jar
ENTRYPOINT ["java", "-jar", "/gifApp.jar"]
