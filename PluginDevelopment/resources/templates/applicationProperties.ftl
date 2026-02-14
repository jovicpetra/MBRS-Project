# Server Configuration
server.port=8080
server.servlet.context-path=/

# JSP View Resolver Configuration
# This tells Spring where to find and how to resolve JSP files
spring.mvc.view.prefix=/
spring.mvc.view.suffix=.jsp

# Embedded Tomcat JSP Engine Configuration
# Enable JSP compilation in the embedded Tomcat
server.tomcat.additional-tld-skip-patterns=spring-boot-starter-tomcat

# Database Configuration
spring.datasource.url=jdbc:h2:mem:beautysalon
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# H2 Console Configuration
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Logging Configuration
logging.level.root=INFO
logging.level.BeautySalon=DEBUG
logging.level.org.springframework.web=DEBUG
