package org.acme.models;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import java.util.Set;

@Entity
@Table(name="users")
public class User extends PanacheEntity {
    public String name;

    // Store internal permissions (e.g., "view", "edit")
    @ElementCollection
    public Set<String> permissions;
}