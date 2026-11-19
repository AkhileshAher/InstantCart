package in.akhilesh.instantcart.service;

import in.akhilesh.instantcart.dto.AddressRequest;
import in.akhilesh.instantcart.dto.AddressResponse;
import in.akhilesh.instantcart.entity.Address;
import in.akhilesh.instantcart.entity.User;
import in.akhilesh.instantcart.repository.AddressRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public List<AddressResponse> getAllAddresses(User user) {

        return addressRepository.findByUserId(user.getId())
                .stream()
                .map(AddressResponse::from)
                .toList();
    }


    public AddressResponse createAddress(User user, AddressRequest req) {

        Address address = new Address();
        address.setUser(user);
        apply(address, req);
        boolean first = addressRepository.findByUserId(user.getId()).isEmpty();
        return save(user, address, first || Boolean.TRUE.equals(req.isDefault()));
    }


    public AddressResponse updateAddress(User user, Long id, @Valid AddressRequest req) {
        Address address = owned(user, id);
        apply(address, req);
        return save(user, address, Boolean.TRUE.equals(req.isDefault()));
    }

    public void deleteAddress(User user, Long id) {
        addressRepository.delete(owned(user, id));
    }


    private Address owned(User user, Long id) {
        return addressRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Address Not Found"));
    }

    private AddressResponse save(User user, Address address, boolean makeDefault) {
        if (makeDefault) {
            List<Address> others = addressRepository.findByUserId(user.getId());
            others.forEach(a -> a.setIsDefault(false));
            addressRepository.saveAll(others);
        }
        address.setIsDefault(makeDefault);
        return AddressResponse.from(addressRepository.save(address));
    }

    private void apply(Address a, AddressRequest r) {
        a.setLabel(r.label());
        a.setAddressLine(r.addressLine());
        a.setCity(r.city());
        a.setState(r.state());
        a.setZip(r.zip());
        a.setLat(r.lat());
        a.setLng(r.lng());
    }

}