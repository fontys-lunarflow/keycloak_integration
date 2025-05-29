package org.acme;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.acme.dto.UserDto;
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
    
    @GET
    @Path("/whoami")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"LunarFlowViewers", "LunarFlowEditors", "LunarFlowAdmins"})
    public Response whoAmI() {
        String keycloakId = securityUtils.getCurrentUserKeycloakId();
        UserDto user = keycloakService.getUserById(keycloakId);
        
        Map<String, Object> info = new HashMap<>();
        info.put("username", user.username);
        info.put("fullName", user.fullName);
        info.put("email", user.email);
        info.put("id", user.id);
        info.put("groups", user.groups);
        info.put("isAdmin", user.isLunarFlowAdmin());
        info.put("isEditor", user.isLunarFlowEditor());
        info.put("isViewer", user.isLunarFlowViewer());
        
        return Response.ok(info).build();
    }
    
    @GET
    @Path("/admin-check")
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed("LunarFlowAdmins")
    public String adminOnly() {
        return "If you can see this, you have admin access!";
    }
}