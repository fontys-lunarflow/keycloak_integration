package org.acme.dto;

import java.util.Set;

/**
 * Data Transfer Object for user information retrieved from Keycloak.
 * Contains essential user details and role/group information.
 */
public class UserDto {
    public String id;          // Keycloak ID
    public String username;
    public String email;
    public String firstName;
    public String lastName;
    public String fullName;    // Convenience field combining first and last name
    public Set<String> groups; // Contains role/group information
    
    // Helper methods for role checking
    public boolean hasGroup(String groupName) {
        return groups != null && groups.contains(groupName);
    }
    
    public boolean isLunarFlowAdmin() {
        return hasGroup("LunarFlowAdmins");
    }
    
    public boolean isLunarFlowEditor() {
        return hasGroup("LunarFlowEditors") || isLunarFlowAdmin();
    }
    
    public boolean isLunarFlowViewer() {
        return hasGroup("LunarFlowViewers") || isLunarFlowEditor();
    }
}