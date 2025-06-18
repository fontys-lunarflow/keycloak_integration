package org.acme.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.acme.security.SecurityUtils;
import org.acme.services.KeycloakService;

import java.util.HashMap;
import java.util.Map;

@Path("/api/system")
public class GreetingResource {

    @Inject
    SecurityUtils securityUtils;
    
    @Inject
    KeycloakService keycloakService;

    @GET
    @Path("/status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("version", "1.0.0");
        status.put("timestamp", System.currentTimeMillis());
        
        return Response.ok(status).build();
    }
}