package org.acme;

import java.util.List;
import org.acme.models.User;
import jakarta.transaction.Transactional;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public String hello() {
        
        User user = new User();

        user.name ="asdf";

        user.persist();


        List<User> users = User.listAll();

        System.out.println(users);


        return "Hello from Quarkus REST";
    }
}
