package com.airlock.iam.scim.client;

import com.fasterxml.jackson.core.JsonProcessingException;

public class CreateUsers {

    public static void main(String[] args) {
        new IamScimClient().createUsers("bart.simpson@ergon.ch", "Bart", "Simpson");
        new IamScimClient().createUsers("lisa.simpson@ergon.ch", "Lisa", "Simpson");
        new IamScimClient().createUsers("homer.simpson@ergon.ch", "Homer", "Simpson");
        new IamScimClient().createUsers("marge.simpson@ergon.ch", "Marge", "Simpson");
    }
}
