package org.acme.security;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.acme.dto.UserDto;
import org.acme.services.KeycloakService;

import java.util.HashMap;
import java.util.Map;

@Path("/test-security")
@Produces(MediaType.APPLICATION_JSON)
public class TestSecurity {

    @Inject
    SecurityUtils securityUtils;
    
    @Inject
    KeycloakService keycloakService;

    @GET
    @Path("/public")
    public Response testPublic() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "This endpoint is public");
        response.put("authenticated", securityUtils.getCurrentUsername() != null);
        
        if (securityUtils.getCurrentUsername() != null) {
            response.put("username", securityUtils.getCurrentUsername());
        }
        
        return Response.ok(response).build();
    }

    @GET
    @Path("/viewer")
    @RolesAllowed({"LunarFlowViewers", "LunarFlowEditors", "LunarFlowAdmins"})
    public Response testViewer() {
        UserDto user = keycloakService.getUserById(securityUtils.getCurrentUserKeycloakId());
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "You have Viewer access");
        response.put("user", user);
        response.put("effectiveRole", getHighestRole());
        
        return Response.ok(response).build();
    }

    @GET
    @Path("/editor")
    @RolesAllowed({"LunarFlowEditors", "LunarFlowAdmins"})
    public Response testEditor() {
        UserDto user = keycloakService.getUserById(securityUtils.getCurrentUserKeycloakId());
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "You have Editor access");
        response.put("user", user);
        response.put("effectiveRole", getHighestRole());
        
        return Response.ok(response).build();
    }
    
    @GET
    @Path("/admin")
    @RolesAllowed("LunarFlowAdmins")
    public Response testAdmin() {
        UserDto user = keycloakService.getUserById(securityUtils.getCurrentUserKeycloakId());
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "You have Admin access");
        response.put("user", user);
        response.put("effectiveRole", getHighestRole());
        
        return Response.ok(response).build();
    }
   
    @GET
    @Path("/groups")
    @Produces(MediaType.TEXT_PLAIN)
    public Response debugGroups() {
        return Response.ok(securityUtils.getGroups()).build();
    }
    
    @GET
    @Path("/whoami")
    @RolesAllowed({"LunarFlowViewers", "LunarFlowEditors", "LunarFlowAdmins"})
    public Response whoAmI() {
        UserDto user = keycloakService.getUserById(securityUtils.getCurrentUserKeycloakId());
        return Response.ok(user).build();
    }
    
    private String getHighestRole() {
        if (securityUtils.isLunarFlowAdmin()) {
            return "LunarFlowAdmins";
        } else if (securityUtils.isLunarFlowEditor()) {
            return "LunarFlowEditors";
        } else if (securityUtils.isLunarFlowViewer()) {
            return "LunarFlowViewers";
        } else {
            return "None";
        }
    }
}