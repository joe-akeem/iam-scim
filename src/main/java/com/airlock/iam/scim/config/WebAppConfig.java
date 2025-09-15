package com.airlock.iam.scim.config;

import com.airlock.iam.scim.handler.UserHandler;
import com.airlock.iam.scim.controller.IamUserEndpointDefinition;
import de.captaingoldfish.scim.sdk.common.resources.ServiceProvider;
import de.captaingoldfish.scim.sdk.common.resources.complex.*;
import de.captaingoldfish.scim.sdk.common.resources.multicomplex.AuthenticationScheme;
import de.captaingoldfish.scim.sdk.server.endpoints.ResourceEndpoint;
import de.captaingoldfish.scim.sdk.server.schemas.ResourceType;
import de.captaingoldfish.scim.sdk.server.schemas.custom.ResourceTypeFeatures;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class WebAppConfig {

    @Bean
    public ServiceProvider getServiceProviderConfig()
    {
        AuthenticationScheme authScheme = AuthenticationScheme.builder()
                .name("Bearer")
                .description("Authentication scheme using the OAuth "
                        + "Bearer Token Standard")
                .specUri("http://www.rfc-editor.org/info/rfc6750")
                .type("oauthbearertoken")
                .build();
        return ServiceProvider.builder()
                .filterConfig(FilterConfig.builder().supported(true).maxResults(50).build())
                .sortConfig(SortConfig.builder().supported(true).build())
                .changePasswordConfig(ChangePasswordConfig.builder().supported(true).build())
                .bulkConfig(BulkConfig.builder().supported(true).maxOperations(10).build())
                .patchConfig(PatchConfig.builder().supported(true).build())
                .authenticationSchemes(Collections.singletonList(authScheme))
                .eTagConfig(ETagConfig.builder().supported(true).build())
                .build();
    }

    @Bean
    public ResourceEndpoint getResourceEndpoint(ServiceProvider serviceProvider)
    {
        return new ResourceEndpoint(serviceProvider);
    }

    @Bean
    public ResourceType getUserResourceType(ResourceEndpoint resourceEndpoint, UserHandler userHandler)
    {
        ResourceType userResourceType = resourceEndpoint.registerEndpoint(new IamUserEndpointDefinition(userHandler));
        userResourceType.setFeatures(ResourceTypeFeatures.builder().autoFiltering(true).autoSorting(true).build());
//        userResourceType.getMainSchema().addSchemaAttribute(...); FIXME: add attributes depending on user context data

        return userResourceType;
    }
}
