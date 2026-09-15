package in.akhilesh.instantcart.security;

import in.akhilesh.instantcart.entity.DeliveryPartner;
import org.bson.types.ObjectId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class DeliveryPartnerUserDetails implements UserDetails {

    private final DeliveryPartner deliveryPartner;

    public DeliveryPartnerUserDetails(DeliveryPartner deliveryPartner) {
        this.deliveryPartner = deliveryPartner;
    }

    public DeliveryPartner getDeliveryPartner() {
        return deliveryPartner;
    }

    public ObjectId getId() {
        return deliveryPartner.getId();
    }

    public String getName() {
        return deliveryPartner.getName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ROLE_DELIVERY")
        );
    }

    @Override
    public String getUsername() {
        return deliveryPartner.getEmail();
    }

    @Override
    public String getPassword() {
        return deliveryPartner.getPassword();
    }

    @Override
    public boolean isEnabled() {
        return deliveryPartner.getIsActive();
    }
}