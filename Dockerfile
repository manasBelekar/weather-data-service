#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS builder
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:11-jre-slim AS buildpack
COPY --from=builder /home/app/target/weather-data-service-0.0.1-SNAPSHOT.jar /usr/local/lib/weather-data-service.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/weather-data-service.jar"]




# # -----------------------------------------------------------------------------
# FROM nginx:1.13.12-alpine as prod
# # -----------------------------------------------------------------------------

# # Advance user login details 
# COPY ./nginx.conf /etc/nginx/conf.d/default.conf

# COPY --from=buildpack  /builder/ /usr/share/nginx/html/