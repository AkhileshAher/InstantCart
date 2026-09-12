package in.akhilesh.instantcart.security;

import in.akhilesh.instantcart.entity.User;
import org.bson.types.ObjectId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public ObjectId getId() {
        return user.getId();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        // ROLE_VENDOR / ROLE_CUSTOMER / ROLE_ADMIN
        authorities.add(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );

        // Permissions
        Set<SimpleGrantedAuthority> permissions = user.getRole()
                        .getPermissions()
                        .stream()
                        .map(permission ->
                                new SimpleGrantedAuthority(
                                        permission.name()
                                )
                        )
                        .collect(Collectors.toSet());

        authorities.addAll(permissions);

        return authorities;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}