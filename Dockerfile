FROM amazoncorretto:19

add ing-sw-2023-spineto-solbiati-spezzi-romano-1.0-SNAPSHOT.jar ing-sw-2023-spineto-solbiati-spezzi-romano-1.0-SNAPSHOT.jar
COPY . /app
WORKDIR /app
EXPOSE 25565
CMD java -jar ing-sw-2023-spineto-solbiati-spezzi-romano-1.0-SNAPSHOT.jar

