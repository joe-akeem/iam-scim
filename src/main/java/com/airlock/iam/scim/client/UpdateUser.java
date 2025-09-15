package com.airlock.iam.scim.client;

import com.fasterxml.jackson.core.JsonProcessingException;

public class UpdateUser {
    public static void main(String[] args) throws JsonProcessingException {
        new IamScimClient().updateUser();
    }
}
