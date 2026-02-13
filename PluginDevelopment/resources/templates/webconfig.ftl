package ${package}.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration for JSP view resolution in Spring Boot
 * Enables proper handling of JSP files from src/main/webapp directory
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        // Disable default view resolver and use JSP view resolver
        registry.jsp("/", ".jsp");
    }
}
