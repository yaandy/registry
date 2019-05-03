FROM java:8
EXPOSE 8083
ADD target/reg-0.0.1-SNAPSHOT.jar reg-0.0.1-SNAPSHOT.jar
ADD /sql_container_health.sh sql_container_health.sh
HEALTHCHECK CMD sql_container_health.sh

ENTRYPOINT ["java","-jar","reg-0.0.1-SNAPSHOT.jar"]
