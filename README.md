# Quarkus template

This repository serves as a template/scaffold for dockerized Quarkus application.

# Requirements

```
$ docker -v
Docker version 27.3.1, build ce12230

$ docker-compose -v
Docker Compose version v2.29.7-desktop.1

$ asdf list java
 *openjdk-21

$ java --version
openjdk 21 2023-09-19
OpenJDK Runtime Environment (build 21+35-2513)
OpenJDK 64-Bit Server VM (build 21+35-2513, mixed mode, sharing)

$ quarkus -version
3.19.3
```

***IMPORTANT:** docker and docker-compose versions are just my current installed versions, but **matching Java, OpenJDK and Quarkus versions are MANDATORY for the app to work***

# Build

You can build the app in couple of ways.

## Production

For production, there are 2 ways you can build the app:
- Compilation using JVM
- Compilation using GraalVM (local native build)
- Compilation using GraalVM (containerised local build)

If you want to know what is the practical difference, you can take a look for yourself:
- [Quarkus Runtime Performance](https://quarkus.io/blog/runtime-performance/)
- [Native Images and Quarkus: JVM-less Java](https://medium.com/bishop-co/native-images-and-quarkus-jvm-less-java-5d4cd4211e41)

### Compilation using JVM

Before you build the image, you have to compile the app locally. To compile the project, run this:

```
./mvnw package
```

If you want a clean packaging process, you can add a **clean** command before the package:

```
./mvnw clean package
```

To build the Docker image, run this:

```
docker build -f src/main/docker/Dockerfile.jvm -t quarkus/test-jvm .
```

To spin up the container using newly built image, run this:

```
docker run -i --rm -p 8080:8080 quarkus/test-jvm
```

This will compile the demo app, archive it in a .jar file, package it in a Docker image and run it. The project will run on [http://localhost:8080](http://localhost:8080). To test the app, try visiting [http://localhost:8080/hello](http://localhost:8080/hello).

### Compilation using GraalVM (local native build)

If you want to use GraalVM, [make sure you have it installed and setup locally](https://quarkus.io/guides/maven-tooling#building-a-native-executable). Make sure it supports OpenJDK and Java versions listed in the [requirements section](#requirements).

Before you build the image, you have to compile the app locally. To compile the project, run this:

```
./mvnw package -Dnative
```

If you want a clean packaging process, you can add a **clean** command before the package:

```
./mvnw clean package -Dnative
```

To build the Docker image, run this:
```
docker build -f src/main/docker/Dockerfile.native -t quarkus/test-native .
```

To spin up the container using newly built image, run this:

```
docker run -i --rm -p 8080:8080 quarkus/test-native
```

### Compilation using GraalVM (containerised local build)

If you want to use GraalVM but don't have it locally, you can use the `-Dquarkus.native.container-build=true` argument to have the image built using a disposable container. For more info, visit [Quarkus documentation](https://quarkus.io/guides/building-native-image#container-runtime).

To streamline this process, you can find the custom-made [Dockerfile.native-multistage](./src/main/docker/Dockerfile.native-multistage).

To compile the app and package it in a Docker image, run this:

```
docker build -f src/main/docker/Dockerfile.native-multistage -t quarkus/test-multistage .
```

To spin up the container using newly built image, run this:
```
docker run -i --rm -p 8080:8080 quarkus/test-multistage
```

## Development

To streamline the development process, you can find the custom made [Dockerfile.dev](./src/main/docker/Dockerfile.dev).

The image is pretty barebones, but in conjunction with [docker-compose.yml](./docker-compose.yml) it makes the complete development process.

That includes means that development environment is fully dockerized environment that supports Live Reload.

To build all the images specified in the docker-compose.yml, run this:

```
docker-compose build
```

To spin up all the services specified in the docker-compose.yml, run this:

```
docker-compose up
```


## My changes to the files

- [src/main/docker/Dockerfile.dev](src/main/docker/Dockerfile.dev) --> created a development image
- [src/main/docker/Dockerfile.native-multistage](src/main/docker/Dockerfile.native-multistage) --> created a multistage GraalVM image for production
- [src/main/resources/application.properties](src/main/resources/application.properties) --> enabled Quarkus to listen to all network interfaces when in development mode (0.0.0.0)
- [.dockerignore](.dockerignore) --> Commented out the first line, enabled the whole project to be mounted in the docker image for live reload
- [docker-compose.yml](docker-compose.yml) --> created a configfile for easier project startup

  
