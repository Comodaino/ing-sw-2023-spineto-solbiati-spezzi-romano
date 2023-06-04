# Prova Finale di Ingegneria del Software - AA 2021-2022

![MyShelfie](src/main/resources/images/Publisher%20material/Title%202000x618px.png)

Implementation of the tabletop game: [MyShelfie](https://www.craniocreations.it/prodotto/my-shelfie).

---

## Tools used

| Tool  | Description                                                                                                                                                                             |
|-----------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| IntelliJ IDEA   | Integrated development environment (IDE) written in Java for developing computer software written in Java, Kotlin, Groovy, and other JVM-based languages.                     |
| Docker          | Set of platform as a service (PaaS) products that use OS-level virtualization to deliver software in packages called containers.                                              |


## Libreries and Plugins

| Library/Plugin  | Description                                                                |
|-----------------|----------------------------------------------------------------------------|
| Maven           | Build automation tool used primarily for Java projects                     |
| JUnit           | Unit testing framework for the Java programming language                   |
| JavaFx          | Software platform for creating and delivering desktop applications         |

---

## Functionalities

| Functionality       | Implemented         |
|---------------------|---------------------|
| Simplified rules    | :heavy_check_mark:  |
| Complete rules      | :heavy_check_mark:  |
| Socket              | :heavy_check_mark:  |
| RMI                 | :heavy_check_mark:  |
| Tui                 | :heavy_check_mark:  |
| Gui                 | :x:                 |

| Advanced Functionalities       | Implemented        |
|--------------------------------|--------------------|
| Multiple matches               | :heavy_check_mark: |
| Chat                           | :heavy_check_mark: |
| Persistence                    | :x:                |
| Resilience to disconnections   | :x:                |

---

## Tests

These are screenshot directly from intellij showing coverage fo our tests, since the common goals are so many and each match doesn't use more than two of them, we decided to test them separately 

Percentage coverages for classes, methods and lines of code:
![imageNotFound](testScreenshots/controllerAndModelCoverage.jpg)
![imageNotFound](testScreenshots/commonGoalsCoverage.jpg)

---


## Execution

Java 20 is required by the project

### To execute the both client and server just execute the jar with (127.0.0.1 will be the default ip on which the client tries to connect): 

```
java -jar myshelfie.jar
```

### It's possible to add an IP address at the end of the command to connect to a different IP:

```
java -jar myshelfie.jar 192.168.1.1
```

---

## JAR
- [Latest release](https://github.com/Comodaino/ing-sw-2023-spineto-solbiati-spezzi-romano/releases/latest)

## Docker

To test portability and compatibility of the app, we created a docker container to run the app (the app was also tested on two fresh linux installation (Arch and Garuda)

To build the container: 
```
sudo docker build -t nameOfTheImage .
```
To run the container: 
```
sudo docker run -p -i -t nameOfTheImage
```
Docker is required to create and run the image, to create the image copy in a new folder both Dockerfile and the jar executable

---

## Group members:

- [_**Spineto Alessio**_](https://github.com/Comodaino)
- [_**Solbiati Nicoló**_](https://github.com/NicoSolbia)
- [_**Spezzi Clara**_](https://github.com/claraspezzi)
- [_**Romano Alessandra**_](https://github.com/Aleromano01)
