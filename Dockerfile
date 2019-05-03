FROM java:8
EXPOSE 8083
ADD target/reg-0.0.1-SNAPSHOT.jar reg-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","reg-0.0.1-SNAPSHOT.jar"]