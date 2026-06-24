package net.mirjalal.kogeneesti.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import net.mirjalal.kogeneesti.properties.CorsProperties;

@Configuration
@EnableConfigurationProperties(CorsProperties.class)
public class CorsConfig implements WebMvcConfigurer{

    private final CorsProperties corsProperties;

    public CorsConfig(CorsProperties corsProperties) {
        this.corsProperties = corsProperties;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // apply to all endpoints
                .allowedOrigins(corsProperties.allowedOrigins())
                // .allowedOrigins("https://mirjalal.net")
                .allowedMethods(corsProperties.allowedMethods())
                .allowedHeaders(corsProperties.allowedHeaders())
                .allowCredentials(true);
    }
}