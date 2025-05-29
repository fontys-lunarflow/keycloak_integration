package org.acme.services;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.acme.dto.UserDto;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;

import java.util.List;
import java.util.Set;
import java.util.Collections;
import java.util.stream.Collectors;

@ApplicationScoped
public class KeycloakService {

    @ConfigProperty(name = "quarkus.oidc.auth-server-url")
    String authServerUrl;
    
    @ConfigProperty(name = "quarkus.oidc.client-id")
    String clientId;
    
    @ConfigProperty(name = "quarkus.oidc.credentials.secret")
    String clientSecret;
    
    private String realm;
    private Keycloak keycloak;
    
    public KeycloakService() {
    }
    
    private void initialiseRealm() {
    // Extract realm name from the Keycloak URL
    // Validates URL format and throws helpful errors
        if (authServerUrl == null) {
            throw new IllegalStateException("Auth server URL is null. Check your configuration.");
        }
        
        if (!authServerUrl.contains("/realms/")) {
            throw new IllegalStateException(
                "Invalid auth server URL format: " + authServerUrl + 
                ". Expected format: 'https://<server>/realms/<realm-name>'"
            );
        }
        
        this.realm = authServerUrl.substring(authServerUrl.lastIndexOf("/realms/") + 8);
    }
    
    private Keycloak getKeycloakClient() {
    // Lazy initialization - only creates client when needed
    // Sets up connection to Keycloak Admin API
    // Uses client credentials flow for authenticatio
        if (keycloak == null) {
            // initialise realm if not done yet
            if (realm == null) {
                initialiseRealm();
            }
            
            try {
                String authServerBaseUrl = authServerUrl.substring(0, authServerUrl.indexOf("/realms/"));
                keycloak = KeycloakBuilder.builder()
                    .serverUrl(authServerBaseUrl)
                    .realm(realm)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .grantType("client_credentials")
                    .build();
            } catch (Exception e) {
                throw new IllegalStateException(
                    "Failed to initialise Keycloak client with URL: " + authServerUrl +
                    ", realm: " + realm +
                    ", client ID: " + clientId +
                    ". Error: " + e.getMessage(), e
                );
            }
        }
        return keycloak;
    }

    public List<UserDto> getAllUsers() {
        return getKeycloakClient().realm(realm).users().list().stream()
            .map(this::mapToUserDto)
            .collect(Collectors.toList());
    }
    
    public UserDto getUserById(String id) {
        UserRepresentation user = getKeycloakClient().realm(realm).users().get(id).toRepresentation();
        if (user == null) {
            return null;
        }
        return mapToUserDto(user);
    }
    
    public UserDto getUserByUsername(String username) {
        List<UserRepresentation> users = getKeycloakClient().realm(realm).users().search(username, true);
        if (users.isEmpty()) {
            return null;
        }
        return mapToUserDto(users.get(0));
    }
    
    public List<UserDto> getUsersByGroup(String groupName) {
        return getKeycloakClient().realm(realm).groups().groups().stream()
            .filter(g -> g.getName().equals(groupName))
            .findFirst()
            .map(group -> getKeycloakClient().realm(realm).groups().group(group.getId()).members())
            .orElse(Collections.emptyList())
            .stream()
            .map(this::mapToUserDto)
            .collect(Collectors.toList());
    }
    
    private Set<String> getUserGroups(String userId) {
        return getKeycloakClient().realm(realm).users().get(userId).groups().stream()
            .map(GroupRepresentation::getName)
            .collect(Collectors.toSet());
    }
    
    private UserDto mapToUserDto(UserRepresentation user) {
        UserDto dto = new UserDto();
        dto.id = user.getId();
        dto.username = user.getUsername();
        dto.email = user.getEmail();
        dto.firstName = user.getFirstName();
        dto.lastName = user.getLastName();
        dto.fullName = (user.getFirstName() != null ? user.getFirstName() + " " : "") + 
                      (user.getLastName() != null ? user.getLastName() : "");
        dto.groups = getUserGroups(user.getId());
        return dto;
    }
}