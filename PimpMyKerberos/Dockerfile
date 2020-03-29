# Use the official image as a parent image
FROM openjdk:14-jdk-slim-buster

# Set the working directory
WORKDIR /pimpMyKerberos/jar

# Copy the jar file
COPY ./build/libs/PimpMyKerberos.jar .

WORKDIR /pimpMyKerberos/config
# Copy the config file
COPY ./configFile/aCore.xml .

# Listening on https
EXPOSE 443

WORKDIR /pimpMyKerberos/jar
# Run the specified command within the container.
CMD [ "java", "-jar","PimpMyKerberos.jar","../config/aCore.xml" ]

