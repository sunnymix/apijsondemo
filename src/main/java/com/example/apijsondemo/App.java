package com.example.apijsondemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@SpringBootApplication(scanBasePackages = {"com.example", "com.example.apijsondemo"})
@EnableConfigurationProperties
public class App implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void customize(ConfigurableServletWebServerFactory server) {
        server.setPort(9030);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                    .allowedOriginPatterns("*")
                    .allowedMethods("*")
                    .allowCredentials(true)
                    .maxAge(3600);
            }
        };
    }

    @Component
    public static class AppReady implements ApplicationListener<ApplicationReadyEvent> {

        @Autowired
        private APIJSONInit.APIJSONDBConfig apiJsonDbConfig;

        @Override
        public void onApplicationEvent(ApplicationReadyEvent event) {
            System.out.println("APP READY!");
            var configInit = new APIJSONInit.APIJSONSQLConfigInit(apiJsonDbConfig);
            APIJSONInit.init(configInit);
        }
    }

}
