FROM java:8
EXPOSE 8083
ADD target/reg-0.0.1-SNAPSHOT.jar reg-0.0.1-SNAPSHOT.jar
COPY wait_it.sh wait_it.sh
HEALTHCHECK CMD sql_container_health.sh
ENTRYPOINT ["./wait_it.sh mysql:3306 -t30", "java","-jar","reg-0.0.1-SNAPSHOT.jar"]
