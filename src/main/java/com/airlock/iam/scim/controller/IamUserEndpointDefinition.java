package com.airlock.iam.scim.controller;

import de.captaingoldfish.scim.sdk.common.resources.User;
import de.captaingoldfish.scim.sdk.common.utils.JsonHelper;
import de.captaingoldfish.scim.sdk.server.endpoints.EndpointDefinition;
import de.captaingoldfish.scim.sdk.server.endpoints.ResourceHandler;

import java.util.Collections;

public class IamUserEndpointDefinition extends EndpointDefinition
{

    public IamUserEndpointDefinition(ResourceHandler<User> resourceHandler)
    {
        super(JsonHelper.loadJsonDocument(IamClassPathReferences.IAM_USER_RESOURCE_TYPE_JSON),
                JsonHelper.loadJsonDocument(IamClassPathReferences.IAM_USER_SCHEMA_JSON), // FIXME: clarify if the RFC allows to modify the core Users schema. For sure when it comes to read/write? But can we remove attributes?
                Collections.emptyList(),
                resourceHandler);
    }
}
