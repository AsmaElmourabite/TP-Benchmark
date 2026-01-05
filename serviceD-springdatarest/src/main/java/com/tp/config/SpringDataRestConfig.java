package com.tp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class SpringDataRestConfig implements RepositoryRestConfigurer {
    
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        // Activer HAL pour la sortie JSON (défaut dans Spring Data REST)
        config.setDefaultMediaType(MediaType.APPLICATION_JSON);
        
        // Désactiver le cache HTTP - retourner le body après création/mise à jour
        config.setReturnBodyOnCreate(true);
        config.setReturnBodyOnUpdate(true);
        
        // Exposer les IDs dans les réponses JSON
        config.exposeIdsFor(com.tp.entity.Category.class, com.tp.entity.Item.class);
        
        // Configuration CORS si nécessaire
        cors.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), 
                          HttpMethod.PUT.name(), HttpMethod.DELETE.name(), HttpMethod.PATCH.name());
    }
}

