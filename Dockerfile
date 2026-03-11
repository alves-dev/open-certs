FROM eclipse-temurin:25-jre-alpine

COPY build/libs/application.jar /app/application.jar

RUN ln -snf /usr/share/zoneinfo/America/Sao_Paulo /etc/localtime \
    && echo America/Sao_Paulo > /etc/timezone

EXPOSE 8080

CMD ["java", "-jar", "/app/application.jar"]