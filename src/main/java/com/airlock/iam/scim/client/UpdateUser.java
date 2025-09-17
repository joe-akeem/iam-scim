package com.airlock.iam.scim.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.captaingoldfish.scim.sdk.client.response.ServerResponse;
import de.captaingoldfish.scim.sdk.common.resources.User;

import java.util.List;

public class UpdateUser {
    public static void main(String[] args) throws JsonProcessingException {

        for (String userName : List.of("bart.simpson@ergon.ch", "lisa.simpson@ergon.ch", "homer.simpson@ergon.ch", "marge.simpson@ergon.ch")) {
            ServerResponse<User> userResponse = new IamScimClient().getOneUser(userName);
            User user = userResponse.getResource();
            user.setActive(false);
            new IamScimClient().updateUser(userName, user);
        }
    }
}
