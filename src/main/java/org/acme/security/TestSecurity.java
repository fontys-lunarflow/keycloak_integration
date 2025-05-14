package org.acme.security;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/test-security")
public class TestSecurity {

    @Inject
    SecurityUtils securityUtils;

    @GET
    @Path("/user")
    public Response testUser() {
        if (securityUtils.isLunarFlowUser()) {
            return Response.ok("Is LunarFlowUser").build();
        }
        return Response.status(403).entity("Not LunarFlowUser").build();
    }

    @GET
    @Path("/admin")
    public Response testAdmin() {
        if (securityUtils.isLunarFlowAdmin()) {
            return Response.ok("Is LunarFlowAdmin").build();
        }
        return Response.status(403).entity("Not LunarFlowAdmin").build();
    }
}