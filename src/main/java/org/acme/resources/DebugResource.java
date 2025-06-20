package org.acme.resources;

import io.quarkus.oidc.AccessTokenCredential;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.acme.dto.UserDto;
import org.acme.security.SecurityUtils;
import org.acme.services.KeycloakService;
import io.quarkus.security.identity.SecurityIdentity;

import java.util.HashMap;
import java.util.Map;

/**
 * Testing and diagnostic endpoints.
 */
@Path("/api/debug")
@Produces(MediaType.APPLICATION_JSON)
public class DebugResource {

    @Inject
    SecurityUtils securityUtils;
    
    @Inject
    KeycloakService keycloakService;

    @Inject
    SecurityIdentity identity;

    /**
     * Public endpoint to test basic access without authentication.
     * Returns a message indicating the endpoint is public.
     */
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

    /**
     * Viewer-only endpoint to test access for Lunarflow viewers.
     * Returns user information and confirms viewer access.
     */
    @GET
    @Path("/viewer")
    public Response testViewer() {
        UserDto user = keycloakService.getUserById(securityUtils.getCurrentUserKeycloakId());
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "You have Viewer access");
        response.put("user", user);
        response.put("effectiveRole", getHighestRole());
        
        return Response.ok(response).build();
    }

    /**
     * Editor-only endpoint to test access for Lunarflow editors.
     * Returns user information and confirms editor access.
     */
    @GET
    @Path("/editor")
    public Response testEditor() {
        UserDto user = keycloakService.getUserById(securityUtils.getCurrentUserKeycloakId());
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "You have Editor access");
        response.put("user", user);
        response.put("effectiveRole", getHighestRole());
        
        return Response.ok(response).build();
    }
    
    /**
     * Admin-only endpoint to test access for Lunarflow administrators.
     * Returns user information and confirms admin access.
     */
    @GET
    @Path("/admin")
    public Response testAdmin() {
        UserDto user = keycloakService.getUserById(securityUtils.getCurrentUserKeycloakId());
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "You have Admin access");
        response.put("user", user);
        response.put("effectiveRole", getHighestRole());
        
        return Response.ok(response).build();
    }
   
    /**
     * Debug endpoint to list all groups the user belongs to.
     * Useful for verifying group membership and roles.
     */
    @GET
    @Path("/groups")
    @Produces(MediaType.TEXT_PLAIN)
    public Response debugGroups() {
        return Response.ok(securityUtils.getGroups()).build();
    }  
    
    /**
     * Token information endpoint for debugging JWT tokens.
     * Admin-only for security reasons.
     */
    @GET
    @Path("/token-info")
    public Response tokenInfo() {
        Map<String, Object> tokenInfo = new HashMap<>();
        
        identity.getCredentials().forEach(cred -> {
            if (cred instanceof AccessTokenCredential) {
                AccessTokenCredential tokenCred = (AccessTokenCredential)cred;
                tokenInfo.put("token", tokenCred.getToken());
                
                // Use the injected JsonWebToken instead of trying to access it from the credential
                if (identity.getPrincipal() != null) {
                    tokenInfo.put("principal", identity.getPrincipal().getName());
                }
            }
        });
        
        // Add basic JWT information that's available from the SecurityIdentity
        tokenInfo.put("authenticated", identity.isAnonymous() ? "no" : "yes");
        tokenInfo.put("roles", identity.getRoles());
        
        return Response.ok(tokenInfo).build();
    }
    
    /**
     * Helper method to determine the highest role of the current user.
     * Returns a string representation of the effective access level.
     */
    private String getHighestRole() {
        if (securityUtils.isLunarflowAdmin()) {
            return "LunarflowAdmins";
        } else if (securityUtils.isLunarflowEditor()) {
            return "LunarflowEditors";
        } else if (securityUtils.isLunarflowViewer()) {
            return "LunarflowViewers";
        } else {
            return "None";
        }
    }
}

