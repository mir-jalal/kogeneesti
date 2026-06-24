package net.mirjalal.kogeneesti.configuration;

import java.util.Random;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KogeneestiConfiguration {
    @Bean
    public Random random() {
        return new Random();
    }
}
