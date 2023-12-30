package com.mmd.library.configuration;

import com.mmd.library.entity.Book;
import com.mmd.library.entity.Review;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class DataRestConfig implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

        config.setBasePath("api");

        //CORS Configuration
        String allowedOrigin = "http://localhost:3000";
        cors.addMapping(config.getBasePath() + "/books/**")
                .allowedOrigins(allowedOrigin);
        cors.addMapping(config.getBasePath() + "/reviews/**")
                .allowedOrigins(allowedOrigin);
        cors.addMapping("/login")
                .allowedOrigins(allowedOrigin);


        config.exposeIdsFor(Book.class);
        config.exposeIdsFor(Review.class);


        HttpMethod [] disabledHttpMethods = {HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.PATCH, HttpMethod.POST};


        disableUnsupportedMethods(Book.class,   config, disabledHttpMethods);
        disableUnsupportedMethods(Review.class, config, disabledHttpMethods);

    }

    private void disableUnsupportedMethods(Class<?> theClass, RepositoryRestConfiguration config, HttpMethod[] disabledHttpMethods){
        config.getExposureConfiguration()
                .forDomainType(theClass)
                .disablePutForCreation()
                .withItemExposure(((metdata, httpMethods) -> httpMethods.disable(disabledHttpMethods)))
                .withAssociationExposure(((metdata, httpMethods) -> httpMethods.disable(disabledHttpMethods)))
                .withCollectionExposure(((metdata, httpMethods) -> httpMethods.disable(disabledHttpMethods)));
    }

}
