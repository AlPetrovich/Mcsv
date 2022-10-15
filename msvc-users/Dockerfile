FROM openjdk:17-jdk-alpine as builder

WORKDIR /app/msvc-users
# copio el pom.xml padre en carpeta app
COPY ./pom.xml /app
#copio mvn en una carpeta mvn en workdir
COPY ./msvc-users/.mvn ./.mvn
#copio el ejecutable en la raiz de workdir
COPY ./msvc-users/mvnw .
COPY ./msvc-users/pom.xml .

#salta test y compilacion de test y main
RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
#RUN ./mvnw dependency:go-offline (alternativa)
#copiar codigo fuente completo en workdir msvc-users
COPY ./msvc-users/src ./src
#si realizo un cambio, solo cambia esta instruccion, lo demas en CACHE
RUN ./mvnw clean package -DskipTests

# # # # # # # # a partir de una imagen creo otra
FROM openjdk:17-jdk-alpine
WORKDIR /app
RUN mkdir ./logs
# copio el jar de la construccion anterior en app, imagen que usaré
COPY --from=builder /app/msvc-users/target/msvc-users-0.0.1-SNAPSHOT.jar .
EXPOSE 8001
ENTRYPOINT ["java","-jar","msvc-users-0.0.1-SNAPSHOT.jar"]

#docker build -t usuarios . -f .\msvc-users\Dockerfile