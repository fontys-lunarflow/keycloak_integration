package org.acme.security;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import io.quarkus.security.identity.SecurityIdentity;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.HashSet;
import java.util.Set;

/**
 * Utility class to handle security-related operations such as checking user roles
 * and accessing user information from the SecurityIdentity and JsonWebToken.
 */
@RequestScoped
public class SecurityUtils {

    // Inject the SecurityIdentity to access user roles and identity
    @Inject
    SecurityIdentity identity;
    
    // Inject the JsonWebToken to access JWT claims
    @Inject
    JsonWebToken jwt;

    // Checks if the user has the LunarFlow user role
    public boolean isLunarFlowViewer() {
        return identity.getRoles().contains("LunarFlowViewers") || isLunarFlowEditor();
    }

    // Checks if the user has the LunarFlow user role
    public boolean isLunarFlowEditor() {
        return identity.getRoles().contains("LunarFlowEditors") || isLunarFlowAdmin();
    }
    
    // Checks if the user has the LunarFlow admin role
    public boolean isLunarFlowAdmin() {
        return identity.getRoles().contains("LunarFlowAdmins");
    }
    
    // Checks if the user has any of the LunarFlow roles
    public Set<String> getGroups() {
        Set<String> allRoles = identity.getRoles();
        Set<String> lunarFlowRoles = new HashSet<>();
        
        // Only include the three LunarFlow roles
        if (allRoles.contains("LunarFlowViewers")) {
            lunarFlowRoles.add("LunarFlowViewers");
        }
        if (allRoles.contains("LunarFlowEditors")) {
            lunarFlowRoles.add("LunarFlowEditors");
        }
        if (allRoles.contains("LunarFlowAdmins")) {
            lunarFlowRoles.add("LunarFlowAdmins");
        }
        
        return lunarFlowRoles;
    }
    
    // Returns the current user's Keycloak ID from the JWT
    public String getCurrentUserKeycloakId() {
        // The subject claim is the Keycloak user ID
        return jwt.getSubject();
    }
    
    // Returns the current user's username from the SecurityIdentity
    public String getCurrentUsername() {
        return identity.getPrincipal().getName();
    }
}