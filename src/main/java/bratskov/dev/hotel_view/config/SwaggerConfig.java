package bratskov.dev.hotel_view.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hotel View API")
                        .description("RESTful API for managing hotel information, including search, creation, and amenities management.")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Timofei Bratskov")
                        ));
    }
}