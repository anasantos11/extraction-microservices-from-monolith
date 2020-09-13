package br.com.pucminas;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@SpringBootApplication
public class ExtractionApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExtractionApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedMethods("GET", "PUT", "POST", "DELETE");
            }

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {

                registry.addResourceHandler("/**/*").addResourceLocations("classpath:/static/").resourceChain(true)
                        .addResolver(new PathResourceResolver() {
                            @Override
                            protected Resource getResource(String resourcePath, Resource location) throws IOException {
                                Resource requestedResource = location.createRelative(resourcePath);
                                return requestedResource.exists() && requestedResource.isReadable() ? requestedResource
                                        : new ClassPathResource("/static/index.html");
                            }
                        });
            }

        };
    }

}
