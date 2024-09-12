package org.example.springbootproject.configure;

import org.example.springbootproject.utils.Constants;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    public void addCorsMappings(CorsRegistry registry) {
        final long MAX_AGE_SECS = 3600;

        registry.addMapping("/**")
                .allowedOrigins(Constants.FRONT_END_URL)
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .maxAge(MAX_AGE_SECS);
    }
}
