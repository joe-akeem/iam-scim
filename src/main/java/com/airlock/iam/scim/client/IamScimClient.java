package com.airlock.iam.scim.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.captaingoldfish.scim.sdk.client.ScimClientConfig;
import de.captaingoldfish.scim.sdk.client.ScimRequestBuilder;
import de.captaingoldfish.scim.sdk.client.response.ServerResponse;
import de.captaingoldfish.scim.sdk.common.constants.EndpointPaths;
import de.captaingoldfish.scim.sdk.common.resources.User;
import de.captaingoldfish.scim.sdk.common.resources.complex.Name;
import de.captaingoldfish.scim.sdk.common.response.ListResponse;

import static de.captaingoldfish.scim.sdk.common.constants.enums.SortOrder.DESCENDING;

public class IamScimClient {

    private final ScimClientConfig scimClientConfig;
    private final String scimApplicationBaseUrl = "http://localhost:8880/scim/v2";

    public IamScimClient() {
       scimClientConfig = ScimClientConfig.builder()
                .connectTimeout(5)
                .requestTimeout(5)
                .socketTimeout(5)
//                .clientAuth(getClientAuthKeystore())
//                .truststore(getTruststore())
                // hostname verifier disabled for tests
                .hostnameVerifier((s, sslSession) -> true)
                .build();
    }

    public void createUsers(String userName, String givenName, String familyName) {
        try (ScimRequestBuilder scimRequestBuilder = new ScimRequestBuilder(scimApplicationBaseUrl, scimClientConfig)) {
            User user = User.builder()
                    .userName(userName)
                    .name(Name.builder()
                            .givenName(givenName)
                            .familyName(familyName)
                            .build())
                    .active(true)
                    .build();
            String endpointPath = EndpointPaths.USERS;
            scimRequestBuilder.create(User.class, endpointPath)
                    .setResource(user)
                    .sendRequest();
        }
    }

    public ServerResponse<User> getOneUser() {
        String endpointPath = EndpointPaths.USERS;
        try (ScimRequestBuilder scimRequestBuilder = new ScimRequestBuilder(scimApplicationBaseUrl, scimClientConfig)) {
            return scimRequestBuilder.get(User.class, endpointPath, "bart.simpson@ergon.ch").sendRequest();
        }
    }

    public ServerResponse<ListResponse<User>> getAllUsers() {
        String endpointPath = EndpointPaths.USERS;
        try (ScimRequestBuilder scimRequestBuilder = new ScimRequestBuilder(scimApplicationBaseUrl, scimClientConfig)) {
            return scimRequestBuilder.list(User.class, endpointPath)
                    .count(50)
//                    .filter("username", Comparator.CO, "bart")
//                    .and("locale", Comparator.EQ, "EN")
//                    .build()
                    .sortBy("username")
                    .sortOrder(DESCENDING)
                    .post()
                    .sendRequest();
        }
    }

    public void updateUser() {
        try (ScimRequestBuilder scimRequestBuilder = new ScimRequestBuilder(scimApplicationBaseUrl, scimClientConfig);) {
            String endpointPath = EndpointPaths.USERS;
            User updateUser = User.builder()
                    .name(Name.builder()
                            .givenName("Bart the Great")
                            .build())
                    .build();
            ServerResponse<User> userServerResponse = scimRequestBuilder.update(User.class, endpointPath, "bart.simpson@ergon.ch")
                    .setResource(updateUser)
                    .sendRequest();
            new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(userServerResponse.getResource());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteUser(String id) {
        try (ScimRequestBuilder scimRequestBuilder = new ScimRequestBuilder(scimApplicationBaseUrl, scimClientConfig)) {
            String endpointPath = EndpointPaths.USERS;

            scimRequestBuilder.delete(User.class, endpointPath, id).sendRequest();
        }
    }
}
