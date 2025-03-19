package hw6.integration.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/image_storage/profiles/**")
                .addResourceLocations("file:/Users/park-youchan/Desktop/uploads/profiles/");
        registry.addResourceHandler("/image_storage/posts/**")
                .addResourceLocations("file:/Users/park-youchan/Desktop/uploads/posts/");

    }
}
