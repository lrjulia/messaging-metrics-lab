FROM eclipse-temurin:21-jre

# Instala netcat (versão openbsd)
RUN apt-get update && apt-get install -y netcat-openbsd

# Copia o JAR gerado
COPY target/*.jar app.jar

# Copia o script de espera
COPY wait-for-rabbitmq.sh wait-for-rabbitmq.sh
RUN chmod +x wait-for-rabbitmq.sh

# Expõe a porta da aplicação
EXPOSE 8080

# Usa o script como entrypoint
ENTRYPOINT ["./wait-for-rabbitmq.sh"]
CMD ["java", "-jar", "app.jar"]