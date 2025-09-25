# Para produção

# Estágio 1: Build da aplicação com Maven e JDK completo
FROM mcr.microsoft.com/openjdk/jdk:21-ubuntu AS build

WORKDIR /app

# Copia o wrapper do Maven e o pom.xml para aproveitar o cache de dependências
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Baixa todas as dependências do projeto
RUN ./mvnw dependency:go-offline

# Copia o código-fonte da aplicação
COPY src ./src

# Compila e empacota a aplicação, pulando os testes
RUN ./mvnw package -DskipTests -fae

# Estágio 2: Execução da aplicação com JRE (imagem final mais leve)
FROM mcr.microsoft.com/openjdk/jdk:21-ubuntu

WORKDIR /app

# Copia apenas o .jar gerado no estágio de build
COPY --from=build /app/target/*.jar pixelpro.jar

# Expõe a porta que a aplicação Spring Boot usa (padrão 8080)
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "pixelpro.jar"]

