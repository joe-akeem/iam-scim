package com.airlock.iam.scim.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.captaingoldfish.scim.sdk.client.response.ServerResponse;
import de.captaingoldfish.scim.sdk.common.resources.User;

public class GetOneUser {

    public static void main(String[] args) throws JsonProcessingException {
        ServerResponse<User> oneUser = new IamScimClient().getOneUser("bart.simpson@ergon.ch");

        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(oneUser.getResource()));
    }
}
