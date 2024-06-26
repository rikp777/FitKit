package nl.rikp.paymentservice.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Article service",
                version = "v1",
                description = "This is the article service, for article management",
                contact = @Contact(url = "https://www.linkedin.com/in/rikpeeters-nl/", name = "Rik Peeters")
        ))
public class OpenApiConfig {
}

