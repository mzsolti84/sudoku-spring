# the first stage of our build will use a maven 3.9.1 parent image
FROM maven:3.9.1-amazoncorretto-17 AS MAVEN_BUILD
# copy the pom and src code to the container
COPY ./ ./
# package our application code
RUN mvn clean package
# the second stage of our build will use open jdk
FROM openjdk:17-slim
# copy only the artifacts we need from the first stage and discard the rest
COPY --from=MAVEN_BUILD /target/sudoku-1.0.1-SNAPSHOT.jar /sudoku.jar
# set the startup command to execute the jar
CMD ["java", "-jar", "/sudoku.jar"]