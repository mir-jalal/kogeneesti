package net.mirjalal.kogeneesti.configuration;

import org.apache.tika.Tika;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StorageConfiguration {

    @Bean
    public Tika tika() {
        return new Tika();
    }
}
