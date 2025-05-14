package org.acme.security;

import io.quarkus.oidc.AccessTokenCredential;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Path("/auth")
public class Authenticate {

    @Inject
    SecurityIdentity identity;

    @GET
    @Path("/login")
    @Produces(MediaType.TEXT_HTML)
    public String login() {
        // This endpoint will trigger authentication if not logged in
        return "<h1>Logged in as: " + identity.getPrincipal().getName() + "</h1>" +
               "<h2>Your details:</h2>" +
               "<pre>" + identity.getAttributes() + "</pre>" +
               "<p><a href=\"/auth/logout\">Logout</a></p>";
    }

    @GET
    @Path("/profile")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> profile() {
        // Convert roles to a List to make it serializable
        List<String> rolesList = new ArrayList<>(identity.getRoles());
        
        // Extract only the token values we need
        Map<String, Object> tokenInfo = new HashMap<>();
        identity.getCredentials().forEach(cred -> {
            if (cred instanceof AccessTokenCredential) {
                tokenInfo.put("accessToken", ((AccessTokenCredential)cred).getToken());
            }
        });
        
        return Map.of(
            "username", identity.getPrincipal().getName(),
            "roles", rolesList,
            "tokenInfo", tokenInfo
        );
    }
}