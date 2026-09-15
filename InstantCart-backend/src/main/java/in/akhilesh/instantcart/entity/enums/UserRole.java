package in.akhilesh.instantcart.entity.enums;

import java.util.Set;

public enum UserRole {
    CUSTOMER(Set.of(Permission.READ,Permission.WRITE,Permission.UPDATE,Permission.DELETE)),
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
