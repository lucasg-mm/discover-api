package com.discover.discoverapi.config;

import com.discover.discoverapi.entities.Album;
import com.discover.discoverapi.entities.Artist;
import com.discover.discoverapi.entities.Genre;
import com.discover.discoverapi.entities.Track;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

@Configuration
public class OpenAPIConfig {
    private Schema getSchemaFromClass(Class<?> theClass){
        ResolvedSchema resolvedSchema = ModelConverters.getInstance()
                .resolveAsResolvedSchema(
                        new AnnotatedType(theClass).resolveAsRef(false));
        return resolvedSchema.schema;
    }

    private Content getPaginatedResultContent(Schema schemaTypeInItems){
        return new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                new io.swagger.v3.oas.models.media.MediaType().schema(new MapSchema()
                        .addProperties("items", new ArraySchema().items(schemaTypeInItems))
                        .addProperties("totalItems", new IntegerSchema())
                        .addProperties("totalPages", new IntegerSchema())));
    }

    @Bean
    public OpenAPI customOpenAPI(){
        // used schemas
        Schema albumSchema = getSchemaFromClass(Album.class);
        Schema trackSchema = getSchemaFromClass(Track.class);
        Schema artistSchema = getSchemaFromClass(Artist.class);
        Schema genreSchema = getSchemaFromClass(Genre.class);

        // searched result's response description
        final String searchedResultsResponseDescription = "The paginated search results.";

        return new OpenAPI()
                .info(
                        new Info()
                                .title("Discover API")
                                .description("A Spring Boot REST API to create, read, update and delete albums," +
                                        " tracks, artists and musical genres.")
                                .version("1.0.0")
                )
                .components(new Components()
                        .addResponses("albumPaginatedResponse", new io.swagger.v3.oas.models.responses
                                .ApiResponse()
                                .description(searchedResultsResponseDescription)
                                .content(getPaginatedResultContent(albumSchema)))
                        .addResponses("trackSearchResponse", new io.swagger.v3.oas.models.responses
                                .ApiResponse()
                                .description(searchedResultsResponseDescription)
                                .content(getPaginatedResultContent(trackSchema)))
                        .addResponses("artistSearchResponse", new io.swagger.v3.oas.models.responses
                                .ApiResponse()
                                .description(searchedResultsResponseDescription)
                                .content(getPaginatedResultContent(artistSchema)))
                        .addResponses("genreSearchResponse", new io.swagger.v3.oas.models.responses
                                .ApiResponse()
                                .description(searchedResultsResponseDescription)
                                .content(getPaginatedResultContent(genreSchema))));
    }
}
