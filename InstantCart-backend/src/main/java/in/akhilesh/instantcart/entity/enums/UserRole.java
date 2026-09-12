package in.akhilesh.instantcart.entity.enums;

import javax.management.relation.Role;
import java.util.Set;

public enum UserRole {
    USER(Set.of(Permission.READ)),
    VENDOR(Set.of(Permission.READ,Permission.WRITE,Permission.DELETE)),
    DELIVERY(Set.of(Permission.READ));

    private final Set<Permission> permissions;

    UserRole(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
}
