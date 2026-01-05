package com.tp.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class JerseyApplication extends Application {
    
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        
        // Resources
        classes.add(com.tp.resource.CategoryResource.class);
        classes.add(com.tp.resource.ItemResource.class);
        
        // Jackson JSON Provider
        classes.add(org.glassfish.jersey.jackson.JacksonFeature.class);
        
        return classes;
    }
}

