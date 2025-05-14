package org.acme.security;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import io.quarkus.security.identity.SecurityIdentity;
import java.util.Set;

@RequestScoped
public class SecurityUtils {

    @Inject
    SecurityIdentity identity;

    public boolean isLunarFlowUser() {
        return identity.getRoles().contains("LunarFlowUsers");
    }

    public boolean isLunarFlowAdmin() {
        return identity.getRoles().contains("LunarFlowAdmins");
    }
    
    public Set<String> getGroups() {
        return identity.getRoles();
    }
    
    public String getGroupsDebug() {
        return "Groups: " + String.join(", ", identity.getRoles());
    }
}