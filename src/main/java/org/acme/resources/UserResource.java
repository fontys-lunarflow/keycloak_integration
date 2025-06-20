package org.acme.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.acme.services.KeycloakService;

/**
 * Resource for managing users and user information.
 */
@Path("/api/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    KeycloakService keycloakService;

    @GET
    @Path("/id/{id}")
    public Response getUserById(@PathParam("id") String id) {
        return Response.ok(keycloakService.getUserById(id)).build();
    }

    @GET
    @Path("/username/{username}")
    public Response getUserByUsername(@PathParam("username") String username) {
        return Response.ok(keycloakService.getUserByUsername(username)).build();
    }

    @GET
    @Path("/group/{groupName}")
    public Response getUsersByGroup(@PathParam("groupName") String groupName) {
        return Response.ok(keycloakService.getUsersByGroup(groupName)).build();
    }

    @GET
    @Path("/all")
    public Response getAllUsers() {
        return Response.ok(keycloakService.getAllUsers()).build();
    }
}