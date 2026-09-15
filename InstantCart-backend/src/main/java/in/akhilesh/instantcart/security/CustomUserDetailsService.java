package in.akhilesh.instantcart.security;

import in.akhilesh.instantcart.entity.DeliveryPartner;
import in.akhilesh.instantcart.entity.User;
import in.akhilesh.instantcart.repository.DeliveryPartnerRepository;
import in.akhilesh.instantcart.repository.UserRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final DeliveryPartnerRepository deliveryRepository;

    public CustomUserDetailsService(UserRepository userRepository,DeliveryPartnerRepository deliveryRepository) {
        this.userRepository = userRepository;
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email).orElse(null);

        if(user != null) {
            return new CustomUserDetails(user);
        }

        DeliveryPartner deliveryPartner = deliveryRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User or Delivery Partner not found"));

        return new DeliveryPartnerUserDetails(deliveryPartner);


    }
}