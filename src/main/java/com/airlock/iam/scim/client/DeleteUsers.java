package com.airlock.iam.scim.client;

public class DeleteUsers {

    public static void main(String[] args) {
        new IamScimClient().deleteUser("bart.simpson@ergon.ch");
        new IamScimClient().deleteUser("lisa.simpson@ergon.ch");
        new IamScimClient().deleteUser("homer.simpson@ergon.ch");
        new IamScimClient().deleteUser("marge.simpson@ergon.ch");
    }
}
