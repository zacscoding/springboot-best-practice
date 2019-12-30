package best.server.config;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * For test config
 */
@TestConfiguration
public class RestDocsConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public RestDocsMockMvcConfigurationCustomizer restDocsMockMvcConfigurationCustomizer() {
        return configurer -> configurer.operationPreprocessors()
                                       .withRequestDefaults(prettyPrint())
                                       .withResponseDefaults(prettyPrint());
    }
}
