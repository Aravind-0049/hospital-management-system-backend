COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
