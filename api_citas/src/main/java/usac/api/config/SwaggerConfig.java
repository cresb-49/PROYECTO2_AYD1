/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * Clase de configuración de Swagger para la API de E-commerce. Esta clase
 * configura la documentación de la API utilizando OpenAPI 3 y Springdoc.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Configura el bean de OpenAPI para personalizar la documentación de la
     * API.
     *
     * @return OpenAPI personalizado con el título, versión y descripción de la
     * API.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de citas") // Título de la API que aparecerá en la documentación
                        .version("1.0") // Versión de la API
                        .description("Documentación de la API para el proyecto de citas"));  // Descripción de la API
    }

    /**
     * Configura el bean GroupedOpenApi para agrupar los endpoints públicos de
     * la API.
     *
     * @return GroupedOpenApi que agrupa todas las rutas que empiezan con
     * "/api/**".
     */
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("citas-public") // Nombre del grupo que aparecerá en Swagger UI
                .pathsToMatch("/api/**") // Define las rutas que serán incluidas en este grupo
                .build();
    }
}
