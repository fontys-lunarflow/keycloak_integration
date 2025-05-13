package org.acme.security;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.acme.models.User;
import io.quarkus.security.identity.SecurityIdentity;

@RequestScoped
public class SecurityUtils {

    @Inject
    SecurityIdentity identity;

    public boolean isLunarFlowUser() {
        return identity.hasRole("LunarFlowUsers");
    }

    public boolean isLunarFlowAdmin() {
        return identity.hasRole("LunarFlowAdmins");
    }

    public boolean hasRole(String role) {
        return identity.hasRole(role);
    }

    public boolean hasPermission(User user, String permission) {
        return user.permissions != null && user.permissions.contains(permission);
    }
}