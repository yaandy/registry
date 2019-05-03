FROM java:8
EXPOSE 8083
ADD target/reg-0.0.1-SNAPSHOT.jar reg-0.0.1-SNAPSHOT.jar
HEALTHCHECK CMD --interval=1m --timeout=30s
until nc -z -v -w30 localhost 3306
do
  echo "Waiting for database connection..."
  # wait for 5 seconds before check again
  sleep 15
done
ENTRYPOINT ["java","-jar","reg-0.0.1-SNAPSHOT.jar"]
