package org.acme.dto;

import java.util.Set;

/**
 * Data Transfer Object for user information retrieved from Keycloak.
 */
public class UserDto {
    public String id;          // Keycloak ID
    public String username;
    public String email;
    public String firstName;
    public String lastName;
    public String fullName;    
    public Set<String> groups;
    
}