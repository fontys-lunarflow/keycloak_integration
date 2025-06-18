package org.acme.security;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.annotation.Priority;
import jakarta.enterprise.inject.Produces;
import jakarta.interceptor.Interceptor;

import org.eclipse.microprofile.jwt.JsonWebToken;
import static org.mockito.Mockito.mock;

/**
 * Provides mock security components for testing
 */
@ApplicationScoped
@Alternative
@Priority(Interceptor.Priority.APPLICATION)
public class MockSecurityUtils extends SecurityUtils {
    
    @Produces
    @ApplicationScoped
    @Alternative
    @Priority(Interceptor.Priority.APPLICATION)
    public JsonWebToken jsonWebToken() {
        return mock(JsonWebToken.class);
    }
}