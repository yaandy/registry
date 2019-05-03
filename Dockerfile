FROM java:8
EXPOSE 8083
ADD target/reg-0.0.1-SNAPSHOT.jar reg-0.0.1-SNAPSHOT.jar
HEALTHCHECK CMD --interval=2m --timeout=30s
curl --fail http://localhost:3306/ || exit 1
ENTRYPOINT ["java","-jar","reg-0.0.1-SNAPSHOT.jar"]
