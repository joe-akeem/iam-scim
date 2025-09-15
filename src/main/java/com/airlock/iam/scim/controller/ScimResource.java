package com.airlock.iam.scim.controller;

import com.airlock.iam.scim.config.ScimAuthentication;
import de.captaingoldfish.scim.sdk.common.constants.HttpHeader;
import de.captaingoldfish.scim.sdk.common.constants.enums.HttpMethod;
import de.captaingoldfish.scim.sdk.common.response.ScimResponse;
import de.captaingoldfish.scim.sdk.server.endpoints.Context;
import de.captaingoldfish.scim.sdk.server.endpoints.ResourceEndpoint;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/scim/v2")
public class ScimResource {

    private final ResourceEndpoint resourceEndpoint;

    public ScimResource(ResourceEndpoint resourceEndpoint) {
        this.resourceEndpoint = resourceEndpoint;
    }

    @RequestMapping(value = "/**", method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT,
            RequestMethod.PATCH,
            RequestMethod.DELETE}, produces = HttpHeader.SCIM_CONTENT_TYPE)
    public @ResponseBody String handleScimRequest(HttpServletRequest request,
                                                  HttpServletResponse response,
                                                  @RequestBody(required = false) String requestBody)
    {
        Map<String, String> httpHeaders = getHttpHeaders(request);
        String query = request.getQueryString() == null ? "" : "?" + request.getQueryString();

        ScimAuthentication scimAuthentication = new ScimAuthentication();

        ScimResponse scimResponse = resourceEndpoint.handleRequest(request.getRequestURL().toString() + query,
                HttpMethod.valueOf(request.getMethod()),
                requestBody,
                httpHeaders,
                new Context(scimAuthentication));
        response.setContentType(HttpHeader.SCIM_CONTENT_TYPE);
        scimResponse.getHttpHeaders().forEach(response::setHeader);
        response.setStatus(scimResponse.getHttpStatus());
        return scimResponse.toPrettyString();
    }

    private Map<String, String> getHttpHeaders(HttpServletRequest request)
    {
        Map<String, String> httpHeaders = new HashMap<>();
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration != null && enumeration.hasMoreElements())
        {
            String headerName = enumeration.nextElement();
            httpHeaders.put(headerName, request.getHeader(headerName));
        }
        return httpHeaders;
    }
}
