FROM amazoncorretto:17-alpine3.17-jdk as builder
WORKDIR application
ADD maven/${project.build.finalName}.jar ./
RUN java -Djarmode=layertools -jar ${project.build.finalName}.jar extract

FROM amazoncorretto:17-alpine3.17-jdk

WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "-Dspring.profiles.active=localmysql", "-Djava.security.egd=file:/dev/./urandom", "org.springframework.boot.loader.JarLauncher"]

#FROM amazoncorretto:17-alpine3.17-jdk
#ENV JAVA_OPTS " -Xms512m -Xmx512m -Dspring.profiles.active=localmysql -Djava.security.egd=file:/dev/./urandom"
#WORKDIR application
#COPY ../../../target/spring-6-rest-mvc-0.0.1-SNAPSHOT.jar ./
#EXPOSE 8081
#ENTRYPOINT exec java $JAVA_OPTS  -jar spring-6-rest-mvc-0.0.1-SNAPSHOT.jar
