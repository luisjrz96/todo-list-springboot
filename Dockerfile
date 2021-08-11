FROM gradle:7.1.1-jdk11 AS TEMP_BUILD_IMAGE
#Definig working directory for the gradle tasks
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME

# Copying the gradle resources to the container (build.gradle, settings.gradle)
COPY build.gradle settings.gradle $APP_HOME
#Copying the gradle directory to the container
COPY gradle $APP_HOME/gradle
#Copying all the app resources
COPY --chown=gradle:gradle . /home/gradle/src
USER root
RUN chown -R gradle /home/gradle/src
#Executing the gradle command to generate the jar file
RUN gradle build || return 0
COPY . .
RUN gradle clean build


#Using this container to run the generated jar file
FROM adoptopenjdk/openjdk11:alpine-jre
ENV ARTIFACT_NAME=todo-list-0.0.1-SNAPSHOT.jar
ENV APP_HOME=/usr/app/

#Defining the working directory and copying the generated jar file by the gradle image
WORKDIR $APP_HOME
COPY --from=TEMP_BUILD_IMAGE $APP_HOME/build/libs/$ARTIFACT_NAME .

#Exposing a port for the app and defining the entrypoint
EXPOSE 8080
ENTRYPOINT exec java -jar ${ARTIFACT_NAME}