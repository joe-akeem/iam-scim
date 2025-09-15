package com.airlock.iam.scim.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.captaingoldfish.scim.sdk.common.resources.User;

public class GetAllUsers {

    public static void main(String[] args) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();

        new IamScimClient().getAllUsers().getResource().getListedResources().forEach(user -> {
            printUser(user, objectMapper);
        });
    }

    private static void printUser(User user, ObjectMapper objectMapper) {
        try {
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(user));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
