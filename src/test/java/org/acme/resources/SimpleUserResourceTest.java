package org.acme.resources;

import jakarta.ws.rs.core.Response;
import org.acme.dto.UserDto;
import org.acme.services.KeycloakService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Simple non-Quarkus test for UserResource that avoids CDI
 */
@ExtendWith(MockitoExtension.class)
public class SimpleUserResourceTest {

    @Mock
    KeycloakService keycloakService;
    
    @InjectMocks
    UserResource userResource;
    
    private UserDto mockUser;
    private List<UserDto> mockUsers;
    
    @BeforeEach
    public void setup() {
        // Create mock user
        mockUser = new UserDto();
        mockUser.id = "test-id-123";
        mockUser.username = "testuser";
        mockUser.email = "test@example.com";
        mockUser.firstName = "Test";
        mockUser.lastName = "User";
        mockUser.fullName = "Test User";
        mockUser.groups = new HashSet<>(Set.of("LunarflowViewers"));
        
        // Create second mock user
        UserDto mockUser2 = new UserDto();
        mockUser2.id = "admin-id-456";
        mockUser2.username = "adminuser";
        mockUser2.email = "admin@example.com";
        mockUser2.fullName = "Admin User";
        
        // Create list of mock users
        mockUsers = Arrays.asList(mockUser, mockUser2);
    }
    
    @Test
    public void testGetUserById() {
        // Arrange
        when(keycloakService.getUserById("test-id-123")).thenReturn(mockUser);
        
        // Act
        Response response = userResource.getUserById("test-id-123");
        
        // Assert
        assertEquals(200, response.getStatus());
        UserDto resultUser = (UserDto) response.getEntity();
        assertEquals("test-id-123", resultUser.id);
        assertEquals("testuser", resultUser.username);
        verify(keycloakService).getUserById("test-id-123");
    }
    
    @Test
    public void testGetUserByUsername() {
        // Arrange
        when(keycloakService.getUserByUsername("testuser")).thenReturn(mockUser);
        
        // Act
        Response response = userResource.getUserByUsername("testuser");
        
        // Assert
        assertEquals(200, response.getStatus());
        UserDto resultUser = (UserDto) response.getEntity();
        assertEquals("testuser", resultUser.username);
        assertEquals("test@example.com", resultUser.email);
        verify(keycloakService).getUserByUsername("testuser");
    }
    
    @Test
    public void testGetUsersByGroup() {
        // Arrange
        when(keycloakService.getUsersByGroup("LunarflowViewers")).thenReturn(mockUsers);
        
        // Act
        Response response = userResource.getUsersByGroup("LunarflowViewers");
        
        // Assert
        assertEquals(200, response.getStatus());
        @SuppressWarnings("unchecked")
        List<UserDto> resultUsers = (List<UserDto>) response.getEntity();
        assertEquals(2, resultUsers.size());
        assertEquals("testuser", resultUsers.get(0).username);
        assertEquals("adminuser", resultUsers.get(1).username);
        verify(keycloakService).getUsersByGroup("LunarflowViewers");
    }
    
    @Test
    public void testGetAllUsers() {
        // Arrange
        when(keycloakService.getAllUsers()).thenReturn(mockUsers);
        
        // Act
        Response response = userResource.getAllUsers();
        
        // Assert
        assertEquals(200, response.getStatus());
        @SuppressWarnings("unchecked")
        List<UserDto> resultUsers = (List<UserDto>) response.getEntity();
        assertEquals(2, resultUsers.size());
        assertEquals("test-id-123", resultUsers.get(0).id);
        assertEquals("admin-id-456", resultUsers.get(1).id);
        verify(keycloakService).getAllUsers();
    }
}