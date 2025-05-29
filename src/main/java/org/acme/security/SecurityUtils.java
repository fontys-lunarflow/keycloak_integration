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

    // Checks if the user has the Lunarflow user role
    public boolean isLunarflowViewer() {
        return identity.getRoles().contains("LunarflowViewers") || isLunarflowEditor();
    }

    // Checks if the user has the Lunarflow user role
    public boolean isLunarflowEditor() {
        return identity.getRoles().contains("LunarflowEditors") || isLunarflowAdmin();
    }
    
    // Checks if the user has the Lunarflow admin role
    public boolean isLunarflowAdmin() {
        return identity.getRoles().contains("LunarflowAdmins");
    }
    
    // Checks if the user has any of the Lunarflow roles
    public Set<String> getGroups() {
        Set<String> allRoles = identity.getRoles();
        Set<String> LunarflowRoles = new HashSet<>();
        
        // Only include the three Lunarflow roles
        if (allRoles.contains("LunarflowViewers")) {
            LunarflowRoles.add("LunarflowViewers");
        }
        if (allRoles.contains("LunarflowEditors")) {
            LunarflowRoles.add("LunarflowEditors");
        }
        if (allRoles.contains("LunarflowAdmins")) {
            LunarflowRoles.add("LunarflowAdmins");
        }
        
        return LunarflowRoles;
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